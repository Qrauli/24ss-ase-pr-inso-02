package at.ase.respond.dispatcher.presentation.event;

import at.ase.respond.dispatcher.persistence.model.ResourceState;
import at.ase.respond.dispatcher.persistence.model.ResourceType;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Sent or received if the status of a resource has changed.
 *
 * @param recipientResourceId the recipient of this message
 */
public record ResourceStatusUpdatedEvent(OffsetDateTime timestamp, String resourceId, ResourceType type,
        ResourceState state, UUID assignedIncident) {
}
