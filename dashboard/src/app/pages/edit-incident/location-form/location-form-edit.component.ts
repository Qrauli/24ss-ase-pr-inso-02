import {Component} from "@angular/core";
import * as Leaflet from "leaflet";
import {Geocoder} from "leaflet-control-geocoder";
import {Observable, firstValueFrom} from "rxjs";
import {GeocodingResult} from "leaflet-control-geocoder/src/geocoders/api";
import {FormBuilder, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HeaderComponent} from "../../../components/header/header.component";
import {MatButtonModule} from "@angular/material/button";
import {MatStepperModule} from "@angular/material/stepper";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {MatChipsModule} from "@angular/material/chips";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatListModule} from "@angular/material/list";
import {MatTabsModule} from "@angular/material/tabs";
import {MatIconModule} from "@angular/material/icon";
import {LeafletModule} from "@asymmetrik/ngx-leaflet";
import {MatRadioModule} from "@angular/material/radio";
import {MatMenuModule} from "@angular/material/menu";
import {MatTableModule} from "@angular/material/table";
import {MatDividerModule} from "@angular/material/divider";
import {geocoderAddressConverter, LocationAddress, LocationCoordinates, prettyLocationAddress} from "../../../dtos/incident";
import {NgIf} from "@angular/common";
import {TranslateModule, TranslateService} from '@ngx-translate/core';


@Component({
  selector: 'location-form-edit',
  standalone: true,
  imports: [HeaderComponent,
    MatButtonModule,
    MatStepperModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatChipsModule,
    MatSidenavModule,
    MatListModule,
    MatTabsModule,
    MatIconModule,
    LeafletModule,
    MatRadioModule,
    MatMenuModule,
    MatTableModule,
    MatDividerModule,
    TranslateModule,
    NgIf],
  templateUrl: './location-form-edit.component.html',
  styleUrl: '../edit-incident.component.css'
})
export class LocationFormEditComponent {

  constructor(private formBuilder: FormBuilder, public translate: TranslateService) {
  }

  form = this.formBuilder.group({
    street: [''],
    postalCode: [''],
    city: [''],
    additionalInformation: [''],
  });

  // ####################### Map & Coordinates ####################### //

  map: Leaflet.Map;
  marker: Leaflet.Marker;
  geocoder = new (Geocoder as any).nominatim({
    geocodingQueryParams: {
      "countrycodes": "at",
      "accept-language": "de-AT"
    },
    // see https://nominatim.org/release-docs/develop/api/Reverse/#result-restriction
    reverseQueryParams: {
      "zoom": "18",
      "accept-language": "de-AT"
    }
  });

  coordinates: LocationCoordinates;
  options: Leaflet.MapOptions = {
    layers: [
      Leaflet.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {attribution: '&copy; OpenStreetMap contributors'})
    ],
    zoom: 10,
    center: new Leaflet.LatLng(48.227747192035764, 16.40545336304577)
  };

  async onMapReady(map: Leaflet.Map) {
    this.map = map;
    setTimeout(() => {
      map.invalidateSize();
    }, 0);

    await firstValueFrom(this.translate.get('INCIDENT.ADDRESS_SEARCH'));

    const osmGeocoder = (Leaflet.Control as any).geocoder({
      position: 'topright',
      geocoder: this.geocoder,
      collapsed: false,
      text: this.translate.instant('INCIDENT.ADDRESS_SEARCH'),
      placeholder: this.translate.instant('INCIDENT.STREET_SEARCH'),
      defaultMarkGeocode: false
    }).addTo(map);

    osmGeocoder.on('markgeocode', (e: any) => {
      // to review result object
      console.log(e);
      // coordinates for result
      const coords = new Leaflet.LatLng(e.geocode.center.lat, e.geocode.center.lng);
      // center map on result
      map.setView(coords, 16);
      this.updateMarker(coords);
      this.updateAddress(e.geocode.properties.address);

      this.coordinates = {latitude: e.geocode.center.lat, longitude: e.geocode.center.lng};
    });
  }

  updateMarker(latlng: Leaflet.LatLng) {
    if (this.marker) {
      this.map.removeLayer(this.marker);
    }
    this.marker = Leaflet.marker(latlng, {
      icon: Leaflet.icon({
        iconSize: [25, 41],
        iconAnchor: [13, 41],
        iconUrl: 'leaflet/marker-icon.png',
        shadowUrl: 'leaflet/marker-shadow.png',
        popupAnchor: [0, -42]
      })
    });
    this.marker.addTo(this.map);
  }

  // handles the selection of a location via clicking on the map
  // updates the marker and coordinates according to the map
  // does a reverse location search to get the address
  selectLocation(event: any) {
    this.updateMarker(event.latlng);
    this.coordinates = {latitude: event.latlng.lat, longitude: event.latlng.lng};

    const reverseLocationSearch: Observable<GeocodingResult[]> = new Observable(observer => {
      this.geocoder.reverse({
        lat: this.coordinates.latitude,
        lng: this.coordinates.longitude
      }, 1, (result: GeocodingResult[] | undefined) => {
        observer.next(result);
        observer.complete();
      })
    });
    reverseLocationSearch.subscribe(result => {
      this.updateAddress(result[0].properties.address);
    })
  }

  // ####################### Address ####################### //

  updateAddress(address: { city: string, county: string, postcode: string, road: string, house_number: string }) {

    const internalFormat: LocationAddress = geocoderAddressConverter(address);

    this.updateAddressInternal(internalFormat);
  }

  updateAddressInternal(internalFormat: LocationAddress) {
    this.form.controls.city.patchValue(internalFormat.city);
    this.form.controls.postalCode.patchValue(internalFormat.postalCode);
    this.form.controls.street.patchValue(internalFormat.street);

    this.marker.bindPopup(
      this.translate.instant('INCIDENT.ADDRESS') + ": ".concat(
        prettyLocationAddress(internalFormat)
      )).openPopup();
  }
}
