import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailIncidentComponent } from './detail-incident.component';

import { RouterTestingModule } from "@angular/router/testing";


describe('DetailIncidentComponent', () => {
  let component: DetailIncidentComponent;
  let fixture: ComponentFixture<DetailIncidentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailIncidentComponent, RouterTestingModule]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DetailIncidentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
