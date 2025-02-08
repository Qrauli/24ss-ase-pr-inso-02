import { TestBed, async } from '@angular/core/testing';
import { Injectable } from '@angular/core';
import { Observable, of as observableOf, throwError } from 'rxjs';

import { AuthService } from './auth.service';
import { OAuthModule, OAuthService } from 'angular-oauth2-oidc';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';

@Injectable()
class MockRouter {
  navigate() {};
}

describe('AuthService', () => {
  let service: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [OAuthModule.forRoot(), HttpClientTestingModule], providers: [{provide: Router, useClass: MockRouter}]});
    service = TestBed.inject(AuthService);
  });

  it('should run #isLoggedIn()', async () => {
    (<any>service).oauthService = (<any>service).oauthService || {};
    spyOn((<any>service).oauthService, 'hasValidAccessToken');
    (<any>service).isLoggedIn();
    expect((<any>service).oauthService.hasValidAccessToken).toHaveBeenCalled();
  });

  it('should run #loginOauth()', async () => {
    spyOn(service, 'isLoggedIn');
    (<any>service).oauthService = (<any>service).oauthService || {};
    spyOn((<any>service).oauthService, 'tryLoginImplicitFlow').and.returnValue({
      then: function() {
        return {
          catch: function() {
            return [
              null
            ];
          }
        };
      }
    });
    spyOn<any>(service, 'storeIdToken');
    (<any>service).loginOauth();
    expect((<any>service).isLoggedIn).toHaveBeenCalled();
    expect((<any>service).oauthService.tryLoginImplicitFlow).toHaveBeenCalled();
  });

  it('should run #logout()', async () => {
    spyOn<any>(service, 'logoutOauth');
    (<any>service).logout();
    expect((<any>service).logoutOauth).toHaveBeenCalled();
  });

  it('should run #logoutOauth()', async () => {
    (<any>service).oauthService = (<any>service).oauthService || {};
    spyOn((<any>service).oauthService, 'logOut');
    (<any>service).router = (<any>service).router || {};
    spyOn((<any>service).router, 'navigate');
    (<any>service).logoutOauth();
    expect((<any>service).oauthService.logOut).toHaveBeenCalled();
    expect((<any>service).router.navigate).toHaveBeenCalled();
  });

  it('should run #getStoredUserrole()', async () => {
    spyOn<any>(service, 'getStoredIdToken').and.returnValue("getStoredIdToken");
    (<any>service).oauthService = (<any>service).oauthService || {};
    spyOn((<any>service).oauthService, 'processIdToken').and.returnValue(Promise.resolve({
        idTokenClaims: {
          realm_access: {
            roles: []
          }
        }
      }));
    await (<any>service).getStoredUserrole();
    expect((<any>service).getStoredIdToken).toHaveBeenCalled();
  });

  
  it('should run #getStoredUserroleError()', async () => {
    spyOn<any>(service, 'getStoredIdToken').and.returnValue("getStoredIdToken");
    (<any>service).oauthService = (<any>service).oauthService || {};
    spyOn((<any>service).oauthService, 'processIdToken').and.returnValue(Promise.reject("error"));
    await (<any>service).getStoredUserrole();
    expect((<any>service).getStoredIdToken).toHaveBeenCalled();
  });

  it('should run #getStoredUserroleErrorNoToken()', async () => {
    spyOn<any>(service, 'getStoredIdToken');
    (<any>service).oauthService = (<any>service).oauthService || {};
    await (<any>service).getStoredUserrole();
    expect((<any>service).getStoredIdToken).toHaveBeenCalled();
  });

  it('should run #storeIdToken()', async () => {

    (<any>service).storeIdToken("");
    expect(localStorage.getItem('id_token')).toEqual("");
  });

  it('should run #getStoredIdToken()', async () => {

    expect((<any>service).getStoredIdToken("")).toBeFalsy();

  });

  it('should run #triggerLogin()', async () => {
    (<any>service).oauthService = (<any>service).oauthService || {};
    spyOn((<any>service).oauthService, 'initImplicitFlow');
    (<any>service).triggerLogin();
    expect((<any>service).oauthService.initImplicitFlow).toHaveBeenCalled();
  });

});