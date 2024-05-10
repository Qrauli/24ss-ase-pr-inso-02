package at.ase.respond.datafeeder.service.impl;

import at.ase.respond.datafeeder.presentation.dto.ResourceStatusUpdatedEvent;
import at.ase.respond.datafeeder.service.MessageReceiver;
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

	@Override
	@RabbitListener(queues = "${rabbit.queues.resources}")
	public void receive(Channel channel, Message message, ResourceStatusUpdatedEvent payload) throws IOException {
		log.debug("Received resource status update payload {} in message {}", payload, message);
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}

}
