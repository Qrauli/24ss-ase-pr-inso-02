import { TestBed, async } from '@angular/core/testing';
import { Injectable } from '@angular/core';
import { Observable, of as observableOf, throwError } from 'rxjs';

import { NotificationService } from './notification.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('NotificationService', () => {
  let service: NotificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({imports: [HttpClientTestingModule]});
    service = TestBed.inject(NotificationService);
  });

  it('should run #showDefaultNotification()', async () => {
    (<any>service).notifications = (<any>service).notifications || {};
    spyOn((<any>service).notifications, 'push');
    spyOn<any>(service, 'showNotification');
    (<any>service).showDefaultNotification("", "", 0, function() {});
    expect((<any>service).notifications.push).toHaveBeenCalled();
    expect((<any>service).showNotification).toHaveBeenCalled();
  });

  it('should run #showPriorityNotification()', async () => {
    (<any>service).notifications = (<any>service).notifications || {};
    spyOn((<any>service).notifications, 'push');
    spyOn<any>(service, 'showNotification');
    (<any>service).showPriorityNotification("", "", 0, function() {});
    expect((<any>service).notifications.push).toHaveBeenCalled();
    expect((<any>service).showNotification).toHaveBeenCalled();
  });

  it('should run #showErrorNotification()', async () => {
    (<any>service).notifications = (<any>service).notifications || {};
    spyOn((<any>service).notifications, 'push');
    spyOn<any>(service, 'showNotification');
    (<any>service).showErrorNotification("", "", 0, function() {});
    expect((<any>service).notifications.push).toHaveBeenCalled();
    expect((<any>service).showNotification).toHaveBeenCalled();
  });

});