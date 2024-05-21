import { HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { Incident, State } from "./dto/incident";
import { Patient, Sex } from "./dto/patient";
import { v4 as uuidv4 } from 'uuid';
import { Resource, ResourceState, ResourceType } from './dto/resource';
import { RequestState, ResourceRequest } from './dto/resource-request';

let incidents: Incident[] = [];
let resources: Resource[] = [];
let resourceRequests: ResourceRequest[] = [];

function mockIncidents(entries: number) {
  if (incidents.length >= entries) return;
  let length = incidents.length;

  for (let i = 0; i < entries - length; i++) {

    const patients: Patient[] = [];
    for (let j = 0; j < i + 1; j++) {
      const patient: Patient = {
        age: j,
        sex: Sex.UNKNOWN
      }
      patients.push(patient);
    }

    const incident: Incident = {
      id: uuidv4(),
      patients: patients,
      numberOfPatients: patients.length,
      code: "code" + incidents.length,
      questionaryId: "questionary" + incidents.length,
      location: {
        address: {
          street: "Hauptstrasse " + incidents.length,
          postalCode: "100" + incidents.length,
          city: "Wien",
          additionalInformation: "Bei Tor " + incidents.length + " richtig stark dagegen treten."
        },
        coordinates: {
          latitude: 48.227747192035764,
          longitude: 16.40545336304577
        }
      },
      state: incidents.length % 2 == 0 ? State.READY : State.DISPATCHED
    };
    incidents.push(incident);
  }
}

function mockResources(entries: number) {
  if (resources.length >= entries) return;
  let length = resources.length;

  for (let i = 0; i < entries - length; i++) {
    let assignment: string | undefined = incidents[Math.floor(Math.random() * incidents.length)].id;
    if (Math.random() < 0.7) {
      assignment = undefined;
    }
    const resource: Resource = {
      id: "FLO-" + length,
      type: ResourceType.KTW,
      state: assignment ? ResourceState.DISPATCHED : ResourceState.AVAILABE,
      location: {
        latitude: 48.227747192035764,
        longitude: 16.40545336304577
      },
      assignedIncident: assignment
    };
    resources.push(resource);
  }
}
function mockResourceRequests(entries: number) {
  if (resourceRequests.length >= entries) return;
  let length = resourceRequests.length;

  for (let i = 0; i < entries - length; i++) {
    const request: ResourceRequest = {
      id: uuidv4(),
      assignedIncident: incidents[Math.floor(Math.random() * incidents.length)].id,
      requestedResourceType: "Request " + i,
      state: RequestState.OPEN,
      resourceId: resources[Math.floor(Math.random() * resources.length)].id
    };
    resourceRequests.push(request);
  }
}

const resourceData = [
  { id: 'FLO-1', type: 'Rettungswagen', location: 'Wien', assignedIncident: 'test' },
  { id: 'FLO-2', type: 'Rettungswagen', location: 'Wien', assignedIncident: 'test' },
  { id: 'FLO-3', type: 'Rettungswagen', location: 'Wien', assignedIncident: 'test' },
  { id: 'FLO-4', type: 'Rettungswagen', location: 'Wien', assignedIncident: 'test' },
  { id: 'FLO-5', type: 'Rettungswagen', location: 'Wien', assignedIncident: 'test' },
  { id: 'FLO-6', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'FLO-7', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'FLO-8', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'FLO-9', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'FLO-10', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'FLO-11', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'FLO-12', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'FLO-13', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'FLO-14', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'FLO-15', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'FLO-16', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'FLO-17', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'FLO-18', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'FLO-19', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'FLO-20', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
];

const resourceDataAdditional = [
  { id: 'RKK-1', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-2', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-3', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-4', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-5', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-6', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-7', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-8', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-9', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-10', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-11', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-12', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-13', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-14', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-15', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-16', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-17', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-18', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-19', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
  { id: 'RKK-20', type: 'Rettungswagen', location: 'Wien', assignedIncident: null },
];

export const mockInterceptor: HttpInterceptorFn = (req, next) => {

  incidents = Object.assign([], incidents);
  mockIncidents(6);
  mockResources(5);
  mockResourceRequests(3);

  if (req.method === "GET") {
    if (req.url === "http://localhost:4200/incidents") {
      if (Math.random() < 0.5 && incidents.length < 10) {
        mockIncidents(incidents.length + 1);
      }
      return of(new HttpResponse({ status: 200, body: incidents }));
    }
    else if (req.url.startsWith("http://localhost:4200/incidents/")) {
      const parts = req.url.split("/");
      const id = parts[parts.length - 1];
      const incident = incidents.find(incident => incident.id === id);
      if (incident) {
        return of(new HttpResponse({ status: 200, body: incident }));
      } else {
        return of(new HttpResponse({ status: 404 }));
      }
    } else if (req.url === "http://localhost:4200/resources?additional=false") {
      return of(new HttpResponse({ status: 200, body: resourceData }));
    } else if (req.url === "http://localhost:4200/resources?additional=true") {
      return of(new HttpResponse({ status: 200, body: resourceDataAdditional }));
    } else if (req.url === "http://localhost:4200/requests") {
      return of(new HttpResponse({ status: 200, body: resourceRequests }));
    }
  }

  if (req.method === "POST") {
    if (req.url === "http://localhost:4200/incidents") {
      const incident = req.body as Incident;
      incident.id = incidents.length.toString();
      incidents.push(incident);
      return of(new HttpResponse({ status: 200, body: incident }));
    }
  }

  if (req.method === "PUT") {
    if (req.url === "http://localhost:4200/incidents") {
      // TODO not tested yet
      const updated = req.body as Incident;
      const index = incidents.findIndex(incident => incident.id === updated.id);
      if (index == -1) {
        return of(new HttpResponse({ status: 404 }));
      } else {
        incidents[index] = updated;
        return of(new HttpResponse({ status: 200, body: incidents[index] }));
      }
    }
    if (req.url.startsWith("http://localhost:4200/requests/")) {
      const parts = req.url.split("/");
      const id = parts[parts.length - 2];
      const index = resourceRequests.findIndex(request => request.id === id);
      if (index) {
        resourceRequests.splice(index, 1);
        const updated = req.body as ResourceRequest;
        return of(new HttpResponse({ status: 200, body: updated }));
      } else {
        return of(new HttpResponse({ status: 404 }));
      }
    }
  }

  return next(req);
};
