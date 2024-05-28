import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, switchMap, map } from 'rxjs';
import { Incident } from '../dtos/incident';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class IncidentService {

  constructor(private httpClient: HttpClient) { }

  getIncidentsOngoing(): Observable<Incident[]> {
    return this.httpClient.get<Incident[]>(environment.resourceUrl + 'incidents');
  }

  getIncidentById(id: string): Observable<Incident> {
    //return this.httpClient.get<Incident>(environment.incidentUrl + `incidents/${id}`);
    return this.httpClient.get<Incident>(environment.incidentUrl + `incidents/${id}`)
    .pipe(
      switchMap(
        incident =>{
          return this.httpClient.get<Incident>(environment.resourceUrl + `incidents/${id}`)
          .pipe(
            map(incidentState => {
              incident.state = incidentState.state;
              //TODO: add categorization questions
              return incident;
            })
          )
        }
      ));
  }

  saveIncident(incident: Incident): Observable<Incident> {
    return this.httpClient.post<Incident>(environment.incidentUrl + 'incidents', incident);
  }

  updateIncident(incident: Incident): Observable<Incident> {
    return this.httpClient.put<Incident>(environment.incidentUrl + 'incidents', incident);
  }
}
