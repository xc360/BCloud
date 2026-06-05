import {Component, Input, OnInit} from '@angular/core';
import {ProgressBallModel} from './progress-ball.model';

@Component({
  selector: 'app-progress-ball',
  templateUrl: './progress-ball.component.html',
  styleUrls: ['./progress-ball.component.scss']
})
export class ProgressBallComponent implements OnInit {
  @Input()
  public config: ProgressBallModel;

  constructor() {
  }

  ngOnInit() {
    if (!this.config) {
      this.config = {
        showValue: 0
      };
    }
    if (!this.config.animSpeed) {
      this.config.animSpeed = 5;
    }
    if (!this.config.unit) {
      this.config.unit = '%';
    }
    if (!this.config.fontSize) {
      this.config.fontSize = 0;
    }
  }
}
