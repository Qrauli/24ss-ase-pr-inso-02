import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DispatcherComponent } from './dispatcher.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import { HttpClientTestingModule } from '@angular/common/http/testing';


describe('DispatcherComponent', () => {
  let component: DispatcherComponent;
  let fixture: ComponentFixture<DispatcherComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DispatcherComponent, NoopAnimationsModule, HttpClientTestingModule]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DispatcherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
