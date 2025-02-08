package at.ase.respond.incident.persistence.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a location address value object.
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class LocationAddress {

    private String street;

    private String postalCode;

    private String city;

    private String additionalInformation;

}
