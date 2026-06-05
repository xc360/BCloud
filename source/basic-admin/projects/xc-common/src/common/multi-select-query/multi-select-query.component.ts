import {Component, Input, OnDestroy, OnInit, Renderer2} from '@angular/core';

@Component({
  selector: 'app-select-month',
  templateUrl: './multi-select-query.component.html',
  styleUrls: ['./multi-select-query.component.scss']
})
export class MultiSelectQueryComponent implements OnInit, OnDestroy {

  public value: any; // 输入框值
  public show; // 显示选择框
  @Input() public selectItem: Array<{ name: string, value: string, children: Array<{ name, value, disabled }> }>; // 选择项
  @Input() public selectedValue: any; // 选中值
  public selectedData: { name: string, value: string, children: Array<{ name, value, disabled }> }; // 选中数据
  @Input() public selectedArray: Array<{ oneLevel, twoLevel }>; // 选中值2
  public checkedMap: any; // 选中map
  public documentClick: any;

  /**
   * 描述：构造函数，最先执行
   * 作者： xc
   * 时间：2020-02-25 09:42:15
   */
  constructor(private renderer: Renderer2) {
    this.checkedMap = {};
    this.selectedArray = [];
    this.show = false;
    this.selectItem = [
      {
        name: '2018年', value: '2018',
        children: [
          {name: '1月', value: 1, disabled: false},
          {name: '2月', value: 2, disabled: false},
          {name: '3月', value: 3, disabled: false},
          {name: '4月', value: 4, disabled: false},
          {name: '5月', value: 5, disabled: false},
          {name: '6月', value: 6, disabled: false},
          {name: '7月', value: 7, disabled: false},
          {name: '8月', value: 8, disabled: false},
          {name: '9月', value: 9, disabled: false},
          {name: '10月', value: 10, disabled: false},
          {name: '11月', value: 11, disabled: false},
          {name: '12月', value: 12, disabled: false}
        ]
      },
      {
        name: '2019年', value: '2019',
        children: [
          {name: '1月', value: 1, disabled: false},
          {name: '2月', value: 2, disabled: false},
          {name: '3月', value: 3, disabled: false},
          {name: '4月', value: 4, disabled: false},
          {name: '5月', value: 5, disabled: false},
          {name: '6月', value: 6, disabled: false},
          {name: '7月', value: 7, disabled: false},
          {name: '8月', value: 8, disabled: false},
          {name: '9月', value: 9, disabled: false},
          {name: '10月', value: 10, disabled: false},
          {name: '11月', value: 11, disabled: false},
          {name: '12月', value: 12, disabled: false}
        ]
      }
    ];
  }

  /**
   * 描述：递归获取元素指定父级
   * 作者： xc
   * 时间：2020-02-25 09:42:15
   */
  static parent(elem, dir, cls) {
    let cur = elem[dir];
    while (cur && cur.nodeType !== 9) {
      cur = cur[dir];
      if (cur.className && cur.className.indexOf(cls) !== -1) {
        return cur;
      }
    }
    return null;
  }

  /**
   * 描述：初始化创建全局事件
   * 作者： xc
   * 时间：2020-02-25 09:42:15
   */
  ngOnInit() {
    const that = this;
    this.documentClick = this.renderer.listen('document', 'click', (event) => {
      const parent = MultiSelectQueryComponent.parent(event.target, 'parentNode', 'app-month-calendar');
      if (parent === null) {
        that.show = false;
      }
    });
    this.selectedValue = this.selectItem[0].value;
    this.selectChange(this.selectedValue);
  }

  /**
   * 描述：退出页面关闭事件
   * 作者： xc
   * 时间：2020-02-25 09:42:15
   */
  ngOnDestroy() {
    this.documentClick();
  }


  /**
   * 描述：清空选择
   * 作者： xc
   * 时间：2020-02-25 09:42:15
   */
  clear() {
    this.value = undefined;
    this.selectedArray = [];
    this.checkedMap = {};
  }

  /**
   * 描述：下拉框选中处理
   * 作者： xc
   * 时间：2020-02-25 09:42:15
   */
  public selectChange(value) {
    this.checkedMap = {};
    this.selectItem.forEach((ref) => {
      if (ref.value === value) {
        this.selectedData = ref;
      }
    });
    if (this.selectedArray) {
      this.selectedArray.forEach((ref, index) => {
        if (value === ref.oneLevel) {
          this.checkedMap[ref.twoLevel] = true;
        }
      });
    }
  }

  /**
   * 描述：选最后一级
   * 作者： xc
   * 时间：2020-02-25 09:42:15
   */
  public selection(select) {
    if (select.disabled) {
      return;
    }
    if (this.checkedMap[select.value]) {
      this.checkedMap[select.value] = false;
      this.selectedArray.forEach((ref, index) => {
        if (ref.twoLevel === select.value && this.selectedValue === ref.oneLevel) {
          this.selectedArray.splice(index, 1);
        }
      });
    } else {
      this.selectedArray.push({oneLevel: this.selectedValue, twoLevel: select.value});
      this.checkedMap[select.value] = true;
    }
    this.value = '';
    this.selectedArray.forEach((ref, index) => {
      if (index === this.selectedArray.length - 1) {
        this.value += ref.oneLevel + '-' + ref.twoLevel;
      } else {
        this.value += ref.oneLevel + '-' + ref.twoLevel + ',';
      }
    });
  }
}
