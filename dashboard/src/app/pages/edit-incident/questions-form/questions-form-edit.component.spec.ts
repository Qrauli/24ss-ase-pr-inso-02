import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestionsFormEditComponent } from './questions-form-edit.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import { TranslateModule } from '@ngx-translate/core';
import { Observable, of as observableOf, throwError } from 'rxjs';

describe('EditIncidentComponent', () => {
  let component: QuestionsFormEditComponent;
  let fixture: ComponentFixture<QuestionsFormEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuestionsFormEditComponent, NoopAnimationsModule, HttpClientTestingModule, TranslateModule.forRoot()]
    })
      .compileComponents();

    fixture = TestBed.createComponent(QuestionsFormEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should run #fetchExistingQuestionary()', async () => {
    (<any>component).categorizationService = (<any>component).categorizationService || {};
    spyOn((<any>component).categorizationService, 'findById').and.returnValue(observableOf({}));
    spyOn((<any>component).categorizationService, 'createSession').and.returnValue(observableOf({}));
    spyOn(component, 'setAnswers');
    (<any>component).notificationService = (<any>component).notificationService || {};
    spyOn((<any>component).notificationService, 'showErrorNotification');
    component.translate = component.translate || {};
    spyOn(component.translate, 'instant');
    spyOn(component, 'readIncidentBaseInformation');
    (<any>component).fetchExistingQuestionary({
      questionaryId: {}
    });
    expect((<any>component).categorizationService.findById).toHaveBeenCalled();
    expect((<any>component).setAnswers).toHaveBeenCalled();
  });

  it('should run #setAnswers()', async () => {
    (<any>component).categorizationService = (<any>component).categorizationService || {};
    spyOn((<any>component).categorizationService, 'getBaseQuestionBundle').and.returnValue({
      answer: {
        answers: {
          "mpdsProtocolId": "mpdsProtocolId"
        }
      }
    });
    spyOn((<any>component).categorizationService, 'getProtocolQuestionBundles').and.returnValue([]);
    component.setAnswers();
    expect((<any>component).categorizationService.getBaseQuestionBundle).toHaveBeenCalled();
    expect((<any>component).categorizationService.getProtocolQuestionBundles).toHaveBeenCalled();
  });

  it('should run #readIncidentBaseInformation()', async () => {
    spyOn(component, 'saveAnswerWithIndexChange');
    (<any>component).readIncidentBaseInformation({
      location: {
        address: {
          city: {},
          street: {},
          postalCode: {},
          additionalInformation: {}
        },
        coordinates: {
          latitude: {
            toString: function() {}
          },
          longitude: {
            toString: function() {}
          }
        }
      },
      numberOfPatients: {
        toString: function() {}
      },
      patients: {
        0: {
          age: {
            toString: function() {}
          },
          sex: {
            valueOf: function() {}
          }
        },
        length: {}
      }
    });
    expect((<any>component).saveAnswerWithIndexChange).toHaveBeenCalled();
  });

  it('should run #saveAnswerWithIndexChange()', async () => {
    (<any>component).categorizationService = (<any>component).categorizationService || {};
    spyOn((<any>component).categorizationService, 'saveAnswer').and.returnValue(observableOf({}));
    spyOn(component, 'changeIndex');
    (<any>component).notificationService = (<any>component).notificationService || {};
    spyOn((<any>component).notificationService, 'showErrorNotification');
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'instant');
    (<any>component).saveAnswerWithIndexChange({
      questionType: {},
      questionId: {},
      protocolId: {}
    }, {});
    expect((<any>component).categorizationService.saveAnswer).toHaveBeenCalled();
    expect((<any>component).changeIndex).toHaveBeenCalled();
  });

  it('should run #changeIndex()', async () => {

    (<any>component).changeIndex(0);
    expect((<any>component).selectedIndex).toEqual(0);

  });

  it('should run #getSelectionQuestion()', async () => {
    (<any>component).categorizationService = (<any>component).categorizationService || {};
    spyOn((<any>component).categorizationService, 'getBaseQuestionBundle');
    expect((<any>component).getSelectionQuestion()).toBeFalsy();
  });

  it('should run #getSelectionQuestionCat()', async () => {
    (<any>component).categorizationService = (<any>component).categorizationService || {};
    (<any>component).categorization = true;
    spyOn((<any>component).categorizationService, 'getBaseQuestionBundle');
    expect((<any>component).getSelectionQuestion()).toBeFalsy();
  });

  it('should run #isSelectionQuestionDefined()', async () => {
    spyOn(component, 'getSelectionQuestion');
    expect((<any>component).isSelectionQuestionDefined()).toBeFalsy();
  });

  
  it('should run #isSelectionQuestionDefinedCat()', async () => {
    spyOn(component, 'getSelectionQuestion');
    (<any>component).categorization = true;
    expect((<any>component).isSelectionQuestionDefined()).toBeFalsy();
  });

  it('should run #isSelectionQuestionAnswered()', async () => {
    (<any>component).categorizationService = (<any>component).categorizationService || {};
    spyOn((<any>component).categorizationService, 'isBaseQuestionAnswered');
    expect((<any>component).isSelectionQuestionAnswered()).toBeFalsy();
  });

  it('should run #isSelectionQuestionAnsweredCat()', async () => {
    (<any>component).categorizationService = (<any>component).categorizationService || {};
    spyOn((<any>component).categorizationService, 'isBaseQuestionAnswered');
    (<any>component).categorization = true;
    expect((<any>component).isSelectionQuestionAnswered()).toBeFalsy();
  });

  it('should run #answerSelectionQuestion()', async () => {
    spyOn(component, 'saveAnswerWithIndexChange');
    (<any>component).answerSelectionQuestion({});
    expect((<any>component).saveAnswerWithIndexChange).toHaveBeenCalled();
  });

  it('should run #getProtocolQuestions()', async () => {
    (<any>component).categorizationService = (<any>component).categorizationService || {};
    spyOn((<any>component).categorizationService, 'getProtocolQuestionBundles').and.returnValue([{
      protocolQuestion: {}
    }]);
    (<any>component).getProtocolQuestions();
    expect((<any>component).categorizationService.getProtocolQuestionBundles).toHaveBeenCalled();
  });

  it('should run #isProtocolQuestionAnswerd()', async () => {
    (<any>component).categorizationService = (<any>component).categorizationService || {};
    spyOn((<any>component).categorizationService, 'isProtocolQuestionAnswered');
    expect((<any>component).isProtocolQuestionAnswerd({})).toBeFalsy();
  });

  it('should run #isProtocolQuestionAnswerdCat()', async () => {
    (<any>component).categorizationService = (<any>component).categorizationService || {};
    spyOn((<any>component).categorizationService, 'isProtocolQuestionAnswered');
    (<any>component).categorization = true;
    expect((<any>component).isProtocolQuestionAnswerd({})).toBeFalsy();
  });

  it('should run #answerProtocolQuestion()', async () => {
    spyOn(component, 'saveAnswerWithIndexChange');
    (<any>component).answerProtocolQuestion({
      id: {},
      protocolId: {}
    }, {}, {});
    expect((<any>component).saveAnswerWithIndexChange).toHaveBeenCalled();
  });

  it('should run #getTranslationForBaseQuestion()', async () => {
    (<any>component).translate = (<any>component).translate || {};
    (<any>component).translate.currentLang = 'currentLang';
    spyOn((<any>component).translate, 'instant');
    (<any>component).getTranslationForBaseQuestion({
      id: {},
      text: {}
    });
    expect((<any>component).translate.instant).toHaveBeenCalled();
  });

  it('should run #getTranslationForBaseQuestionOption()', async () => {
    (<any>component).translate = (<any>component).translate || {};
    (<any>component).translate.currentLang = 'currentLang';
    spyOn((<any>component).translate, 'instant').and.returnValue({
      index: {}
    });
    (<any>component).getTranslationForBaseQuestionOption({}, {
      id: {},
      fields: {
        0: {
          options: {
            index: {}
          }
        }
      }
    });
    expect((<any>component).translate.instant).toHaveBeenCalled();
  });

  it('should run #getTranslationForProtocolQuestion()', async () => {
    (<any>component).translate = (<any>component).translate || {};
    (<any>component).translate.currentLang = 'currentLang';
    spyOn((<any>component).translate, 'instant');
    (<any>component).getTranslationForProtocolQuestion({
      id: {},
      protocolId: {},
      text: {}
    });
    expect((<any>component).translate.instant).toHaveBeenCalled();
  });

  it('should run #getTranslationForProtocolQuestionOption()', async () => {
    (<any>component).translate = (<any>component).translate || {};
    (<any>component).translate.currentLang = 'currentLang';
    spyOn((<any>component).translate, 'instant').and.returnValue({
      index: {}
    });
    (<any>component).getTranslationForProtocolQuestionOption({}, {
      id: {},
      protocolId: {},
      fields: {
        0: {
          options: {
            index: {
              text: {}
            }
          }
        }
      }
    });
    expect((<any>component).translate.instant).toHaveBeenCalled();
  });
});
