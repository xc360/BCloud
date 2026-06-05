import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MultiSelectQueryComponent } from './multi-select-query.component';

describe('MultiSelectQueryComponent', () => {
  let component: MultiSelectQueryComponent;
  let fixture: ComponentFixture<MultiSelectQueryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MultiSelectQueryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MultiSelectQueryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
