import {Component, OnInit} from '@angular/core';
import {TableFindModel} from '../../../../../../projects/xc-common/src/public-api';
import {ActivatedRoute, Router} from '@angular/router';
import {BasicManagementService} from '../../basic-management.service';

@Component({
  selector: 'app-task-log',
  templateUrl: './task-log.component.html',
  styleUrls: ['./task-log.component.scss']
})
export class TaskLogComponent implements OnInit {

  public tableFindModel: TableFindModel;
  public appId;
  public taskId;
  public logInfo = {
    isVisible: false,
    content: ''
  };

  constructor(public basicManagementService: BasicManagementService,
              public router: Router,
              public activatedRoute: ActivatedRoute) {
    this.activatedRoute.queryParams.subscribe(queryParams => {
      this.appId = queryParams.appId;
      this.taskId = queryParams.id;
    });
  }

  ngOnInit(): void {
    this.init();
  }

  /**
   * 初始化配置
   */
  public init() {
    const myThis = this;
    this.tableFindModel = new TableFindModel();
    const COMMON_TYPE = TableFindModel.COMMON_TYPE;
    this.tableFindModel.initConfig({
      tables: [
        {name: '类型', field: 'type', type: COMMON_TYPE.table.DICT_DESC, dictType: 'taskLogType'},
        {name: '记录标识', field: 'code', type: COMMON_TYPE.table.TEXT},
        {name: '请求地址', field: 'url', type: COMMON_TYPE.table.TEXT},
        {name: '是否成功', field: 'status', type: COMMON_TYPE.table.DICT_DESC, dictType: 'whether'},
        {name: '执行耗时', field: 'duration', type: COMMON_TYPE.table.TEXT},
        {name: '创建时间', field: 'createTime', type: COMMON_TYPE.table.TEXT, isSort: true},
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          initFun(dataModel, table, that) {
            dataModel['details'] = dataModel.data['status'] === '1';
          },
          operates: [
            {
              name: '错误信息',
              field: 'details',
              confirmFun(dataModel, that) {
                myThis.logInfo.isVisible = true;
                myThis.logInfo.content = dataModel.data.errorMsg;
              }
            },
          ]
        }
      ],
      finds: [
        {
          name: '记录标识',
          field: 'code',
          input: {
            maxLength: 255
          }
        },
        {
          name: '类型',
          field: 'type',
          select: {
            dictType: 'taskLogType'
          }
        },
        {
          name: '请求地址',
          field: 'url',
          input: {
            maxLength: 255
          }
        },
        {
          name: '是否成功',
          field: 'status',
          select: {
            dictType: 'whether'
          }
        }
      ],
      buttons: [
        {
          name: '返回',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'rollback',
          confirmFun(formInfo, that) {
            myThis.router.navigateByUrl('/home/basicManage/taskManage/taskList?appId=' + myThis.appId);
          }
        }],
      initDataFun(formInfo, sortPage, callback, that) {// 数据处理回调 config：配置，dataModels：数据，that：当前对象
        const req = {
          size: sortPage.size,
          likeFields: [],
          sortField: sortPage['sortField'],
          sortRule: sortPage['sortRule']
        };
        if (!req['sortField']) {
          req['sortField'] = 'updateTime';
        }
        if (!req['sortRule']) {
          req['sortRule'] = 'descend';
        }
        for (const find of that.config.finds) {
          req[find.field] = formInfo[find.field];
        }
        myThis.basicManagementService.getAppTaskLogPage(myThis.appId, myThis.taskId, sortPage.current, req).subscribe(res => {
          const array = res['resData'].map((data) => {
            return {data};
          });
          callback(array, {total: res['total']});
        });
      },
      frontPaging: false,
      frontSort: false
    });
  }
}
