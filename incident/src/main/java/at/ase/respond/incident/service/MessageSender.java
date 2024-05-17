package at.ase.respond.incident.service;

import at.ase.respond.common.event.IncidentCreatedOrUpdatedEvent;

public interface MessageSender {

    /**
     * Publishes the specified message to the broker.
     *
     * @param message the message to be published
     */
    void publish(IncidentCreatedOrUpdatedEvent message);

}
