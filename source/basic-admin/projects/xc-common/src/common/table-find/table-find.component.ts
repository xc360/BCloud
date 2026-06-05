import {Component, Input, OnInit} from '@angular/core';
import {TableFindModel} from './table-find.model';
import {CommonTool} from '@ccxc/tool';

@Component({
  selector: 'app-table-find',
  templateUrl: './table-find.component.html',
  styleUrls: ['./table-find.component.scss']
})
export class TableFindComponent implements OnInit {

  @Input()
  public tableFindModel: TableFindModel;
  public COMMON_TYPE = TableFindModel.COMMON_TYPE;
  public commonTool = CommonTool;

  constructor() {
  }

  ngOnInit() {
    if (!CommonTool.notNull(this.tableFindModel)) {
      this.tableFindModel = new TableFindModel();
    }
  }

  /**
   * 修改current
   */
  public indexChange(e) {
    if (!this.tableFindModel.config.frontPaging) {
      this.tableFindModel.paging(e, null);
    } else {
      this.tableFindModel.pagingModel.current = e;
    }
  }

  /**
   * 修改size
   */
  public sizeChange(e) {
    if (!this.tableFindModel.config.frontPaging) {
      this.tableFindModel.paging(null, e);
    }
  }

  /**
   * 排序
   * event 排序字段与规则
   */
  public sort(event) {
    this.tableFindModel.sort(event.key, event.value);
  }

  public search(event, table) {
    this.tableFindModel.formInfo[table.field] = event;
  }

  /**
   * 计算页数
   */
  public totalPage() {
    const pagingModel = this.tableFindModel.pagingModel;
    // 计算总页数
    let totalPage = 1;
    if (pagingModel.total !== 0) {
      totalPage = pagingModel.total / pagingModel.size;
      if (totalPage % 1 !== 0) {
        totalPage = Math.floor(totalPage) + 1;
      }
    }
    return totalPage;
  }

  // 移出
  public enlargeMouseLeave(imageId, index) {
    const enlarge = document.getElementById('enlarge' + index);
    enlarge.remove();
  }

  // 移入
  public enlargeMouseEnter(imageId, index) {
    const div = document.createElement('div');
    div.id = 'enlarge' + index;
    const style = 'z-index: 1000;position: absolute;margin-top: -82px;margin-left: 45px';
    div.setAttribute('style', style);
    imageId.parentNode.appendChild(div);
    const enlarge = document.getElementById('enlarge' + index);
    enlarge.innerHTML = `<img src="` + imageId.src + `" style="width: 200px;height: 200px;border-radius: 100px;">`;
  }
}
