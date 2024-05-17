package at.ase.respond.common.dto;

import java.io.Serializable;

/**
 * Represents the coordinates of a location.
 *
 * @param latitude  the latitude of the location
 * @param longitude the longitude of the location
 */
public record LocationCoordinatesDTO(
        Double latitude,
        Double longitude
) implements Serializable {
}
