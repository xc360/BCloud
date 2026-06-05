import {Component, OnInit, ViewChild} from '@angular/core';
import {TableFindModel} from '../../../../../../projects/xc-common/src/common/table-find/table-find.model';
import {FormInfoModel} from '../../../../../../projects/xc-common/src/common/form-info/form-info.model';
import {FormInfoComponent} from '../../../../../../projects/xc-common/src/common/form-info/form-info.component';
import {OverallSelectComponent} from '../../../../../../projects/xc-common/src/common/overall-select/overall-select.component';
import {ApiManagementService} from '../../api-management.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {ActivatedRoute, Router} from '@angular/router';
import {AppService} from '../../../../app.service';
import {BUTTON_CODE} from '../../../../config/button-code';

@Component({
  selector: 'app-supplier-list',
  templateUrl: './supplier-list.component.html',
  styleUrls: ['./supplier-list.component.scss']
})
export class SupplierListComponent implements OnInit {

  public tableFindModel: TableFindModel;
  public formInfoModel: FormInfoModel;
  public messageId;
  public isVisible = false;
  @ViewChild(FormInfoComponent, {static: true})
  public formInfoComponent: FormInfoComponent;
  public appId;
  @ViewChild(OverallSelectComponent, {static: true})
  public overallSelectComponent: OverallSelectComponent;
  public listInfo = {
    isImport: true, // 是否是导入
    isVisible: false,
    content: ''
  };

  constructor(public messageManagementService: ApiManagementService,
              public message: NzMessageService,
              public modalService: NzModalService,
              public router: Router,
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
  public initForm() {
    this.formInfoModel.initForm({
      components: [
        {
          name: '名称',
          field: 'name',
          required: true,
          input: {
            maxLength: 60
          }
        },
        {
          name: '标识',
          field: 'code',
          required: true,
          input: {
            maxLength: 60
          }
        },
        {
          name: '访问ID',
          field: 'accessId',
          required: true,
          input: {
            maxLength: 60
          }
        },
        {
          name: '访问Secret',
          field: 'accessSecret',
          required: true,
          input: {
            maxLength: 60
          }
        },
        {
          name: '其他配置(json格式)',
          field: 'otherConfig',
          textarea: {
            rows: 4,
            maxLength: 2000000
          }
        },
        {
          name: '状态',
          field: 'status',
          switch: {}
        }
      ]
    }, {status: true});
  }

  /**
   * 确认提交1
   */
  public listInfoOk() {
    const req = JSON.parse(this.listInfo.content);
    for (const r of req) {
      if (!r['name'] || !r['code'] || !r['accessId'] || !r['accessSecret'] || !r['status']) {
        this.message.success('数据格式错误！');
        return;
      }
    }
    this.appService.setLoading(true);
    this.messageManagementService.createAppApiSupplierList(this.appId, req).subscribe(res => {
      this.listInfo.isVisible = false;
      this.appService.setLoading(false);
      this.message.success('导入成功！');
      this.tableFindModel.getData(null, null);
    });
  }

  /**
   * 确认提交
   */
  public confirmOk() {
    this.appService.setLoading(true);
    const req = {
      name: this.formInfoModel.formInfo.name,
      code: this.formInfoModel.formInfo.code,
      accessId: this.formInfoModel.formInfo.accessId,
      accessSecret: this.formInfoModel.formInfo.accessSecret,
      otherConfig: this.formInfoModel.formInfo.otherConfig,
      status: this.formInfoModel.formInfo.status ? '0' : '1'
    };
    if (!this.messageId) {
      this.messageManagementService.createAppApiSupplier(this.appId, req).subscribe(res => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('创建成功！');
        this.tableFindModel.getData(null, null);
      });
    } else {
      this.messageManagementService.updateAppApiSupplier(this.appId, this.messageId, req).subscribe(res => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('修改成功！');
        this.tableFindModel.getData(null, null);
      });
    }

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
        {name: '名称', field: 'name', type: COMMON_TYPE.table.TEXT},
        {name: '标识', field: 'code', type: COMMON_TYPE.table.TEXT},
        {name: '状态', field: 'status', type: COMMON_TYPE.table.DICT_DESC, dictType: 'effectStatus'},
        {name: '更新时间', field: 'updateTime', type: COMMON_TYPE.table.TEXT, isSort: true},
        {name: '创建时间', field: 'createTime', type: COMMON_TYPE.table.TEXT, isSort: true},
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          operates: [
            {
              name: '编辑',
              field: 'edit',
              bAuthority: BUTTON_CODE.b_app_api_supplier_edit,
              confirmFun(dataModel, that) {
                myThis.isVisible = true;
                myThis.messageId = dataModel.data.id;
                myThis.formInfoModel.formInfo = {
                  name: dataModel.data['name'],
                  code: dataModel.data['code'],
                  accessId: dataModel.data['accessId'],
                  accessSecret: dataModel.data['accessSecret'],
                  otherConfig: dataModel.data['otherConfig'],
                  status: dataModel.data['status'] === '0'
                };
              }
            },
            {
              name: '删除',
              field: 'delete',
              bAuthority: BUTTON_CODE.b_app_api_supplier_delete,
              confirmFun(dataModel, that) {
                myThis.modalService.confirm({
                  nzTitle: '确认要删除吗?',
                  nzContent: '<b style="color: red;">删除后数据无法找回哦！</b>',
                  nzOkText: '确认',
                  nzOkType: 'danger',
                  nzOnOk: () => {
                    myThis.appService.setLoading(true);
                    myThis.messageManagementService.deleteAppApiSupplier(myThis.appId, dataModel.data.id).subscribe(res => {
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
          name: '名称',
          field: 'name',
          input: {
            maxLength: 60
          }
        },
        {
          name: '消息标识',
          field: 'code',
          input: {
            maxLength: 60
          }
        },
        {
          name: '状态',
          field: 'status',
          select: {
            dictType: 'effectStatus'
          }
        }
      ],
      buttons: [
        {
          name: '新增',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_api_supplier_add,
          confirmFun(formInfo, that) {
            myThis.isVisible = true;
            myThis.messageId = null;
            myThis.formInfoModel.formInfo = {status: true};
          }
        },
        {
          name: '导入',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_api_supplier_import,
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
          bAuthority: BUTTON_CODE.b_app_api_supplier_export,
          confirmFun(formInfo, that) {
            const req = {
              likeFields: []
            };
            for (const find of that.config.finds) {
              req[find.field] = formInfo[find.field];
            }
            myThis.messageManagementService.getAppApiSupplierList(myThis.appId, req).subscribe(res => {
              const array = [];
              for (const data of res) {
                array.push({
                  name: data['name'],
                  code: data['code'],
                  accessId: data['accessId'],
                  accessSecret: data['accessSecret'],
                  otherConfig: data['otherConfig'],
                  status: data['status']
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
          likeFields: ['name'],
          sortField: sortPage['sortField'],
          sortRule: sortPage['sortRule']
        };
        for (const find of that.config.finds) {
          req[find.field] = formInfo[find.field];
        }
        myThis.messageManagementService.getAppApiSupplierPage(myThis.appId, sortPage.current, req).subscribe(res => {
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
