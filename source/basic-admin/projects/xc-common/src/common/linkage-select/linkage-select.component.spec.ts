import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LinkageSelectComponent } from './linkage-select.component';

describe('LinkageSelectComponent', () => {
  let component: LinkageSelectComponent;
  let fixture: ComponentFixture<LinkageSelectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LinkageSelectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LinkageSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
