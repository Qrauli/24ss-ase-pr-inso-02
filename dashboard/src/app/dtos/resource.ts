import {LocationCoordinates} from "./locationCoordinates";

export enum ResourceType {
  KTW = "KTW",
  RTW = "RTW",
  FISU = "FISU",
  NEF = "NEF",
  NAH = "NAH",
}

export enum ResourceState {
  AVAILABE = "AVAILABLE",
  DISPATCHED = "DISPATCHED",
  ON_ROUTE_TO_INCIDENT = "ON_ROUTE_TO_INCIDENT",
  AT_INCIDENT = "AT_INCIDENT",
  ON_ROUTE_TO_HOSPITAL = "ON_ROUTE_TO_HOSPITAL",
  AT_HOSPITAL = "AT_HOSPITAL",
  UNAVAILABLE = "UNAVAILABLE",
}

export interface Resource {
    id: string;
    type: ResourceType;
    state: ResourceState;
    locationCoordinates: LocationCoordinates;
    assignedIncident?: string;
}
