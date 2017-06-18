import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientConsultationComponent } from './client-consultation.component';

describe('ClientConsultationComponent', () => {
  let component: ClientConsultationComponent;
  let fixture: ComponentFixture<ClientConsultationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClientConsultationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientConsultationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
