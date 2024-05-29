/**
 * Represents an incident.
 *
 * @param id the id of the incident (can be {@code null})
 * @param patients a collection of involved patients, typically there is a single patient
 * @param numberOfPatients the number of involved patients, typically 1
 * @param categorization the categorization of the incident
 * @param location the location of the incident
 * @param status status of the incident
 */
export interface Incident {
  id: string;
  patients: Patient[];
  numberOfPatients: number;
  code: String;
  location: Location;
  state: State;
  questionaryId: string;
}

// TODO define status
export enum State {
  READY="READY",
  DISPATCHED="DISPATCHED",
  COMPLETED="COMPLETED"
}

/**
 * Represents a location containing a description as well as coordinates.
 *
 * @param address the description of this location
 * @param coordinates the coordinates of this location
 */
export interface Location {
  address: LocationAddress;
  coordinates?: LocationCoordinates;
}

/**
 * Represents the coordinates of a location.
 *
 * @param latitude the latitude of the location
 * @param longitude the longitude of the location
 */
export interface LocationCoordinates {
  latitude: number;
  longitude: number;
}

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
 * Represents a patient.
 *
 * @param age the approximate age of the patient
 * @param sex the sex of the patient
 */
export interface Patient {
  age: number;
  sex: Sex;
}

export enum Sex {
  UNKNOWN = "Unknown",
  FEMALE = "Female",
  MALE = "Male"
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
