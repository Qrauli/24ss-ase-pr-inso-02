import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddIncidentComponent } from './add-incident.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('AddIncidentComponent', () => {
  let component: AddIncidentComponent;
  let fixture: ComponentFixture<AddIncidentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddIncidentComponent, NoopAnimationsModule, HttpClientTestingModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddIncidentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have a table', () => {
    expect(component.table).toBeTruthy();
  });

  it('should have a map', () => {
    expect(component.map).toBeTruthy();
  });

  it('should have an osmGeocoder', () => {
    expect(component.osmGeocoder).toBeTruthy();
  });


});
