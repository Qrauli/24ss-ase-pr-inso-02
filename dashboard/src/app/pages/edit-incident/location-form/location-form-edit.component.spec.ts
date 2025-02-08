import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LocationFormEditComponent } from './location-form-edit.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import { TranslateModule } from '@ngx-translate/core';
import { LatLng, Marker } from 'leaflet';

describe('AddIncidentComponent', () => {
  let component: LocationFormEditComponent;
  let fixture: ComponentFixture<LocationFormEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LocationFormEditComponent, NoopAnimationsModule, HttpClientTestingModule, TranslateModule.forRoot()]
    })
      .compileComponents();

    fixture = TestBed.createComponent(LocationFormEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have a map', () => {
    expect(component.map).toBeTruthy();
  });

  it('should run #updateMarker()', async () => {
    (<any>component).map = (<any>component).map || {};
    spyOn((<any>component).map, 'removeLayer');
    (<any>component).marker = (<any>component).marker || new Marker(new LatLng(0, 0));
    (<any>component).updateMarker(new LatLng(0, 0));
    expect((<any>component).map.removeLayer).toHaveBeenCalled();
  });

  it('should run #selectLocation()', async () => {
    spyOn(component, 'updateMarker');
    (<any>component).coordinates = (<any>component).coordinates || {};
    (<any>component).coordinates.latitude = 'latitude';
    (<any>component).coordinates.longitude = 'longitude';
    (<any>component).geocoder = (<any>component).geocoder || {};
    spyOn((<any>component).geocoder, 'reverse');
    spyOn(component, 'updateAddress');
    (<any>component).selectLocation({
      latlng: {
        lat: {},
        lng: {}
      }
    });
    expect((<any>component).updateMarker).toHaveBeenCalled();
    expect((<any>component).geocoder.reverse).toHaveBeenCalled();
  });

  it('should run #updateAddress()', async () => {
    (<any>component).form = (<any>component).form || {};
    (<any>component).form.controls = {
      city: {
        patchValue: function() {}
      },
      postalCode: {
        patchValue: function() {}
      },
      street: {
        patchValue: function() {}
      }
    };
    (<any>component).marker = (<any>component).marker || new Marker(new LatLng(0, 0));
    spyOn((<any>component).marker, 'bindPopup').and.returnValue({
      openPopup: function() {}
    });
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'instant');
    (<any>component).updateAddress({});
    expect((<any>component).marker.bindPopup).toHaveBeenCalled();
    expect((<any>component).translate.instant).toHaveBeenCalled();
  });
});
