import {Component, OnInit, ViewChild} from '@angular/core';
import {TableFindModel} from '../../../../../../projects/xc-common/src/public-api';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {AppService} from '../../../../app.service';
import {FormInfoModel} from '../../../../../../projects/xc-common/src/public-api';
import {FormInfoComponent} from '../../../../../../projects/xc-common/src/public-api';
import {BUTTON_CODE} from '../../../../config/button-code';
import {BasicManagementService} from '../../basic-management.service';
import {OverallSelectComponent} from '../../../../../../projects/xc-common/src/public-api';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-info-list',
  templateUrl: './info-list.component.html',
  styleUrls: ['./info-list.component.scss']
})
export class InfoListComponent implements OnInit {

  public tableFindModel: TableFindModel;
  public formInfoModel: FormInfoModel;
  public infoId;
  public isVisible = false;
  @ViewChild(FormInfoComponent, {static: true})
  public formInfoComponent: FormInfoComponent;
  public listInfo = {
    isImport: true, // 是否是导入
    isVisible: false,
    content: ''
  };
  public appId;
  @ViewChild(OverallSelectComponent, {static: true})
  public overallSelectComponent: OverallSelectComponent;

  constructor(public basicManagementService: BasicManagementService,
              public message: NzMessageService,
              public router: Router,
              public modalService: NzModalService,
              public activatedRoute: ActivatedRoute,
              public appService: AppService) {
    this.formInfoModel = new FormInfoModel();
    this.activatedRoute.queryParams.subscribe(queryParams => {
      if (queryParams.appId) {
        this.getBasicData(queryParams.appId);
      }
    });
  }

  /**
   * 获取基础数据
   */
  public getBasicData(appId) {
    this.appId = appId;
    this.init();
    this.initForm();
  }

  ngOnInit() {

  }

  /**
   * 初始化表单
   */
  initForm() {
    this.formInfoModel.initForm({
      components: [
        {
          name: '信息key',
          field: 'key',
          required: true,
          input: {
            maxLength: 60
          }
        }, {
          name: '信息值',
          field: 'value',
          required: true,
          textarea: {
            maxLength: 2000000,
            rows: 4
          }
        },
        {
          name: '信息类型',
          field: 'type',
          required: true,
          select: {
            dictType: 'infoType'
          }
        },
        {
          name: '信息状态',
          field: 'status',
          switch: {}
        },
        {
          name: '信息描述',
          field: 'describe',
          textarea: {
            maxLength: 200,
            rows: 4
          }
        }
      ]
    }, {status: true});
  }

