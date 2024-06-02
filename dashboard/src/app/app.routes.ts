import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { featureFlagGuard } from './services/auth/auth.guard';
import { DispatcherComponent } from './pages/dispatcher/dispatcher.component';
import { CalltakerComponent } from './pages/calltaker/calltaker.component';
import { AddIncidentComponent } from './pages/add-incident/add-incident.component';
import { DetailIncidentComponent } from './pages/detail-incident/detail-incident.component';
import { EditIncidentComponent } from './pages/edit-incident/edit-incident.component';
import {SplashscreenComponent} from "./components/splashscreen/splashscreen.component";

export const routes: Routes = [
    { path: 'incidents/add', component: AddIncidentComponent, canActivate: [featureFlagGuard('calltaker', '/login')]},
    { path: 'incident/:id', component: DetailIncidentComponent, canActivate: [featureFlagGuard('calltaker', '/login')]},
    { path: 'incident/:id/edit', component: EditIncidentComponent, canActivate: [featureFlagGuard('calltaker', '/login')]},
    { path: 'login', component: LoginComponent, canActivate: [featureFlagGuard('login', '/')] },
    { path: 'dispatcher', component: DispatcherComponent, canActivate: [featureFlagGuard('dispatcher', '/')]},
    { path: 'calltaker', component: CalltakerComponent, canActivate: [featureFlagGuard('calltaker', '/')]},
    { path: '', component: SplashscreenComponent },
  ];
