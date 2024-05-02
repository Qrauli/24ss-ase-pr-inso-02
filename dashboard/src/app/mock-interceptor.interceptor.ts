import {HttpInterceptorFn, HttpResponse} from '@angular/common/http';
import {of} from 'rxjs';
import {Incident, Status} from "./dto/incident";
import {Patient, Sex} from "./dto/patient";

const incidents: Incident[] = [];

function mockIncidents(entries: number) {
  if (incidents.length >= entries) return;

  for (let i = 0; i < entries; i++) {

    const patients: Patient[] = [];
    for (let j = 0; j < i + 1; j++) {
      const patient: Patient = {
        age: j,
        sex: Sex.UNKNOWN
      }
      patients.push(patient);
    }

    const incident: Incident = {
      id: i.toString(),
      patients: patients,
      numberOfPatients: patients.length,
      categorization: {
        id: i.toString(),
        code: "code" + i
      },
      location: {
        description: {
          street: "Hauptstrasse " + i,
          postalCode: "100" + i,
          city: "Wien",
          additionalInformation: "Bei Tor " + i + " richtig stark dagegen treten."
        },
        coordinates: {
          latitude: 48.227747192035764,
          longitude: 16.40545336304577
        }
      },
      status: i % 2 == 0 ? Status.CREATED : Status.DISPATCHED
    };
    incidents.push(incident);
  }
}

const resourceData = [
  {id: 'FLO-1', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'FLO-2', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'FLO-3', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'FLO-4', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'FLO-5', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'FLO-6', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'FLO-7', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'FLO-8', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'FLO-9', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'FLO-10', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'FLO-11', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'FLO-12', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'FLO-13', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'FLO-14', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'FLO-15', type: 'Rettungswagen', location: 'Wien', assigned: true},
  {id: 'FLO-16', type: 'Rettungswagen', location: 'Wien', assigned: true},
  {id: 'FLO-17', type: 'Rettungswagen', location: 'Wien', assigned: true},
  {id: 'FLO-18', type: 'Rettungswagen', location: 'Wien', assigned: true},
  {id: 'FLO-19', type: 'Rettungswagen', location: 'Wien', assigned: true},
  {id: 'FLO-20', type: 'Rettungswagen', location: 'Wien', assigned: true},
];

const resourceDataAdditional = [
  {id: 'RKK-1', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-2', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-3', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-4', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-5', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-6', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-7', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-8', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-9', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-10', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-11', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-12', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-13', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-14', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-15', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-16', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-17', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-18', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-19', type: 'Rettungswagen', location: 'Wien', assigned: false},
  {id: 'RKK-20', type: 'Rettungswagen', location: 'Wien', assigned: false},
];

export const mockInterceptor: HttpInterceptorFn = (req, next) => {
  mockIncidents(6);

  if (req.method === "GET") {
    if (req.url === "http://localhost:4200/incidents") {
      return of(new HttpResponse({status: 200, body: incidents}));
    } else if (req.url.startsWith("http://localhost:4200/incidents/")) {
      const parts = req.url.split("/");
      const id = parts[parts.length - 1];
      const incident = incidents.find(incident => incident.id === id);
      if (incident) {
        return of(new HttpResponse({status: 200, body: incident}));
      } else {
        return of(new HttpResponse({status: 404}));
      }
    } else if (req.url === "http://localhost:4200/resources?additional=false") {
      return of(new HttpResponse({status: 200, body: resourceData}));
    } else if (req.url === "http://localhost:4200/resources?additional=true") {
      return of(new HttpResponse({status: 200, body: resourceDataAdditional}));
    }
  }

  if (req.method === "POST") {
    if (req.url === "http://localhost:4200/incidents") {
      const incident = req.body as Incident;
      incident.id = incidents.length.toString();
      incidents.push(incident);
      return of(new HttpResponse({status: 200, body: incident}));
    }
  }

  if (req.method === "PUT") {
    if (req.url === "http://localhost:4200/incidents") {
      // TODO not tested yet
      const updated = req.body as Incident;
      const index = incidents.findIndex(incident => incident.id === updated.id);
      if (index == -1) {
        return of(new HttpResponse({status: 404}));
      } else {
        incidents[index] = updated;
        return of(new HttpResponse({status: 200, body: incidents[index]}));
      }
    }
  }

  return next(req);
};
