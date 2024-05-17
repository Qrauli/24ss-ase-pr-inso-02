package at.ase.respond.common.dto;

import java.io.Serializable;

/**
 * Represents a location.
 *
 * @param address     the address of the location
 * @param coordinates the coordinates of the location
 */
public record LocationDTO(
        LocationAddressDTO address,
        LocationCoordinatesDTO coordinates
) implements Serializable {
}
