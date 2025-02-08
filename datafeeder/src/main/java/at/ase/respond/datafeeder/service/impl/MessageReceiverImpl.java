package at.ase.respond.datafeeder.service.impl;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import at.ase.respond.common.event.IncidentStatusUpdatedEvent;
import at.ase.respond.datafeeder.service.MessageReceiver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageReceiverImpl implements MessageReceiver {

	@Override
	@RabbitListener(queues = "${rabbit.queues.incidents}")
	public void receive(Channel channel, Message message, IncidentStatusUpdatedEvent payload) throws IOException {
		log.debug("Received status update payload {} in message {}", payload, message);
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}

}
