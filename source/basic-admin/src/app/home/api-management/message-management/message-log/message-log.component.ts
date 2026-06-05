import {Component, OnInit} from '@angular/core';
import {TableFindModel} from '../../../../../../projects/xc-common/src/public-api';
import {ActivatedRoute, Router} from '@angular/router';
import {ApiManagementService} from '../../api-management.service';
import {BUTTON_CODE} from "../../../../config/button-code";

@Component({
  selector: 'app-message-log',
  templateUrl: './message-log.component.html',
  styleUrls: ['./message-log.component.scss']
})
export class MessageLogComponent implements OnInit {

  public tableFindModel: TableFindModel;
  public appId;
  public messageTemplateId;
  public logInfo = {
    isVisible: false,
    content: ''
  };

  constructor(public messageManagementService: ApiManagementService,
              public router: Router,
              public activatedRoute: ActivatedRoute) {
    this.activatedRoute.queryParams.subscribe(queryParams => {
      this.appId = queryParams.appId;
      this.messageTemplateId = queryParams.id;
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
        {
          name: '接收者账号', field: 'account', type: COMMON_TYPE.table.TEXT, isSort: true,
          tdStyle: {
            minWidth: '180px',
          }
        },
        {
          name: '是否成功', field: 'status', type: COMMON_TYPE.table.DICT_DESC, dictType: 'whether',
          tdStyle: {
            minWidth: '50px',
          }
        },
        {
          name: '更新时间', field: 'updateTime', type: COMMON_TYPE.table.TEXT, isSort: true,
          tdStyle: {
            minWidth: '150px',
          }
        },
        {
          name: '创建时间', field: 'createTime', type: COMMON_TYPE.table.TEXT, isSort: true,
          tdStyle: {
            minWidth: '150px',
          }
        },
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          operates: [
            {
              name: '查看',
              field: 'details',
              confirmFun(dataModel, that) {
                myThis.logInfo.isVisible = true;
                myThis.logInfo.content = dataModel.data.content;
              }
            },
          ]
        }
      ],
      finds: [
        {
          name: '接收者账号',
          field: 'account',
          input: {
            maxLength: 60
          }
        },
        {
          name: '消息内容',
          field: 'content',
          input: {
            maxLength: 60
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
            myThis.router.navigateByUrl('/home/apiManage/messageManage/templateList?appId=' + myThis.appId);
          }
        }],
      initDataFun(formInfo, sortPage, callback, that) {// 数据处理回调 config：配置，dataModels：数据，that：当前对象
        const req = {
          size: sortPage.size,
          likeFields: ['content'],
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
        myThis.messageManagementService.getAppMessageLogPage(myThis.appId,
          myThis.messageTemplateId, sortPage.current, req).subscribe(res => {
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
