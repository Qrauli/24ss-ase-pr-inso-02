package at.ase.respond.dispatcher.service;

import at.ase.respond.common.event.AdditionalResourcesRequestedEvent;
import at.ase.respond.common.event.IncidentCreatedOrUpdatedEvent;
import at.ase.respond.common.event.ResourceLocationUpdatedEvent;
import at.ase.respond.common.event.ResourceStatusUpdatedEvent;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.io.IOException;

public interface MessageReceiver {

    /**
     * Handles the incoming message, processes the payload and sends an acknowledgment.
     * This event is received when a new incident was created.
     *
     * @param channel The channel on which the message was received
     * @param message The raw AMQP message received
     * @param payload The deserialized payload of the message
     * @throws IOException If an error occurs while acknowledging the message
     */
    void receive(Channel channel, Message message, IncidentCreatedOrUpdatedEvent payload) throws IOException;

    /**
     * Handles the incoming message, processes the payload and sends an acknowledgment.
     * This event is received when the location of a resource was updated.
     *
     * @param channel The channel on which the message was received
     * @param message The raw AMQP message received
     * @param payload The deserialized payload of the message
     * @throws IOException If an error occurs while acknowledging the message
     */
    void receive(Channel channel, Message message, ResourceLocationUpdatedEvent payload) throws IOException;

    /**
     * Handles the incoming message, processes the payload and sends an acknowledgment.
     * This event is received when the status of a resource was updated.
     *
     * @param channel The channel on which the message was received
     * @param message The raw AMQP message received
     * @param payload The deserialized payload of the message
     * @throws IOException If an error occurs while acknowledging the message
     */
    void receive(Channel channel, Message message, ResourceStatusUpdatedEvent payload) throws IOException;

    /**
     * Handles the incoming message, processes the payload and sends an acknowledgment.
     * @param channel The channel on which the message was received
     * @param message The raw AMQP message received
     * @param payload The deserialized payload of the message
     * @throws IOException If an error occurs while acknowledging the message
     */
    void receive(Channel channel, Message message, AdditionalResourcesRequestedEvent payload) throws IOException;

}
