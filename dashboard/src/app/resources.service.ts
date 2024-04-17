import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Resource } from './dto/resource';

@Injectable({
  providedIn: 'root'
})
export class ResourceService {

  constructor(private httpClient: HttpClient) { }

  getResources(): Observable<Resource[]> {
    return this.httpClient.get<Resource[]>('http://localhost:4200/resources?additional=false');

  }

  getResourcesAdditional(){
    return this.httpClient.get<Resource[]>('http://localhost:4200/resources?additional=true');
  }
}
