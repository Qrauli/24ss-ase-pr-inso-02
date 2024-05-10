package at.ase.respond.dispatcher.service.impl;

import at.ase.respond.dispatcher.persistence.IncidentRepository;
import at.ase.respond.dispatcher.persistence.ResourceRepository;
import at.ase.respond.dispatcher.persistence.model.LocationCoordinates;
import at.ase.respond.dispatcher.persistence.model.Resource;
import at.ase.respond.dispatcher.persistence.model.ResourceState;
import at.ase.respond.dispatcher.presentation.event.ResourceLocationUpdatedEvent;
import at.ase.respond.dispatcher.presentation.event.ResourceStatusUpdatedEvent;
import at.ase.respond.dispatcher.presentation.mapper.IncidentMapper;
import at.ase.respond.dispatcher.presentation.mapper.ResourceMapper;
import at.ase.respond.dispatcher.presentation.event.IncidentCreatedEvent;
import at.ase.respond.dispatcher.presentation.dto.ResourceDTO;
import at.ase.respond.dispatcher.presentation.event.IncidentCreatedEvent;
import at.ase.respond.dispatcher.presentation.mapper.LocationCoordinatesMapper;
import at.ase.respond.dispatcher.service.MessageReceiver;
import at.ase.respond.dispatcher.service.ResourceService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageReceiverImpl implements MessageReceiver {

    private final IncidentRepository repository;

    private final ResourceRepository resourceRepository;

    private final ResourceService resourceService;

    @Override
    @RabbitListener(queues = "${rabbit.queues.incidents}")
    public void receive(Channel channel, Message message, IncidentCreatedEvent payload) throws IOException {
        log.debug("Received incident payload {}", payload);
        repository.save(IncidentMapper.toEntity(payload));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @Override
    @RabbitListener(queues = "${rabbit.queues.resources.location}")
    public void receive(Channel channel, Message message, ResourceLocationUpdatedEvent payload) throws IOException {
        log.debug("Received resource location update payload {} in message {}", payload, message);
        try {
            LocationCoordinates locationCoordinates = LocationCoordinatesMapper.toEntity(payload.locationCoordinates());
            resourceService.updateLocation(payload.resourceId(), locationCoordinates);
        }
        catch (IllegalArgumentException e) {
            log.error("Failed to update resource location for id {}: Resource not found", payload.resourceId());
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @Override
    @RabbitListener(queues = "${rabbit.queues.resources.status}")
    public void receive(Channel channel, Message message, ResourceStatusUpdatedEvent payload) throws IOException {
        log.debug("Received resource status update payload {} in message {}", payload, message);
        ResourceState state = payload.state();
        if (state == null) {
            return;
        }

        try {
            resourceService.updateState(payload.resourceId(), state);
        }
        catch (IllegalArgumentException e) {
            if (state == ResourceState.AVAILABLE) {
                log.info("Resource with id {} not found, creating new resource", payload.resourceId());
                Resource newResource = new Resource(payload.resourceId(), payload.type(), state,
                        new LocationCoordinates(0.0, 0.0), null);
                resourceService.create(newResource);
            }
            else {
                log.error("Failed to update resource state for id {}: Resource not found", payload.resourceId());
                throw e;
            }
        }

        if (state == ResourceState.UNAVAILABLE) {
            log.info("Resource with id {} is unavailable, deleting", payload.resourceId());
            resourceService.deleteById(payload.resourceId());
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
