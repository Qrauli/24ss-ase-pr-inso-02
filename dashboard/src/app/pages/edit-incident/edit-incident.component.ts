import {Component, OnInit, ViewChild} from '@angular/core';
import {HeaderComponent} from '../../components/header/header.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatStepperModule} from '@angular/material/stepper';
import {MatButtonModule} from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';
import {ActivatedRoute, Router} from '@angular/router';
import {MatChipsModule} from '@angular/material/chips';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import {MatTabsModule} from '@angular/material/tabs';
import {MatIconModule} from '@angular/material/icon';
import {LeafletModule} from '@asymmetrik/ngx-leaflet';
import {MatRadioModule} from '@angular/material/radio';
import {MatMenuModule} from '@angular/material/menu';
import {MatTableModule} from '@angular/material/table';
import "leaflet-control-geocoder/dist/Control.Geocoder.js";
import {MatDividerModule} from '@angular/material/divider';
import {Incident, State} from "../../dtos/incident";
import {IncidentService} from "../../services/incidents.service";
import {LocationFormEditComponent} from "./location-form/location-form-edit.component";
import {PersonsFormEditComponent} from "./persons-form/persons-form-edit.component";
import {QuestionsFormEditComponent} from "./questions-form/questions-form-edit.component";
import {NgForOf, NgIf} from "@angular/common";
import {NotificationService} from "../../services/notification.service";
import {CategorizationService} from "../../services/categorization.service";
import {StepperSelectionEvent} from "@angular/cdk/stepper";
import * as Leaflet from "leaflet";
import {TranslateModule, TranslateService} from '@ngx-translate/core';


@Component({
  selector: 'app-add-incident',
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
    MatIconModule,
    LeafletModule,
    MatRadioModule,
    MatMenuModule,
    MatTableModule,
    MatDividerModule,
    LocationFormEditComponent,
    TranslateModule,
    PersonsFormEditComponent, QuestionsFormEditComponent, NgIf, NgForOf],
  templateUrl: './edit-incident.component.html',
  styleUrl: './edit-incident.component.css'
})
export class EditIncidentComponent implements OnInit {

  incident: Incident;

  questionaryId = '';

  locationFormLabel = "location";
  personsFormLabel = "persons";
  questionsFormLabel = "questions";

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private incidentService: IncidentService, private categorizationService: CategorizationService, private notificationService: NotificationService, public translate: TranslateService) {
    this.incident = {
      id: '',
      patients: [],
      numberOfPatients: 0,
      code: '',
      location: {
        address: {
          street: '',
          postalCode: '',
          city: '',
          additionalInformation: ''
        },
        coordinates: {latitude: 48.227747192035764, longitude: 16.40545336304577}
      },
      state: State.READY,
      questionaryId: '',
      callerNumber: ''
    };
  }

  ngOnInit(): void {
    this.incidentService.getIncidentById(this.activatedRoute.snapshot.params['id']).subscribe({
      next: (value) => {
        this.incident = value;
        console.log("Successfully fetched incident " + this.incident.id);

        this.locationFormComponent.form.patchValue({
          street: this.incident.location.address.street,
          postalCode: this.incident.location.address.postalCode,
          city: this.incident.location.address.city,
          additionalInformation: this.incident.location.address.additionalInformation,
        });

        const coords = new Leaflet.LatLng(this.incident.location.coordinates!.latitude, this.incident.location.coordinates!.longitude);
        this.locationFormComponent.map.setView(coords, 16);
        this.locationFormComponent.updateMarker(coords);
        this.locationFormComponent.updateAddressInternal(this.incident.location.address);

        this.locationFormComponent.coordinates = { latitude: this.incident.location.coordinates!.latitude, longitude: this.incident.location.coordinates!.longitude };

        this.personFormComponent.form.patchValue({caller: {number: this.incident.callerNumber}});
        this.personFormComponent.form.patchValue({numberOfPatients: this.incident.numberOfPatients.toString()});
        this.personFormComponent.patients = this.incident.patients;

        // passed to form as input
        this.questionaryId = this.incident.questionaryId;

        this.selectedCategory = this.incident.code.toString();
      },
      error: (err) => {
        this.notificationService.showErrorNotification(
          this.translate.instant('INCIDENT.EDIT_FETCH_ERROR') + ': ' + err.message,
          'OK',
          7000
        );
      }
    });
  }

  handleSelectionChange(event: StepperSelectionEvent) {
    const previous = event.previouslySelectedStep.label;
    switch (previous) {
      case this.locationFormLabel: {
        const locationFormValues = this.locationFormComponent.form.value;

        this.incident.location = {
          address: {
            street: locationFormValues.street!,
            postalCode: locationFormValues.postalCode!,
            city: locationFormValues.city!,
            additionalInformation: locationFormValues.additionalInformation!
          },
          coordinates: this.locationFormComponent.coordinates
        }

        break;
      }
      case this.personsFormLabel: {
        const numberOfPatients = this.personFormComponent.patients.length;

        this.incident.patients = this.personFormComponent.patients;
        this.incident.numberOfPatients = numberOfPatients;

        break;
      }
      case this.questionsFormLabel: {

        this.summaryTags.concat(this.questionsFormComponent.summaryTags);

        this.recommendedCategory = this.questionsFormComponent.recommendation;
        this.incident.questionaryId = this.questionsFormComponent.questionaryId;
        break;
      }
    }

    if (event.selectedStep.label == this.questionsFormLabel) {
      this.questionsFormComponent.fetchExistingQuestionary(this.incident);
    }
  }

  @ViewChild(LocationFormEditComponent) locationFormComponent: LocationFormEditComponent;

  @ViewChild(PersonsFormEditComponent) personFormComponent: PersonsFormEditComponent;

  @ViewChild(QuestionsFormEditComponent) questionsFormComponent: QuestionsFormEditComponent;

  // ####################### Categorization ####################### //

  recommendedCategory = '';
  selectedCategory = '';

  changeCategory(category: string): void {
    this.selectedCategory = category;
  }

  summaryTags: string[] = [];

  loadSummary(): boolean {
    this.summaryTags = [];

    if (this.incident.numberOfPatients == 1) {
      this.summaryTags.push(this.translate.instant('INCIDENT.HURT_PERSON'));
    }

    if (this.incident.numberOfPatients > 1) {
      this.summaryTags.push(this.incident.numberOfPatients + this.translate.instant('INCIDENT.HURT_PERSONS'));
    }

    return this.summaryTags.length > 0;
  }

  // ####################### Saving ####################### //

  categorySet(): boolean {
    return !!this.selectedCategory;
  }

  locationCoordinatesSet(): boolean {
    return !!this.incident.location.coordinates;
  }

  updateIncident(): void {

    this.incident.code = this.selectedCategory;

    this.incidentService.updateIncident(this.incident).subscribe({
      next: (_value) => {
        this.questionsFormComponent.readIncidentBaseInformation(this.incident); // update answer in CategorizationService
        this.router.navigate(['/calltaker']).then((navigated: boolean) => {
          if (navigated) {
            this.notificationService.showDefaultNotification(
              this.translate.instant('INCIDENT.EDIT_SUCCESS'),
              'OK',
              7000
            );
          }
        });
      },
      error: (err) => {
        this.notificationService.showErrorNotification(
          this.translate.instant('INCIDENT.EDIT_ERROR') + ': ' + err.message,
          'OK',
          7000
        );
      }
    });
  }
}
