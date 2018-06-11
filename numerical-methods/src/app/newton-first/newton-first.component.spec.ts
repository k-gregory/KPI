import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewtonFirstComponent } from './newton-first.component';

describe('NewtonFirstComponent', () => {
  let component: NewtonFirstComponent;
  let fixture: ComponentFixture<NewtonFirstComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewtonFirstComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewtonFirstComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
