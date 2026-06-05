import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-overall-select',
  templateUrl: './overall-select.component.html',
  styleUrls: ['./overall-select.component.scss']
})
export class OverallSelectComponent implements OnInit {
  @Output()
  public onSelected: EventEmitter<string> = new EventEmitter<string>();
  public options;
  @Input()
  public labelName;
  @Input()
  public openLabel = true;
  @Input()
  public labelStyle;
  public id;
  public config: any;

  constructor() {

  }

  ngOnInit() {
  }

  /**
   * 初始化
   */
  public init(config) {
    this.config = config;
    this.config.openFun((options) => {
      this.options = options;
      const id = localStorage.getItem('overallSelectKey');
      let bool = true;
      for (const option of this.options) {
        if (option.value === id) {
          bool = false;
        }
      }
      if (bool) {
        if (this.options && this.options.length > 0) {
          this.id = this.options[0].value;
        }
      } else {
        this.id = id;
      }
      this.onSelected.emit(this.id);
    });
  }

  /**
   * 打开下拉时间
   */
  public openFun($event) {
    if ($event) {
      this.config.openFun((options) => {
        this.options = options;
        const id = localStorage.getItem('overallSelectKey');
        let bool = true;
        for (const option of this.options) {
          if (option.value === id) {
            bool = false;
          }
        }
        if (bool) {
          if (this.options && this.options.length > 0) {
            this.id = this.options[0].value;
          }
        } else {
          this.id = id;
        }
        this.onSelected.emit(this.id);
      });
    }

  }

  /**
   * 选中应用调用
   */
  public selectedChange($event) {
    this.id = $event;
    localStorage.setItem('overallSelectKey', this.id);
    this.onSelected.emit(this.id);
  }
}
