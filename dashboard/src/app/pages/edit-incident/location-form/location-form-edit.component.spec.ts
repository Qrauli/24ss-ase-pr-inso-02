import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LocationFormEditComponent } from './location-form-edit.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import { TranslateModule } from '@ngx-translate/core';

describe('AddIncidentComponent', () => {
  let component: LocationFormEditComponent;
  let fixture: ComponentFixture<LocationFormEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LocationFormEditComponent, NoopAnimationsModule, HttpClientTestingModule, TranslateModule.forRoot()]
    })
      .compileComponents();

    fixture = TestBed.createComponent(LocationFormEditComponent);
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
