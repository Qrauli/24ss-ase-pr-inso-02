package at.ase.respond.incident.persistence.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a location coordinates value object.
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class LocationCoordinates {

    private Double latitude;

    private Double longitude;

}
