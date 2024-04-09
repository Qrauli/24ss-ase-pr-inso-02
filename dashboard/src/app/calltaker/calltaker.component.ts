import { AfterViewInit, Component, ViewChild } from '@angular/core';
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
import { NgFor, NgIf } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Incident } from '../incident';
import {MatSort, MatSortModule} from '@angular/material/sort';
import {MatInputModule} from '@angular/material/input';
import {MatExpansionModule} from '@angular/material/expansion';



const ELEMENT_DATA: Incident[] = [
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
  {id: 13, status: true, location: 'Wien', class: 'Herzstillstand', time: '13:30'},
  {id: 14, status: true, location: 'Wien', class: 'Atemnot', time: '12:30'},
  {id: 15, status: true, location: 'Wien', class: 'Atemnot', time: '12:30'},
  {id: 16, status: true, location: 'Wien', class: 'Atemnot', time: '12:30'},

];


@Component({
  selector: 'app-calltaker',
  standalone: true,
  imports: [
    MatCardModule,
    MatButtonModule,
    MatToolbarModule,
    MatFormFieldModule,
    MatListModule,
    FormsModule,
    MatIconModule,
    HeaderComponent,
    NgFor,
    MatTableModule,
    NgIf,
    MatSortModule,
    MatInputModule,
    MatExpansionModule
    ],
  templateUrl: './calltaker.component.html',
  styleUrl: './calltaker.component.css'
})
export class CalltakerComponent implements AfterViewInit{


  displayedColumns: string[] = ['status', 'location', 'class', 'time'];
  dataSource = new MatTableDataSource(ELEMENT_DATA);;
  @ViewChild(MatSort) sort: MatSort;

  /**
   * After the view has been initialized, initilize the sorting
   */
  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
  }


  constructor(private router: Router, private authService: AuthService) { }


  /**
   * Perform the main action of the page
   */
  performMainAction() {
    this.router.navigate(['/incidents/add']);
  }

  /**
   * Show the details of the incident
   * @param row the incident to show the details of
   */
  showDetail(row: any) {
    //console.log(row.id);
    this.router.navigate(['/incident/' + row.id]);
  }

  /**
   * 
   * @param e the event to filter the incidents
   */

  doFilter = (e: Event) => {
    const value = (e.target as HTMLInputElement).value;
    this.dataSource.filter = value.trim().toLocaleLowerCase();
  }

}
