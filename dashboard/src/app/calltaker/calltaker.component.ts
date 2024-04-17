import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
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
import { NgFor, NgIf } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Incident } from '../dto/incident';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatInputModule } from '@angular/material/input';
import { MatExpansionModule } from '@angular/material/expansion';
import { IncidentService } from '../incidents.service';


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
export class CalltakerComponent implements OnInit {


  displayedColumns: string[] = ['status', 'location', 'class', 'time'];
  dataSource: MatTableDataSource<Incident>;
  @ViewChild(MatSort) sort: MatSort;


  constructor(private router: Router, private authService: AuthService, private incidentService: IncidentService) { }

  ngOnInit(): void {

    this.incidentService.getIncidentsOngoing().subscribe(data => {
      this.dataSource = new MatTableDataSource(data);
      this.dataSource.sort = this.sort;
    });
  }

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
