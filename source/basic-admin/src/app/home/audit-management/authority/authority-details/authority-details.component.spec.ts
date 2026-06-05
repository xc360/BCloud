import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthorityDetailsComponent } from './authority-details.component';

describe('AuthorityDetailsComponent', () => {
  let component: AuthorityDetailsComponent;
  let fixture: ComponentFixture<AuthorityDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AuthorityDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthorityDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
