import {Component, OnInit, ViewChild} from '@angular/core';
import {TableFindModel} from '../../../../../../projects/xc-common/src/public-api';
import {AppService} from '../../../../app.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormInfoModel} from '../../../../../../projects/xc-common/src/public-api';
import {FormInfoComponent} from '../../../../../../projects/xc-common/src/public-api';
import {BUTTON_CODE} from '../../../../config/button-code';
import {BasicManagementService} from '../../basic-management.service';
import {OverallSelectComponent} from '../../../../../../projects/xc-common/src/public-api';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-group-list',
  templateUrl: './group-list.component.html',
  styleUrls: ['./group-list.component.scss']
})
export class GroupListComponent implements OnInit {

  public tableFindModel: TableFindModel;
  public formInfoModel: FormInfoModel;
  public isVisible = false;
  public groupId;
  @ViewChild(FormInfoComponent, {static: true})
  public formInfoComponent: FormInfoComponent;
  public appId;
  @ViewChild(OverallSelectComponent, {static: true})
  public overallSelectComponent: OverallSelectComponent;
  public listInfo = {
    isImport: true, // 是否是导入
    isVisible: false,
    type: 'data',
    content: ''
  };

  constructor(public basicManagementService: BasicManagementService,
              public message: NzMessageService,
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
    this.basicManagementService.getAppRoleList(this.appId, {}).subscribe(res => {
      const array = [];
      for (const data of res) {
        array.push({
          name: data.name,
          value: data.id
        });
      }
      this.initForm(array);
    });
  }

  ngOnInit() {
  }

  /**
   * 初始化表单
   */
  public initForm(array) {
    this.formInfoModel.initForm({
      components: [
        {
          name: '用户组名称',
          field: 'name',
          required: true,
          input: {
            maxLength: 60
          }
        },
        {
          name: '用户组标识',
          field: 'code',
          required: true,
          input: {
            maxLength: 60
          }
        },
        {
          name: '用户组的角色',
          field: 'roleIds',
          select: {
            options: array,
            multiple: true
          }
        },
        {
          name: '用户组状态',
          field: 'status',
          switch: {}
        },
        {
          name: '用户组描述',
          field: 'describe',
          textarea: {
            maxLength: 200,
            rows: 4
          }
        }
      ]
    }, {
      status: true
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
      describe: this.formInfoModel.formInfo.describe,
      status: this.formInfoModel.formInfo.status ? '0' : '1',
      roleIds: this.formInfoModel.formInfo.roleIds
    };
    if (!this.groupId) {
      this.basicManagementService.createAppGroup(this.appId, req).subscribe(res => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('用户组创建成功！');
        this.tableFindModel.getData(null, null);
      });
    } else {
      this.basicManagementService.updateAppGroup(this.appId, this.groupId, req).subscribe(res => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('用户组修改成功！');
        this.tableFindModel.getData(null, null);
      });
    }
  }

  /**
   * 初始化列表
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
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          operates: [
            {
              name: '编辑',
              field: 'edit',
              bAuthority: BUTTON_CODE.b_app_group_edit,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.basicManagementService.getAppGroup(myThis.appId, dataModel.data.id).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.isVisible = true;
                  myThis.groupId = res['id'];
                  myThis.formInfoModel.formInfo = {
                    name: res['name'],
                    code: res['code'],
                    describe: res['describe'],
                    status: res['status'] === '0',
                    roleIds: res['roleIds']
                  };
                });
              }
            },
            {
              name: '删除',
              field: 'delete',
              bAuthority: BUTTON_CODE.b_app_group_delete,
              confirmFun(dataModel, that) {
                myThis.modalService.confirm({
                  nzTitle: '确认要删除吗?',
                  nzContent: '<b style="color: red;">删除后数据无法找回哦！</b>',
                  nzOkText: '确认',
                  nzOkType: 'danger',
                  nzOnOk: () => {
                    myThis.appService.setLoading(true);
                    myThis.basicManagementService.deleteAppGroup(myThis.appId, dataModel.data.id).subscribe(res => {
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
          name: '名称', field: 'name', input: {
            maxLength: 60
          }
        },
        {
          name: '标识', field: 'code', input: {
            maxLength: 60
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
          bAuthority: BUTTON_CODE.b_app_group_add,
          confirmFun(formInfo, that) {
            myThis.isVisible = true;
            myThis.groupId = null;
            myThis.formInfoModel.formInfo = {status: true};
          }
        },
        {
          name: '导入',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_group_import,
          confirmFun(formInfo, that) {
            myThis.listInfo = {
              isImport: true,
              isVisible: true,
              type: 'data',
              content: ''
            };
          }
        },
        {
          name: '导出',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_group_export,
          confirmFun(formInfo, that) {
            const req = {
              likeFields: ['name']
            };
            for (const find of that.config.finds) {
              req[find.field] = formInfo[find.field];
            }
            myThis.basicManagementService.getAppGroupList(myThis.appId, req).subscribe(res => {
              const array = [];
              for (const data of res) {
                array.push({
                  name: data['name'],
                  code: data['code'],
                  type: data['type'],
                  status: data['status'],
                  describe: data['describe']
                });
              }
              myThis.listInfo = {
                isImport: false,
                isVisible: true,
                type: 'data',
                content: JSON.stringify(array)
              };
            });
          }
        },
        {
          name: '关联导入',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_group_role_relation_import,
          confirmFun(formInfo, that) {
            myThis.listInfo = {
              isImport: true,
              isVisible: true,
              type: 'relation',
              content: ''
            };
          }
        },
        {
          name: '关联导出',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_group_role_relation_export,
          confirmFun(formInfo, that) {
            myThis.basicManagementService.getAppGroupRoleRelationList(myThis.appId, {}).subscribe(res => {
              const array = [];
              for (const data of res) {
                array.push({
                  groupCode: data['groupCode'],
                  roleCode: data['roleCode']
                });
              }
              myThis.listInfo = {
                isImport: false,
                isVisible: true,
                type: 'relation',
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
        myThis.basicManagementService.getAppGroupPage(myThis.appId, sortPage.current, req).subscribe(res => {
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

  /**
   * 确认提交
   */
  public listInfoOk() {
    const req = JSON.parse(this.listInfo.content);
    if (this.listInfo.type === 'data') {
      for (const r of req) {
        if (!r['name'] || !r['code'] || !r['status']) {
          this.message.success('数据格式错误！');
          return;
        }
      }
      this.appService.setLoading(true);
      this.basicManagementService.createAppGroupList(this.appId, req).subscribe(res => {
        this.listInfo.isVisible = false;
        this.appService.setLoading(false);
        this.message.success('导入成功！');
        this.tableFindModel.getData(null, null);
      });
    } else {
      for (const r of req) {
        if (!r['groupCode'] || !r['roleCode']) {
          this.message.success('数据格式错误！');
          return;
        }
      }
      this.appService.setLoading(true);
      this.basicManagementService.createAppGroupRoleRelationList(this.appId, req).subscribe(res => {
        this.listInfo.isVisible = false;
        this.appService.setLoading(false);
        this.message.success('导入成功！');
        this.tableFindModel.getData(null, null);
      });
    }
  }
}
