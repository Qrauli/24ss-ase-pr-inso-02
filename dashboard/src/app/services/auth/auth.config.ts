import { AuthConfig } from "angular-oauth2-oidc";
import { environment } from "../../../environments/environment";
import * as jwksData from "../../../assets/jwks.json";

export const authConfig: AuthConfig = {
  loginUrl : environment.idpLoginUrl,
  issuer: environment.idpIssuer,
  clientId : environment.idpClientId,
  redirectUri : window.location.origin + '/',
  responseType: 'id_token token',
  scope : 'openid profile email',
  jwks: jwksData,
  // showDebugInformation : true,
  oidc: false,
  requestAccessToken: true,
  strictDiscoveryDocumentValidation: false,
  disableAtHashCheck: true,
}
