import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {OAuthModule} from "angular-oauth2-oidc";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import { TranslateModule } from '@ngx-translate/core';


describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginComponent, HttpClientTestingModule, NoopAnimationsModule, OAuthModule.forRoot(), TranslateModule.forRoot()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have a form', () => {
    expect((<any>component).form).toBeTruthy();
  });

  it('should run #constructor()', async () => {
    expect(component).toBeTruthy();
  });

  it('should run #loginOauth()', async () => {
    (<any>component).form = (<any>component).form || {};
    (<any>component).form.value = {
      userName: {}
    };
    (<any>component).loginOauth();
    expect((<any>component).form.value.userName).toEqual({});
  });


});
