import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ProgressBallComponent} from './progress-ball.component';

@NgModule({
  declarations: [ProgressBallComponent],
  exports: [ProgressBallComponent],
  imports: [
    CommonModule
  ]
})
export class ProgressBallModule {
}
