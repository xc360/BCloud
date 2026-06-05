import {Component, Input, OnInit} from '@angular/core';
import {TableFindModel} from '../table-find/table-find.model';
import {CommonTool} from '@ccxc/tool';

@Component({
  selector: 'app-list-find',
  templateUrl: './list-find.component.html',
  styleUrls: ['./list-find.component.scss']
})
export class ListFindComponent implements OnInit {

  @Input()
  public tableFindModel: TableFindModel;
  public COMMON_TYPE = TableFindModel.COMMON_TYPE;
  public commonTool = CommonTool;
  public rule;

  constructor() {
  }

  ngOnInit() {
  }

  /**
   * 修改current
   */
  public indexChange(e) {
    // 前端分页
    const config = this.tableFindModel.config;
    if (config.openPaging && config.frontPaging) {
      if (CommonTool.notNull(e)) {
        this.tableFindModel.pagingModel.current = e;
        this.tableFindModel.pagingHandle();
      }
    } else {
      this.tableFindModel.paging(e, null);
    }
  }

  /**
   * 修改size
   */
  public sizeChange(e) {
    // 前端分页
    const config = this.tableFindModel.config;
    if (config.openPaging && config.frontPaging) {
      if (CommonTool.notNull(e)) {
        this.tableFindModel.pagingModel.size = e;
        this.tableFindModel.pagingHandle();
      }
    } else {
      this.tableFindModel.paging(null, e);
    }
  }

  /**
   * 排序
   * event 排序字段与规则
   */
  public sort(field) {
    if (this.rule === 'asc') {
      this.rule = 'desc';
      this.tableFindModel.sort(field, 'descend');
    } else if (this.rule === 'desc') {
      this.rule = '';
      this.tableFindModel.sort(field, null);
    } else {
      this.rule = 'asc';
      this.tableFindModel.sort(field, 'ascend');
    }
    // 前端分页
    const config = this.tableFindModel.config;
    if (config.openPaging && config.frontPaging) {
      this.tableFindModel.pagingHandle();
    }
  }
}
