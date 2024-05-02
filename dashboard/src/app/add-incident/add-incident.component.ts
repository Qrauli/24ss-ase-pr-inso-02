import {AfterViewChecked, Component, ViewChild} from '@angular/core';
import {HeaderComponent} from '../header/header.component';
import {FormBuilder, Validators, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatStepperModule} from '@angular/material/stepper';
import {MatButtonModule} from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';
import {Router} from '@angular/router';
import {MatChipsModule} from '@angular/material/chips';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import {MatTabsModule} from '@angular/material/tabs';
import {MatIconModule} from '@angular/material/icon';
import {LeafletModule} from '@asymmetrik/ngx-leaflet';
import * as Leaflet from 'leaflet';
import {MatRadioModule} from '@angular/material/radio';
import {MatMenuModule} from '@angular/material/menu';
import {MatTable, MatTableModule} from '@angular/material/table';
import "leaflet-control-geocoder/dist/Control.Geocoder.js";
import {Geocoder} from 'leaflet-control-geocoder';
import {MatDividerModule} from '@angular/material/divider';
import {LocationCoordinates} from "../dto/location-coordinates";
import {Patient, Sex} from "../dto/patient";
import {Incident, Status} from "../dto/incident";
import {Observable} from "rxjs";
import {GeocodingResult} from "leaflet-control-geocoder/src/geocoders/api";
import {IncidentService} from "../incidents.service";


@Component({
  selector: 'app-add-incident',
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
    MatDividerModule],
  templateUrl: './add-incident.component.html',
  styleUrl: './add-incident.component.css'
})
export class AddIncidentComponent {

  constructor(private formBuilder: FormBuilder, private router: Router, private incidentService: IncidentService) {
  }

  // ####################### Map & Coordinates ####################### //

  map: Leaflet.Map;
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
  osmGeocoder: any;
  marker: Leaflet.Marker;

  locationCoordinates: LocationCoordinates = {latitude: 48.227747192035764, longitude: 16.40545336304577};

  options: Leaflet.MapOptions = {
    layers: [
      Leaflet.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {attribution: '&copy; OpenStreetMap contributors'})
    ],
    zoom: 10,
    center: new Leaflet.LatLng(this.locationCoordinates.latitude, this.locationCoordinates.longitude)
  };

  onMapReady(map: Leaflet.Map) {
    this.map = map;
    setTimeout(() => {
      map.invalidateSize();
    }, 0);

    const options = {
      position: 'topright',
      geocoder: this.geocoder,
      collapsed: false,
      text: 'Adresse suchen',
      placeholder: 'Straße eingeben',
      defaultMarkGeocode: false
    };

    this.osmGeocoder = (Leaflet.Control as any).geocoder(options).addTo(map);

    this.osmGeocoder.on('markgeocode', (e: any) => {
      // to review result object
      console.log(e);
      // coordinates for result
      const coords = new Leaflet.LatLng(e.geocode.center.lat, e.geocode.center.lng);
      // center map on result
      map.setView(coords, 16);
      this.updateMarker(coords);
      this.updateAddress(e.geocode.properties.address);

      this.locationCoordinates = {latitude: e.geocode.center.lat, longitude: e.geocode.center.lng};
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

  selectLocation(event: any) {
    this.updateMarker(event.latlng);
    this.locationCoordinates = {latitude: event.latlng.lat, longitude: event.latlng.lng};
    this.reverseSearchLocation(this.locationCoordinates);
  }

  reverseSearchLocation(locationCoordinates: LocationCoordinates) {
    const reverseLocationSearch: Observable<GeocodingResult[]> = new Observable(observer => {
      this.geocoder.reverse({
        lat: locationCoordinates.latitude,
        lng: locationCoordinates.longitude
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

  locationForm = this.formBuilder.group({
    street: [''],
    postalCode: [''],
    city: [''],
    additionalInformation: [''],
  });

  updateAddress(address: { city: string, county: string, postcode: string, road: string, house_number: string }) {
    this.locationForm.controls.city.patchValue(address.city ? address.city : address.county);
    this.locationForm.controls.postalCode.patchValue(address.postcode);
    this.locationForm.controls.street.patchValue(address.road?.concat(address.house_number ? " " + address.house_number : ""));

    this.marker.bindPopup(
      "Adresse: ".concat(
        address.road ? address.road : "",
        address.house_number ? " " + address.house_number : "", address.road ? ", " : "",
        address.postcode, " ",
        address.city ? address.city : address.county))
      .openPopup();
  }

  // ####################### Patients ####################### //

  @ViewChild(MatTable) table: MatTable<any>;

  displayedColumnsPatients: string[] = ['age', 'sex', 'remove'];

  // TODO caller
  // TODO reactive form, see https://angular.io/guide/reactive-forms#creating-dynamic-forms

  patients: Patient[] = [];

  addPatient(): void {
    this.patients.push({age: 0, sex: Sex.MALE});
    this.table.renderRows();
  }

  removePatient(patient: Patient): void {
    const index = this.patients.indexOf(patient);
    if (index !== undefined && index !== -1) {
      this.patients.splice(index, 1);
      this.table.renderRows();
    }
  }

  // ####################### Questions ####################### //

  selectedIndex = 0;

  // TODO service interaction

  changeIndex(index: number): void {
    this.selectedIndex = index;
  }

  // ####################### Categorization ####################### //

  // TODO service interaction

  recommendedCategory = 'C1C0';
  selectedCategory = 'C1C0';

  changeCategory(category: string): void {
    this.selectedCategory = category;
  }

  // ####################### Saving ####################### //

  saveIncident(): void {
    const incident: Incident = {
      id: "",
      patients: this.patients,
      numberOfPatients: this.patients.length,
      categorization: {
        id: "",
        code: this.selectedCategory
      },
      location: {
        description: {
          street: this.locationForm.value.street!,
          postalCode: this.locationForm.value.postalCode!,
          city: this.locationForm.value.city!,
          additionalInformation: this.locationForm.value.additionalInformation!
        },
        coordinates: this.locationCoordinates
      },
      status: Status.CREATED
    };

    this.incidentService.saveIncident(incident).subscribe(saved => {
      alert(JSON.stringify(saved, null, 2));
      this.router.navigate(['/calltaker']);
    });
  }
}
