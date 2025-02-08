package at.ase.respond.dispatcher.presentation.dto;

import java.io.Serializable;

/**
 * Represents a resource with a distance to a certain location.
 *
 * @param resourceId the id of the resource
 * @param distance   the distance in kilometers
 */
public record ResourceWithDistanceDTO(
        String resourceId,
        double distance
) implements Serializable {
}
