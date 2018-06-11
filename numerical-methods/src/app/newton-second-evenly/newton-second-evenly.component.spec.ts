import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewtonSecondEvenlyComponent } from './newton-second-evenly.component';

describe('NewtonSecondEvenlyComponent', () => {
  let component: NewtonSecondEvenlyComponent;
  let fixture: ComponentFixture<NewtonSecondEvenlyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewtonSecondEvenlyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewtonSecondEvenlyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
