import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {provideHttpClient, withInterceptors, withInterceptorsFromDi} from '@angular/common/http';
import { mockInterceptor } from './mocks/mock-interceptor.interceptor';
import { IncidentService } from './services/incidents.service';
import { ResourceService } from './services/resources.service';
import { provideOAuthClient } from "angular-oauth2-oidc";
import {environment} from "../environments/environment";
import {CategorizationService} from "./services/categorization.service";

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideAnimationsAsync(),
    IncidentService,
    ResourceService,
    CategorizationService,
    provideOAuthClient(
      {
        resourceServer: {
          allowedUrls: [ environment.incidentUrl, environment.resourceUrl, environment.categorizationUrl ],
          sendAccessToken: true,
        }
      }
    ),
    environment.mock ? provideHttpClient(withInterceptors([mockInterceptor]), withInterceptorsFromDi()) : provideHttpClient(withInterceptorsFromDi())
  ]
};
