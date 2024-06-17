import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LocationFormComponent } from './location-form.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import { TranslateModule } from '@ngx-translate/core';

describe('AddIncidentComponent', () => {
  let component: LocationFormComponent;
  let fixture: ComponentFixture<LocationFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LocationFormComponent, NoopAnimationsModule, HttpClientTestingModule, TranslateModule.forRoot()]
    })
      .compileComponents();

    fixture = TestBed.createComponent(LocationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have a map', () => {
    expect(component.map).toBeTruthy();
  });
});
