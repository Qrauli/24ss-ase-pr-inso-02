export enum RequestState {
    FINISHED = "FINISHED",
    OPEN = "OPEN"
  }

export interface ResourceRequest {
    id: string;
    assignedIncident: string;
    requestedResourceType: string;
    state: RequestState;
    resourceId: string;

}