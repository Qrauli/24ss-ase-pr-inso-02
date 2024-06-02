import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatCardModule } from '@angular/material/card'
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { HeaderComponent } from '../../components/header/header.component';
import { MatListModule } from '@angular/material/list';
import { Incident } from '../../dtos/incident';
import { NgClass, NgFor, NgIf } from '@angular/common';
import { Resource, ResourceState } from '../../dtos/resource';
import { MatTabsModule } from '@angular/material/tabs';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import * as Leaflet from 'leaflet';
import { IncidentService } from '../../services/incidents.service';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent, ConfirmDialogModel } from '../../components/confirm-dialog/confirm-dialog.component';
import { ResourceService } from '../../services/resources.service';
import { interval, switchMap, timer } from "rxjs";
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSortHeader } from "@angular/material/sort";
import {
  MatSnackBar,
  MatSnackBarHorizontalPosition,
  MatSnackBarRef,
  MatSnackBarVerticalPosition,
} from '@angular/material/snack-bar';
import { ResourceRequest } from '../../dtos/resource-request';
import { LocationCoordinates } from '../../dtos/locationCoordinates';


@Component({
  selector: 'app-dispatcher',
  standalone: true,
  imports: [
    MatCardModule,
    MatButtonModule,
    MatToolbarModule,
    MatFormFieldModule,
    FormsModule,
    MatIconModule,
    HeaderComponent,
    MatListModule,
    NgFor,
    NgClass,
    NgIf,
    MatTabsModule,
    MatInputModule,
    MatTableModule,
    LeafletModule,
    MatGridListModule,
    MatSlideToggleModule,
    MatTooltipModule,
    MatSortHeader
  ],
  templateUrl: './dispatcher.component.html',
  styleUrl: './dispatcher.component.css'
})
export class DispatcherComponent implements OnInit {

  selectedIncident: string | null = null;
  assignedResources: Resource[] | null = null;
  selectedIncidentData: Incident | null = null;
  selectedIncidentMarker: Leaflet.Marker | null = null;

  recommended: Set<number> = new Set();

  resourceRequests: ResourceRequest[] = [];

  showDispatched: boolean = false;

  incidents: Incident[] = [];

  resources: Resource[];

  resourceMarkers: Leaflet.Marker[];


  resourcesAdditional: Resource[];

  notifications: any[] = [];

  displayedColumnsResources: string[] = ['status', 'id', 'type', 'locate', 'assign'];
  displayedColumnsResourcesAdditional: string[] = ['status', 'id', 'type', 'locate', 'assign'];
  displayedColumnsIncidents: string[] = ['status', 'location', 'class'];

  map: Leaflet.Map;


  options: Leaflet.MapOptions = {
    layers: [
      Leaflet.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { attribution: '&copy; OpenStreetMap contributors' })
    ],
    zoom: 10,
    center: new Leaflet.LatLng(48.227747192035764, 16.40545336304577)
  };

  constructor(private router: Router, private authService: AuthService, private incidentService: IncidentService, private resourcesService: ResourceService, public dialog: MatDialog, private _snackBar: MatSnackBar) { }
  ngOnInit(): void {
    this.incidentService.getIncidentsOngoing().subscribe(data => {
      data.sort((a, b) => a.state.localeCompare(b.state));
      this.incidents = data;
    });

    this.resourcesService.getOpenResourceRequests().subscribe(data => {
      this.resourceRequests = data;
    });

    timer(0, 5000)
      .pipe(
        switchMap(() => this.resourcesService.getResources())
      )
      .subscribe(data => {
        this.resources = data;
        this.showLocations();
      }
      )

    interval(5000)
      .pipe(
        switchMap(() => this.incidentService.getIncidentsOngoing())
      )
      .subscribe(data => {
        data.sort((a, b) => a.state.localeCompare(b.state));
        this.incidentRefresher(data);
      }
      )

    interval(5000)
      .pipe(
        switchMap(() => this.resourcesService.getOpenResourceRequests())
      )
      .subscribe(data => {
        this.requestRefresher(data);
      }
      )

    this.resourcesService.getResourcesAdditional().subscribe(data => {
      this.resourcesAdditional = data;
    });
  }

  /**
   * checks if the given incident is already in the list of incidents
   * @param data refreshed incidents
   */
  incidentRefresher(data: Incident[]): void {
    for (let i = 0; i < data.length; i++) {
      let flag = false;
      for (let j = 0; j < this.incidents.length; j++) {
        if (this.incidents[j].id == data[i].id) {
          flag = true;
          break;
        }
      }
      if (!flag) {
        this.notifications.push(data[i]);
      }
    }
    this.incidents = data;
    if (!this._snackBar._openedSnackBarRef?._open) {
      this.displayNotifications();
    }
  }

  /**
   * checks if the given resource request is already in the list of resource requests
   * @param data refreshed resource requests
   */
  requestRefresher(data: ResourceRequest[]): void {
    for (let i = 0; i < data.length; i++) {
      let flag = false;
      for (let j = 0; j < this.resourceRequests.length; j++) {
        if (this.resourceRequests[j].id == data[i].id) {
          flag = true;
          break;
        }
      }
      if (!flag) {
        this.notifications.push(data[i]);
      }
    }
    this.resourceRequests = data;
    if (!this._snackBar._openedSnackBarRef?._open) {
      this.displayNotifications();
    }
  }

