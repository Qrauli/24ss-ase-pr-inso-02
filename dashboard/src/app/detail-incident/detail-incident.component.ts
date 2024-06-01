import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { MatListModule } from '@angular/material/list';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import * as Leaflet from 'leaflet';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { MatGridListModule } from '@angular/material/grid-list';
import { IncidentService } from '../incidents.service';
import { Incident } from '../dto/incident';
import { NgFor } from '@angular/common';
import { ResourceService } from '../resources.service';
import { switchMap, timer } from 'rxjs';
import { Resource } from '../dto/resource';


@Component({
  selector: 'app-detail-incident',
  standalone: true,
  imports: [
    HeaderComponent,
    MatListModule,
    MatButtonModule,
    LeafletModule,
    NgFor,
    MatGridListModule],
  templateUrl: './detail-incident.component.html',
  styleUrl: './detail-incident.component.css'
})
export class DetailIncidentComponent implements OnInit {

  incident: Incident | undefined;

  resourceMarkers: Leaflet.Marker[] = [];

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private incidentService: IncidentService, private resourcesService: ResourceService) { }

  ngOnInit(): void {
    this.incidentService.getIncidentById(this.activatedRoute.snapshot.params['id']).subscribe(data => {
      this.incident = data;
      if (this.map) {
        Leaflet.marker(new Leaflet.LatLng(this.incident.location.coordinates.latitude, this.incident.location.coordinates.longitude), {
          icon: Leaflet.icon({
            iconSize: [37, 61],
            iconAnchor: [19, 61],
            iconUrl: 'assets/incident.png',
            shadowUrl: 'leaflet/marker-shadow.png'
          })
        }).addTo(this.map).bindTooltip("Einsatz", { permanent: true, direction: 'center' });
      }
    });

    timer(0, 5000)
      .pipe(
        switchMap(() => this.resourcesService.getResources())
      )
      .subscribe(data => {
        this.showLocations(data);
      }
      )
    //TODO: fetch categorization data
  }
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
    if (this.incident) {
      Leaflet.marker(new Leaflet.LatLng(this.incident.location.coordinates.latitude, this.incident.location.coordinates.longitude), {
        icon: Leaflet.icon({
          iconSize: [37, 61],
          iconAnchor: [19, 61],
          iconUrl: 'assets/incident.png',
          shadowUrl: 'leaflet/marker-shadow.png'
        })
      }).addTo(this.map).bindTooltip("Einsatz", { permanent: true, direction: 'center' });

    }

  }

  /**
   * displays the locations of the resources on the map
   */
  showLocations(resources: Resource[]) {
    if (this.resourceMarkers) {
      for (let i = 0; i < this.resourceMarkers.length; i++) {
        this.map.removeLayer(this.resourceMarkers[i]);
      }
    }

    this.resourceMarkers = [];

    for (let i = 0; i < resources.length; i++) {
      if (resources[i].assignedIncident == this.incident?.id) {
        let marker = Leaflet.marker(new Leaflet.LatLng(resources[i].locationCoordinates.latitude, resources[i].locationCoordinates.longitude),
          {
            icon: Leaflet.icon({
              iconSize: [37, 61],
              iconAnchor: [19, 61],
              iconUrl: 'assets/ambulance_blue.png',
              shadowUrl: 'leaflet/marker-shadow.png',
              popupAnchor: [0, -42]
            })
          });
        marker.bindTooltip(resources[i].id, { permanent: true, direction: 'center' });
        this.resourceMarkers.push(marker);
        marker.addTo(this.map);
      }
    }

  }

}
