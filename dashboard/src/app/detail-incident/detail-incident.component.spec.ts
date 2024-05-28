import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailIncidentComponent } from './detail-incident.component';

import { RouterTestingModule } from "@angular/router/testing";
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {OAuthModule} from "angular-oauth2-oidc";


describe('DetailIncidentComponent', () => {
  let component: DetailIncidentComponent;
  let fixture: ComponentFixture<DetailIncidentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailIncidentComponent, RouterTestingModule, HttpClientTestingModule, OAuthModule.forRoot()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailIncidentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
