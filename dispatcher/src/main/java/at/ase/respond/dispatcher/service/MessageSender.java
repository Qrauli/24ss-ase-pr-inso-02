package at.ase.respond.dispatcher.service;

import at.ase.respond.dispatcher.presentation.event.ResourceStatusUpdatedEvent;

public interface MessageSender {

    /**
     * Publishes the specified message to the broker.
     * @param message the message to be published
     */
    void publish(ResourceStatusUpdatedEvent message);

}
