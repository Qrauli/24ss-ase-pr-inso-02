import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {Answer, Categorization} from "../dtos/categorization";

@Injectable({
  providedIn: 'root'
})
export class CategorizationService {

  constructor(private httpClient: HttpClient) { }

  createSession(): Observable<Categorization> {
    return this.httpClient.post<Categorization>(environment.categorizationUrl + 'categorization', null, { headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${environment.mockCalltakerToken}`
      })});
  }

  saveAnswer(sessionId: string, answer: Answer): Observable<Categorization> {
    return this.httpClient.put<Categorization>(environment.categorizationUrl + `categorization/${sessionId}`, answer, { headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${environment.mockCalltakerToken}`
      })});
  }

  findById(sessionId: string): Observable<Categorization> {
    return this.httpClient.get<Categorization>(environment.categorizationUrl + `categorization/${sessionId}`, { headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${environment.mockCalltakerToken}`
      })});
  }

}
