package at.ase.respond.common.dto;

import java.io.Serializable;

/**
 * Represents the address of a location.
 *
 * @param street                the street of this location
 * @param postalCode            the postal code of this location
 * @param city                  the city of this location
 * @param additionalInformation additional information of this location, e.g. central station platform 1
 */
public record LocationAddressDTO(
        String street,
        String postalCode,
        String city,
        String additionalInformation
) implements Serializable {
}
