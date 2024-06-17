import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalltakerComponent } from './calltaker.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {OAuthModule} from "angular-oauth2-oidc";
import { TranslateModule } from '@ngx-translate/core';


describe('CalltakerComponent', () => {
  let component: CalltakerComponent;
  let fixture: ComponentFixture<CalltakerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CalltakerComponent, NoopAnimationsModule, HttpClientTestingModule, OAuthModule.forRoot(), TranslateModule.forRoot()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CalltakerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
