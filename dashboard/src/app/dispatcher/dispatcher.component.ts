import { Component } from '@angular/core';
import { MatCardModule} from '@angular/material/card'
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import {MatFormFieldModule} from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import {MatIconModule} from '@angular/material/icon';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { HeaderComponent } from '../header/header.component';
import {MatListModule} from '@angular/material/list';
import { Incident } from '../incident';
import { NgClass, NgFor, NgIf } from '@angular/common';
import { Resource } from '../resource';
import {MatTabsModule} from '@angular/material/tabs';
import {MatInputModule} from '@angular/material/input';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';




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
    MatTableModule
      ],
  templateUrl: './dispatcher.component.html',
  styleUrl: './dispatcher.component.css'
})
export class DispatcherComponent {

  selectedIncident: Incident | null = null;

  recommended: Set<number> = new Set();

  incidents: Incident[] = [
    {id: 1, status: false, location: 'Wien', class: 'Atemnot', time: '12:30'},
    {id: 2, status: false, location: 'Wien', class: 'Atemnot', time: '12:30'},
    {id: 3, status: false, location: 'Wien', class: 'Atemnot', time: '12:30'},
    {id: 4, status: false, location: 'Wien', class: 'Atemnot', time: '12:30'},
    {id: 5, status: false, location: 'Wien', class: 'Atemnot', time: '12:30'},
    {id: 6, status: true, location: 'Wien', class: 'Atemnot', time: '12:30'},
    {id: 7, status: true, location: 'Wien', class: 'Atemnot', time: '12:30'},
    {id: 8, status: true, location: 'Wien', class: 'Atemnot', time: '12:30'},
    {id: 9, status: true, location: 'Wien', class: 'Atemnot', time: '12:30'},
    {id: 10, status: true, location: 'Wien', class: 'Atemnot', time: '12:30'},
    {id: 11, status: true, location: 'Wien', class: 'Atemnot', time: '12:30'},
    {id: 12, status: true, location: 'Wien', class: 'Atemnot', time: '12:30'},
    {id: 13, status: true, location: 'Wien', class: 'Atemnot', time: '12:30'},
    {id: 14, status: true, location: 'Wien', class: 'Atemnot', time: '12:30'},
    {id: 15, status: true, location: 'Wien', class: 'Atemnot', time: '12:30'},
    {id: 16, status: true, location: 'Wien', class: 'Atemnot', time: '12:30'},
  
  ];

  resources: Resource[] = [
    {id: 1, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 2, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 3, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 4, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 5, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 6, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 7, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 8, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 9, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 10, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 11, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 12, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 13, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 14, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 15, type: 'Rettungswagen', location: 'Wien', assigned: true},
    {id: 16, type: 'Rettungswagen', location: 'Wien', assigned: true},
    {id: 17, type: 'Rettungswagen', location: 'Wien', assigned: true},
    {id: 18, type: 'Rettungswagen', location: 'Wien', assigned: true},
    {id: 19, type: 'Rettungswagen', location: 'Wien', assigned: true},
    {id: 20, type: 'Rettungswagen', location: 'Wien', assigned: true},
  ];

  resourcesAdditional: Resource[] = [
    {id: 1, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 2, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 3, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 4, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 5, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 6, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 7, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 8, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 9, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 10, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 11, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 12, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 13, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 14, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 15, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 16, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 17, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 18, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 19, type: 'Rettungswagen', location: 'Wien', assigned: false},
    {id: 20, type: 'Rettungswagen', location: 'Wien', assigned: false},
  ];

  displayedColumnsResources: string[] = ['status', 'type', 'location',  'locate', 'assign'];
  displayedColumnsResourcesAdditional: string[] = ['status', 'type', 'location',  'locate', 'assign'];
  displayedColumnsIncidents: string[] = ['status', 'location', 'class', 'time'];



  constructor(private router: Router, private authService: AuthService) { }

  /**
   * selects the given incident for further inspection
   * @param incident 
   */
  selectIncident(incident: Incident): void {
    if (this.selectedIncident === incident) {
      this.selectedIncident = null;
      this.recommended = new Set();
      return;
    }
    this.selectedIncident = incident;
    this.recommended = new Set([1,2,3]);;
  }

  /**
   * Unselects the currently selected incident
   */

  unselectIncident(): void {
    this.selectedIncident = null;
    this.recommended = new Set();;
  }

}
