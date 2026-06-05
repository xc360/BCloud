import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthorityAuditComponent } from './authority-audit.component';

describe('AuthorityAuditComponent', () => {
  let component: AuthorityAuditComponent;
  let fixture: ComponentFixture<AuthorityAuditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AuthorityAuditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthorityAuditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
