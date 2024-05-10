package at.ase.respond.datafeeder.service.impl;

import at.ase.respond.datafeeder.config.RabbitConfig;
import at.ase.respond.datafeeder.presentation.dto.ResourceLocationUpdatedEvent;
import at.ase.respond.datafeeder.presentation.dto.ResourceStatusUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSenderImpl implements at.ase.respond.datafeeder.service.MessageSender {

	private final RabbitConfig rabbitConfig;

	private final RabbitTemplate rabbitTemplate;

	@Override
	public void send(ResourceStatusUpdatedEvent payload) {
		log.debug("Sending resource status update payload {}", payload);
		rabbitTemplate.convertAndSend(rabbitConfig.exchange().getName(), rabbitConfig.resourceStatusRoutingKey(), payload);
	}

	@Override
	public void send(ResourceLocationUpdatedEvent payload) {
		log.debug("Sending resource location update payload {}", payload);
		rabbitTemplate.convertAndSend(rabbitConfig.exchange().getName(), rabbitConfig.resourceLocationRoutingKey(), payload);
	}

}
