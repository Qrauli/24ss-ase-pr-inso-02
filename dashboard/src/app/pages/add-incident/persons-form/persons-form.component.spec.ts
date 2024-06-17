import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PersonsFormComponent } from './persons-form.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import { TranslateModule } from '@ngx-translate/core';

describe('AddIncidentComponent', () => {
  let component: PersonsFormComponent;
  let fixture: ComponentFixture<PersonsFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PersonsFormComponent, NoopAnimationsModule, HttpClientTestingModule, TranslateModule.forRoot()]
    })
      .compileComponents();

    fixture = TestBed.createComponent(PersonsFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
