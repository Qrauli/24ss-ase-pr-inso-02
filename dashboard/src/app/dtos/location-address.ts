/**
 * Represents the address of a location
 *
 * @param street the street of this location
 * @param postalCode the postal code of this location
 * @param city the city of this location
 * @param additionalInformation additional information of this location, e.g. central
 * station platform 1
 */
export interface LocationAddress {
  street: string;
  postalCode: string;
  // TODO maybe rename to place since this better represents its intention (may also be county, etc.)
  city: string;
  additionalInformation: string;
}

/**
 * Converts an object given the format returned by the geocoder to the internal format, i.e. {@link LocationAddress}
 *
 * @param geocoderAddressFormat object in the geocoder format
 */
export function geocoderAddressConverter(geocoderAddressFormat: { city: string, county: string, postcode: string, road: string, house_number: string }): LocationAddress {
  return {
    street: geocoderAddressFormat.road?.concat(geocoderAddressFormat.house_number ? " " + geocoderAddressFormat.house_number : ""),
    postalCode: geocoderAddressFormat.postcode,
    city: geocoderAddressFormat.city ? geocoderAddressFormat.city : geocoderAddressFormat.county,
    additionalInformation: ""
  };
}

/**
 * Gives a string that can be used to show the properly formated general address information
 *
 * @param locationAddress the address
 */
export function prettyLocationAddress(locationAddress: LocationAddress): string {
  return (locationAddress.street ? locationAddress.street + ", " : "") +
    (locationAddress.postalCode ? locationAddress.postalCode + " " : "") +
    (locationAddress.city ? locationAddress.city : "");
}


