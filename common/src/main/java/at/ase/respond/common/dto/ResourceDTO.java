package at.ase.respond.common.dto;

import at.ase.respond.common.ResourceState;
import at.ase.respond.common.ResourceType;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Represents a resource.
 *
 * @param id                  the unique identifier of the resource
 * @param type                the type of the resource
 * @param state               the state of the resource
 * @param locationCoordinates the location coordinates of the resource
 * @param assignedIncident    the unique identifier of the incident the resource is currently
 *                            assigned to (null if not assigned to any incident)
 * @param updatedAt           the timestamp of the last update of the resource
 */
public record ResourceDTO(
        String id,
        ResourceType type,
        ResourceState state,
        LocationCoordinatesDTO locationCoordinates,
        UUID assignedIncident,
        ZonedDateTime updatedAt
) implements Serializable {
}
