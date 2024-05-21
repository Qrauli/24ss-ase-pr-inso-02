package at.ase.respond.datafeeder.service;

import at.ase.respond.common.event.AdditionalResourcesRequestedEvent;
import at.ase.respond.common.event.ResourceLocationUpdatedEvent;
import at.ase.respond.common.event.ResourceStatusUpdatedEvent;

public interface MessageSender {

    /**
     * Publishes the specified event to the broker.
     *
     * @param message the event to publish
     */
    void publish(ResourceStatusUpdatedEvent message);

    /**
     * Publishes the specified event to the broker.
     *
     * @param message the event to publish
     */
    void publish(ResourceLocationUpdatedEvent message);

    void publish(AdditionalResourcesRequestedEvent payload);
}
