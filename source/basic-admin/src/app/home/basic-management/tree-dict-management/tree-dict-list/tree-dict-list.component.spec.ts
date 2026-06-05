import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TreeDictListComponent } from './tree-dict-list.component';

describe('TreeDictListComponent', () => {
  let component: TreeDictListComponent;
  let fixture: ComponentFixture<TreeDictListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TreeDictListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TreeDictListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
