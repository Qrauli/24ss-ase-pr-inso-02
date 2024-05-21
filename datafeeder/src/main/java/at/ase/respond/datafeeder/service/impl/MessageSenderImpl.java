package at.ase.respond.datafeeder.service.impl;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import at.ase.respond.common.event.AdditionalResourcesRequestedEvent;
import at.ase.respond.common.event.ResourceLocationUpdatedEvent;
import at.ase.respond.common.event.ResourceStatusUpdatedEvent;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessageSenderImpl implements at.ase.respond.datafeeder.service.MessageSender {

	@Value("${rabbit.exchange}")
	private String exchange;

	@Value("${rabbit.routes.resources.status}")
	private String statusRoute;

	@Value("${rabbit.routes.resources.location}")
	private String locationRoute;

	@Value("${rabbit.routes.requests}")
	private String requestsRoute;

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
	public void publish(ResourceStatusUpdatedEvent message) {
		log.debug("Publish resource status update {}", message);
		rabbit.convertAndSend(exchange, statusRoute, message);
	}

	@Override
	public void publish(ResourceLocationUpdatedEvent message) {
		log.debug("Publish resource location update {}", message);
		rabbit.convertAndSend(exchange, locationRoute, message);
	}

    @Override
    public void publish(AdditionalResourcesRequestedEvent payload) {
        log.debug("Sending resource request payload {}", payload);
        rabbit.convertAndSend(exchange, requestsRoute, payload);
    }
}
