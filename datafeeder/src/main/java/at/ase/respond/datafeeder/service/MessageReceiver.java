package at.ase.respond.datafeeder.service;

import java.io.IOException;

import org.springframework.amqp.core.Message;

import com.rabbitmq.client.Channel;

import at.ase.respond.common.event.IncidentStatusUpdatedEvent;

public interface MessageReceiver {

    /**
     * Handles the incoming message, processes the payload and sends an acknowledgment.
     * This event is received when the status of an incident was updated.
     *
     * @param channel The channel on which the message was received
     * @param message The raw AMQP message received
     * @param payload The deserialized payload of the message
     * @throws IOException If an error occurs while acknowledging the message
     */
    void receive(Channel channel, Message message, IncidentStatusUpdatedEvent payload) throws IOException;

}
