import {LocationCoordinates} from "./locationCoordinates";

export enum ResourceType {
  NEF = "NEF",
  RTW = "RTW",
  KTW = "KTW",
}

export interface Resource {
    id: string;
    type: ResourceType;
    location: LocationCoordinates;
    assignedIncident?: string;
}
