import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { ActivatedRoute, Router } from '@angular/router';
import {FormBuilder, Validators, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatStepperModule} from '@angular/material/stepper';
import {MatSelectModule} from '@angular/material/select';
import {MatChipsModule} from '@angular/material/chips';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatTabsModule} from '@angular/material/tabs';
import {MatIconModule} from '@angular/material/icon';


@Component({
  selector: 'app-edit-incident',
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
    MatIconModule],
  templateUrl: './edit-incident.component.html',
  styleUrl: './edit-incident.component.css'
})
export class EditIncidentComponent {

  incident: string = '';

  constructor(private _formBuilder: FormBuilder, private router: Router, private activatedRoute: ActivatedRoute) {
    this.incident = "/incident/" + this.activatedRoute.snapshot.params['id'];
  }

  firstFormGroup = this._formBuilder.group({
    firstCtrl: ['', Validators.required],
  });
  secondFormGroup = this._formBuilder.group({
    secondCtrl: ['', Validators.required],
  });
  isLinear = true;
  selectedIndex = 0;

  saveIncident(): void {
    // Implement your save logic here
    this.router.navigate([this.incident]);
  }

  changeIndex(index: number): void {
    this.selectedIndex = index;
  }

}
