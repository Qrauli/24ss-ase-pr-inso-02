package at.ase.respond.incident.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * Represents the coordinates of a location.
 *
 * @param latitude the latitude of the location
 * @param longitude the longitude of the location
 */
@Schema(description = "A DTO representing the coordinates of a location")
public record LocationCoordinatesDTO(Double latitude, Double longitude) implements Serializable {
}
