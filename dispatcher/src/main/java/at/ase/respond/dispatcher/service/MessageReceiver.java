package at.ase.respond.dispatcher.service;

import at.ase.respond.dispatcher.presentation.event.IncidentCreatedEvent;
import at.ase.respond.dispatcher.presentation.dto.ResourceDTO;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.io.IOException;

public interface MessageReceiver {

    /**
     * Handles the incoming message, processes the payload and sends an acknowledgment.
     * This event is received when a new incident was created.
     * @param channel The channel on which the message was received
     * @param message The raw AMQP message received
     * @param payload The deserialized payload of the message
     * @throws IOException If an error occurs while acknowledging the message
     */
    void receive(Channel channel, Message message, IncidentCreatedEvent payload) throws IOException;

    /**
     * Handles the incoming message, processes the payload and sends an acknowledgment.
     * @param channel The channel on which the message was received
     * @param message The raw AMQP message received
     * @param payload The deserialized payload of the message
     * @throws IOException If an error occurs while acknowledging the message
     */
    void receive(Channel channel, Message message, ResourceDTO payload) throws IOException;

}
