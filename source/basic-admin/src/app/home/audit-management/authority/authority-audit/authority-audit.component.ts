import {Component, OnInit} from '@angular/core';
import {NzMessageService} from 'ng-zorro-antd';
import {TableFindModel} from '../../../../../../projects/xc-common/src/common/table-find/table-find.model';
import {FormInfoModel} from '../../../../../../projects/xc-common/src/common/form-info/form-info.model';
import {AuditManagementService} from '../../audit-management.service';
import {HomeService} from '../../../home.service';
import {AppManagementService} from '../../../app-management/app-management.service';
import {AppService} from '../../../../app.service';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {BUTTON_CODE} from '../../../../config/button-code';

@Component({
  selector: 'app-authority-audit',
  templateUrl: './authority-audit.component.html',
  styleUrls: ['./authority-audit.component.scss']
})
export class AuthorityAuditComponent implements OnInit {


  public tableFindModel: TableFindModel;
  public formInfoModel: FormInfoModel;
  public isVisible = false;
  public appList;
  public authorityAppList;

  public auditStatus = '1';
  public tabIndex = 0;

  constructor(public auditManagementService: AuditManagementService,
              public homeService: HomeService,
              public appManagementService: AppManagementService,
              public appService: AppService,
              public router: Router,
              public activatedRoute: ActivatedRoute,
              public message: NzMessageService) {
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
      this.appManagementService.getAppList().subscribe(res => {
        this.appList = [];
        for (const row of res) {
          this.appList.push({
            name: row['name'],
            value: row['value']
          });
        }
        if (this.authorityAppList && this.appList) {
          this.init();
        }
      });
      this.homeService.getCurrentUserAppList({}).subscribe(res => {
        this.authorityAppList = [];
        for (const data of res) {
          this.authorityAppList.push({
            name: data.appName,
            value: data.id
          });
        }
        if (this.authorityAppList && this.appList) {
          this.init();
        }
      });
    });
  }

  ngOnInit() {
  }

  /**
   * 初始化表单
   */
  public initForm(formInfo) {
    const myThis = this;
    this.formInfoModel.initForm({
      components: [
        {
          name: '是否通过',
          field: 'whether',
          required: true,
          hidden: formInfo.auditStatus === '2',
          select: {
            dictType: 'whether',
            confirmFun(form, component, that) {
              for (const obj of myThis.formInfoModel.formConfig.components) {
                if (obj.field === 'reason') {
                  obj.hidden = form['whether'] === '0';
                }
              }
            }
          }
        },
        {
          name: '拒绝原因(拒绝时填写)',
          field: 'reason',
          required: formInfo.whether === '1',
          hidden: formInfo.whether === '0',
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
    const type = this.formInfoModel.formInfo.type;
    if (type === 'audit') {
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
      this.auditManagementService.updateAuditAppAuthorize(formInfo.id, req).subscribe(res => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('审核成功！');
        this.tableFindModel.getData(null, null);
      });
    } else {
      let index = 0;
      const array = this.tableFindModel.getSelectedArray();
      for (const obj of array) {
        const whether = this.formInfoModel.formInfo.whether;
        let reason = this.formInfoModel.formInfo.reason;
        if (whether === '1') {
          if (!reason) {
            this.message.warning('拒绝原因不能为空！');
            return;
          }
        } else {
          reason = '';
        }
        const req = {
          auditStatus: whether === '1' ? '3' : '2',
          reason
        };
        this.appService.setLoading(true);
        this.auditManagementService.updateAuditAppAuthorize(obj.data.id, req).subscribe(res => {
          index++;
          if (index === array.length) {
            this.appService.setLoading(false);
            this.isVisible = false;
            this.message.success('操作成功！');
            this.tableFindModel.getData(null, null);
          }
        });
      }
    }
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
        {name: '权限名称', field: 'authorityName', type: COMMON_TYPE.table.CHECKBOX},
        {name: '权限标识', field: 'authorityCode', type: COMMON_TYPE.table.TEXT},
        {name: '权限类型', field: 'authorityType', type: COMMON_TYPE.table.DICT_DESC, dictType: 'authorityType'},
        {name: '权限应用', field: 'authorityAppId', type: COMMON_TYPE.table.DICT_DESC, options: myThis.appList},
        {name: '申请应用', field: 'appId', type: COMMON_TYPE.table.DICT_DESC, options: myThis.authorityAppList},
        {name: '审核状态', field: 'auditStatus', type: COMMON_TYPE.table.DICT_DESC, dictType: 'auditStatus'},
        {name: '申请时间', field: 'applyTime', type: COMMON_TYPE.table.TEXT, isSort: true},
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          operates: [
            {
              name: myThis.auditStatus === '1' ? '审核' : '查看',
              field: 'details',
              bAuthority: BUTTON_CODE.b_authority_audit_details,
              confirmFun(dataModel, that) {
                myThis.router.navigate(['/home/auditManage/authority/authorityDetails'], {
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
              bAuthority: BUTTON_CODE.b_authority_audit_reject,
              hidden: myThis.auditStatus !== '2',
              confirmFun(dataModel, that) {
                myThis.isVisible = true;
                myThis.initForm({
                  id: dataModel.data.id,
                  type: 'audit'
                });
              }
            }
          ]
        }
      ],
      finds: [
        {
          name: '权限名称',
          field: 'name',
          input: {
            maxLength: 60
          }
        }, {
          name: '权限标识',
          field: 'code',
          input: {
            maxLength: 60
          }
        },
        {
          name: '权限应用',
          field: 'authorityAppId',
          select: {
            options: myThis.appList
          }
        },
        {
          name: '申请应用',
          field: 'appId',
          select: {
            options: myThis.authorityAppList
          }
        }
      ],
      buttons: [{
        name: '批量审核',
        type: COMMON_TYPE.button.MIDDLING,
        icon: 'plus-circle',
        bAuthority: BUTTON_CODE.b_authority_audit,
        confirmFun(formInfo, that) {
          myThis.isVisible = true;
          myThis.initForm({
            auditStatus: '1',
            type: 'batchAudit'
          });
        }
      }],
      initDataFun(formInfo, sortPage, callback, that) {// 数据处理回调 config：配置，dataModels：数据，that：当前对象
        const req = {
          size: sortPage.size,
          likeFields: [],
          sortField: sortPage['sortField'],
          sortRule: sortPage['sortRule'],
          auditStatus: myThis.auditStatus
        };
        for (const find of that.config.finds) {
          req[find.field] = formInfo[find.field];
        }
        myThis.auditManagementService.getAuditAppAuthorizePage(sortPage.current, req).subscribe(res => {
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
