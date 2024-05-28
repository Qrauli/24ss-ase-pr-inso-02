import { Injectable } from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";

const DEFAULT_NOTIFICATION_DURATION: number = 7000;

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private notifications: {
    message: string,
    action?: string,
    duration: number,
    onAction?: () => void,
    panelClass: string[]
  }[] = [];

  constructor(private _snackBar: MatSnackBar) { }

  showDefaultNotification(message: string, action?: string, duration?: number, onAction?: () => void) {
    this.notifications.push({
      message: message,
      action: action,
      duration: duration ?? DEFAULT_NOTIFICATION_DURATION,
      onAction: onAction,
      panelClass: ['alert-default']});
    this.showNotification();
  }

  showPriorityNotification(message: string, action?: string, duration?: number, onAction?: () => void) {
    this.notifications.push({
      message: message,
      action: action,
      duration: duration ?? DEFAULT_NOTIFICATION_DURATION,
      onAction: onAction,
      panelClass: ['alert-red']});
    this.showNotification();
  }

  showErrorNotification(message: string, action?: string, duration?: number, onAction?: () => void) {
    this.notifications.push({
      message: message,
      action: action,
      duration: duration ?? DEFAULT_NOTIFICATION_DURATION,
      onAction: onAction,
      panelClass: ['alert-red']});
    this.showNotification();
  }

  private showNotification() {
    if (this.notifications.length > 0) {
      if (this._snackBar._openedSnackBarRef?._open) {
        this._snackBar._openedSnackBarRef.afterDismissed().subscribe(() => this.showNotification())
        return;
      }
      const notification = this.notifications.shift()!;
      const ref = this._snackBar.open(
        notification.message, notification.action, {
          horizontalPosition: 'center',
          verticalPosition: 'top',
          duration: notification.duration ?? DEFAULT_NOTIFICATION_DURATION,
          panelClass: notification.panelClass
        });

      if (notification.onAction != undefined) {
        ref.onAction().subscribe(notification.onAction);
      }
      ref.afterDismissed().subscribe(() => this.showNotification());
    }
  }
}
