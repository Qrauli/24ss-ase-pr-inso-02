package at.ase.respond.incident.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * Represents a location containing a description as well as coordinates.
 *
 * @param description the description of this location
 * @param coordinates the coordinates of this location
 */
@Schema(description = "A DTO representing a location")
public record LocationDTO(LocationAddressDTO description, LocationCoordinatesDTO coordinates) implements Serializable {
}
