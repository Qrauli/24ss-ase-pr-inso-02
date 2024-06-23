package at.ase.respond.dispatcher.service.impl;

import at.ase.respond.common.IncidentState;
import at.ase.respond.common.ResourceRequestState;
import at.ase.respond.common.event.IncidentCreatedOrUpdatedEvent;
import at.ase.respond.common.event.ResourceLocationUpdatedEvent;
import at.ase.respond.common.event.ResourceStatusUpdatedEvent;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.dispatcher.persistence.model.Incident;
import at.ase.respond.dispatcher.persistence.model.Resource;
import at.ase.respond.dispatcher.persistence.model.ResourceRequest;
import at.ase.respond.common.event.AdditionalResourcesRequestedEvent;

import at.ase.respond.dispatcher.presentation.mapper.LocationCoordinatesMapper;
import at.ase.respond.dispatcher.presentation.mapper.LocationMapper;
import at.ase.respond.dispatcher.presentation.mapper.PatientMapper;
import at.ase.respond.dispatcher.service.IncidentService;
import at.ase.respond.dispatcher.service.MessageReceiver;
import at.ase.respond.dispatcher.service.ResourceRequestService;
import at.ase.respond.dispatcher.service.ResourceService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageReceiverImpl implements MessageReceiver {

    private final IncidentService incidentService;

    private final ResourceRequestService resourceRequestService;

    private final ResourceService resourceService;

    private final LocationMapper locationMapper;

    private final LocationCoordinatesMapper locationCoordinatesMapper;

    private final PatientMapper patientMapper;

    @Override
    @RabbitListener(queues = "${rabbit.queues.incidents}")
    public void receive(Channel channel, Message message, IncidentCreatedOrUpdatedEvent payload) throws IOException {
        log.debug("Received incident payload {}", payload);

        Incident incident;
        try {
            // Check if incident already exists, if so, keep its state
            incident = incidentService.findById(payload.id());
        } catch (NotFoundException e) {
            // Incident does not exist yet, so it is in READY state
            incident = new Incident();
            incident.setId(payload.id());
            incident.setState(IncidentState.READY);
        }

        incident.setCode(payload.code());
        incident.setPatients(payload.patients().stream().map(patientMapper::toVO).toList());
        incident.setNumberOfPatients(payload.numberOfPatients());
        incident.setLocation(locationMapper.toVO(payload.location()));
        incident.setCallerNumber(payload.callerNumber());

        incidentService.save(incident);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @Override
    @RabbitListener(queues = "${rabbit.queues.requests}")
    public void receive(Channel channel, Message message, AdditionalResourcesRequestedEvent payload) throws IOException {
        log.debug("Received additional request payload {}", payload);

        Resource resource = resourceService.findById(payload.resourceId());
        if (resource == null) {
            log.error("Resource with id {} not found", payload.resourceId());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        ResourceRequest resourceRequest = ResourceRequest.builder()
                .id(UUID.randomUUID())
                .assignedIncident(resource.getAssignedIncident())
                .resource(resource)
                .state(ResourceRequestState.OPEN)
                .requestedResourceType(payload.requestedResourceType())
                .build();

        resourceRequestService.create(resourceRequest);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @Override
    @RabbitListener(queues = "${rabbit.queues.resources.location}")
    public void receive(Channel channel, Message message, ResourceLocationUpdatedEvent payload) throws IOException {
        log.debug("Received resource location update payload {}", payload);

        try {
            GeoJsonPoint location = locationCoordinatesMapper.toGeoJsonPoint(payload.locationCoordinates());
            resourceService.updateLocation(payload.resourceId(), location);
        } catch (NotFoundException e) {
            log.error("Failed to update resource location for id {}: Resource not found", payload.resourceId());
        }

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @Override
    @RabbitListener(queues = "${rabbit.queues.resources.status}")
    public void receive(Channel channel, Message message, ResourceStatusUpdatedEvent payload) throws IOException {
        log.debug("Received resource status update payload {}", payload);

        try {
            resourceService.updateState(payload.resourceId(), payload.state());
        } catch (NotFoundException e) {
            log.error("Failed to update resource status for id {}: Resource not found", payload.resourceId());
        }

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
