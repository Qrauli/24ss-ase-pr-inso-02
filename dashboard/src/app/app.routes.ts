import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { featureFlagGuard } from './auth/auth.guard';
import { DispatcherComponent } from './dispatcher/dispatcher.component';
import { CalltakerComponent } from './calltaker/calltaker.component';
import { AddIncidentComponent } from './add-incident/add-incident.component';
import { DetailIncidentComponent } from './detail-incident/detail-incident.component';
import { EditIncidentComponent } from './edit-incident/edit-incident.component';
import {SplashscreenComponent} from "./splashscreen/splashscreen.component";

export const routes: Routes = [
    { path: 'incidents/add', component: AddIncidentComponent, canActivate: [featureFlagGuard('calltaker', '/login')]},
    { path: 'incident/:id', component: DetailIncidentComponent, canActivate: [featureFlagGuard('calltaker', '/login')]},
    { path: 'incident/:id/edit', component: EditIncidentComponent, canActivate: [featureFlagGuard('calltaker', '/login')]},
    { path: 'login', component: LoginComponent, canActivate: [featureFlagGuard('login', '/')] },
    { path: 'dispatcher', component: DispatcherComponent, canActivate: [featureFlagGuard('dispatcher', '/')]},
    { path: 'calltaker', component: CalltakerComponent, canActivate: [featureFlagGuard('calltaker', '/')]},
    { path: '', component: SplashscreenComponent },
  ];
