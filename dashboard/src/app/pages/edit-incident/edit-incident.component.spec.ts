import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditIncidentComponent } from './edit-incident.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {OAuthModule} from "angular-oauth2-oidc";
import {RouterTestingModule} from "@angular/router/testing";
import { TranslateModule } from '@ngx-translate/core';

describe('AddIncidentComponent', () => {
  let component: EditIncidentComponent;
  let fixture: ComponentFixture<EditIncidentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditIncidentComponent, RouterTestingModule, NoopAnimationsModule, HttpClientTestingModule, OAuthModule.forRoot(), TranslateModule.forRoot() ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditIncidentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
