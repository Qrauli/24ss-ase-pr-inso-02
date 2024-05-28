import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Resource } from '../dtos/resource';
import { environment } from '../../environments/environment';
import { RequestState, ResourceRequest } from '../dtos/resource-request';

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

  getOpenResourceRequests(): Observable<ResourceRequest[]> {
    return this.httpClient.get<ResourceRequest[]>(environment.resourceUrl + 'requests');
  }

  finishRequest(request: ResourceRequest): Observable<ResourceRequest> {
    return this.httpClient.put<ResourceRequest>(environment.resourceUrl + 'requests/' + request.id + '/state', null);
  }


}
