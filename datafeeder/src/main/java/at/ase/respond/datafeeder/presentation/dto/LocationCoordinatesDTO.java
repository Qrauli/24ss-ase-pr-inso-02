package at.ase.respond.datafeeder.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * Represents the coordinates of a locationCoordinates.
 *
 * @param latitude the latitude of the locationCoordinates
 * @param longitude the longitude of the locationCoordinates
 */
@Schema(description = "A DTO representing the coordinates of a locationCoordinates")
public record LocationCoordinatesDTO(
        @Schema(description = "The latitude of the location", example = "48.18982")
        Double latitude,
        @Schema(description = "The longitude of the location", example = "16.36457")
        Double longitude) implements Serializable {
}
