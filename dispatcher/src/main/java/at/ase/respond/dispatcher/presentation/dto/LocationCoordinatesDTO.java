package at.ase.respond.dispatcher.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * Represents the coordinates of a locationCoordinates.
 *
 * @param latitude the latitude of the locationCoordinates
 * @param longitude the longitude of the locationCoordinates
 */
@Schema(description = "A DTO representing the coordinates of a locationCoordinates")
public record LocationCoordinatesDTO(Double latitude, Double longitude) implements Serializable {
}
