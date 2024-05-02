import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Incident } from './dto/incident';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class IncidentService {

  constructor(private httpClient: HttpClient) { }

  getIncidentsOngoing(): Observable<Incident[]> {
    return this.httpClient.get<Incident[]>( environment.incidentUrl + 'incidents');
  }

  getIncidentById(id: string): Observable<Incident> {
    return this.httpClient.get<Incident>(environment.incidentUrl + `incidents/${id}`);
  }

  saveIncident(incident: Incident): Observable<Incident> {
    return this.httpClient.post<Incident>(environment.incidentUrl + 'incidents', incident);
  }

  updateIncident(incident: Incident): Observable<Incident> {
    return this.httpClient.put<Incident>(environment.incidentUrl + 'incidents', incident);
  }
}
