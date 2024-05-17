package at.ase.respond.common.event;

import at.ase.respond.common.dto.LocationCoordinatesDTO;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Represents a change in the location of a resource.
 *
 * <p>Published when the location of a resource has changed.
 *
 * @param resourceId          the resource identifier
 * @param locationCoordinates the new location of the resource
 * @param timestamp           the timestamp of the update
 */
public record ResourceLocationUpdatedEvent(
        String resourceId,
        LocationCoordinatesDTO locationCoordinates,
        ZonedDateTime timestamp
) implements Serializable {
}
