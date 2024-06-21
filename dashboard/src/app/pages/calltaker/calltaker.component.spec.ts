import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalltakerComponent } from './calltaker.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {OAuthModule} from "angular-oauth2-oidc";
import { TranslateModule } from '@ngx-translate/core';


describe('CalltakerComponent', () => {
  let component: CalltakerComponent;
  let fixture: ComponentFixture<CalltakerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CalltakerComponent, NoopAnimationsModule, HttpClientTestingModule, OAuthModule.forRoot(), TranslateModule.forRoot()]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CalltakerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('should run #ngOnInit()', async () => {
    (<any>component).incidentService = (<any>component).incidentService || {};
    spyOn((<any>component).incidentService, 'getIncidentsOngoingDispatcher');
    (<any>component).dataSource = (<any>component).dataSource || {};
    (<any>component).dataSource.sort = 'sort';
    (<any>component).dataSource.data = {
      sort: function() {
        return [
          {
            "state": {}
          },
          {
            "state": {}
          }
        ];
      }
    };
    (<any>component).notificationService = (<any>component).notificationService || {};
    spyOn((<any>component).notificationService, 'showErrorNotification');
    (<any>component).translate = (<any>component).translate || {};
    spyOn((<any>component).translate, 'instant');
    (<any>component).ngOnInit();
    expect((<any>component).incidentSubscription).toBeDefined();
  });

  it('should run #ngOnDestroy()', async () => {
    (<any>component).incidentSubscription = (<any>component).incidentSubscription || {};
    spyOn((<any>component).incidentSubscription, 'unsubscribe');
    (<any>component).ngOnDestroy();
    expect((<any>component).incidentSubscription.unsubscribe).toHaveBeenCalled();
  });

  it('should run #performMainAction()', async () => {
    (<any>component).router = (<any>component).router || {};
    spyOn((<any>component).router, 'navigate');
    (<any>component).performMainAction();
    expect((<any>component).router.navigate).toHaveBeenCalled();
  });

  it('should run #showDetail()', async () => {
    (<any>component).router = (<any>component).router || {};
    spyOn((<any>component).router, 'navigate');
    (<any>component).showDetail({
      id: {}
    });
    expect((<any>component).router.navigate).toHaveBeenCalled();
  });
});
