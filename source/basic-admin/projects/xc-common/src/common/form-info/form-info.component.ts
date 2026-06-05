import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {FormInfoModel} from './form-info.model';
import {CommonTool} from '@ccxc/tool';

@Component({
  selector: 'app-form-info',
  templateUrl: './form-info.component.html',
  styleUrls: ['./form-info.component.scss']
})
export class FormInfoComponent implements OnInit {

  @Input() public formInfoModel: FormInfoModel;
  public expandedKeys = [];

  constructor(private el: ElementRef) {
  }

  ngOnInit() {

  }

  notNull(obj) {
    return CommonTool.notNull(obj);
  }


  /**
   * 处理选中
   */
  isNotSelected(data, value: string): boolean {
    return data ? data.indexOf(value) === -1 : true;
  }

  /**
   * 获取对象key集合
   */
  public getObjKeys(data) {
    return Object.getOwnPropertyNames(data);
  }

  /**
   * 获取焦点
   */
  public focus(id) {
    const dom = this.el.nativeElement.querySelector('#' + id);
    if (dom) {
      dom.focus();
    }
  }

  public openChange(parentKey, trees) {
    if (parentKey) {
      const that = this;
      setTimeout(() => {
        that.expandedKeys = that.getParentKeys(parentKey, trees);
      });
    }
  }


  /**
   * 获取所有的父级id
   */
  public getParentKeys(parentKey, trees) {
    let array = [];
    for (const tree of trees) {
      if (tree.key === parentKey) {
        return [tree.key];
      }
      if (tree.children) {
        array = array.concat(this.getParentKeys(parentKey, tree.children));
        if (array.length > 0) {
          array.push(tree.key);
          return array;
        }
      }
    }
    return array;
  }

  /**
   * 删除图片时间
   */
  public deletePictureClick(component, index) {
    if (component.uploadPicture.deleteFun) {
      component.uploadPicture.deleteFun(this.formInfoModel.formInfo, index, component);
    } else {
      this.formInfoModel.formInfo[component.field].splice(index, 1);
    }
  }
}
