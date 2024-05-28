import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {OAuthService} from "angular-oauth2-oidc";
import {authConfig} from "./auth.config";
import {JwksValidationHandler} from "angular-oauth2-oidc-jwks";

@Injectable({
    providedIn: 'root',
})
export class AuthService {

  constructor(
    private oauthService: OAuthService,
    private router: Router
  ) {
    this.oauthService.configure(authConfig);
    this.oauthService.tokenValidationHandler = new JwksValidationHandler();
  }

  /**
   *
   * @returns true if the user is logged in, false otherwise
   */

  isLoggedIn() {
    return this.oauthService.hasValidAccessToken();
  }

  /**
   * Logs in the user
   * @param user the user to log in
   */

  loginOauth() {
    if (!this.isLoggedIn()) {
      this.oauthService.tryLoginImplicitFlow(
        {
          onTokenReceived: context => {
            console.log("onTokenReceived", context);
            this.storeIdToken(context.accessToken);
          },
          disableNonceCheck: true
        })
        .then(r => console.log("tryLogin result: ", r))
        .catch(e => console.error("tryLogin error", e));
        }
    }

  /**
   * Logs out the user
   */

  logout() {
    this.logoutOauth();
  }

  private logoutOauth() {
    localStorage.removeItem('userrole');
    localStorage.removeItem('id_token');
    this.oauthService.logOut();
    this.router.navigate(['']);
  }

  async hasRole(role: string) {
    const roles = await this.getStoredUserrole();
    return roles.includes(role);
  }

  private async getStoredUserrole(): Promise<string[]> {
    const storedIdToken = this.getStoredIdToken()
    if (storedIdToken) {
      return this.oauthService.processIdToken(storedIdToken, storedIdToken, true)
        .then(token => {
          let claims = token.idTokenClaims as { 'realm_access': { 'roles': [string] } };
          return claims.realm_access.roles;
        })
        .catch(e => {
          console.error("processIdToken error", e);
          return [];
        });
    }
    return [];
  }

  private storeIdToken(token: string) {
    localStorage.setItem('id_token', token);
  }

  private getStoredIdToken() {
    return localStorage.getItem('id_token');
  }

  triggerLogin() {
    this.oauthService.initImplicitFlow();
  }

}
