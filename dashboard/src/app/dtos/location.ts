import {LocationAddress} from "./location-address";
import {LocationCoordinates} from "./location-coordinates";

/**
 * Represents a location containing a description as well as coordinates.
 *
 * @param address the description of this location
 * @param coordinates the coordinates of this location
 */
export interface Location {
  address: LocationAddress;
  coordinates: LocationCoordinates;
}
