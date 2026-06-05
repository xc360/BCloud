import {Component, OnInit, ViewChild} from '@angular/core';
import {TableFindModel} from '../../../../../../projects/xc-common/src/public-api';
import {NzMessageService} from 'ng-zorro-antd';
import {AppService} from '../../../../app.service';
import {FormInfoModel} from '../../../../../../projects/xc-common/src/public-api';
import {BUTTON_CODE} from '../../../../config/button-code';
import {BasicManagementService} from '../../basic-management.service';
import {OverallSelectComponent} from '../../../../../../projects/xc-common/src/public-api';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {

  public tableFindModel: TableFindModel;
  public userId;
  public formInfoModel: FormInfoModel;
  public isVisible = false;
  public appId;
  @ViewChild(OverallSelectComponent, {static: true})
  public overallSelectComponent: OverallSelectComponent;
  public groupArray;
  public roleArray;

  constructor(public basicManagementService: BasicManagementService,
              public message: NzMessageService,
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

  ngOnInit() {
  }

  /**
   * 获取基础数据
   */
  public getBasicData(appId) {
    this.appId = appId;
    this.basicManagementService.getAppGroupList(this.appId, {}).subscribe(res => {
      const groupArray = [];
      for (const data of res) {
        groupArray.push({
          name: data.name,
          value: data.id
        });
      }
      this.groupArray = groupArray;
      if (this.groupArray && this.roleArray) {
        this.initForm(this.groupArray, this.roleArray);
        this.init(this.groupArray, this.roleArray);
      }
    });
    this.basicManagementService.getAppRoleList(this.appId, {}).subscribe(res1 => {
      const roleArray = [];
      for (const data of res1) {
        roleArray.push({
          name: data.name,
          value: data.id
        });
      }
      this.roleArray = roleArray;
      if (this.groupArray && this.roleArray) {
        this.initForm(this.groupArray, this.roleArray);
        this.init(this.groupArray, this.roleArray);
      }
    });
  }

  /**
   * 初始化表单
   */
  public initForm(groupArray, roleArray) {
    this.formInfoModel.initForm({
      components: [
        {
          name: '账户的角色',
          field: 'roleIds',
          select: {
            options: roleArray,
            multiple: true
          }
        },
        {
          name: '账户的用户组',
          field: 'groupIds',
          select: {
            options: groupArray,
            multiple: true
          }
        },
        {
          name: '备注',
          field: 'remark',
          input: {
            maxLength: 255
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
    this.basicManagementService.updateUserGroupRole(this.appId, this.userId, {
      groupIds: this.formInfoModel.formInfo.groupIds,
      roleIds: this.formInfoModel.formInfo.roleIds,
      remark: this.formInfoModel.formInfo.remark
    }).subscribe(res => {
      this.appService.setLoading(false);
      this.isVisible = false;
      this.message.success('操作成功！');
      this.tableFindModel.getData(null, null);
    });
  }

  /**
   * 初始化配置
   */
  public init(groupArray, roleArray) {
    const myThis = this;
    this.tableFindModel = new TableFindModel();
    const COMMON_TYPE = TableFindModel.COMMON_TYPE;
    this.tableFindModel.initConfig({
      tables: [
        {name: '账号', field: 'account', type: COMMON_TYPE.table.TEXT},
        {name: '手机号', field: 'phone', type: COMMON_TYPE.table.TEXT},
        {name: '邮箱', field: 'email', type: COMMON_TYPE.table.TEXT},
        {name: '备注', field: 'remark', type: COMMON_TYPE.table.TEXT},
        {name: '登录失败次数', field: 'failRecord', type: COMMON_TYPE.table.TEXT},
        {name: '初始化管理员', field: 'initialAdmin', type: COMMON_TYPE.table.DICT_DESC, dictType: 'whether'},
        {name: '授权时间', field: 'authorizeTime', type: COMMON_TYPE.table.TEXT, isSort: true},
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          operates: [
            {
              name: '编辑',
              field: 'edit',
              bAuthority: BUTTON_CODE.b_app_user_edit,
              confirmFun(dataModel, that) {
                myThis.basicManagementService.getUserGroupRole(myThis.appId, dataModel.data.id).subscribe(res => {
                  myThis.isVisible = true;
                  myThis.userId = dataModel.data.id;
                  myThis.formInfoModel.formInfo = {
                    groupIds: res['groupIds'],
                    roleIds: res['roleIds'],
                    remark: res['remark']
                  };
                });
              }
            },
            {
              name: '查看',
              field: 'details',
              bAuthority: BUTTON_CODE.b_app_user_details,
              confirmFun(dataModel, that) {
                myThis.router.navigate(['/home/basicManage/userManage/userDetails'], {
                  queryParams: {
                    appId: myThis.appId,
                    id: dataModel.data.id
                  }
                });
              }
            }
          ]
        }
      ],
      finds: [
        {
          name: '账号',
          field: 'account',
          input: {
            maxLength: 60
          }
        },
        {
          name: '手机号',
          field: 'phone',
          input: {
            maxLength: 60
          }
        },
        {
          name: '邮箱',
          field: 'email',
          input: {
            maxLength: 60
          }
        },
        {
          name: '备注',
          field: 'remark',
          input: {
            maxLength: 255
          }
        },
        {
          name: '角色',
          field: 'roleId',
          select: {
            options: roleArray
          }
        },
        {
          name: '用户组',
          field: 'groupId',
          select: {
            options: groupArray
          }
        }
      ],
      buttons: [],
      initDataFun(formInfo, sortPage, callback, that) {// 数据处理回调 config：配置，dataModels：数据，that：当前对象
        const req = {
          size: sortPage.size,
          likeFields: ['remark'],
          sortField: sortPage['sortField'],
          sortRule: sortPage['sortRule']
        };
        if (!req['sortField']) {
          req['sortField'] = 'authorizeTime';
        }
        if (!req['sortRule']) {
          req['sortRule'] = 'descend';
        }
        for (const find of that.config.finds) {
          req[find.field] = formInfo[find.field];
        }
        myThis.basicManagementService.getAppUserPage(myThis.appId, sortPage.current, req).subscribe(res => {
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
