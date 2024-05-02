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
  // TODO rename postcode
  postalCode: string;
  // TODO rename place
  city: string;
  additionalInformation: string;
}
