import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DispatcherComponent } from './dispatcher.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {OAuthModule} from "angular-oauth2-oidc";
import { TranslateModule } from '@ngx-translate/core';
import { Observable, Subscription, of as observableOf, throwError } from 'rxjs';
import { LatLng, Marker } from 'leaflet';
import { ResourceState, ResourceType } from '../../dtos/resource';
import { State } from '../../dtos/incident';

describe('DispatcherComponent', () => {
  let component: DispatcherComponent;
  let fixture: ComponentFixture<DispatcherComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DispatcherComponent, NoopAnimationsModule, HttpClientTestingModule, OAuthModule.forRoot(), TranslateModule.forRoot()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DispatcherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should run #ngOnDestroy()', async () => {
    (<any>component).subscriptions = (<any>component).subscriptions || {};
    (<any>component).subscriptions = [new Subscription()];
    (<any>component).ngOnDestroy();
    expect((<any>component).subscriptions[0].closed).toBeTrue();
  });

  it('should run #incidentRefresher()', async () => {
    spyOn(component, 'codeIsPriority');
    (<any>component).notificationService = (<any>component).notificationService || {};
    spyOn((<any>component).notificationService, 'showPriorityNotification');
    spyOn((<any>component).notificationService, 'showDefaultNotification');
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'instant');
    spyOn(component, 'selectIncident');
    (<any>component).incidents = [
      {
        id: "",
        location: {
          coordinates: {
            latitude: 0,
            longitude: 0
          },
          address: {
            street: '',
            postalCode: '',
            city: '',
            additionalInformation: ''
          }
        },
        patients: [],
        numberOfPatients: 0,
        code: "",
        state: State.READY,
        questionaryId: ''
      }
    ];
    (<any>component).incidentRefresher([
      {
        id: "",
        location: {
          coordinates: {
            latitude: 0,
            longitude: 0
          },
          address: {
            street: '',
            postalCode: '',
            city: '',
            additionalInformation: ''
          }
        },
        patients: [],
        numberOfPatients: 0,
        code: "",
        state: State.READY,
        questionaryId: ''
      }
    ]);
    expect(component.codeIsPriority).not.toHaveBeenCalled();
  });

  it('should run #incidentRefresherPrio()', async () => {
    spyOn(component, 'codeIsPriority').and.returnValue(true);
    (<any>component).notificationService = (<any>component).notificationService || {};
    spyOn((<any>component).notificationService, 'showPriorityNotification');
    spyOn((<any>component).notificationService, 'showDefaultNotification');
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'instant');
    spyOn(component, 'selectIncident');
    (<any>component).incidents = [];
    (<any>component).incidentRefresher([
      {
        id: "",
        location: {
          coordinates: {
            latitude: 0,
            longitude: 0
          },
          address: {
            street: '',
            postalCode: '',
            city: '',
            additionalInformation: ''
          }
        },
        patients: [],
        numberOfPatients: 0,
        code: "",
        state: State.READY,
        questionaryId: ''
      }
    ]);
    expect(component.codeIsPriority).toHaveBeenCalled();
    expect((<any>component).notificationService.showPriorityNotification).toHaveBeenCalled();
  });

  it('should run #incidentRefresherDefault()', async () => {
    spyOn(component, 'codeIsPriority').and.returnValue(false);
    (<any>component).notificationService = (<any>component).notificationService || {};
    spyOn((<any>component).notificationService, 'showPriorityNotification');
    spyOn((<any>component).notificationService, 'showDefaultNotification');
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'instant');
    spyOn(component, 'selectIncident');
    (<any>component).incidents = [];
    (<any>component).incidentRefresher([
      {
        id: "",
        location: {
          coordinates: {
            latitude: 0,
            longitude: 0
          },
          address: {
            street: '',
            postalCode: '',
            city: '',
            additionalInformation: ''
          }
        },
        patients: [],
        numberOfPatients: 0,
        code: "",
        state: State.READY,
        questionaryId: ''
      }
    ]);
    expect(component.codeIsPriority).toHaveBeenCalled();
    expect((<any>component).notificationService.showDefaultNotification).toHaveBeenCalled();
  });

  it('should run #requestRefresher()', async () => {
    (<any>component).resourceRequests = (<any>component).resourceRequests || {};
    (<any>component).resourceRequests.j = {
      id: {}
    };
    (<any>component).notificationService = (<any>component).notificationService || {};
    spyOn((<any>component).notificationService, 'showDefaultNotification');
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'instant');
    spyOn(component, 'selectIncident');
    spyOn(component, 'incidentFromId');
    (<any>component).resourceRequests = [{id: ""}];
    (<any>component).requestRefresher([
      {
        id: ""
      }
    ]);
    expect((<any>component).notificationService.showDefaultNotification).not.toHaveBeenCalled();
  });

  it('should run #requestRefresherNotification()', async () => {
    (<any>component).resourceRequests = (<any>component).resourceRequests || {};
    (<any>component).resourceRequests.j = {
      id: {}
    };
    (<any>component).notificationService = (<any>component).notificationService || {};
    spyOn((<any>component).notificationService, 'showDefaultNotification');
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'instant');
    spyOn(component, 'selectIncident');
    spyOn(component, 'incidentFromId');
    (<any>component).resourceRequests = [{id: ""}];
    (<any>component).requestRefresher([
      {
        id: "1"
      }
    ]);
    expect((<any>component).notificationService.showDefaultNotification).toHaveBeenCalled();
  });

  it('should run #onMapReady()', async () => {
    let map = {
      invalidateSize: function() {}
    };
    (<any>component).onMapReady(map);
    expect((<any>component).map).toEqual(map);
  });

  it('should run #showLocations()', async () => {
    (<any>component).resourceMarkers = [
      new Marker(new LatLng(0, 0))
    ];
    (<any>component).map = (<any>component).map || {};
    spyOn((<any>component).map, 'removeLayer');
    (<any>component).resources = (<any>component).resources || {};
    (<any>component).resources = [{
      state: {},
      assignedIncident: {},
      locationCoordinates: {
        latitude: {},
        longitude: {}
      },
      id: {}
    }];
    (<any>component).showLocations();
    expect((<any>component).map.removeLayer).toHaveBeenCalled();
  });

  it('should run #unassignResource()', async () => {
    (<any>component).assignedResources = (<any>component).assignedResources || [];
    spyOn((<any>component).assignedResources, 'indexOf');
    spyOn((<any>component).assignedResources, 'splice');
    (<any>component).unassignResource({
      id: {}
    });
    expect((<any>component).assignedResources.indexOf).toHaveBeenCalled();
  });

  it('should run #codeIsPriority()', async () => {

    expect((<any>component).codeIsPriority("2")).toBeTrue();  

  });

  it('should run #assignResource()', async () => {
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'instant');
    (<any>component).dialog = (<any>component).dialog || {};
    spyOn((<any>component).dialog, 'open').and.returnValue({
      afterClosed: function() {
        return observableOf({});
      }
    });
    (<any>component).assignedResources = (<any>component).assignedResources || [];
    spyOn((<any>component).assignedResources, 'push');
    (<any>component).assignResource({
      assignedIncident: {},
      id: {}
    });
    expect((<any>component).translate.instant).toHaveBeenCalled();
    expect((<any>component).dialog.open).toHaveBeenCalled();
  });

  it('should run #selectIncident()', async () => {
    (<any>component).incidentService = (<any>component).incidentService || {};
    spyOn((<any>component).incidentService, 'getRecommendations').and.returnValue(observableOf([]));
    component.recommended = component.recommended || {};
    spyOn(component.recommended, 'has');
    component.resources = component.resources || [];
    spyOn(component.resources, 'sort').and.returnValue([
      {
        "id": "",
        type: ResourceType.NEF,
        state: ResourceState.AVAILABE,
        locationCoordinates: {
          latitude: 0,
          longitude: 0
        }
      },
      {
        "id": "",
        type: ResourceType.NEF,
        state: ResourceState.AVAILABE,
        locationCoordinates: {
          latitude: 0,
          longitude: 0
        }
      }
    ]);
    component.map = component.map || {};
    spyOn(component.map, 'removeLayer');
    component.translate = component.translate || {};
    spyOn(component.translate, 'instant');
    spyOn(component, 'showLocations');
    spyOn(component, 'zoomToLocation');
    component.selectIncident({
      id: "",
      location: {
        coordinates: {
          latitude: 0,
          longitude: 0
        },
        address: {
          street: '',
          postalCode: '',
          city: '',
          additionalInformation: ''
        }
      },
      patients: [],
      numberOfPatients: 0,
      code: "",
      state: State.READY,
      questionaryId: ''
    });
    expect((<any>component).incidentService.getRecommendations).toHaveBeenCalled();
    expect(component.resources.sort).toHaveBeenCalled();
    expect(component.showLocations).toHaveBeenCalled();
    expect(component.zoomToLocation).toHaveBeenCalled();
  });


  it('should run #unselectIncident()', async () => {
    (<any>component).map = (<any>component).map || {};
    spyOn((<any>component).map, 'removeLayer');
    spyOn(component, 'showLocations');
    (<any>component).unselectIncident();
    expect((<any>component).map.removeLayer).toHaveBeenCalled();
    expect((<any>component).showLocations).toHaveBeenCalled();
  });

  it('should run #dispatchIncident()', async () => {
    (<any>component).assignedResources = (<any>component).assignedResources || [];
    (<any>component).assignedResources = ['assignedResources'];
    (<any>component).assignedResources.type = 'type';
    (<any>component).resources = (<any>component).resources || [];
    spyOn((<any>component).resources, 'find').and.returnValue([
      {
        "id": {}
      }
    ]);
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'instant');
    (<any>component).dialog = (<any>component).dialog || {};
    (<any>component).resources = [{id: ""}];
    (<any>component).selectedIncident = "test";
    (<any>component).assignedResources = ["test"];
    spyOn((<any>component).dialog, 'open').and.returnValue({
      afterClosed: function() {
        return observableOf(true);
      }
    });
    (<any>component).resourcesService = (<any>component).resourcesService || {};
    spyOn((<any>component).resourcesService, 'assignResources');
    (<any>component).dispatchIncident();
    expect((<any>component).translate.instant).toHaveBeenCalled();
    expect((<any>component).dialog.open).toHaveBeenCalled();
    expect((<any>component).resourcesService.assignResources).toHaveBeenCalled();
  });

  it('should run #hasRequestFalse()', async () => {
    (<any>component).resourceRequests = (<any>component).resourceRequests || {};
    (<any>component).resourceRequests.i = {
      assignedIncident: {}
    };
    expect((<any>component).hasRequest({
      id: {}
    })).toBeFalse();

  });

  it('should run #hasRequestTrue()', async () => {
    (<any>component).resourceRequests = (<any>component).resourceRequests || {};
    (<any>component).resourceRequests = [{
      assignedIncident: ""
    }];
    expect((<any>component).hasRequest({
      id: ""
    })).toBeTrue();

  });


  it('should run #getRequests()', async () => {
    (<any>component).resourceRequests = (<any>component).resourceRequests || {};
    (<any>component).resourceRequests = [{
      assignedIncident: ""
    }];
    expect((<any>component).getRequests({
      id: ""
    })).toHaveSize(1);

  });

  it('should run #finishRequest()', async () => {
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'instant');
    (<any>component).dialog = (<any>component).dialog || {};
    spyOn((<any>component).dialog, 'open').and.returnValue({
      afterClosed: function() {
        return observableOf(true);
      }
    });
    (<any>component).resourcesService = (<any>component).resourcesService || {};
    spyOn((<any>component).resourcesService, 'finishRequest').and.returnValue(observableOf({}));
    (<any>component).resourceRequests = (<any>component).resourceRequests || {};
    (<any>component).resourceRequests = ['resourceRequests'];
    (<any>component).finishRequest({
      id: {}
    });
    expect((<any>component).translate.instant).toHaveBeenCalled();
    expect((<any>component).dialog.open).toHaveBeenCalled();
    expect((<any>component).resourcesService.finishRequest).toHaveBeenCalled();
  });

  it('should run #incidentFromId()', async () => {
    (<any>component).incidents = [
      {
        "id": ""
      }
    ];
    expect((<any>component).incidentFromId("")).not.toBeNull();
  });
  
  
  it('should run #zoomToLocation()', async () => {
    (<any>component).map = (<any>component).map || {};
    spyOn((<any>component).map, 'setView');
    (<any>component).zoomToLocation({
      latitude: 0,
      longitude: 0
    });
    expect((<any>component).map.setView).toHaveBeenCalled();
  });

  it('should run #completeIncident()', async () => {
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'instant');
    (<any>component).dialog = (<any>component).dialog || {};
    spyOn((<any>component).dialog, 'open').and.returnValue({
      afterClosed: function() {
        return observableOf(true);
      }
    });
    (<any>component).resourcesService = (<any>component).resourcesService || {};
    spyOn((<any>component).resourcesService, 'completeIncident').and.returnValue(observableOf({}));
    spyOn(component, 'unselectIncident');
    (<any>component).completeIncident({});
    expect((<any>component).translate.instant).toHaveBeenCalled();
    expect((<any>component).dialog.open).toHaveBeenCalled();
    expect((<any>component).resourcesService.completeIncident).toHaveBeenCalled();
  });
});
