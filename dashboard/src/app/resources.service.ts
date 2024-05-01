import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Resource } from './dto/resource';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ResourceService {

  constructor(private httpClient: HttpClient) { }

  getResources(): Observable<Resource[]> {
    return this.httpClient.get<Resource[]>('http://localhost:8082/resources?additional=false');

  }

  getResourcesAdditional(){
    return this.httpClient.get<Resource[]>('http://localhost:8082/resources?additional=true');
  }

  assignResources(incident: number, resources: Resource[]): void {
    resources.map(resource =>
      this.httpClient
        .post<Resource>(`http://localhost:8082/resources/${resource.id}/assign/${incident}`, null)
        .subscribe()
    );
  }
}
