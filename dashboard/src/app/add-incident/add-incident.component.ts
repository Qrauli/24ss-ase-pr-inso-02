import { AfterViewChecked, Component, ViewChild } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import {FormBuilder, Validators, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatStepperModule} from '@angular/material/stepper';
import {MatButtonModule} from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';
import { Router } from '@angular/router';
import {MatChipsModule} from '@angular/material/chips';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import {MatTabsModule} from '@angular/material/tabs';
import {MatIconModule} from '@angular/material/icon';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import * as Leaflet from 'leaflet';
import {MatRadioModule} from '@angular/material/radio';
import {MatMenuModule} from '@angular/material/menu';
import { Person } from '../dto/person';
import { MatTable, MatTableDataSource, MatTableModule } from '@angular/material/table';


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
    MatTableModule   ],
  templateUrl: './add-incident.component.html',
  styleUrl: './add-incident.component.css'
})
export class AddIncidentComponent{

  @ViewChild(MatTable) table: MatTable<any>;

  displayedColumnsPersons: string[] = ['name', 'gender', 'age', 'remove'];

  map: Leaflet.Map;
  marker: Leaflet.Marker;
  persons: Person[] = [];

  firstFormGroup = this._formBuilder.group({
    firstCtrl: ['', Validators.required],
  });
  secondFormGroup = this._formBuilder.group({
    secondCtrl: ['', Validators.required],
  });
  isLinear = true;
  selectedIndex = 0;
  selectedCategory = 'Atemnot';
  
  ngOnInit(): void {
  }

  options: Leaflet.MapOptions = {
    layers: [
      Leaflet.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { attribution: '&copy; OpenStreetMap contributors' })
    ],
    zoom: 10,
    center: new Leaflet.LatLng(48.227747192035764, 16.40545336304577)
  };

  onMapReady(map: Leaflet.Map) {
    this.map = map;
    setTimeout(() => {
      map.invalidateSize();
    }, 0);
  }



  selectLocation(event: any) {

    if (this.marker) {
      this.map.removeLayer(this.marker);
    }
    this.marker = Leaflet.marker(event.latlng, {icon: Leaflet.icon({
      iconSize: [ 25, 41 ],
      iconAnchor: [ 13, 41 ],
      iconUrl: 'leaflet/marker-icon.png',
      shadowUrl: 'leaflet/marker-shadow.png'
    })});
    this.marker.addTo(this.map);
  }



  constructor(private _formBuilder: FormBuilder, private router: Router) {}


  saveIncident(): void {
    // Implement your save logic here
    this.router.navigate(['/calltaker']);
  }

  changeIndex(index: number): void {
    this.selectedIndex = index;
  }

  changeCategory(category: string): void {
    this.selectedCategory = category;
  }

  addPerson(): void {
    this.persons.push({name: '', age: 0, gender: 'male'});
    this.table.renderRows();
  }

  removePerson(person: Person): void {
    const index = this.persons.indexOf(person);
    if (index !== undefined && index !== -1) {
      this.persons.splice(index, 1);
      this.table.renderRows();
    }
  }

}
