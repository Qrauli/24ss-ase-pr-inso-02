import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderComponent } from './header.component';
import {OAuthModule} from "angular-oauth2-oidc";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import { TranslateModule } from '@ngx-translate/core';
import { Observable, of as observableOf, throwError } from 'rxjs';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeaderComponent, HttpClientTestingModule, OAuthModule.forRoot(), TranslateModule.forRoot()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should run #logout()', async () => {
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'instant');
    (<any>component).dialog = (<any>component).dialog || {};
    spyOn((<any>component).dialog, 'open').and.returnValue({
      afterClosed: function() {
        return observableOf({});
      }
    });
    (<any>component).authService = (<any>component).authService || {};
    spyOn((<any>component).authService, 'logout');
    (<any>component).logout();
    expect((<any>component).translate.instant).toHaveBeenCalled();
    expect((<any>component).dialog.open).toHaveBeenCalled();
  });

  it('should run #goBack()', async () => {
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'instant');
    (<any>component).dialog = (<any>component).dialog || {};
    spyOn((<any>component).dialog, 'open').and.returnValue({
      afterClosed: function() {
        return observableOf({});
      }
    });
    (<any>component).router = (<any>component).router || {};
    spyOn((<any>component).router, 'navigate');
    (<any>component).goBack();
    expect((<any>component).translate.instant).toHaveBeenCalled();
    expect((<any>component).dialog.open).toHaveBeenCalled();
  });

  it('should run #changeLang()', async () => {
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'use');
    (<any>component).changeLang({});
    expect((<any>component).translate.use).toHaveBeenCalled();
  });
});
