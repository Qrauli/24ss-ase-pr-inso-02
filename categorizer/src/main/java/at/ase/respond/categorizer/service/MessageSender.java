package at.ase.respond.categorizer.service;

import at.ase.respond.categorizer.presentation.event.IncidentCreatedEvent;

public interface MessageSender {

    /**
     * Publishes the specified message to the broker.
     * @param message the message to be published
     */
    void publish(IncidentCreatedEvent message);

}
