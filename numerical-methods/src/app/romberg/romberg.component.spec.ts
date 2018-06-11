import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RombergComponent } from './romberg.component';

describe('RombergComponent', () => {
  let component: RombergComponent;
  let fixture: ComponentFixture<RombergComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RombergComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RombergComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
