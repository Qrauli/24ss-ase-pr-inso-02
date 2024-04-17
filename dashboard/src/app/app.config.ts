import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { mockInterceptor } from './mock-interceptor.interceptor';
import { IncidentService } from './incidents.service';
import { ResourceService } from './resources.service';

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes), provideAnimationsAsync(), IncidentService, ResourceService, provideHttpClient(withInterceptors(
    [mockInterceptor]
))]
};
