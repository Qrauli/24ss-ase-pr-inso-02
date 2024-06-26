package at.ase.respond.dispatcher.service.impl;

import at.ase.respond.common.event.IncidentStatusUpdatedEvent;
import at.ase.respond.dispatcher.service.MessageSender;
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
    private String resourcesBaseRoute;

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
    public void publish(IncidentStatusUpdatedEvent message) {
        String routingKey = resourcesBaseRoute + "." + message.resourceId();
        log.debug("Publishing incident status updated {} (routing key {})", message, routingKey);
        rabbit.convertAndSend(exchange, routingKey, message);
    }

}
