import {Component, OnInit, ViewChild} from '@angular/core';
import {BasicService} from '../../../../basic/basic.service';
import {WebsiteManagementService} from '../../website-management.service';
import {AppService} from '../../../../app.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {ActivatedRoute, Router} from '@angular/router';
import {BUTTON_CODE} from '../../../../config/button-code';
import {TableFindModel} from '../../../../../../projects/xc-common/src/common/table-find/table-find.model';
import {FormInfoModel} from '../../../../../../projects/xc-common/src/common/form-info/form-info.model';
import {FormInfoComponent} from '../../../../../../projects/xc-common/src/common/form-info/form-info.component';

@Component({
  selector: 'app-column-type-list',
  templateUrl: './data-type-list.component.html',
  styleUrls: ['./data-type-list.component.scss']
})
export class DataTypeListComponent implements OnInit {

  public tableFindModel: TableFindModel;
  public formInfoModel: FormInfoModel;
  public isVisible = false;
  @ViewChild(FormInfoComponent, {static: true})
  public formInfoComponent: FormInfoComponent;
  public appId;
  public listInfo = {
    isImport: true, // 是否是导入
    isVisible: false,
    content: ''
  };

  constructor(public basicService: BasicService,
              public websiteManagementService: WebsiteManagementService,
              public appService: AppService,
              public message: NzMessageService,
              public modalService: NzModalService,
              public activatedRoute: ActivatedRoute,
              public router: Router) {
    this.tableFindModel = new TableFindModel();
    this.formInfoModel = new FormInfoModel();
    this.activatedRoute.queryParams.subscribe(queryParams => {
      if (queryParams.appId) {
        this.appId = queryParams.appId;
        this.init();
      }
    });
  }

  ngOnInit(): void {
  }

  /**
   * 初始化表单
   */
  initForm(formInfo) {
    this.isVisible = true;
    this.formInfoModel.initForm({
      components: [
        {
          name: '数据类型名称',
          field: 'name',
          required: true,
          input: {
            maxLength: 60
          }
        },
        {
          name: '数据类型标识',
          field: 'code',
          required: true,
          input: {
            maxLength: 60
          }
        },
        {
          name: '排序',
          field: 'seq',
          input: {
            maxLength: 9
          }
        },
        {
          name: '状态',
          field: 'status',
          switch: {}
        }
      ]
    }, formInfo);
  }


  /**
   * 初始化配置
   */
  public init() {
    const myThis = this;
    const COMMON_TYPE = TableFindModel.COMMON_TYPE;
    const config = {
      tables: [
        {name: '数据类型名称', field: 'name', type: COMMON_TYPE.table.TEXT},
        {name: '栏目标识', field: 'code', type: COMMON_TYPE.table.TEXT},
        {name: '排序', field: 'seq', type: COMMON_TYPE.table.TEXT, isSort: true},
        {name: '状态', field: 'status', type: COMMON_TYPE.table.DICT_DESC, dictType: 'effectStatus'},
        {name: '更新时间', field: 'updateTime', type: COMMON_TYPE.table.TEXT, isSort: true},
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          operates: [
            {
              name: '编辑',
              field: 'edit',
              bAuthority: BUTTON_CODE.b_app_data_type_edit,
              confirmFun(dataModel, that) {
                myThis.initForm({
                  id: dataModel.data.id,
                  name: dataModel.data.name,
                  code: dataModel.data.code,
                  seq: dataModel.data.seq,
                  status: dataModel.data['status'] === '0'
                });
              }
            },
            {
              name: '上移',
              field: 'up',
              bAuthority: BUTTON_CODE.b_app_data_type_up,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.websiteManagementService.updateAppDataTypeUp(myThis.appId, dataModel.data.id).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.message.success('移动成功！');
                  myThis.tableFindModel.getData(null, null);
                });
              }
            },
            {
              name: '下移',
              field: 'down',
              bAuthority: BUTTON_CODE.b_app_data_type_down,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.websiteManagementService.updateAppDataTypeDown(myThis.appId, dataModel.data.id).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.message.success('移动成功！');
                  myThis.tableFindModel.getData(null, null);
                });
              }
            },
            {
              name: '删除',
              field: 'delete',
              bAuthority: BUTTON_CODE.b_app_data_type_delete,
              confirmFun(dataModel, that) {
                myThis.modalService.confirm({
                  nzTitle: '确认要删除吗?',
                  nzContent: '<b style="color: red;">删除后数据无法找回哦！</b>',
                  nzOkText: '确认',
                  nzOkType: 'danger',
                  nzOnOk: () => {
                    myThis.appService.setLoading(true);
                    myThis.websiteManagementService.deleteAppDataType(myThis.appId, dataModel.data.id).subscribe(res => {
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
          name: '类型名称',
          field: 'name',
          input: {
            maxLength: 60
          }
        },
        {
          name: '类型标识',
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
          name: '添加',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_data_type_add,
          confirmFun(formInfo, that) {
            myThis.initForm({status: true});
          }
        },
        {
          name: '导入',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_data_type_import,
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
          bAuthority: BUTTON_CODE.b_app_data_type_export,
          confirmFun(formInfo, that) {
            const req = {
              likeFields: ['name']
            };
            for (const find of that.config.finds) {
              req[find.field] = formInfo[find.field];
            }
            myThis.websiteManagementService.getAppDataTypeList(myThis.appId, req).subscribe(res => {
              const array = [];
              for (const data of res) {
                array.push({
                  name: data['name'],
                  code: data['code'],
                  seq: data['seq'],
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
        myThis.websiteManagementService.getAppDataTypePage(myThis.appId, sortPage.current, req).subscribe(res => {
          const array = res['resData'].map((data) => {
            return {data};
          });
          callback(array, {total: res['total']});
        });
      },
      frontPaging: false,
      frontSort: false
    };
    this.tableFindModel.initConfig(config);
  }

  /**
   * 确认提交1
   */
  public listInfoOk() {
    const req = JSON.parse(this.listInfo.content);
    for (const r of req) {
      if (!r['name'] || !r['code'] || !r['status']) {
        this.message.success('数据格式错误！');
        return;
      }
    }
    this.appService.setLoading(true);
    this.websiteManagementService.createAppDataTypeList(this.appId, req).subscribe(res => {
      this.listInfo.isVisible = false;
      this.appService.setLoading(false);
      this.message.success('导入成功！');
      this.tableFindModel.getData(null, null);
    });
  }


  /**
   * 确定
   */
  confirmOk() {
    this.appService.setLoading(true);
    const req = {
      name: this.formInfoModel.formInfo.name,
      code: this.formInfoModel.formInfo.code,
      seq: this.formInfoModel.formInfo.seq,
      status: this.formInfoModel.formInfo.status ? '0' : '1'
    };
    if (!this.formInfoModel.formInfo.id) {
      this.websiteManagementService.createAppDataType(this.appId, req).subscribe(() => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('创建成功！');
        this.tableFindModel.getData(null, null);
      });
    } else {
      this.websiteManagementService.updateAppDataType(this.appId, this.formInfoModel.formInfo.id, req).subscribe(() => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('修改成功！');
        this.tableFindModel.getData(null, null);
      });
    }
  }
}
