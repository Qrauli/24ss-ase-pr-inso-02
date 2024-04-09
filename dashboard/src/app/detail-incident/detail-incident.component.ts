import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import {MatListModule} from '@angular/material/list';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-detail-incident',
  standalone: true,
  imports: [HeaderComponent, MatListModule,  MatButtonModule],
  templateUrl: './detail-incident.component.html',
  styleUrl: './detail-incident.component.css'
})
export class DetailIncidentComponent {

  constructor(private router: Router, private activatedRoute: ActivatedRoute) {}

  /**
   * Navigate to the edit incident page
   */
  editIncident(): void {
    this.router.navigate(['/incident/' + this.activatedRoute.snapshot.params['id'] + '/edit']);
  }

}