  /**
   * 确认提交
   */
  confirmOk() {
    this.appService.setLoading(true);
    const req = {
      key: this.formInfoModel.formInfo.key,
      value: this.formInfoModel.formInfo.value,
      type: this.formInfoModel.formInfo.type,
      status: this.formInfoModel.formInfo.status ? '0' : '1',
      describe: this.formInfoModel.formInfo.describe
    };
    if (!this.infoId) {
      this.basicManagementService.createAppInfo(this.appId, req).subscribe(res => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('信息创建成功！');
        this.tableFindModel.getData(null, null);
      });
    } else {
      this.basicManagementService.updateAppInfo(this.appId, this.infoId, req).subscribe(res => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('信息修改成功！');
        this.tableFindModel.getData(null, null);
      });
    }

  }

  /**
   * 确认提交1
   */
  public listInfoOk() {
    const req = JSON.parse(this.listInfo.content);
    for (const r of req) {
      if (!r['key'] || !r['value'] || !r['type'] || !r['status']) {
        this.message.success('数据格式错误！');
        return;
      }
    }
    this.appService.setLoading(true);
    this.basicManagementService.createAppInfoList(this.appId, req).subscribe(res => {
      this.listInfo.isVisible = false;
      this.appService.setLoading(false);
      this.message.success('导入成功！');
      this.tableFindModel.getData(null, null);
    });
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
        {name: '信息key', field: 'key', type: COMMON_TYPE.table.TEXT, isSort: true},
        {
          name: '信息值', field: 'value', type: COMMON_TYPE.table.TEXT,
          tdStyle: {
            maxWidth: '200px',
            overflow: 'hidden',
            whiteSpace: 'nowrap',
            textOverflow: 'ellipsis'
          }
        },
        {name: '类型', field: 'type', type: COMMON_TYPE.table.DICT_DESC, dictType: 'infoType'},
        {name: '状态', field: 'status', type: COMMON_TYPE.table.DICT_DESC, dictType: 'effectStatus'},
        {name: '更新时间', field: 'updateTime', type: COMMON_TYPE.table.TEXT, isSort: true},
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          operates: [
            {
              name: '编辑',
              field: 'edit',
              bAuthority: BUTTON_CODE.b_app_info_edit,
              confirmFun(dataModel, that) {
                myThis.isVisible = true;
                myThis.infoId = dataModel.data.id;
                myThis.formInfoModel.formInfo = {
                  key: dataModel.data['key'],
                  value: dataModel.data['value'],
                  type: dataModel.data['type'],
                  status: dataModel.data['status'] === '0',
                  describe: dataModel.data['describe']
                };
              }
            },
            {
              name: '删除',
              field: 'delete',
              bAuthority: BUTTON_CODE.b_app_info_delete,
              confirmFun(dataModel, that) {
                myThis.modalService.confirm({
                  nzTitle: '确认要删除吗?',
                  nzContent: '<b style="color: red;">删除后数据无法找回哦！</b>',
                  nzOkText: '确认',
                  nzOkType: 'danger',
                  nzOnOk: () => {
                    myThis.appService.setLoading(true);
                    myThis.basicManagementService.deleteAppInfo(myThis.appId, dataModel.data.id).subscribe(res => {
                      myThis.appService.setLoading(false);
                      myThis.message.success('删除成功！');
                      myThis.tableFindModel.getData(null, null);
                    });
                  },
                  nzCancelText: '取消'
                });
              }
            }
          ]
        }
      ],
      finds: [
        {
          name: '信息key', field: 'key', input: {
            maxLength: 60
          }
        },
        {
          name: '类型', field: 'type', select: {
            dictType: 'infoType'
          }
        },
        {
          name: '状态', field: 'status', select: {
            dictType: 'effectStatus'
          }
        }
      ],
      buttons: [
        {
          name: '新增',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_info_add,
          confirmFun(formInfo, that) {
            myThis.isVisible = true;
            myThis.infoId = null;
            myThis.formInfoModel.formInfo = {status: true};
          }
        },
        {
          name: '信息设置',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_info_add,
          confirmFun(formInfo, that) {
            myThis.router.navigate(['/home/basicManage/infoManage/infoEdit'], {
              queryParams: {
                appId: myThis.appId
              }
            });
          }
        },
        {
          name: '导入',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_info_import,
          confirmFun(formInfo, that) {
            myThis.listInfo = {
              isImport: true,
              isVisible: true,
              content: ''
            };
          }
        },
        {
          name: '导出',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_info_export,
          confirmFun(formInfo, that) {
            const req = {
              likeFields: ['describe']
            };
            for (const find of that.config.finds) {
              req[find.field] = formInfo[find.field];
            }
            myThis.basicManagementService.getAppInfoList(myThis.appId, req).subscribe(res => {
              const array = [];
              for (const data of res) {
                array.push({
                  key: data['key'],
                  value: data['value'],
                  type: data['type'],
                  status: data['status'],
                  describe: data['describe']
                });
              }
              myThis.listInfo = {
                isImport: false,
                isVisible: true,
                content: JSON.stringify(array)
              };
            });
          }
        }
      ],
      initDataFun(formInfo, sortPage, callback, that) {// 数据处理回调 config：配置，dataModels：数据，that：当前对象
        const req = {
          size: sortPage.size,
          likeFields: ['describe'],
          sortField: sortPage['sortField'],
          sortRule: sortPage['sortRule']
        };
        for (const find of that.config.finds) {
          req[find.field] = formInfo[find.field];
        }
        myThis.basicManagementService.getAppInfoPage(myThis.appId, sortPage.current, req).subscribe(res => {
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
