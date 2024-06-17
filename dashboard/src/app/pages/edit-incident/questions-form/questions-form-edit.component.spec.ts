import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuestionsFormEditComponent } from './questions-form-edit.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import { TranslateModule } from '@ngx-translate/core';

describe('AddIncidentComponent', () => {
  let component: QuestionsFormEditComponent;
  let fixture: ComponentFixture<QuestionsFormEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuestionsFormEditComponent, NoopAnimationsModule, HttpClientTestingModule, TranslateModule.forRoot()]
    })
      .compileComponents();

    fixture = TestBed.createComponent(QuestionsFormEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
