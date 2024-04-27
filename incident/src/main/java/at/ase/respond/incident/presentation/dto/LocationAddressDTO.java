package at.ase.respond.incident.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * Represents the address of a location
 *
 * @param street the street of this location
 * @param postalCode the postal code of this location
 * @param city the city of this location
 * @param additionalInformation additional information of this location, e.g. central
 * station platform 1
 */
@Schema(description = "A DTO representing the address of a location")
public record LocationAddressDTO(String street, String postalCode, String city,
        String additionalInformation) implements Serializable {
}
