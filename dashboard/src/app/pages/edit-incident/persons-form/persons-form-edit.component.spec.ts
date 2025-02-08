import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PersonsFormEditComponent } from './persons-form-edit.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import { TranslateModule } from '@ngx-translate/core';
import { Patient, Sex } from '../../../dtos/incident';

describe('AddIncidentComponent', () => {
  let component: PersonsFormEditComponent;
  let fixture: ComponentFixture<PersonsFormEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PersonsFormEditComponent, NoopAnimationsModule, HttpClientTestingModule, TranslateModule.forRoot()]
    })
      .compileComponents();

    fixture = TestBed.createComponent(PersonsFormEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should run #addPatient()', async () => {
    (<any>component).patients = (<any>component).patients || {};
    spyOn((<any>component).patients, 'push');
    (<any>component).form = (<any>component).form || {};
    (<any>component).form.controls = {
      numberOfPatients: {
        patchValue: function() {}
      }
    };
    (<any>component).table = (<any>component).table || {};
    spyOn((<any>component).table, 'renderRows');
    (<any>component).addPatient();
    expect((<any>component).patients.push).toHaveBeenCalled();
    expect((<any>component).table.renderRows).toHaveBeenCalled();
  });

  it('should run #removePatient()', async () => {
    let patient: Patient = {
      age: 0,
      sex: Sex.UNKNOWN
    };
    (<any>component).patients = [patient];
    spyOn((<any>component).patients, 'splice');
    (<any>component).form = (<any>component).form || {};
    (<any>component).form.controls = {
      numberOfPatients: {
        patchValue: function() {}
      }
    };
    (<any>component).table = (<any>component).table || {};
    spyOn((<any>component).table, 'renderRows');
    (<any>component).removePatient(patient);
    expect((<any>component).patients.splice).toHaveBeenCalled();
  });

  it('should run #numberInputOnly()', async () => {

    let event: any = {
      key: "a",
      preventDefault: function() {}
    };
    spyOn(event, 'preventDefault');
    (<any>component).numberInputOnly(event);
    expect(event.preventDefault).toHaveBeenCalled();

  });

  it('should run #updatePatientsAdd()', async () => {
    (<any>component).form = (<any>component).form || {};
    (<any>component).form.value = {
      numberOfPatients: 2
    };
    (<any>component).patients = [
      {
        age: 0,
        sex: Sex.UNKNOWN
      }
    ];
    (<any>component).table = (<any>component).table || {};
    spyOn((<any>component).table, 'renderRows');
    (<any>component).updatePatients();
    expect((<any>component).table.renderRows).toHaveBeenCalled();
  });

  it('should run #updatePatientsRemove()', async () => {
    (<any>component).form = (<any>component).form || {};
    (<any>component).form.value = {
      numberOfPatients: 0
    };
    (<any>component).patients = [
      {
        age: 0,
        sex: Sex.UNKNOWN
      }
    ];
    (<any>component).table = (<any>component).table || {};
    spyOn((<any>component).table, 'renderRows');
    (<any>component).updatePatients();
    expect((<any>component).table.renderRows).toHaveBeenCalled();
  });
});
