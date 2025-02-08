import { TestBed } from '@angular/core/testing';
import { async } from '@angular/core/testing';
import { ResourceService } from './resources.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable, of as observableOf, throwError } from 'rxjs';
import { RequestState } from '../dtos/resource-request';
describe('ResourcesService', () => {
  let service: ResourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [HttpClientTestingModule]});
    service = TestBed.inject(ResourceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should run #getResources()', async () => {
    (<any>service).httpClient = (<any>service).httpClient || {};
    spyOn((<any>service).httpClient, 'get');
    (<any>service).getResources();
    expect((<any>service).httpClient.get).toHaveBeenCalled();
  });

  it('should run #getResourcesAdditional()', async () => {
    (<any>service).httpClient = (<any>service).httpClient || {};
    spyOn((<any>service).httpClient, 'get');
    (<any>service).getResourcesAdditional();
    expect((<any>service).httpClient.get).toHaveBeenCalled();
  });

  it('should run #assignResources()', async () => {
    (<any>service).httpClient = (<any>service).httpClient || {};
    spyOn((<any>service).httpClient, 'post').and.returnValue(observableOf('post'));
    (<any>service).assignResources("", [""]);
    expect((<any>service).httpClient.post).toHaveBeenCalled();
  });

  it('should run #getOpenResourceRequests()', async () => {
    (<any>service).httpClient = (<any>service).httpClient || {};
    spyOn((<any>service).httpClient, 'get');
    (<any>service).getOpenResourceRequests();
    expect((<any>service).httpClient.get).toHaveBeenCalled();
  });

  it('should run #finishRequest()', async () => {
    (<any>service).httpClient = (<any>service).httpClient || {};
    spyOn((<any>service).httpClient, 'put').and.returnValue(observableOf('put'));
    (<any>service).finishRequest(
      {
        id: "id",
        assignedIncident: "",
        requestedResourceType: "",
        state: RequestState.FINISHED,
        resourceId: ""  
      }
    );
    expect((<any>service).httpClient.put).toHaveBeenCalled();
  });

  it('should run #completeIncident()', async () => {
    (<any>service).httpClient = (<any>service).httpClient || {};
    spyOn((<any>service).httpClient, 'put').and.returnValue(observableOf('put'));
    (<any>service).completeIncident("");
    expect((<any>service).httpClient.put).toHaveBeenCalled();
  });
});
