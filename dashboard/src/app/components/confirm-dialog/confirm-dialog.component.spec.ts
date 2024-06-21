import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmDialogComponent } from './confirm-dialog.component';
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import {OAuthModule} from "angular-oauth2-oidc";
import { TranslateModule } from '@ngx-translate/core';

export class MatDialogRefMock {
  close(value = '') {

  }
}

describe('ConfirmDialogComponent', () => {
  let component: ConfirmDialogComponent;
  let fixture: ComponentFixture<ConfirmDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmDialogComponent, MatDialogModule, TranslateModule.forRoot()],
      providers: [
        {provide: MatDialogRef, useClass: MatDialogRefMock},
        {provide: MAT_DIALOG_DATA, useValue: []},
    ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should run #constructor()', async () => {
    expect(component).toBeTruthy();
  });

  it('should run #onConfirm()', async () => {
    component.dialogRef = component.dialogRef || {};
    spyOn<any>(component.dialogRef, 'close');
    component.onConfirm();
    expect(component.dialogRef.close).toHaveBeenCalled();
  });

  it('should run #onDismiss()', async () => {
    component.dialogRef = component.dialogRef || {};
    spyOn<any>(component.dialogRef, 'close');
    component.onDismiss();
    expect(component.dialogRef.close).toHaveBeenCalled();
  });
});
