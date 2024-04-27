package at.ase.respond.incident.persistence.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a location value object containing coordinates and address.
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private LocationCoordinates coordinates;

    private LocationAddress address;

}
