import {Component, OnInit} from '@angular/core';
import {NzMessageService} from 'ng-zorro-antd';
import {BasicManagementService} from '../../../basic-management/basic-management.service';
import {AuditManagementService} from '../../audit-management.service';
import {AppService} from '../../../../app.service';
import {FormInfoModel} from '../../../../../../projects/xc-common/src/common/form-info/form-info.model';
import {TableFindModel} from '../../../../../../projects/xc-common/src/common/table-find/table-find.model';
import {BUTTON_CODE} from '../../../../config/button-code';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';

@Component({
  selector: 'app-app-audit',
  templateUrl: './app-audit.component.html',
  styleUrls: ['./app-audit.component.scss']
})
export class AppAuditComponent implements OnInit {

  public tableFindModel: TableFindModel;
  public formInfoModel: FormInfoModel;
  public isVisible = false;
  public type;

  public auditStatus = '1';
  public tabIndex = 0;

  constructor(public appService: AppService,
              public message: NzMessageService,
              public basicManagementService: BasicManagementService,
              public activatedRoute: ActivatedRoute,
              public router: Router,
              public auditManagementService: AuditManagementService) {
    this.formInfoModel = new FormInfoModel();
    this.activatedRoute.queryParams.subscribe(queryParams => {
      if (queryParams.auditStatus) {
        this.auditStatus = queryParams.auditStatus;
        if (this.auditStatus === '1') {
          this.tabIndex = 0;
        } else if (this.auditStatus === '2') {
          this.tabIndex = 1;
        } else {
          this.tabIndex = 2;
        }
      }
      this.init();
    });
  }

  ngOnInit() {
  }

  /**
   * 初始化表单
   */
  initForm(formInfo) {
    this.formInfoModel.initForm({
      components: [
        {
          name: '拒绝原因',
          field: 'reason',
          required: true,
          textarea: {
            maxLength: 255,
            rows: 5
          }
        }
      ]
    }, formInfo);
  }

  /**
   * 审核提交
   */
  confirmOk() {
    const formInfo = this.formInfoModel.formInfo;
    if (!formInfo.reason) {
      this.message.warning('拒绝原因不能为空！');
      return;
    }
    const req = {
      auditStatus: '3',
      reason: formInfo.reason
    };
    this.appService.setLoading(true);
    this.auditManagementService.updateManageAuditApp(formInfo.id, req).subscribe(res => {
      this.appService.setLoading(false);
      this.isVisible = false;
      this.message.success('审核成功！');
      this.tableFindModel.getData(null, null);
    });
  }

  /**
   * 初始化列表数据
   */
  public init() {
    const myThis = this;
    this.tableFindModel = new TableFindModel();
    const COMMON_TYPE = TableFindModel.COMMON_TYPE;
    this.tableFindModel.initConfig({
      tables: [
        {name: '应用名称', field: 'appName', type: COMMON_TYPE.table.TEXT},
        {name: '应用ID', field: 'appId', type: COMMON_TYPE.table.TEXT},
        {name: '应用用户', field: 'nickName', type: COMMON_TYPE.table.TEXT},
        {name: '应用状态', field: 'status', type: COMMON_TYPE.table.DICT_DESC, dictType: 'effectStatus'},
        {name: '审核状态', field: 'auditStatus', type: COMMON_TYPE.table.DICT_DESC, dictType: 'auditStatus'},
        {name: '发布时间', field: 'applyTime', type: COMMON_TYPE.table.TEXT, isSort: true},
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          operates: [
            {
              name: myThis.auditStatus === '1' ? '审核' : '查看',
              field: 'details',
              bAuthority: BUTTON_CODE.b_app_audit_details,
              confirmFun(dataModel, that) {
                myThis.router.navigate(['/home/auditManage/app/appDetails'], {
                  queryParams: {
                    id: dataModel.data.id,
                    auditStatus: myThis.auditStatus
                  }
                });
              }
            },
            {
              name: '拒绝审核',
              field: 'rejectAudit',
              bAuthority: BUTTON_CODE.b_app_audit_reject,
              hidden: myThis.auditStatus !== '2',
              confirmFun(dataModel, that) {
                myThis.isVisible = true;
                myThis.initForm({id: dataModel.data.id});
              }
            }
          ]
        }
      ],
      finds: [
        {
          name: '应用名称', field: 'appName', input: {
            maxLength: 60
          }
        },
        {
          name: '应用ID', field: 'appId', input: {
            maxLength: 60
          }
        }
      ],
      buttons: [],
      initDataFun(formInfo, sortPage, callback, that) {// 数据处理回调 config：配置，dataModels：数据，that：当前对象
        const req = {
          size: sortPage.size,
          likeFields: ['appName'],
          sortField: sortPage['sortField'],
          sortRule: sortPage['sortRule'],
          auditStatus: myThis.auditStatus
        };
        if (!req['sortField']) {
          req['sortField'] = 'applyTime';
        }
        if (!req['sortRule']) {
          req['sortRule'] = 'descend';
        }
        for (const find of that.config.finds) {
          req[find.field] = formInfo[find.field];
        }
        myThis.auditManagementService.getManageAuditAppPage(sortPage.current, req).subscribe(res => {
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
