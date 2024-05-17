package at.ase.respond.dispatcher.persistence.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the coordinates of a location, as a value object.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationCoordinatesVO {

    private Double latitude;

    private Double longitude;

}
