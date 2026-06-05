import {Component, OnInit, ViewChild} from '@angular/core';
import {
  FormInfoComponent,
  FormInfoModel,
  OverallSelectComponent,
  TableFindModel
} from '../../../../../../projects/xc-common/src/public-api';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {AppService} from '../../../../app.service';
import {BUTTON_CODE} from '../../../../config/button-code';
import {BasicManagementService} from '../../basic-management.service';
import {BasicService} from '../../../../basic/basic.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-role-list',
  templateUrl: './role-list.component.html',
  styleUrls: ['./role-list.component.scss']
})
export class RoleListComponent implements OnInit {
  public tableFindModel: TableFindModel;
  public formInfoModel: FormInfoModel;
  @ViewChild(FormInfoComponent, {static: true})
  public formInfoComponent: FormInfoComponent;
  public isVisible = false;
  public roleId;
  public appId;
  public authorityList = [];
  public trees = [];
  @ViewChild(OverallSelectComponent, {static: true})
  public overallSelectComponent: OverallSelectComponent;
  public listInfo = {
    isImport: true, // 是否是导入
    isVisible: false,
    type: 'data',
    content: ''
  };

  constructor(public basicManagementService: BasicManagementService,
              public basicService: BasicService,
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
    this.basicManagementService.getAppAuthorityList(this.appId, {types: ['0', '1', '2']}).subscribe(res => {
      const nodes = [];
      for (const arr of res) {
        if (arr.status === '0') {
          if (arr.type === '1') {
            continue;
          }
          if (arr.type === '0') {
            arr.name = '菜单-' + arr.name;
          } else if (arr.type === '2') {
            arr.name = '按钮-' + arr.name;
          }
          nodes.push({
            title: arr.name,
            node: arr.node,
            parentNode: arr.parentNode,
            key: arr.id,
            seq: arr.seq
          });
        }
      }
      this.trees = this.basicService.getTrees('root', nodes);
      this.authorityList = res;
    });
    this.init();
  }

  ngOnInit() {

  }


