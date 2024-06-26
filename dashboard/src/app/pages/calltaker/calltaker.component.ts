import {Component, OnInit, ViewChild} from '@angular/core';
import {MatCardModule} from '@angular/material/card'
import {MatButtonModule} from '@angular/material/button';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormsModule} from '@angular/forms';
import {MatIconModule} from '@angular/material/icon';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth/auth.service';
import {HeaderComponent} from '../../components/header/header.component';
import {MatListModule} from '@angular/material/list';
import {NgFor, NgIf} from '@angular/common';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import {Incident, State} from '../../dtos/incident';
import {MatSort, MatSortModule} from '@angular/material/sort';
import {MatInputModule} from '@angular/material/input';
import {MatExpansionModule} from '@angular/material/expansion';
import {IncidentService} from '../../services/incidents.service';
import {geocoderAddressConverter, prettyLocationAddress} from "../../dtos/incident";
import {NotificationService} from "../../services/notification.service";
import {TranslateModule, TranslateService} from '@ngx-translate/core';
import {Subscription, switchMap, timer } from 'rxjs';


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
    MatExpansionModule,
    TranslateModule
  ],
  templateUrl: './calltaker.component.html',
  styleUrl: './calltaker.component.css'
})
export class CalltakerComponent implements OnInit {


  displayedColumns: string[] = ['status', 'location', 'class'];
  dataSource: MatTableDataSource<Incident>;
  @ViewChild(MatSort) sort: MatSort;

  private incidentSubscription: Subscription;

  constructor(private router: Router, private authService: AuthService, private incidentService: IncidentService, private notificationService: NotificationService, public translate: TranslateService) { }

  ngOnInit(): void {
    this.incidentSubscription = timer(0, 500)
    .pipe(
      switchMap(() => this.incidentService.getIncidentsOngoingDispatcher())
    )
    .subscribe({
      next: data => {
        this.dataSource = new MatTableDataSource(data);
        this.dataSource.sort = this.sort;
        this.dataSource.data.sort((a, b) => {
          // move created to the top and everything else to the bottom
          if (a.state == State.READY) return -1;
          if (b.state == State.READY) return 1;
          return 0;
        });
      },
      error: (err) => {
        this.notificationService.showErrorNotification(
          this.translate.instant('CALLTAKER.ERROR_FETCH') + ': ' + err.message,
          'OK',
          7000
        );
      }
    }
    )
  }

  ngOnDestroy(): void {
    this.incidentSubscription.unsubscribe();
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

  protected readonly prettyLocationAddress = prettyLocationAddress;
  protected readonly geocoderAddressConverter = geocoderAddressConverter;
}
