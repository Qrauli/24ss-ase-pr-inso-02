package at.ase.respond.incident;

import at.ase.respond.common.event.IncidentCreatedOrUpdatedEvent;
import at.ase.respond.incident.service.impl.MessageSenderImpl;
import at.ase.respond.common.dto.LocationDTO;
import at.ase.respond.common.dto.LocationAddressDTO;
import at.ase.respond.common.dto.LocationCoordinatesDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class MessageSenderTest {

    @InjectMocks
    MessageSenderImpl messageSender;

    @Mock
    RabbitTemplate rabbitTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPublish() {
        IncidentCreatedOrUpdatedEvent event = new IncidentCreatedOrUpdatedEvent(
                UUID.randomUUID(),
                "test",
                "12345678910",
                new LocationDTO(
                        new LocationAddressDTO("", "", "", ""),
                        new LocationCoordinatesDTO(0d, 0d)
                ),
                new ArrayList<>(),
                0,
                ZonedDateTime.now()
        );

        messageSender.publish(event);

        verify(rabbitTemplate, times(1)).convertAndSend(any(), any(), eq(event), any(CorrelationData.class));
    }
}
