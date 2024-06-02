import {AfterViewInit, Component} from "@angular/core";
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
import {
  Answer,
  Categorization,
  FieldType,
  ProtocolQuestion,
  QuestionType
} from "../../../dtos/categorization";
import {Incident, Sex} from "../../../dtos/incident";
import {NgForOf, NgIf} from "@angular/common";

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
    MatDividerModule, NgIf, NgForOf],
  templateUrl: './questions-form.component.html',
  styleUrl: '../add-incident.component.css'
})
export class QuestionsFormComponent implements AfterViewInit {

  constructor( private categorizationService: CategorizationService, private notificationService: NotificationService) {}

  private categorization: Categorization;

  // TODO add functionality
  summaryTags: string[] = [];

  recommendation = '';
  questionaryId = '';

  // ####################### Start Session ####################### //

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

  // ####################### Incident Data for first BASE Question ####################### //

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

    this.saveAnswerWithIndexChange(answer, 0);
    // TODO get summaryTags from Form Component
  }

  // ####################### Save Answers ####################### //

  saveAnswerWithIndexChange(answer: Answer, index: number) {
    this.categorizationService.saveAnswer(this.questionaryId, answer).subscribe({
      next: (value) => {
        this.categorization = value;
        switch (answer.questionType) {
          case QuestionType.BASE:
            console.log("Antwort erfolgreich gespeichert zu Basis Frage mit ID " + answer.questionId);
            break;
          case QuestionType.PROTOCOL:
            console.log("Antwort erfolgreich gespeichert zu Protokoll Frage mit ID " + answer.questionId + " im Protokoll " + answer.protocolId);
            break;
          default:
            console.log("Antwort erfolgreich gespeichert");
        }

        if (this.categorization.dispatchCode && this.categorization.dispatchCode != ''){
          this.recommendation = this.categorization.dispatchCode;
        }

        this.changeIndex(index);
      },
      error: (err) => {
        this.notificationService.showErrorNotification(
          'Fehler in der Kommunikation mit dem Kategorisierungs-Service: \n\n' + JSON.stringify(err, null, 2),
          'OK',
          7000
        );
      }
    });
  }

  // ####################### Stepper ####################### //

  changeIndex(index: number): void {
    this.selectedIndex = index;
  }

  selectedIndex = 0;

  // ####################### Selection Question ####################### //

  selectionQuestionOption: string;

  getSelectionQuestion() {
    if (!this.categorization) {
      return undefined;
    }
    return this.categorizationService.getBaseQuestionBundle(this.categorization, "2");
  }

  isSelectionQuestionDefined() {
    if (!this.categorization) {
      return false;
    }
    return this.getSelectionQuestion() != undefined;
  }

  isSelectionQuestionAnswered() {
    if (!this.categorization) {
      return false;
    }
    return this.categorizationService.isBaseQuestionAnswered(this.categorization, "2");
  }

  answerSelectionQuestion(mpdsProtocolId: string) {
    const answer: Answer = {
      questionType: QuestionType.BASE,
      questionId: "2",
      protocolId: "",
      answers: {
        mpdsProtocolId: mpdsProtocolId
      }
    }

    this.recommendation = '';

    this.saveAnswerWithIndexChange(answer, 1);
  }

  // ####################### Protocol Questions ####################### //

  selectedProtocolOptions: {[protocolId: string]: { [questionId: string]: { [fieldId: string]: string }}} = {};

  getProtocolQuestions() {

    const questions = this.categorizationService.getProtocolQuestionBundles(this.categorization)
      .map(bundle => bundle.protocolQuestion!);

    for (let question of questions) {

      if (!this.selectedProtocolOptions[question.protocolId]) {
        this.selectedProtocolOptions[question.protocolId] = {}
      }

      if (!this.selectedProtocolOptions[question.protocolId][question.id]) {
        this.selectedProtocolOptions[question.protocolId][question.id] = {};
      }
      // for (let field of question.fields) {
      //   this.selectedProtocolOptions[question.protocolId][question.id] = {};
      // }
    }

    return questions;
  }

  isProtocolQuestionAnswerd(id: string) {
    if (!this.categorization) {
      return false;
    }
    return this.categorizationService.isProtocolQuestionAnswered(this.categorization, id);
  }

  answerProtocolQuestion(question: ProtocolQuestion, answers: {[fieldId: string]: string}, index: number) {

    console.log(this.selectedProtocolOptions);
    console.log(answers)

    const answer: Answer = {
      questionType: QuestionType.PROTOCOL,
      questionId: question.id,
      protocolId: question.protocolId,
      answers: answers
    }

    this.recommendation = '';

    this.saveAnswerWithIndexChange(answer, index);

  }

  protected readonly QuestionType = QuestionType;
  protected readonly FieldType = FieldType;
}
