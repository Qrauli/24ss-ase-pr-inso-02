import {AfterViewInit, Component, Input} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HeaderComponent} from "../../../components/header/header.component";
import {MatButtonModule} from "@angular/material/button";
import {MatStepperModule} from "@angular/material/stepper";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {MatChipsModule} from "@angular/material/chips";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatListModule} from "@angular/material/list";
import {MatTabsModule} from "@angular/material/tabs";
import {MatIconModule} from "@angular/material/icon";
import {LeafletModule} from "@asymmetrik/ngx-leaflet";
import {MatRadioModule} from "@angular/material/radio";
import {MatMenuModule} from "@angular/material/menu";
import {MatTableModule} from "@angular/material/table";
import {MatDividerModule} from "@angular/material/divider";
import {CategorizationService} from "../../../services/categorization.service";
import {NotificationService} from "../../../services/notification.service";
import {Answer, Categorization, QuestionType} from "../../../dtos/categorization";
import {Incident, Sex} from "../../../dtos/incident";

@Component({
  selector: 'questions-form',
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
    MatDividerModule],
  templateUrl: './questions-form.component.html',
  styleUrl: '../add-incident.component.css'
})
export class QuestionsFormComponent implements AfterViewInit {

  constructor( private categorizationService: CategorizationService, private notificationService: NotificationService) {}

  private categorization: Categorization;

  summaryTags: string[] = [];

  recommendation = '';
  questionaryId = '';

  ngAfterViewInit(): void {
    this.categorizationService.createSession().subscribe({
      next: (value) => {
        this.categorization = value;
        this.questionaryId = this.categorization.sessionID;
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

  readIncidentBaseInformation(incident: Incident) {

    const answer: Answer = {
      questionType: QuestionType.BASE,
      questionId: "1",
      protocolId: "",
      answers: {
        "city": incident.location.address.city,
        "street": incident.location.address.street,
        "postalCode": incident.location.address.postalCode,
        "additionalData": incident.location.address.additionalInformation,
        "lat": incident.location.coordinates ? incident.location.coordinates.latitude.toString(): "",
        "lon": incident.location.coordinates ? incident.location.coordinates.longitude.toString(): "",
        // "name": incident. TODO callee
        // "number": incident. TODO callee
        "numberOfPeople": incident.numberOfPatients.toString(),
        "age": incident.patients.length == 1 ? incident.patients[0].age.toString() : "0",
        "gender": incident.patients.length == 1 ? incident.patients[0].sex.valueOf() : Sex.UNKNOWN.valueOf()
      }
    }

    console.log("Answers after loading incident information: " + answer.answers);

    this.categorizationService.saveAnswer(this.questionaryId, answer).subscribe({
      next: (value) => {
        this.categorization = value;
        console.log("Successfull sent base incident information");
        console.log("Answers after sending incident information: " + answer.answers);
      },
      error: (err) => {
        this.notificationService.showErrorNotification(
          'Fehler in der Kommunikation mit dem Kategorisierungs-Service: \n\n' + JSON.stringify(err, null, 2),
          'OK',
          7000
        );
      }
    });

    // TODO get summaryTags from Form Component
    this.summaryTags.push("Bewusstlos", "Schnittwunde", "schwer zugänglich");

    console.log("Summary tags in questiosn form" + this.summaryTags);
  }

  // ####################### Questions ####################### //

  selectedIndex = 0;

  changeIndex(index: number): void {
    this.selectedIndex = index;
  }
}
