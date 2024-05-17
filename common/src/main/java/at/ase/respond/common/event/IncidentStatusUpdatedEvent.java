package at.ase.respond.common.event;

import at.ase.respond.common.dto.IncidentDTO;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Represents an event that indicates that the status of an incident has been updated.
 *
 * <p>Published when the status of an incident has been updated.
 *
 * @param resourceId the resource identifier of the resource that receives the event
 * @param incident   the incident with the updated status
 * @param timestamp  the timestamp of the status update
 */
public record IncidentStatusUpdatedEvent(
        String resourceId,
        IncidentDTO incident,
        ZonedDateTime timestamp
) implements Serializable {
}
