package at.ase.respond.common.event;

import at.ase.respond.common.ResourceState;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Represents a change in the status of a resource.
 *
 * <p>Published when the status of a resource has changed.
 *
 * @param resourceId the resource identifier
 * @param state      the new state of the resource
 * @param timestamp  the timestamp of the update
 */
public record ResourceStatusUpdatedEvent(
        String resourceId,
        ResourceState state,
        ZonedDateTime timestamp
) implements Serializable {
}
