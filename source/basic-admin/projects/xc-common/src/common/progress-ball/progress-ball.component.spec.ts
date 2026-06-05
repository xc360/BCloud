import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgressBallComponent } from './progress-ball.component';

describe('ProgressBallComponent', () => {
  let component: ProgressBallComponent;
  let fixture: ComponentFixture<ProgressBallComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProgressBallComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProgressBallComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
