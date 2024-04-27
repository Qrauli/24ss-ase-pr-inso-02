package at.ase.respond.incident.service;

import at.ase.respond.incident.presentation.event.IncidentCreatedEvent;

public interface MessageSender {

    /**
     * Publishes the specified message to the broker.
     * @param message the message to be published
     */
    void publish(IncidentCreatedEvent message);

}
