import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AppAuditComponent } from './app-audit.component';

describe('AppAuditComponent', () => {
  let component: AppAuditComponent;
  let fixture: ComponentFixture<AppAuditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppAuditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppAuditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
