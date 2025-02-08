import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import {Answer, Categorization } from "../dtos/categorization";

@Injectable({
  providedIn: 'root'
})
export class CategorizationService {

  constructor(private httpClient: HttpClient) { }

  createSession(): Observable<Categorization> {
    return this.httpClient.post<Categorization>(environment.categorizationUrl + 'categorization', null, { headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })});
  }

  saveAnswer(sessionId: string, answer: Answer): Observable<Categorization> {
    return this.httpClient.put<Categorization>(environment.categorizationUrl + `categorization/${sessionId}`, answer, { headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })});
  }

  findById(sessionId: string): Observable<Categorization> {
    return this.httpClient.get<Categorization>(environment.categorizationUrl + `categorization/${sessionId}`, { headers: new HttpHeaders({
        'Content-Type': 'application/json',
      })});
  }

  getBaseQuestionBundles(categorization: Categorization) {
    return categorization.questionBundles
      .filter(bundle => bundle.baseQuestion != undefined);
  }

  getBaseQuestionBundle(categorization: Categorization, id: string) {
    return this.getBaseQuestionBundles(categorization)
      .find(bundle => bundle.baseQuestion!.id == id);
  }

  isBaseQuestionAnswered(categorization: Categorization, id: string) {
    return this.getBaseQuestionBundle(categorization, id)?.answer != undefined;
  }

  getProtocolQuestionBundles(categorization: Categorization) {
    return categorization.questionBundles
      .filter(bundle => bundle.protocolQuestion != undefined);
  }

  getProtocolQuestionBundle(categorization: Categorization, id: string) {
    return this.getProtocolQuestionBundles(categorization)
      .find(bundle => bundle.protocolQuestion!.id == id);
  }

  isProtocolQuestionAnswered(categorization: Categorization, id: string) {
    return this.getProtocolQuestionBundle(categorization, id)?.answer != undefined;
  }

}
