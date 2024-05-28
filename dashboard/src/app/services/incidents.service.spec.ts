import { TestBed } from '@angular/core/testing';

import { IncidentService } from './incidents.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('IncidentsService', () => {
  let service: IncidentService;

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [HttpClientTestingModule]});
    service = TestBed.inject(IncidentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
