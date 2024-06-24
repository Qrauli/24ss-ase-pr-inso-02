package at.ase.respond.dispatcher.service;

import at.ase.respond.common.IncidentState;
import at.ase.respond.common.ResourceRequestState;
import at.ase.respond.common.ResourceState;
import at.ase.respond.common.ResourceType;
import at.ase.respond.common.Sex;
import at.ase.respond.common.dto.LocationAddressDTO;
import at.ase.respond.common.dto.LocationCoordinatesDTO;
import at.ase.respond.common.dto.LocationDTO;
import at.ase.respond.common.dto.PatientDTO;
import at.ase.respond.common.event.AdditionalResourcesRequestedEvent;
import at.ase.respond.common.event.IncidentCreatedOrUpdatedEvent;
import at.ase.respond.common.event.ResourceLocationUpdatedEvent;
import at.ase.respond.common.event.ResourceStatusUpdatedEvent;
import at.ase.respond.common.exception.NotFoundException;
import at.ase.respond.dispatcher.persistence.model.Incident;
import at.ase.respond.dispatcher.persistence.model.Resource;
import at.ase.respond.dispatcher.persistence.model.ResourceRequest;
import com.rabbitmq.client.Channel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class MessageReceiverTest {

    @Captor
    private ArgumentCaptor<Incident> incidentArgumentCaptor;

    @Captor
    private ArgumentCaptor<ResourceRequest> resourceRequestArgumentCaptor;

    @MockBean
    private Channel channel;

    @MockBean
    private ResourceService resourceService;

    @MockBean
    private IncidentService incidentService;

    @MockBean
    private ResourceRequestService resourceRequestService;

    private Message dummyMessage;

    @Autowired
    private MessageReceiver receiver;

    @BeforeEach
    void setUp() {
        dummyMessage = new Message(new byte[]{}, new MessageProperties());
    }

    @Test
    void whenNoIncident_incidentReceived_thenIncidentIsCreated() throws IOException {
        LocationAddressDTO address = new LocationAddressDTO("street", "city", "zip", "additional");
        LocationCoordinatesDTO coordinates = new LocationCoordinatesDTO(1.0, 2.0);
        LocationDTO location = new LocationDTO(address, coordinates);
        IncidentCreatedOrUpdatedEvent event = new IncidentCreatedOrUpdatedEvent(
                UUID.randomUUID(),
                "01A01",
                "0000 123 456 78",
                location,
                List.of(new PatientDTO(24, Sex.UNKNOWN)),
                1,
                ZonedDateTime.of(2024, 6, 24, 0, 0, 0, 0, ZonedDateTime.now().getZone())
        );

        when(incidentService.findById(event.id())).thenThrow(NotFoundException.class);
        when(incidentService.save(incidentArgumentCaptor.capture())).thenReturn(null);

        receiver.receive(channel, dummyMessage, event);

        verify(channel).basicAck(dummyMessage.getMessageProperties().getDeliveryTag(), false);

        Incident capturedIncident = incidentArgumentCaptor.getValue();
        assertAll(
                () -> assertThat(capturedIncident.getId()).isEqualTo(event.id()),
                () -> assertThat(capturedIncident.getState()).isEqualTo(IncidentState.READY),
                () -> assertThat(capturedIncident.getCode()).isEqualTo(event.code()),
                () -> assertThat(capturedIncident.getPatients()).hasSize(event.numberOfPatients()),
                () -> assertThat(capturedIncident.getNumberOfPatients()).isEqualTo(event.numberOfPatients()),
                () -> assertThat(capturedIncident.getCallerNumber()).isEqualTo(event.callerNumber())
        );
    }

    @Test
    void whenIncident_incidentReceived_thenIncidentIsCreated() throws IOException {
        LocationAddressDTO address = new LocationAddressDTO("street", "city", "zip", "additional");
        LocationCoordinatesDTO coordinates = new LocationCoordinatesDTO(1.0, 2.0);
        LocationDTO location = new LocationDTO(address, coordinates);
        IncidentCreatedOrUpdatedEvent event = new IncidentCreatedOrUpdatedEvent(
                UUID.randomUUID(),
                "01A01",
                "0000 123 456 78",
                location,
                List.of(new PatientDTO(24, Sex.UNKNOWN)),
                1,
                ZonedDateTime.of(2024, 6, 24, 0, 0, 0, 0, ZonedDateTime.now().getZone())
        );

        Incident incident = new Incident();
        incident.setId(event.id());

        when(incidentService.findById(event.id())).thenReturn(incident);
        when(incidentService.save(incidentArgumentCaptor.capture())).thenReturn(null);

        receiver.receive(channel, dummyMessage, event);

        verify(channel).basicAck(dummyMessage.getMessageProperties().getDeliveryTag(), false);

        Incident capturedIncident = incidentArgumentCaptor.getValue();
        assertAll(
                () -> assertThat(capturedIncident.getCode()).isEqualTo(event.code()),
                () -> assertThat(capturedIncident.getPatients()).hasSize(event.numberOfPatients()),
                () -> assertThat(capturedIncident.getNumberOfPatients()).isEqualTo(event.numberOfPatients()),
                () -> assertThat(capturedIncident.getCallerNumber()).isEqualTo(event.callerNumber())
        );
    }

    @Test
    void whenNoResource_additionalResourcesRequested_thenNoRequestIsCreated() throws IOException {
        AdditionalResourcesRequestedEvent event = new AdditionalResourcesRequestedEvent(
                "JOK-1",
                ResourceType.NEF,
                ZonedDateTime.of(2024, 6, 24, 0, 0, 0, 0, ZonedDateTime.now().getZone())
        );

        when(resourceService.findById(event.resourceId())).thenThrow(NotFoundException.class);

        receiver.receive(channel, dummyMessage, event);

        verify(channel).basicAck(dummyMessage.getMessageProperties().getDeliveryTag(), false);
        verify(resourceRequestService, times(0)).create((any()));
    }

    @Test
    void whenResource_additionalResourcesRequested_thenRequestIsCreated() throws IOException {
        AdditionalResourcesRequestedEvent event = new AdditionalResourcesRequestedEvent(
                "JOK-1",
                ResourceType.NEF,
                ZonedDateTime.of(2024, 6, 24, 0, 0, 0, 0, ZonedDateTime.now().getZone())
        );
        Incident incident = new Incident();
        incident.setId(UUID.randomUUID());
        Resource resource = new Resource();
        resource.setId("JOK-1");
        resource.setAssignedIncident(incident);

        when(resourceService.findById(event.resourceId())).thenReturn(resource);

        receiver.receive(channel, dummyMessage, event);

        verify(resourceRequestService).create(resourceRequestArgumentCaptor.capture());
        verify(channel).basicAck(dummyMessage.getMessageProperties().getDeliveryTag(), false);

        ResourceRequest capturedResourceRequest = resourceRequestArgumentCaptor.getValue();
        assertAll(
                () -> assertThat(capturedResourceRequest.getAssignedIncident().getId()).isEqualTo(incident.getId()),
                () -> assertThat(capturedResourceRequest.getState()).isEqualTo(ResourceRequestState.OPEN),
                () -> assertThat(capturedResourceRequest.getRequestedResourceType()).isEqualTo(event.requestedResourceType())
        );
    }

    @Test
    void whenNoResource_resourceLocationUpdated_thenNoResourceIsUpdated() throws IOException {
        ResourceLocationUpdatedEvent event = new ResourceLocationUpdatedEvent(
                "JOK-1",
                new LocationCoordinatesDTO(1.0, 2.0),
                ZonedDateTime.of(2024, 6, 24, 0, 0, 0, 0, ZonedDateTime.now().getZone())
        );

        when(resourceService.updateLocation(any(), any())).thenThrow(NotFoundException.class);

        receiver.receive(channel, dummyMessage, event);

        verify(channel).basicAck(dummyMessage.getMessageProperties().getDeliveryTag(), false);
    }

    @Test
    void whenResource_resourceLocationUpdated_thenResourceIsUpdated() throws IOException {
        ResourceLocationUpdatedEvent event = new ResourceLocationUpdatedEvent(
                "JOK-1",
                new LocationCoordinatesDTO(1.0, 2.0),
                ZonedDateTime.of(2024, 6, 24, 0, 0, 0, 0, ZonedDateTime.now().getZone())
        );

        receiver.receive(channel, dummyMessage, event);

        verify(channel).basicAck(dummyMessage.getMessageProperties().getDeliveryTag(), false);
    }

    @Test
    void whenNoResource_resourceStatusUpdated_thenNoResourceIsUpdated() throws IOException {
        ResourceStatusUpdatedEvent event = new ResourceStatusUpdatedEvent(
                "JOK-1",
                ResourceState.AT_HOSPITAL,
                ZonedDateTime.of(2024, 6, 24, 0, 0, 0, 0, ZonedDateTime.now().getZone())
        );

        when(resourceService.updateState(any(), any())).thenThrow(NotFoundException.class);

        receiver.receive(channel, dummyMessage, event);

        verify(channel).basicAck(dummyMessage.getMessageProperties().getDeliveryTag(), false);
    }

    @Test
    void whenResource_resourceStatusUpdated_thenResourceIsUpdated() throws IOException {
        ResourceStatusUpdatedEvent event = new ResourceStatusUpdatedEvent(
                "JOK-1",
                ResourceState.AT_HOSPITAL,
                ZonedDateTime.of(2024, 6, 24, 0, 0, 0, 0, ZonedDateTime.now().getZone())
        );
        
        receiver.receive(channel, dummyMessage, event);

        verify(channel).basicAck(dummyMessage.getMessageProperties().getDeliveryTag(), false);
    }

}