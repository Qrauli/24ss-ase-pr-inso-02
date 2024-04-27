package at.ase.respond.incident.service.impl;

import at.ase.respond.incident.presentation.event.IncidentCreatedEvent;
import at.ase.respond.incident.service.MessageSender;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageSenderImpl implements MessageSender {

    @Value("${rabbit.exchange}")
    private String exchange;

    @Value("${rabbit.routes.incidents}")
    private String route;

    private final RabbitTemplate rabbit;

    public MessageSenderImpl(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    @PostConstruct
    private void postConstruct() {
        rabbit.setConfirmCallback(this::handleConfirmCallback);
    }

    private void handleConfirmCallback(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.debug("Sent message {}", correlationData);
        }
        else {
            log.error("Could not send message {} ({})", correlationData, cause);
        }
    }

    @Override
    public void publish(IncidentCreatedEvent message) {
        rabbit.convertAndSend(exchange, route, message, new CorrelationData(message.id().toString()));
    }

}
