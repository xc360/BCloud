import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VersionDocComponent } from './version-doc.component';

describe('VersionEditComponent', () => {
  let component: VersionDocComponent;
  let fixture: ComponentFixture<VersionDocComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VersionDocComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VersionDocComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
