import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddIncidentComponent } from './add-incident.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {OAuthModule} from "angular-oauth2-oidc";
import { TranslateModule } from '@ngx-translate/core';

describe('AddIncidentComponent', () => {
  let component: AddIncidentComponent;
  let fixture: ComponentFixture<AddIncidentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddIncidentComponent, NoopAnimationsModule, HttpClientTestingModule, OAuthModule.forRoot(), TranslateModule.forRoot()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddIncidentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
