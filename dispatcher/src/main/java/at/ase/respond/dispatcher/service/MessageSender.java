package at.ase.respond.dispatcher.service;

import at.ase.respond.common.event.IncidentStatusUpdatedEvent;

public interface MessageSender {

    /**
     * Publishes the specified message to the broker.
     *
     * @param message the message to be published
     */
    void publish(IncidentStatusUpdatedEvent message);

}
