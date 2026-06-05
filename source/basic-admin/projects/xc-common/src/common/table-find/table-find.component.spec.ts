import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TableFindComponent } from './table-find.component';

describe('TableFindComponent', () => {
  let component: TableFindComponent;
  let fixture: ComponentFixture<TableFindComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TableFindComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableFindComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
