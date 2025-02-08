import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditIncidentComponent } from './edit-incident.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {OAuthModule} from "angular-oauth2-oidc";
import {RouterTestingModule} from "@angular/router/testing";
import { TranslateModule } from '@ngx-translate/core';
import { Observable, of as observableOf, throwError } from 'rxjs';
import { LocationFormEditComponent } from './location-form/location-form-edit.component';

describe('EditIncidentComponent', () => {
  let component: EditIncidentComponent;
  let fixture: ComponentFixture<EditIncidentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LocationFormEditComponent, EditIncidentComponent, RouterTestingModule, NoopAnimationsModule, HttpClientTestingModule, OAuthModule.forRoot(), TranslateModule.forRoot() ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditIncidentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should run #constructor()', async () => {
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
    spyOn((<any>component).questionsFormComponent, 'fetchExistingQuestionary');
    (<any>component).handleSelectionChange({
      previouslySelectedStep: {
        label: {}
      },
      selectedStep: {
        label: "test"
      }
    });
    expect((<any>component).questionsFormComponent.fetchExistingQuestionary).toHaveBeenCalled();
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

  it('should run #updateIncident()', async () => {
    (<any>component).incident = (<any>component).incident || {};
    (<any>component).incident.code = 'code';
    (<any>component).incidentService = (<any>component).incidentService || {};
    spyOn((<any>component).incidentService, 'updateIncident').and.returnValue(observableOf({}));
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
    (<any>component).updateIncident();
    expect((<any>component).incidentService.updateIncident).toHaveBeenCalled();
    expect((<any>component).router.navigate).toHaveBeenCalled();
  });

  it('should run #ngOnInit()', async () => {
    (<any>component).incidentService = (<any>component).incidentService || {};
    spyOn((<any>component).incidentService, 'getIncidentById').and.returnValue(observableOf({
      location: {
        address: {
          street: {}
        },
        coordinates:{
          latitude: 0,
          longitude: 0
        }
      },
      numberOfPatients: 0,
      code: 'code'
    }));
    (<any>component).activatedRoute = (<any>component).activatedRoute || {};
    (<any>component).activatedRoute.snapshot = {
      params: {
        'id': {}
      }
    };
    (<any>component).locationFormComponent = (<any>component).locationFormComponent || {};
    (<any>component).locationFormComponent.form = {
      patchValue: function() {}
    };
    spyOn<any>((<any>component).locationFormComponent, 'updateMarker');
    spyOn<any>((<any>component).locationFormComponent, 'updateAddressInternal');
    (<any>component).locationFormComponent.coordinates = 'coordinates';

    (<any>component).personFormComponent = (<any>component).personFormComponent || {};
    (<any>component).personFormComponent.form = {
      patchValue: function() {}
    };
    (<any>component).personFormComponent.patients = 'patients';
    (<any>component).notificationService = (<any>component).notificationService || {};
    spyOn((<any>component).notificationService, 'showErrorNotification');
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'instant');
    (<any>component).ngOnInit();
    expect((<any>component).incidentService.getIncidentById).toHaveBeenCalled();
    expect((<any>component).locationFormComponent.updateMarker).toHaveBeenCalled();
    expect((<any>component).locationFormComponent.updateAddressInternal).toHaveBeenCalled();
  });


});
