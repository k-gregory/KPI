import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SupporterConsultationComponent } from './supporter-consultation.component';

describe('SupporterConsultationComponent', () => {
  let component: SupporterConsultationComponent;
  let fixture: ComponentFixture<SupporterConsultationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SupporterConsultationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SupporterConsultationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
