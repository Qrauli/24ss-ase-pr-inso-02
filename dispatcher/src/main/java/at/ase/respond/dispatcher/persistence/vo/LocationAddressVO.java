package at.ase.respond.dispatcher.persistence.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the address of a location, as a value object.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationAddressVO {

    private String street;

    private String postalCode;

    private String city;

    private String additionalInformation;

}
