import { TestBed } from '@angular/core/testing';

import { ResourceService } from './resources.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('ResourcesService', () => {
  let service: ResourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [HttpClientTestingModule]});
    service = TestBed.inject(ResourceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
