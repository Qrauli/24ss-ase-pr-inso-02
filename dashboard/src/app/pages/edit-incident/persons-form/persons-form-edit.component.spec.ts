import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PersonsFormEditComponent } from './persons-form-edit.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import { TranslateModule } from '@ngx-translate/core';

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
});
