package at.ase.respond.dispatcher.service;

import at.ase.respond.common.IncidentState;
import at.ase.respond.common.dto.IncidentDTO;
import at.ase.respond.common.event.IncidentStatusUpdatedEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class MessageSenderTest {

    @Captor
    private ArgumentCaptor<String> routingKeyCaptor;

    @MockBean
    private RabbitTemplate rabbit;

    @Autowired
    private MessageSender sender;

    @Test
    void publish_thenPublishEventToRabbit() {
        IncidentStatusUpdatedEvent event = new IncidentStatusUpdatedEvent(
                "JOK-1",
                new IncidentDTO(
                        UUID.randomUUID(),
                        "01A01",
                        "0000 123 456 78",
                        IncidentState.DISPATCHED,
                        null,
                        List.of(),
                        0,
                        null,
                        null,
                        null
                ),
                null
        );

        sender.publish(event);

        verify(rabbit, times(1))
                .convertAndSend(anyString(), routingKeyCaptor.capture(), any(IncidentStatusUpdatedEvent.class));

        String capturedRoutingKey = routingKeyCaptor.getValue();
        assertTrue(capturedRoutingKey.contains(event.resourceId()));
    }

}