onMapReady(map: Leaflet.Map) {
    this.map = map;
    setTimeout(() => {
      map.invalidateSize();
    }, 0);
  }


  /**
   * displays the locations of the resources on the map
   */
  showLocations(): void {
    if (this.resourceMarkers) {
      for (let i = 0; i < this.resourceMarkers.length; i++) {
        this.map.removeLayer(this.resourceMarkers[i]);
      }
    }

    this.resourceMarkers = [];

    for (let i = 0; i < this.resources.length; i++) {
      let iconUrl = 'assets/ambulance_green.png';
      if (this.resources[i].state == ResourceState.DISPATCHED) {
        iconUrl = 'assets/ambulance_red.png';
        if (this.resources[i].assignedIncident == this.selectedIncident) {
          iconUrl = 'assets/ambulance_blue.png';
        }
        else {
          if (this.showDispatched == false) {
            continue;
          }
        }
      }
      let marker = Leaflet.marker(new Leaflet.LatLng(this.resources[i].locationCoordinates.latitude, this.resources[i].locationCoordinates.longitude),
        {
          icon: Leaflet.icon({
            iconSize: [37, 61],
            iconAnchor: [19, 61],
            iconUrl: iconUrl,
            shadowUrl: 'leaflet/marker-shadow.png',
            popupAnchor: [0, -42]
          })
        });
      marker.bindTooltip(this.resources[i].id, { permanent: true, direction: 'center' });
      this.resourceMarkers.push(marker);
      marker.addTo(this.map);
    }
  }

  /**
   * displays the notifications in the snackbar
   */
  displayNotifications(): void {
    if (this.notifications.length > 0) {
      let notification = this.notifications.shift();
      if (<Incident>notification.code) {
        let classString = this.codeIsPriority(notification!.code) ? 'alert-red' : 'alert-default';
        const ref: MatSnackBarRef<any> = this._snackBar.open('Neuer Einsatz: ' + notification!.code, 'Auswählen', {
          horizontalPosition: 'center',
          verticalPosition: 'top',
          duration: 7000,
          panelClass: [classString]
        });
        ref.onAction().subscribe(() => {
          this.selectIncident(notification!);
        });
        ref.afterDismissed().subscribe(() => {
          this.displayNotifications();
        });
      }
      else if (<ResourceRequest>notification.requestedResourceType) {
        const ref: MatSnackBarRef<any> = this._snackBar.open('Neue Resourcen-Anfrage', 'Auswählen', {
          horizontalPosition: 'center',
          verticalPosition: 'top',
          duration: 7000,
          panelClass: ['alert-default']
        });
        ref.onAction().subscribe(() => {
          this.selectIncident(this.incidentFromId(notification.assignedIncident));
        });
        ref.afterDismissed().subscribe(() => {
          this.displayNotifications();
        });
      }
    }
  }

  unassignResource(resource: Resource): void {
    const index = this.assignedResources?.indexOf(resource);
    if (index !== undefined && index !== -1) {
      this.assignedResources?.splice(index, 1);
    }
  }

  codeIsPriority(code: string): boolean {
    //TODO: logic for determining if code is priority
    let num = parseInt(code.substring(code.length - 1));
    if (num % 2 == 0) {
      return true;
    }
    return false;
  }


  assignResource(resource: Resource): void {
    if (resource.assignedIncident) {
      const dialogData = new ConfirmDialogModel("Neu zuweisen", "Diese Resource ist bereits einem Einsatz zugeweisen. <br> Möchten Sie diese Resource sicher neu zuweisen?");

      const dialogRef = this.dialog.open(ConfirmDialogComponent, {
        data: dialogData,
        enterAnimationDuration: 0,
        exitAnimationDuration: 0
      });
      dialogRef.afterClosed().subscribe(dialogResult => {
        if (dialogResult === true) {
          this.assignedResources?.push(resource);
        }
      });
    }
    else {
      this.assignedResources?.push(resource);
    }
  }

  /**
   * selects the given incident for further inspection
   * @param incident
   */
  selectIncident(incident: Incident): void {
    this.selectedIncident = incident.id;
    this.selectedIncidentData = incident;
    /*this.incidentService.getIncidentById(incident.id).subscribe(data => {
      this.selectedIncidentData = data;
    });*/
    this.recommended = new Set([1, 2, 3]);
    this.assignedResources = [];
    if (this.selectedIncidentMarker) {
      this.map.removeLayer(this.selectedIncidentMarker);
    }
    this.selectedIncidentMarker = Leaflet.marker(new Leaflet.LatLng(incident.location!.coordinates!.latitude!, incident.location!.coordinates!.longitude!), {
      icon: Leaflet.icon({
        iconSize: [37, 61],
        iconAnchor: [19, 61],
        iconUrl: 'assets/incident.png',
        shadowUrl: 'leaflet/marker-shadow.png',
        popupAnchor: [0, -42]
      })
    });
    this.selectedIncidentMarker.addTo(this.map);
    this.selectedIncidentMarker.bindTooltip("Einsatz", { permanent: true, direction: 'center' });
    this.showLocations();
    this.zoomToLocation(incident.location!.coordinates!);
  }

  /**
   * Unselects the currently selected incident
   */

  unselectIncident(): void {
    this.selectedIncident = null;
    this.recommended = new Set();
    this.assignedResources = [];
    this.map.removeLayer(this.selectedIncidentMarker!);
    this.selectedIncidentMarker = null;
    this.showLocations();
  }

  dispatchIncident(): void {
    const dialogData = new ConfirmDialogModel("Dispatch", "Sollen die ausgewählten Ressourcen wirklich zum Einsatz geschickt werden?");

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: dialogData,
      enterAnimationDuration: 0,
      exitAnimationDuration: 0
    });

    dialogRef.afterClosed().subscribe(dialogResult => {
      if (dialogResult === true) {
        if (this.selectedIncident && this.assignedResources) {
          this.resourcesService.assignResources(this.selectedIncident, this.assignedResources);
        }
      }
    });
  }

  protected readonly ResourceState = ResourceState;
  hasRequest(incident: Incident): boolean {
    for (let i = 0; i < this.resourceRequests.length; i++) {
      if (this.resourceRequests[i].assignedIncident == incident.id) {
        return true;
      }
    }
    return false;
  }

  getRequests(incident: Incident): ResourceRequest[] {
    let requests: ResourceRequest[] = [];
    for (let i = 0; i < this.resourceRequests.length; i++) {
      if (this.resourceRequests[i].assignedIncident == incident.id) {
        requests.push(this.resourceRequests[i]);
      }
    }
    return requests;
  }

  /**
   * Marks the given request as finished
   * @param request request to be finished
   */

  finishRequest(request: ResourceRequest): void {
    const dialogData = new ConfirmDialogModel("Anfrage abschließen", "Soll die Anfrage wirklich abgeschlossen werden?");
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: dialogData,
      enterAnimationDuration: 0,
      exitAnimationDuration: 0
    });

    dialogRef.afterClosed().subscribe(dialogResult => {
      if (dialogResult === true) {
        this.resourcesService.finishRequest(request).subscribe(data => {
          this.resourceRequests = this.resourceRequests.filter(item => item.id !== request.id);
        });
      }
    });
  }

  incidentFromId(id: string): Incident {
    return this.incidents.find(incident => incident.id === id)!;
  }

  zoomToLocation(location: LocationCoordinates) {
    this.map.setView(new Leaflet.LatLng(location.latitude, location.longitude), 15);
  }

}

