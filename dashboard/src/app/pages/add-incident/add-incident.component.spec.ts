import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddIncidentComponent } from './add-incident.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {OAuthModule} from "angular-oauth2-oidc";
import { TranslateModule } from '@ngx-translate/core';
import { Observable, of as observableOf, throwError } from 'rxjs';

describe('AddIncidentComponent', () => {
  let component: AddIncidentComponent;
  let fixture: ComponentFixture<AddIncidentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddIncidentComponent, NoopAnimationsModule, HttpClientTestingModule, OAuthModule.forRoot(), TranslateModule.forRoot()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddIncidentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('should run #handleSelectionChange()', async () => {
    (<any>component).locationFormComponent = (<any>component).locationFormComponent || {};
    (<any>component).locationFormComponent.form = {
      value: {
        street: {},
        postalCode: {},
        city: {},
        additionalInformation: {}
      }
    };
    (<any>component).locationFormComponent.coordinates = 'coordinates';
    (<any>component).incident = (<any>component).incident || {};
    (<any>component).incident.location = 'location';
    (<any>component).incident.patients = 'patients';
    (<any>component).incident.numberOfPatients = 'numberOfPatients';
    (<any>component).incident.questionaryId = 'questionaryId';
    (<any>component).personFormComponent = (<any>component).personFormComponent || {};
    (<any>component).personFormComponent.patients = {
      length: {}
    };
    (<any>component).summaryTags = (<any>component).summaryTags || {};
       spyOn((<any>component).summaryTags, 'concat');
    (<any>component).questionsFormComponent = (<any>component).questionsFormComponent || {};
    (<any>component).questionsFormComponent.summaryTags = 'summaryTags';
    (<any>component).questionsFormComponent.recommendation = 'recommendation';
    (<any>component).questionsFormComponent.questionaryId = 'questionaryId';
    (<any>component).questionsFormLabel = "test";
    spyOn((<any>component).questionsFormComponent, 'readIncidentBaseInformation');
    (<any>component).handleSelectionChange({
      previouslySelectedStep: {
        label: {}
      },
      selectedStep: {
        label: "test"
      }
    });
    expect((<any>component).questionsFormComponent.readIncidentBaseInformation).toHaveBeenCalled();
  });

  it('should run #changeCategory()', async () => {

    (<any>component).changeCategory('category');
    expect((<any>component).selectedCategory).toEqual('category');

  });

  it('should run #loadSummary()', async () => {
    (<any>component).summaryTags = (<any>component).summaryTags || {};
    spyOn((<any>component).summaryTags, 'push');
    (<any>component).incident = (<any>component).incident || {};
    (<any>component).incident.numberOfPatients = 1;
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'instant');
    expect((<any>component).loadSummary()).toBeTrue();
  });

  it('should run #categorySet()', async () => {

    (<any>component).selectedCategory = 'selectedCategory';
    expect((<any>component).categorySet()).toBeTrue();

  });

  it('should run #locationCoordinatesSet()', async () => {
    (<any>component).incident = (<any>component).incident || {};
    let coordinates = {
      latitude: 0,
      longitude: 0
    };
    (<any>component).incident.location = {
      coordinates: coordinates
    };
    expect((<any>component).locationCoordinatesSet()).toBeTrue();

  });

  it('should run #saveIncident()', async () => {
    (<any>component).incident = (<any>component).incident || {};
    (<any>component).incident.code = 'code';
    (<any>component).incidentService = (<any>component).incidentService || {};
    spyOn((<any>component).incidentService, 'saveIncident').and.returnValue(observableOf({}));
    (<any>component).router = (<any>component).router || {};
    spyOn((<any>component).router, 'navigate').and.returnValue({
      then: function() {
        return [
          null
        ];
      }
    });
    (<any>component).notificationService = (<any>component).notificationService || {};
    spyOn((<any>component).notificationService, 'showDefaultNotification');
    spyOn((<any>component).notificationService, 'showErrorNotification');
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'instant');
    (<any>component).saveIncident();
    expect((<any>component).incidentService.saveIncident).toHaveBeenCalled();
    expect((<any>component).router.navigate).toHaveBeenCalled();
  });
});
