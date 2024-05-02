import { Component, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card'
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { HeaderComponent } from '../header/header.component';
import { MatListModule } from '@angular/material/list';
import { Incident } from '../dto/incident';
import { NgClass, NgFor, NgIf } from '@angular/common';
import { Resource } from '../dto/resource';
import { MatTabsModule } from '@angular/material/tabs';
import { MatInputModule } from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import * as Leaflet from 'leaflet';
import { IncidentService } from '../incidents.service';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent, ConfirmDialogModel } from '../confirm-dialog/confirm-dialog.component';
import { ResourceService } from '../resources.service';
import {interval, switchMap} from "rxjs";
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatSortHeader} from "@angular/material/sort";



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

  selectedIncident: Incident | null = null;
  assignedResources: Resource[] | null = null;
  selectedIncidentData: Incident | null = null;

  recommended: Set<number> = new Set();

  showDispatched: boolean;

  incidents: Incident[];

  resources: Resource[];

  resourcesAdditional: Resource[];

  displayedColumnsResources: string[] = ['status', 'type', 'location', 'locate', 'assign'];
  displayedColumnsResourcesAdditional: string[] = ['status', 'type', 'location', 'locate', 'assign'];
  displayedColumnsIncidents: string[] = ['status', 'location', 'class', ];

  options: Leaflet.MapOptions = {
    layers: [
      Leaflet.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { attribution: '&copy; OpenStreetMap contributors' })
    ],
    zoom: 10,
    center: new Leaflet.LatLng(48.227747192035764, 16.40545336304577)
  };

  constructor(private router: Router, private authService: AuthService, private incidentService: IncidentService, private resourcesService: ResourceService, public dialog: MatDialog) { }

  ngOnInit(): void {
    this.incidentService.getIncidentsOngoing().subscribe(data => {
      this.incidents = data;
    });

    interval(5000)
      .pipe(
        switchMap(() => this.resourcesService.getResources())
      )
      .subscribe(data => {
        this.resources = data;
        }
      )

    this.resourcesService.getResourcesAdditional().subscribe(data => {
      this.resourcesAdditional = data;
    });

  }

  unassignResource(resource: Resource): void {
    const index = this.assignedResources?.indexOf(resource);
    if (index !== undefined && index !== -1) {
      this.assignedResources?.splice(index, 1);
    }
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
    if (this.selectedIncident === incident) {
      this.selectedIncident = null;
      this.recommended = new Set();
      this.assignedResources = [];
      return;
    }
    this.selectedIncident = incident;
    this.incidentService.getIncidentById(incident.id).subscribe(data => {
      this.selectedIncidentData = data;
    });
    this.recommended = new Set([1, 2, 3]);
    this.assignedResources = [];

  }

  /**
   * Unselects the currently selected incident
   */

  unselectIncident(): void {
    this.selectedIncident = null;
    this.recommended = new Set();
    this.assignedResources = [];
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
          this.resourcesService.assignResources(this.selectedIncident.id, this.assignedResources);
        }
      }
    });
  }

}

