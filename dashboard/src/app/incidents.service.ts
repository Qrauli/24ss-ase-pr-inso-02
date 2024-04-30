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
    return this.httpClient.get<Incident[]>('http://localhost:4200/incidents');
  }

  getIncidentById(id: number): Observable<Incident> {
    return this.httpClient.get<Incident>(`http://localhost:4200/incidents/${id}`);
  }


}
