import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, switchMap, map } from 'rxjs';
import { Incident } from '../dtos/incident';
import { environment } from '../../environments/environment';
import { ResourceRecommendation } from '../dtos/resource-recommendation';

@Injectable({
  providedIn: 'root'
})
export class IncidentService {

  constructor(private httpClient: HttpClient) { }

  getIncidentsOngoing(): Observable<Incident[]> {
    return this.httpClient.get<Incident[]>(
      environment.incidentUrl + 'incidents',
      { headers: new HttpHeaders({
          'Content-Type': 'application/json',
        })});
  }

  getIncidentsOngoingDispatcher(): Observable<Incident[]> {
    return this.httpClient.get<Incident[]>(
      environment.resourceUrl + 'incidents',
      { headers: new HttpHeaders({
          'Content-Type': 'application/json',
        })});
  }

  getRecommendations(id: string): Observable<ResourceRecommendation[]> {
    return this.httpClient.get<ResourceRecommendation[]>(environment.resourceUrl + `incidents/${id}/recommendations`, { headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })});
  }

  getIncidentById(id: string): Observable<Incident> {
    //return this.httpClient.get<Incident>(environment.incidentUrl + `incidents/${id}`);
    return this.httpClient.get<Incident>(environment.incidentUrl + `incidents/${id}`, { headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })})
    .pipe(
      switchMap(
        incident =>{
          return this.httpClient.get<Incident>(environment.resourceUrl + `incidents/${id}`, { headers: new HttpHeaders({
              'Content-Type': 'application/json',
            })})
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
    return this.httpClient.post<Incident>(environment.incidentUrl + 'incidents', incident, { headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })});
  }

  updateIncident(incident: Incident): Observable<Incident> {
    return this.httpClient.put<Incident>(environment.incidentUrl + 'incidents', incident, { headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })});
  }
}
