import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OverallSelectComponent } from './overall-select.component';

describe('OverallSelectComponent', () => {
  let component: OverallSelectComponent;
  let fixture: ComponentFixture<OverallSelectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OverallSelectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OverallSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
