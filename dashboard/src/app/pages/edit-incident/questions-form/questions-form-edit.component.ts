import {Component, Input} from "@angular/core";
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
  BaseQuestion,
  Categorization,
  FieldType,
  ProtocolQuestion,
  QuestionType
} from "../../../dtos/categorization";
import {Incident, Sex} from "../../../dtos/incident";
import {NgForOf, NgIf} from "@angular/common";
import {TranslateModule, TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'questions-form-edit',
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
    TranslateModule,
    MatDividerModule, NgIf, NgForOf],
  templateUrl: './questions-form-edit.component.html',
  styleUrl: '../edit-incident.component.css'
})
export class QuestionsFormEditComponent {
  constructor( private categorizationService: CategorizationService, private notificationService: NotificationService, public translate: TranslateService) {}

  private categorization: Categorization;

  // TODO add functionality
  summaryTags: string[] = [];

  recommendation = '';

  @Input() questionaryId = '';

  // ####################### Incident Data for first BASE Question ####################### //

  fetchExistingQuestionary(incident: Incident) {
    if (incident.questionaryId && incident.questionaryId != '') {
      this.categorizationService.findById(this.questionaryId).subscribe({
        next: (value) => {
          this.categorization = value;
          console.log("Successfully fetched categorization session with id " + this.categorization.sessionID);
          this.recommendation = this.categorization.dispatchCode ? this.categorization.dispatchCode : '';
          this.setAnswers();
        },
        error: (err) => {
          this.notificationService.showErrorNotification(
            this.translate.instant('INCIDENT.QUESTIONAIRE.ERROR_SESSION') + ': ' + err.message,
            'OK',
            7000
          );
        }
      });
    } else {
      this.categorizationService.createSession().subscribe({
        next: (value) => {
          this.categorization = value;
          this.questionaryId = this.categorization.sessionID;
          console.log("Generated categorization session with id " + this.categorization.sessionID);
          this.readIncidentBaseInformation(incident);
        },
        error: (err) => {
          this.notificationService.showErrorNotification(
            this.translate.instant('INCIDENT.QUESTIONAIRE.ERROR_SESSION') + ': ' + err.message,
            'OK',
            7000
          );
        }
      });
    }
  }

  setAnswers() {

    const selectionQuestionAnswer = this.categorizationService.getBaseQuestionBundle(this.categorization, "2")?.answer?.answers["mpdsProtocolId"]!;

    if (!selectionQuestionAnswer) {
      return;
    }

    this.selectionQuestionOption = selectionQuestionAnswer;

    for (let bundle of this.categorizationService.getProtocolQuestionBundles(this.categorization)){
      const question = bundle.protocolQuestion!;

      if (!this.selectedProtocolOptions[question.protocolId]) {
        this.selectedProtocolOptions[question.protocolId] = {}
      }

      if (!this.selectedProtocolOptions[question.protocolId][question.id]) {
        this.selectedProtocolOptions[question.protocolId][question.id] = bundle.answer?.answers!;
      }
      // for (let field of question.fields) {
      //   this.selectedProtocolOptions[question.protocolId][question.id] = {};
      // }
    }
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
          this.translate.instant('INCIDENT.QUESTIONAIRE.ERROR_COMMUNICATION') + ': ' + err.message,
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
    if(!mpdsProtocolId){
      return;
    }
    
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

    if(!answers || Object.keys(answers).length == 0){
      return;
    }

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

  getTranslationForBaseQuestion(baseQuestion?: BaseQuestion) {
    if(this.translate.currentLang != 'de'){
      return this.translate.instant('QUESTIONAIRE.BASE.' + baseQuestion?.id + '.TEXT');
    }
    else{
      return baseQuestion?.text;
    }
  }

  getTranslationForBaseQuestionOption(index: number, baseQuestion?: BaseQuestion) {
    if(this.translate.currentLang != 'de'){
      return this.translate.instant('QUESTIONAIRE.BASE.' + baseQuestion?.id + '.OPTIONS')[index];
    }
    else{
      return baseQuestion?.fields[0].options[index];
    }
  }

  getTranslationForProtocolQuestion(protocolQuestion?: ProtocolQuestion) {
    if(this.translate.currentLang != 'de'){
      return this.translate.instant('QUESTIONAIRE.PROTOCOL.' + protocolQuestion?.protocolId + '.' + protocolQuestion?.id + '.TEXT');
    }
    else{
      return protocolQuestion?.text;
    }
  }

  getTranslationForProtocolQuestionOption(index: number, protocolQuestion?: ProtocolQuestion) {
    if(this.translate.currentLang != 'de'){
      return this.translate.instant('QUESTIONAIRE.PROTOCOL.' + protocolQuestion?.protocolId + '.' + protocolQuestion?.id + '.OPTIONS')[index];
    }
    else{
      return protocolQuestion?.fields[0].options[index].text;
    }
  }


  protected readonly QuestionType = QuestionType;
  protected readonly FieldType = FieldType;
}
