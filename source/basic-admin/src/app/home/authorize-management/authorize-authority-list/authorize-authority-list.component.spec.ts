import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthorizeAuthorityListComponent } from './authorize-authority-list.component';

describe('AuthorizeAuthorityListComponent', () => {
  let component: AuthorizeAuthorityListComponent;
  let fixture: ComponentFixture<AuthorizeAuthorityListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AuthorizeAuthorityListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthorizeAuthorityListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
