import {Patient} from "./patient";
import {Categorization} from "./categorization";
import {Location} from "./location";

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
