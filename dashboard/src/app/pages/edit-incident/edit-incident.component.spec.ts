import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditIncidentComponent } from './edit-incident.component';
import { RouterTestingModule } from "@angular/router/testing";
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {OAuthModule} from "angular-oauth2-oidc";
import {HttpClientTestingModule} from "@angular/common/http/testing";


describe('EditIncidentComponent', () => {
  let component: EditIncidentComponent;
  let fixture: ComponentFixture<EditIncidentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditIncidentComponent, HttpClientTestingModule, RouterTestingModule, NoopAnimationsModule, OAuthModule.forRoot()]
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
