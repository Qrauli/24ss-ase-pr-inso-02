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
    return this.httpClient.get<Resource[]>(environment.resourceUrl + 'resources?additional=false');

  }

  getResourcesAdditional(){
    return this.httpClient.get<Resource[]>(environment.resourceUrl + 'resources?additional=true');
  }

  assignResources(incident: string, resources: Resource[]): void {
    resources.map(resource =>
      this.httpClient
        .post<Resource>(environment.resourceUrl + `resources/${resource.id}/assign/${incident}`, null)
        .subscribe()
    );
  }
}
