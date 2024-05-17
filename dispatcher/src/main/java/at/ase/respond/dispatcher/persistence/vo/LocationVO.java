package at.ase.respond.dispatcher.persistence.vo;

import lombok.Data;

/**
 * Represents a location of an incident, as a value object.
 */
@Data
public class LocationVO {

    private LocationAddressVO address;

    private LocationCoordinatesVO coordinates;

}
