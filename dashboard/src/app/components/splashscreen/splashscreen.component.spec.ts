import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SplashscreenComponent } from './splashscreen.component';
import {OAuthModule} from "angular-oauth2-oidc";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('SplashscreenComponent', () => {
  let component: SplashscreenComponent;
  let fixture: ComponentFixture<SplashscreenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SplashscreenComponent, HttpClientTestingModule, OAuthModule.forRoot()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SplashscreenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