  /**
   * 初始化表单
   */
  public initForm(array, form) {
    this.formInfoModel.initForm({
      components: [
        {
          name: '角色名称',
          field: 'name',
          required: true,
          input: {
            maxLength: 60
          }
        },
        {
          name: '角色标识',
          field: 'code',
          required: true,
          input: {
            maxLength: 60
          }
        },
        {
          name: '角色类型',
          field: 'type',
          required: true,
          select: {
            dictType: 'roleType',
            disabled: !!this.roleId
          }
        },
        {
          name: '角色权限',
          field: 'authorityIds',
          selectTree: {
            trees: array,
            checkable: true,
            maxTagCount: 2
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
        },
        {
          name: '角色描述',
          field: 'describe',
          textarea: {
            maxLength: 200,
            rows: 4
          }
        }
      ]
    }, form);
  }

  /**
   * 获取权限
   */
  public getAuthorityList(authorityIds) {
    const authorityList = [];
    for (const authorityId of authorityIds) {
      for (const authority of this.authorityList) {
        if (authorityId === authority.id) {
          authorityList.push(authority);
        }
      }
    }
    return authorityList;
  }

  /**
   * 提交
   */
  public confirmOk() {
    this.appService.setLoading(true);
    const req = {
      isVisible: true,
      name: this.formInfoModel.formInfo.name,
      code: this.formInfoModel.formInfo.code,
      type: this.formInfoModel.formInfo.type,
      describe: this.formInfoModel.formInfo.describe,
      seq: this.formInfoModel.formInfo.seq,
      status: this.formInfoModel.formInfo.status ? '0' : '1',
      authorityIds: []
    };
    const authorityIds = this.formInfoModel.formInfo.authorityIds;
    if (authorityIds) {
      req['authorityIds'] = this.basicManagementService.getSubmitIds(authorityIds, this.authorityList);
    }
    const authorityList = this.getAuthorityList(req['authorityIds']);
    for (const authority of authorityList) {
      for (const arr of this.authorityList) {
        if ((arr.parentNode === authority.node || arr.parentNode === 'root') && arr.type === '1') {
          req['authorityIds'].push(arr.id);
        }
      }
    }
    req['authorityIds'] = req['authorityIds'].filter((item, index) => {
      return req['authorityIds'].indexOf(item) === index;
    });
    if (!this.roleId) {
      this.basicManagementService.createAppRole(this.appId, req).subscribe(res => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('角色创建成功！');
        this.tableFindModel.getData(null, null);
      });
    } else {
      this.basicManagementService.updateAppRole(this.appId, this.roleId, req).subscribe(res => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('角色修改成功！');
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
        {name: '类型', field: 'type', type: COMMON_TYPE.table.DICT_DESC, dictType: 'roleType'},
        {name: '状态', field: 'status', type: COMMON_TYPE.table.DICT_DESC, dictType: 'effectStatus'},
        {name: '排序', field: 'seq', type: COMMON_TYPE.table.TEXT, isSort: true},
        {name: '更新时间', field: 'updateTime', type: COMMON_TYPE.table.TEXT, isSort: true},
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          operates: [
            {
              name: '编辑',
              field: 'edit',
              bAuthority: BUTTON_CODE.b_app_role_edit,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.basicManagementService.getAppRole(myThis.appId, dataModel.data.id).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.isVisible = true;
                  myThis.roleId = res['id'];
                  const authorityList = myThis.getAuthorityList(res['authorityIds']);
                  let authorityIds = [];
                  for (const authority of authorityList) {
                    if (authority.type !== '1') {
                      authorityIds.push(authority.id);
                    }
                  }
                  authorityIds = myThis.basicManagementService.getSelectedIds(authorityIds, myThis.authorityList);
                  const from = {
                    name: res['name'],
                    code: res['code'],
                    describe: res['describe'],
                    authorityIds,
                    type: res['type'],
                    seq: res['seq'],
                    status: dataModel.data['status'] === '0'
                  };
                  myThis.initForm(myThis.trees, from);
                });
              }
            },
            {
              name: '删除',
              field: 'delete',
              bAuthority: BUTTON_CODE.b_app_role_delete,
              confirmFun(dataModel, that) {
                myThis.modalService.confirm({
                  nzTitle: '确认要删除吗?',
                  nzContent: '<b style="color: red;">删除后数据无法找回哦！</b>',
                  nzOkText: '确认',
                  nzOkType: 'danger',
                  nzOnOk: () => {
                    myThis.appService.setLoading(true);
                    myThis.basicManagementService.deleteAppRole(myThis.appId, dataModel.data.id).subscribe(res => {
                      myThis.appService.setLoading(false);
                      myThis.message.success('删除成功！');
                      myThis.tableFindModel.getData(null, null);
                    });
                  },
                  nzCancelText: '取消'
                });
              }
            },
            {
              name: '上移',
              field: 'up',
              bAuthority: BUTTON_CODE.b_app_role_up,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.basicManagementService.updateAppRoleUp(myThis.appId, dataModel.data.id).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.message.success('移动成功！');
                  myThis.tableFindModel.getData(null, null);
                });
              }
            },
            {
              name: '下移',
              field: 'down',
              bAuthority: BUTTON_CODE.b_app_role_down,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.basicManagementService.updateAppRoleDown(myThis.appId, dataModel.data.id).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.message.success('移动成功！');
                  myThis.tableFindModel.getData(null, null);
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
          name: '类型', field: 'type', select: {
            dictType: 'roleType'
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
          bAuthority: BUTTON_CODE.b_app_role_add,
          confirmFun(formInfo, that) {
            myThis.isVisible = true;
            myThis.roleId = null;
            myThis.initForm(myThis.trees, {status: true});
          }
        },
        {
          name: '导入',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_role_import,
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
          bAuthority: BUTTON_CODE.b_app_role_export,
          confirmFun(formInfo, that) {
            const req = {
              likeFields: ['name']
            };
            for (const find of that.config.finds) {
              req[find.field] = formInfo[find.field];
            }
            myThis.basicManagementService.getAppRoleList(myThis.appId, req).subscribe(res => {
              const array = [];
              for (const data of res) {
                array.push({
                  name: data['name'],
                  code: data['code'],
                  type: data['type'],
                  status: data['status'],
                  seq: data['seq'],
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
          bAuthority: BUTTON_CODE.b_app_role_authority_relation_import,
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
          bAuthority: BUTTON_CODE.b_app_role_authority_relation_export,
          confirmFun(formInfo, that) {
            myThis.basicManagementService.getAppRoleAuthorityRelationList(myThis.appId, {}).subscribe(res => {
              const array = [];
              for (const data of res) {
                array.push({
                  roleCode: data['roleCode'],
                  authorityCode: data['authorityCode']
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
        myThis.basicManagementService.getAppRolePage(myThis.appId, sortPage.current, req).subscribe(res => {
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
        if (!r['name'] || !r['code'] || !r['type'] || !r['status']) {
          this.message.success('数据格式错误！');
          return;
        }
      }
      this.appService.setLoading(true);
      this.basicManagementService.createAppRoleList(this.appId, req).subscribe(res => {
        this.listInfo.isVisible = false;
        this.appService.setLoading(false);
        this.message.success('导入成功！');
        this.tableFindModel.getData(null, null);
      });
    } else {
      for (const r of req) {
        if (!r['roleCode'] || !r['authorityCode']) {
          this.message.success('数据格式错误！');
          return;
        }
      }
      this.appService.setLoading(true);
      this.basicManagementService.createAppRoleAuthorityRelationList(this.appId, req).subscribe(res => {
        this.listInfo.isVisible = false;
        this.appService.setLoading(false);
        this.message.success('导入成功！');
        this.tableFindModel.getData(null, null);
      });
    }
  }
}
