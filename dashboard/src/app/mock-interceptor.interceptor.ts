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
      locationCoordinates: {
        latitude: Math.random() * 0.1 + 48.227747192035764,
        longitude: Math.random() * 0.1 + 16.40545336304577
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
      requestedResourceType: "KTW",
      state: RequestState.OPEN,
      resourceId: resources[Math.floor(Math.random() * resources.length)].id
    };
    resourceRequests.push(request);
  }
}

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
      return of(new HttpResponse({ status: 200, body: resources }));
    } else if (req.url === "http://localhost:4200/resources?additional=true") {
      return of(new HttpResponse({ status: 200, body: resources }));
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
