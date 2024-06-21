import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailIncidentComponent } from './detail-incident.component';

import { RouterTestingModule } from "@angular/router/testing";
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {OAuthModule} from "angular-oauth2-oidc";
import { TranslateModule } from '@ngx-translate/core';
import { Observable, of as observableOf, throwError } from 'rxjs';
import { LatLng, Marker } from 'leaflet';

describe('DetailIncidentComponent', () => {
  let component: DetailIncidentComponent;
  let fixture: ComponentFixture<DetailIncidentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailIncidentComponent, RouterTestingModule, HttpClientTestingModule, OAuthModule.forRoot(), TranslateModule.forRoot()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailIncidentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should run #ngOnDestroy()', async () => {
    (<any>component).subcription = (<any>component).subcription || {};
    spyOn((<any>component).subcription, 'unsubscribe');
    (<any>component).ngOnDestroy();
    expect((<any>component).subcription.unsubscribe).toHaveBeenCalled();
  });

  it('should run #editIncident()', async () => {
    (<any>component).router = (<any>component).router || {};
    spyOn((<any>component).router, 'navigate');
    (<any>component).activatedRoute = (<any>component).activatedRoute || {};
    (<any>component).activatedRoute.snapshot = {
      params: {
        'id': {}
      }
    };
    (<any>component).editIncident();
    expect((<any>component).router.navigate).toHaveBeenCalled();
  });

  it('should run #showLocations()', async () => {
    (<any>component).resourceMarkers = [
      new Marker(new LatLng(0, 0))
    ];
    (<any>component).map = (<any>component).map || {};
    spyOn((<any>component).map, 'removeLayer');
    let resources = [{
      state: {},
      assignedIncident: {},
      locationCoordinates: {
        latitude: {},
        longitude: {}
      },
      id: {}
    }];
    (<any>component).showLocations(resources);
    expect((<any>component).map.removeLayer).toHaveBeenCalled();
  });

});
