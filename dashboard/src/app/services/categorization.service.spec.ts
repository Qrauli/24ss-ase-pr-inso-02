import { TestBed } from '@angular/core/testing';

import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Observable, of as observableOf, throwError } from 'rxjs';
import { CategorizationService } from './categorization.service';
import { QuestionType } from '../dtos/categorization';

describe('CategorizationService', () => {
  let service: CategorizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [HttpClientTestingModule]});
    service = TestBed.inject(CategorizationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should run #createSession()', async () => {
    (<any>service).httpClient = (<any>service).httpClient || {};
    spyOn((<any>service).httpClient, 'post').and.returnValue(observableOf('post'));
    service.createSession();
    expect((<any>service).httpClient.post).toHaveBeenCalled();
  });

  it('should run #saveAnswer()', async () => {
    (<any>service).httpClient = (<any>service).httpClient || {};
    spyOn((<any>service).httpClient, 'put').and.returnValue(observableOf('put'));
    service.saveAnswer("", {
      questionType: QuestionType.BASE,
      questionId: '',
      protocolId: '',
      answers: {}
    });
    expect((<any>service).httpClient.put).toHaveBeenCalled();
  });

  it('should run #findById()', async () => {
    (<any>service).httpClient = (<any>service).httpClient || {};
    spyOn((<any>service).httpClient, 'get');
    service.findById("");
    expect((<any>service).httpClient.get).toHaveBeenCalled();
  });

  it('should run #getBaseQuestionBundles()', async () => {

    expect(service.getBaseQuestionBundles({
      sessionID: '',
      questionBundles: [
        {
          baseQuestion: {
            questionType: QuestionType.BASE,
            id: '',
            text: '',
            fields: []
          }
        }
      ]
    }).length).toBe(1);
    
  });

  it('should run #isBaseQuestionAnswered()', async () => {
    spyOn(service, 'getBaseQuestionBundle').and.returnValue({
    });
    service.isBaseQuestionAnswered({
      sessionID: '',
      questionBundles: []
    }, "");
    expect(service.getBaseQuestionBundle).toHaveBeenCalled();
  });

  it('should run #getProtocolQuestionBundles()', async () => {
    expect(service.getProtocolQuestionBundles({
      sessionID: '',
      questionBundles: [
        {
          protocolQuestion: {
            questionType: QuestionType.PROTOCOL,
            id: '',
            text: '',
            protocolId: '',
            fields: []
          }
        }
      ]
    }).length).toBe(1);
  });


  it('should run #isProtocolQuestionAnswered()', async () => {
    spyOn(service, 'getProtocolQuestionBundle').and.returnValue({
     
    });
    service.isProtocolQuestionAnswered({
      sessionID: '',
      questionBundles: []
    }, "");
    expect(service.getProtocolQuestionBundle).toHaveBeenCalled();
  });

  it('should run #getBaseQuestionBundle()', async () => {
    spyOn(service, 'getBaseQuestionBundles').and.returnValue([
      {
        baseQuestion: {
          questionType: QuestionType.BASE,
          id: '',
          text: '',
          fields: []
        }
      },
      {
        baseQuestion: {
          questionType: QuestionType.BASE,
          id: '',
          text: '',
          fields: []
        }
      }
    
    ]);
    service.getBaseQuestionBundle({
      sessionID: '',
      questionBundles: []
    }, "");
    expect(service.getBaseQuestionBundles).toHaveBeenCalled();
  });

  it('should run #getProtocolQuestionBundle()', async () => {
    spyOn(service, 'getProtocolQuestionBundles').and.returnValue([
      {
        protocolQuestion: {
          questionType: QuestionType.PROTOCOL,
          id: '',
          text: '',
          protocolId: '',
          fields: []
        }
      },
      {
        protocolQuestion: {
          questionType: QuestionType.PROTOCOL,
          id: '',
          text: '',
          protocolId: '',
          fields: []
        }
      }
    ]);
    service.getProtocolQuestionBundle({
      sessionID: '',
      questionBundles: []
    }, "");
    expect(service.getProtocolQuestionBundles).toHaveBeenCalled();
  });
});
