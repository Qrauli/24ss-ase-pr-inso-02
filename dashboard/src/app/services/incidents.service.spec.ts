import { TestBed } from '@angular/core/testing';

import { IncidentService } from './incidents.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable, of as observableOf, throwError } from 'rxjs';
import { State } from '../dtos/incident';

describe('IncidentsService', () => {
  let service: IncidentService;

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [HttpClientTestingModule]});
    service = TestBed.inject(IncidentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should run #getIncidentsOngoing()', async () => {
    (<any>service).httpClient = (<any>service).httpClient || {};
    spyOn((<any>service).httpClient, 'get');
    (<any>service).getIncidentsOngoing();
    expect((<any>service).httpClient.get).toHaveBeenCalled();
  });

  it('should run #getIncidentsOngoingDispatcher()', async () => {
    (<any>service).httpClient = (<any>service).httpClient || {};
    spyOn((<any>service).httpClient, 'get');
    (<any>service).getIncidentsOngoingDispatcher();
    expect((<any>service).httpClient.get).toHaveBeenCalled();
  });

  it('should run #getRecommendations()', async () => {
    (<any>service).httpClient = (<any>service).httpClient || {};
    spyOn((<any>service).httpClient, 'get');
    (<any>service).getRecommendations("");
    expect((<any>service).httpClient.get).toHaveBeenCalled();
  });

  it('should run #getIncidentById()', async () => {
    (<any>service).httpClient = (<any>service).httpClient || {};
    spyOn((<any>service).httpClient, 'get').and.returnValue(observableOf({}));
    (<any>service).getIncidentById("");
    expect((<any>service).httpClient.get).toHaveBeenCalled();
  });

  it('should run #saveIncident()', async () => {
    (<any>service).httpClient = (<any>service).httpClient || {};
    spyOn((<any>service).httpClient, 'post').and.returnValue(observableOf('post'));
    (<any>service).saveIncident(
      {
        id: "",
        state: State.READY,
        location: {
          address: {
            street: "",
            postalCode: "",
            city: "",
            additionalInformation: ""
          },
          coordinates: {
            latitude: 0,
            longitude: 0
          }
        },
        patients: [],
        numberOfPatients: 0,
        code: "",
        questionaryId: ''
      }
    );
    expect((<any>service).httpClient.post).toHaveBeenCalled();
  });

  it('should run #updateIncident()', async () => {
    (<any>service).httpClient = (<any>service).httpClient || {};
    spyOn((<any>service).httpClient, 'put').and.returnValue(observableOf('put'));
    (<any>service).updateIncident(
      {
        id: "",
        state: State.READY,
        location: {
          address: {
            street: "",
            postalCode: "",
            city: "",
            additionalInformation: ""
          },
          coordinates: {
            latitude: 0,
            longitude: 0
          }
        },
        patients: [],
        numberOfPatients: 0,
        code: "",
        questionaryId: ''
      }
    );
    expect((<any>service).httpClient.put).toHaveBeenCalled();
  });
});
