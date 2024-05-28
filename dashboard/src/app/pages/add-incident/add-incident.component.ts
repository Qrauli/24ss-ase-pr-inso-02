import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {HeaderComponent} from '../../components/header/header.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatStepperModule} from '@angular/material/stepper';
import {MatButtonModule} from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';
import {Router} from '@angular/router';
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
import {LocationFormComponent} from "./location-form/location-form.component";
import {PersonsFormComponent} from "./persons-form/persons-form.component";
import {QuestionsFormComponent} from "./questions-form/questions-form.component";
import {NgForOf, NgIf} from "@angular/common";
import {NotificationService} from "../../services/notification.service";
import {CategorizationService} from "../../services/categorization.service";
import {Answer, Categorization, QuestionType} from "../../dtos/categorization";

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
    LocationFormComponent,
    PersonsFormComponent, QuestionsFormComponent, NgIf, NgForOf],
  templateUrl: './add-incident.component.html',
  styleUrl: './add-incident.component.css'
})
export class AddIncidentComponent implements AfterViewInit {

  incident: Incident;

  locationFormLabel = "location";
  personsFormLabel = "persons";
  questionsFormLabel = "questions";

  constructor(private router: Router, private incidentService: IncidentService, private categorizationService: CategorizationService, private notificationService: NotificationService) {
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
      questionaryId: ''
    };
  }

  handleSelectionChange(previous: string) {
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

        // if (this.categorization?.sessionID) {
        //
        //   const answerMap = new Map<string, string>();
        //
        //   answerMap.set('street', locationFormValues.street!);
        //   // TODO number?
        //   // TODO City missing
        //   answerMap.set('district', locationFormValues.postalCode!);
        //   answerMap.set('lat', this.locationFormComponent.coordinates.latitude.toString());
        //   answerMap.set('lng', this.locationFormComponent.coordinates.longitude.toString());
        //   answerMap.set('additionalData', locationFormValues.additionalInformation!);
        //
        //   const answer: Answer = {
        //     questionId: "1",
        //     questionType: QuestionType.BASE,
        //     protocolId: "null",
        //     answers: answerMap
        //   };
        //
        //   this.categorizationService.saveAnswer(this.categorization.sessionID, answer).subscribe({
        //     next: (value) => {
        //       this.categorization = value;
        //     },
        //     error: (err) => {
        //       this.notificationService.showErrorNotification(
        //         'Fehler in der Kommunikation mit dem Kategorisierungs Service: \n\n' + JSON.stringify(err, null, 2),
        //         'OK',
        //         7000
        //       );
        //     }
        //   });
        // }

        break;
      }
      case this.personsFormLabel: {

        const locationFormValues = this.personFormComponent.form.value;

        const numberOfPatients = this.personFormComponent.patients.length;

        this.incident.patients = this.personFormComponent.patients;
        this.incident.numberOfPatients = numberOfPatients;

        // if (this.categorization?.sessionID) {
        //
        //   var answerMap = new Map<string, string>();
        //
        //   answerMap.set('number', locationFormValues.caller?.number!);
        //   answerMap.set('name', locationFormValues.caller?.name!);
        //
        //   var answer: Answer = {
        //     questionId: "2",
        //     questionType: QuestionType.BASE,
        //     protocolId: "null",
        //     answers: answerMap
        //   };
        //
        //   this.categorizationService.saveAnswer(this.categorization.sessionID, answer).subscribe({
        //     next: (value) => {
        //       this.categorization = value;
        //     },
        //     error: (err) => {
        //       this.notificationService.showErrorNotification(
        //         'Fehler in der Kommunikation mit dem Kategorisierungs Service: \n\n' + JSON.stringify(err, null, 2),
        //         'OK',
        //         7000
        //       );
        //     }
        //   });
        //
        //   answerMap = new Map<string, string>();
        //
        //   answerMap.set('numberOfPeople', numberOfPatients.toString());
        //
        //   if (numberOfPatients == 1) {
        //     answerMap.set('age', this.personFormComponent.patients[0].age.toString());
        //     answerMap.set('gender', this.personFormComponent.patients[0].sex);
        //   }
        //
        //   answer = {
        //     questionId: "3",
        //     questionType: QuestionType.BASE,
        //     protocolId: "null",
        //     answers: answerMap
        //   };
        //
        //   this.categorizationService.saveAnswer(this.categorization.sessionID, answer).subscribe({
        //     next: (value) => {
        //       this.categorization = value;
        //     },
        //     error: (err) => {
        //       this.notificationService.showErrorNotification(
        //         'Fehler in der Kommunikation mit dem Kategorisierungs Service: \n\n' + JSON.stringify(err, null, 2),
        //         'OK',
        //         7000
        //       );
        //     }
        //   });
        // }

        break;
      }
      case this.questionsFormLabel: {

        console.log(this.categorization);

        this.recommendedCategory = this.questionsFormComponent.recommendation;
        this.incident.questionaryId = this.questionsFormComponent.questionaryId;
        break;
      }
      // default: {
      //   alert(JSON.stringify(this.incident, null, 2));
      // }
    }
  }

  @ViewChild(LocationFormComponent) locationFormComponent: LocationFormComponent;

  @ViewChild(PersonsFormComponent) personFormComponent: PersonsFormComponent;

  @ViewChild(QuestionsFormComponent) questionsFormComponent: QuestionsFormComponent;

  // ####################### Categorization ####################### //

  categorization: Categorization;

  ngAfterViewInit(): void {
    this.categorizationService.createSession().subscribe({
      next: (value) => {
        this.categorization = value;
        console.log("Generated categorization session with id " + this.categorization.sessionID);
      },
      error: (err) => {
        this.notificationService.showErrorNotification(
          'Es konnte keine Session für die Kategorisierung erstellt werden: \n\n' + JSON.stringify(err, null, 2),
          'OK',
          7000
        );
      }
    });
  }

  recommendedCategory = '';
  selectedCategory = '';

  changeCategory(category: string): void {
    this.selectedCategory = category;
  }

  summaryTags: string[] = [];

  loadSummary(): boolean {
    this.summaryTags = [];

    if (this.incident.numberOfPatients == 1) {
      this.summaryTags.push("eine verletzte Person");
    }

    if (this.incident.numberOfPatients > 1) {
      this.summaryTags.push(this.incident.numberOfPatients + " verletzte Personen");
    }

    // TODO get summaryTags from Form Component
    this.summaryTags.push("Bewusstlos", "Schnittwunde", "schwer zugänglich");

    return this.summaryTags.length > 0;
  }

  // ####################### Saving ####################### //

  categorySet(): boolean {
    return !!this.selectedCategory;
  }

  locationCoordinatesSet(): boolean {
    return !!this.incident.location.coordinates;
  }

  saveIncident(): void {

    this.incident.code = this.selectedCategory;

    this.incidentService.saveIncident(this.incident).subscribe({
      next: (_value) => {
        this.router.navigate(['/calltaker']).then((navigated: boolean) => {
          if (navigated) {
            this.notificationService.showDefaultNotification(
              'Einsatz erfolgreich erstellt!',
              'OK',
              7000
            );
          }
        });
      },
      error: (err) => {
        this.notificationService.showErrorNotification(
          'Es ist Fehler beim Erstellen des Einsatzes aufgetreten: \n\n' + JSON.stringify(err, null, 2),
          'OK',
          7000
        );
      }
    });
  }
}
