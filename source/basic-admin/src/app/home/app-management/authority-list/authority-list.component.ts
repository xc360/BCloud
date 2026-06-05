import {Component, OnInit} from '@angular/core';
import {TableFindModel} from '../../../../../projects/xc-common/src/public-api';
import {AppManagementService} from '../app-management.service';
import {ActivatedRoute} from '@angular/router';
import {AppService} from '../../../app.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {BUTTON_CODE} from '../../../config/button-code';
import {FormInfoModel} from '../../../../../projects/xc-common/src/public-api';

@Component({
  selector: 'app-authority-list',
  templateUrl: './authority-list.component.html',
  styleUrls: ['./authority-list.component.scss']
})
export class AuthorityListComponent implements OnInit {

  public tableFindModel: TableFindModel;
  public id: any;
  public appList = [];
  public formInfoModel: FormInfoModel;
  public formInfoModel1: FormInfoModel;
  public isVisible = false;
  public isApplyVisible = false;

  constructor(public appManagementService: AppManagementService,
              public appService: AppService,
              public modalService: NzModalService,
              public message: NzMessageService,
              public activatedRoute: ActivatedRoute) {
    this.formInfoModel = new FormInfoModel();
    this.formInfoModel1 = new FormInfoModel();
    this.activatedRoute.queryParams.subscribe(queryParams => {
      this.id = queryParams.id;
    });
    this.appManagementService.getAppList().subscribe(res => {
      for (const row of res) {
        this.appList.push({
          name: row['name'],
          value: row['value']
        });
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
          textarea: {
            maxLength: 255,
            rows: 5,
            disabled: true
          }
        }
      ]
    }, formInfo);
  }

  /**
   * 申请权限
   */
  confirmOk() {
    const formInfo = this.formInfoModel1.formInfo;
    const req = {
      auditStatus: '1',
      reason: formInfo.reason
    };
    if (formInfo.authorityId) {
      req['authorityId'] = formInfo.authorityId;
    } else {
      req['authorityCodes'] = JSON.parse(formInfo.authorityCodes);
      req['authorityAppId'] = formInfo.authorityAppId;
    }
    this.appService.setLoading(true);
    this.appManagementService.updateAppAuthorize(this.id, req).subscribe(res => {
      this.isApplyVisible = false;
      this.appService.setLoading(false);
      this.message.success('已申请！');
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
        {name: '权限名称', field: 'authorityName', type: COMMON_TYPE.table.TEXT},
        {name: '权限标识', field: 'authorityCode', type: COMMON_TYPE.table.TEXT},
        {name: '权限类型', field: 'authorityType', type: COMMON_TYPE.table.DICT_DESC, dictType: 'authorityType'},
        {name: '权限应用', field: 'authorityAppId', type: COMMON_TYPE.table.DICT_DESC, options: myThis.appList},
        {name: '申请状态', field: 'auditStatus', type: COMMON_TYPE.table.DICT_DESC, dictType: 'auditStatus'},
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          initFun(dataModel, table, that) {
            dataModel['revoke'] = dataModel.data['auditStatus'] === '0' || dataModel.data['auditStatus'] === '3';
            dataModel['reason'] = dataModel.data['auditStatus'] === '3';
            dataModel['cancelApply'] = dataModel.data['auditStatus'] === '1' || dataModel.data['auditStatus'] === '2';
          },
          operates: [
            {
              name: '申请权限',
              field: 'revoke',
              bAuthority: BUTTON_CODE.b_app_apply_authorize,
              confirmFun(dataModel, that) {
                myThis.formInfoModel1.initForm({
                  components: [
                    {
                      name: '申请原因',
                      field: 'reason',
                      textarea: {
                        maxLength: 255,
                        rows: 5
                      }
                    }
                  ]
                }, {authorityId: dataModel.data.authorityId});
                myThis.isApplyVisible = true;
              }
            },
            {
              name: '取消申请',
              field: 'cancelApply',
              bAuthority: BUTTON_CODE.b_app_apply_authorize,
              confirmFun(dataModel, that) {
                myThis.modalService.confirm({
                  nzTitle: '确认要取消申请吗?',
                  nzContent: '<b style="color: red;">取消申请以后给接口将无法使用哦！</b>',
                  nzOkText: '确认',
                  nzOkType: 'danger',
                  nzOnOk: () => {
                    myThis.appService.setLoading(true);
                    myThis.appManagementService.updateAppAuthorize(myThis.id, {
                      authorityId: dataModel.data.authorityId,
                      auditStatus: '0'
                    }).subscribe(res => {
                      myThis.appService.setLoading(false);
                      myThis.message.success('取消申请成功！');
                      myThis.tableFindModel.getData(null, null);
                    });
                  },
                  nzCancelText: '取消'
                });
              }
            },
            {
              name: '拒绝原因', field: 'reason',
              confirmFun(dataModel, that) {
                myThis.initForm({reason: dataModel.data.reason});
                myThis.isVisible = true;
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
        },
        {
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
        }
      ],
      buttons: [{
        name: '批量申请',
        type: COMMON_TYPE.button.MIDDLING,
        icon: 'plus-circle',
        bAuthority: BUTTON_CODE.b_app_apply_authorize,
        confirmFun(formInfo, that) {
          myThis.formInfoModel1.initForm({
            components: [
              {
                name: '权限应用',
                field: 'authorityAppId',
                required: true,
                select: {
                  options: myThis.appList
                }
              },
              {
                name: '权限标识',
                field: 'authorityCodes',
                required: true,
                textarea: {
                  maxLength: 2000000,
                  rows: 10
                }
              },
              {
                name: '申请原因',
                field: 'reason',
                textarea: {
                  maxLength: 255,
                  rows: 5
                }
              }
            ]
          }, {authorityCodes: '[]', authorityAppId: formInfo.authorityAppId});
          myThis.isApplyVisible = true;
        }
      }],
      initDataFun(formInfo, sortPage, callback, that) {// 数据处理回调 config：配置，dataModels：数据，that：当前对象
        const req = {
          likeFields: ['name'],
          size: sortPage.size,
          sortField: sortPage['sortField'],
          sortRule: sortPage['sortRule']
        };
        for (const find of that.config.finds) {
          req[find.field] = formInfo[find.field];
        }
        myThis.appManagementService.getAppAuthorizeAuthorityPage(myThis.id, sortPage.current, req).subscribe(res => {
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
