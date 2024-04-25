import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import {MatListModule} from '@angular/material/list';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import * as Leaflet from 'leaflet';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { MatGridListModule } from '@angular/material/grid-list';


@Component({
  selector: 'app-detail-incident',
  standalone: true,
  imports: [
    HeaderComponent, 
    MatListModule,  
    MatButtonModule,
    LeafletModule,
    MatGridListModule],
  templateUrl: './detail-incident.component.html',
  styleUrl: './detail-incident.component.css'
})
export class DetailIncidentComponent {

  constructor(private router: Router, private activatedRoute: ActivatedRoute) {}
  map: Leaflet.Map;

  /**
   * Navigate to the edit incident page
   */
  editIncident(): void {
    this.router.navigate(['/incident/' + this.activatedRoute.snapshot.params['id'] + '/edit']);
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
  
    Leaflet.marker(new Leaflet.LatLng(48.227747192035764, 16.40545336304577), {icon: Leaflet.icon({
      iconSize: [ 25, 41 ],
      iconAnchor: [ 13, 41 ],
      iconUrl: 'leaflet/marker-icon.png',
      shadowUrl: 'leaflet/marker-shadow.png'
    })}).addTo(this.map);
  }

}
