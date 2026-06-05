import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DataTypeListComponent } from './data-type-list.component';

describe('DataTypeListComponent', () => {
  let component: DataTypeListComponent;
  let fixture: ComponentFixture<DataTypeListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DataTypeListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DataTypeListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
