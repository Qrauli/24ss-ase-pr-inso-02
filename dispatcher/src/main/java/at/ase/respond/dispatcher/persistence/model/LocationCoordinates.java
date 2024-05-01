package at.ase.respond.dispatcher.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a locationCoordinates coordinates value object.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationCoordinates {

    private Double latitude;

    private Double longitude;

}
