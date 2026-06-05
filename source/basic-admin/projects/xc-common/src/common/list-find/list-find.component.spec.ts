import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListFindComponent } from './list-find.component';

describe('ListFindComponent', () => {
  let component: ListFindComponent;
  let fixture: ComponentFixture<ListFindComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListFindComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListFindComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
