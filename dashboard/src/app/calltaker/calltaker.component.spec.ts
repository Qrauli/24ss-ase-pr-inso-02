import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalltakerComponent } from './calltaker.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';


describe('CalltakerComponent', () => {
  let component: CalltakerComponent;
  let fixture: ComponentFixture<CalltakerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CalltakerComponent, NoopAnimationsModule]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CalltakerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
