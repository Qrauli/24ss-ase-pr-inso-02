package at.ase.respond.dispatcher.service.impl;

import at.ase.respond.dispatcher.persistence.IncidentRepository;
import at.ase.respond.dispatcher.presentation.mapper.IncidentMapper;
import at.ase.respond.dispatcher.presentation.dto.IncidentCreatedEvent;
import at.ase.respond.dispatcher.service.MessageReceiver;
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

    @Override
    @RabbitListener(queues = "${rabbit.queues.incidents}")
    public void receive(Channel channel, Message message, IncidentCreatedEvent payload) throws IOException {
        log.debug("Received payload {}", payload);
        repository.save(IncidentMapper.toEntity(payload));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
