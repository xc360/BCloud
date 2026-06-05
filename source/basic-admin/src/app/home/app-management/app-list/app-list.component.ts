import {Component, OnInit, ViewChild} from '@angular/core';
import {
  ArrayConfig,
  FormInfoComponent,
  FormInfoModel,
  TableFindModel
} from '../../../../../projects/xc-common/src/public-api';
import {AppManagementService} from '../app-management.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Router} from '@angular/router';
import {AppService} from '../../../app.service';
import {BUTTON_CODE} from '../../../config/button-code';
import {HomeService} from '../../home.service';
import {CommonTool, FileTool, LoginTool, UploadTool} from '@ccxc/tool';
import {FILE_FORMAT} from '../../../config/enum';

@Component({
  selector: 'app-app-list',
  templateUrl: './app-list.component.html',
  styleUrls: ['./app-list.component.scss']
})
export class AppListComponent implements OnInit {

  public tableFindModel: TableFindModel;
  public secretInfo: { isVisible: boolean, id: string, appId: string, appSecret: string };
  public createApp: {
    isVisible: boolean,
    appId: string;
  };
  public formInfoModel: FormInfoModel;
  @ViewChild(FormInfoComponent, {static: true})
  public formInfoComponent: FormInfoComponent;
  public formInfoModel1: FormInfoModel;
  public formInfoModel2: FormInfoModel;
  public isVisible = false;
  public isApplyVisible = false;
  public groupList = [];

  constructor(public appManagementService: AppManagementService,
              public modalService: NzModalService,
              public router: Router,
              public appService: AppService,
              public homeService: HomeService,
              public message: NzMessageService) {
    this.formInfoModel = new FormInfoModel();
    this.formInfoModel1 = new FormInfoModel();
    this.formInfoModel2 = new FormInfoModel();
    this.createApp = {isVisible: false, appId: ''};
    this.secretInfo = {isVisible: false, id: '', appId: '', appSecret: ''};
    this.appManagementService.getMyAppUserGroupList().subscribe(res => {
      const array = [{name: '无', value: ''}];
      for (const row of res) {
        array.push({
          name: row['name'],
          value: row['id']
        });
      }
      this.groupList = array;
      // 获取用户token信息
      this.init();
    });
  }

  ngOnInit() {
  }

  /**
   * 初始化表单
   */
  initForm1(formInfo) {
    this.formInfoModel1.initForm({
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
   * 初始化表单
   */
  initForm(form) {
    const that = this;
    const tokenInfo = LoginTool.getToken();
    this.formInfoModel.initForm({
      components: [
        {
          name: '应用名称',
          field: 'appName',
          required: true,
          input: {
            maxLength: 60
          }
        },
        {
          name: '应用域名',
          field: 'domain',
          input: {
            maxLength: 255
          }
        },
        {
          name: '应用IP',
          field: 'localIp',
          input: {
            maxLength: 255
          }
        },
        {
          name: '所属组',
          field: 'groupId',
          select: {
            options: this.groupList,
            disabled: this.createApp.appId ? form.userId !== tokenInfo.userId : false,
            openFun(formInfo, component, $event) {
              if ($event) {
                that.appManagementService.getMyAppUserGroupList().subscribe(res => {
                  const array = [{name: '无', value: ''}];
                  for (const row of res) {
                    array.push({
                      name: row['name'],
                      value: row['id']
                    });
                  }
                  component.select.options = array;
                });
              }
            }
          }
        },
        {
          name: '访问地址',
          field: 'homeUrl',
          input: {
            maxLength: 255
          }
        },
        {
          name: '刷新地址',
          field: 'refreshUrl',
          input: {
            maxLength: 255
          }
        },
        {
          name: 'TOKEN有效期',
          field: 'tokenValidTime',
          required: true,
          inputNumber: {
            step: 1,
            min: 0
          }
        },
        {
          name: '刷新TOKEN有效期',
          field: 'refreshTokenValidTime',
          required: true,
          inputNumber: {
            step: 1,
            min: 0
          }
        },
        {
          name: '签名有效时间',
          field: 'signValidTime',
          required: true,
          inputNumber: {
            step: 1,
            min: 0
          }
        },
        {
          name: 'logo地址',
          field: 'logoUrl',
          uploadPicture: {
            loading: false,
            clickFun(formInfo, component) {
              that.uploadLogo(component);
            }
          }
        },
        {
          name: '是否公开',
          field: 'isOpen',
          switch: {}
        },
        {
          name: '允许同时在线',
          field: 'isCoexist',
          switch: {}
        },
        {
          name: '显示用户信息权限',
          field: 'showUserAuthority',
          switch: {}
        },
        {
          name: '是否打开邮箱登录',
          field: 'openEmailLogin',
          switch: {}
        },
        {
          name: '是否打开手机登录',
          field: 'openPhoneLogin',
          switch: {}
        },
        {
          name: '是否打开手机找回密码',
          field: 'openPhoneForget',
          switch: {}
        },
        {
          name: '是否打开邮箱找回密码',
          field: 'openEmailForget',
          switch: {}
        },
        {
          name: '是否打开手机注册',
          field: 'openPhoneRegister',
          switch: {}
        },
        {
          name: '是否打开邮箱注册',
          field: 'openEmailRegister',
          switch: {}
        },
        {
          name: '应用状态',
          field: 'status',
          switch: {}
        }
      ]
    }, form);
  }

  /**
   * 申请发布
   */
  confirmOk() {
    const req = {
      reason: this.formInfoModel2.formInfo.reason,
      auditStatus: '1'
    };
    this.appService.setLoading(true);
    this.appManagementService.updateCurrentUserApp(this.formInfoModel2.formInfo.appId, req).subscribe(res => {
      this.appService.setLoading(false);
      this.isApplyVisible = false;
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
        {name: '应用ID', field: 'appId', type: COMMON_TYPE.table.TEXT},
        {name: '应用名称', field: 'appName', type: COMMON_TYPE.table.TEXT},
        {name: '用户组', field: 'groupId', type: COMMON_TYPE.table.DICT_DESC, options: this.groupList},
        {name: '应用状态', field: 'status', type: COMMON_TYPE.table.DICT_DESC, dictType: 'effectStatus'},
        {name: '审核状态', field: 'auditStatus', type: COMMON_TYPE.table.DICT_DESC, dictType: 'auditStatus'},
        {name: '修改时间', field: 'updateTime', type: COMMON_TYPE.table.TEXT, isSort: true},
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          initFun(dataModel, table, that) {
            dataModel['release'] = dataModel.data['auditStatus'] === '0' || dataModel.data['auditStatus'] === '3';
            dataModel['cancelRelease'] = dataModel.data['auditStatus'] === '1' || dataModel.data['auditStatus'] === '2';
            dataModel['reason'] = dataModel.data['auditStatus'] === '3';
            dataModel['refresh'] = !!dataModel.data['refreshUrl'];
          },
          operates: [
            {
              name: '编辑',
              field: 'edit',
              bAuthority: BUTTON_CODE.b_app_edit,
              confirmFun(dataModel, that) {
                myThis.createApp = {
                  isVisible: true,
                  appId: dataModel.data.id
                };
                myThis.initForm({
                  appName: dataModel.data.appName,
                  domain: dataModel.data.domain,
                  localIp: dataModel.data.localIp,
                  homeUrl: dataModel.data.homeUrl,
                  refreshUrl: dataModel.data.refreshUrl,
                  logoUrl: dataModel.data.logoUrl,
                  groupId: dataModel.data.groupId,
                  userId: dataModel.data.userId,
                  isOpen: dataModel.data.isOpen === '0',
                  isCoexist: dataModel.data.isCoexist === '0',
                  tokenValidTime: dataModel.data.tokenValidTime,
                  refreshTokenValidTime: dataModel.data.refreshTokenValidTime,
                  signValidTime: dataModel.data.signValidTime,
                  openEmailLogin: dataModel.data.openEmailLogin === '0',
                  openPhoneLogin: dataModel.data.openPhoneLogin === '0',
                  openPhoneForget: dataModel.data.openPhoneForget === '0',
                  openEmailForget: dataModel.data.openEmailForget === '0',
                  openPhoneRegister: dataModel.data.openPhoneRegister === '0',
                  openEmailRegister: dataModel.data.openEmailRegister === '0',
                  showUserAuthority: dataModel.data.showUserAuthority === '0',
                  status: dataModel.data['status'] === '0'
                });
              }
            },
            {
              name: '查看秘钥',
              field: 'lookSecret',
              bAuthority: BUTTON_CODE.b_app_query_secret,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.appManagementService.getAppSecret(dataModel.data['id']).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.secretInfo = {
                    appId: dataModel.data['appId'],
                    isVisible: true,
                    id: dataModel.data['id'],
                    appSecret: res['appSecret']
                  };
                });
              }
            },
            {
              name: '申请权限',
              field: 'relation',
              bAuthority: BUTTON_CODE.b_app_apply_authorize,
              confirmFun(dataModel, that) {
                myThis.router.navigateByUrl('/home/appManage/appAuthorityList?id=' + dataModel.data.id);
              }
            },
            {
              name: '发布',
              field: 'release',
              bAuthority: BUTTON_CODE.b_app_release,
              confirmFun(dataModel, that) {
                myThis.formInfoModel2.initForm({
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
                }, {appId: dataModel.data.id});
                myThis.isApplyVisible = true;
              }
            },
            {
              name: '拒绝原因', field: 'reason',
              confirmFun(dataModel, that) {
                myThis.initForm1({reason: dataModel.data.reason});
                myThis.isVisible = true;
              }
            },
            {
              name: '取消发布',
              field: 'cancelRelease',
              bAuthority: BUTTON_CODE.b_app_cancel_release,
              confirmFun(dataModel, that) {
                myThis.modalService.confirm({
                  nzTitle: '确认要取消发布吗?',
                  nzContent: '<b style="color: red;">取消发布后其他应用将无法调用权限！</b>',
                  nzOkText: '确认',
                  nzOkType: 'danger',
                  nzOnOk: () => {
                    const req = {
                      auditStatus: '0'
                    };
                    myThis.appService.setLoading(true);
                    myThis.appManagementService.updateCurrentUserApp(dataModel.data.id, req).subscribe(res => {
                      myThis.appService.setLoading(false);
                      myThis.isVisible = false;
                      myThis.message.success('操作成功！');
                      myThis.tableFindModel.getData(null, null);
                    });
                  },
                  nzCancelText: '取消'
                });
              }
            },
            // },
            {
              name: '刷新配置',
              field: 'refresh',
              bAuthority: BUTTON_CODE.b_app_refresh,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.appManagementService.updateCurrentUserAppRefresh(dataModel.data.id).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.message.success('配置刷新成功！');
                });
              }
            },
            {
              name: '删除',
              field: 'delete',
              bAuthority: BUTTON_CODE.b_app_delete,
              confirmFun(dataModel, that) {
                myThis.deleteApp(dataModel);
              }
            }
          ]
        }
      ],
      finds: [
        {
          name: '应用ID', field: 'appId', input: {
            maxLength: 60
          }
        },
        {
          name: '应用名称', field: 'appName', input: {
            maxLength: 60
          }
        }
      ],
      buttons: [
        {
          name: '新增',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_add,
          confirmFun(formInfo, that) {
            myThis.createApp = {
              isVisible: true,
              appId: null
            };
            myThis.initForm({
              appName: '',
              domain: '',
              localIp: '',
              homeUrl: '',
              refreshUrl: '',
              logoUrl: '',
              groupId: null,
              openEmailLogin: true,
              openPhoneLogin: true,
              openPhoneForget: true,
              openEmailForget: true,
              openPhoneRegister: true,
              openEmailRegister: true,
              showUserAuthority: true,
              status: true,
              isCoexist: true,
              tokenValidTime: 3600000,
              refreshTokenValidTime: 7200000,
              signValidTime: 1800000
            });
          }
        }
      ],
      initDataFun(formInfo, sortPage, callback, that) {// 数据处理回调 config：配置，dataModels：数据，that：当前对象
        const req = {
          size: sortPage.size,
          likeFields: ['appName'],
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
        myThis.appManagementService.getCurrentUserAppPage(sortPage.current, req).subscribe(res => {
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
   * 上传logo
   */
  public uploadLogo(component) {
    const $this = this;
    $this.formInfoModel.formInfo.logoUrl = '';
    const input = document.createElement('input');
    input.style.display = 'none';
    input.type = 'file';
    input.setAttribute('multiple', '');
    const event = document.createEvent('MouseEvents');
    event.initEvent('click', true, true); // 这里的click可以换成你想触发的行为
    input.dispatchEvent(event); // 这里的clickME可以换成你想触发行为的DOM结点
    input.onchange = (e) => {
      const file = e.target['files'][0];
      let bool = false;
      for (const suffix of FILE_FORMAT.imageFormat) {
        if (file.type.indexOf(suffix) !== -1) {
          bool = true;
        }
      }
      if (!bool) {
        $this.message.error('文件只能为图片格式！');
        return;
      }
      const tokenInfo = LoginTool.getToken();
      $this.appService.setLoading(true, '上传中...');
      $this.uploadFolderFile(file, {
        folderPath: '/app/' + tokenInfo.userId + '/logo',
        fileName: FileTool.randomFileName(file.name)
      }, component, (downloadUrl) => {
        $this.appService.setLoading(false);
        $this.formInfoModel.formInfo.logoUrl = downloadUrl;
      });
    };
  }

  /**
   * 上传文件数据处理
   */
  public uploadFolderFile(file, config, component: ArrayConfig, fun) {
    const myThis = this;
    this.homeService.getMyAppUploadFileSign().subscribe(res => {
      const xcUploadTool = new UploadTool(null);
      xcUploadTool.setConfig({
        uploadUrl: res.uploadUrl, // 上传的url地址
        resumed: '0',
        sign: res,
        md5Loading: null,
        uploadLoading: null
      });
      xcUploadTool.setFileData({file, fileName: config.fileName, folderPath: config.folderPath});
      if (component) {
        component.uploadPicture.loading = true;
      }
      xcUploadTool.start().then(httpRes => {
        if (component) {
          component.uploadPicture.loading = false;
        }
        fun(httpRes.downloadUrl);
      }).catch(() => {
        if (component) {
          component.uploadPicture.loading = false;
        }
        myThis.appService.setLoading(false);
        myThis.message.error('文件上传失败！');
      });
    });
  }

  /**
   * 更新秘钥
   */
  public updateSecret(secretInfo) {
    this.modalService.confirm({
      nzTitle: '确定要更新秘钥吗?',
      nzContent: '<b style="color: red;">更新秘钥后无法回退原秘钥哦！</b>',
      nzOkText: '确认',
      nzOkType: 'danger',
      nzOnOk: () => {
        this.appService.setLoading(true);
        this.appManagementService.updateAppSecret(secretInfo['id']).subscribe(res => {
          this.appService.setLoading(false);
          this.message.success('更新秘钥成功！');
          secretInfo['appSecret'] = res['appSecret'];
        });
      },
      nzCancelText: '取消'
    });
  }

  /**
   * 删除
   * 参数 data 数据
   */
  public deleteApp(dataModel) {
    this.modalService.confirm({
      nzTitle: '确认要删除应用吗?',
      nzContent: '<b style="color: red;">应用删除后关联的所有数据都将删除！</b>',
      nzOkText: '确认',
      nzOkType: 'danger',
      nzOnOk: () => {
        this.appService.setLoading(true);
        this.appManagementService.deleteCurrentUserApp(dataModel.data.id).subscribe(res => {
          this.appService.setLoading(false);
          this.message.success('应用删除成功！');
          this.tableFindModel.getData(null, null);
        });
      },
      nzCancelText: '取消'
    });
  }

  /**
   * 创建应用提交方法
   */
  public createAppOk() {
    this.appService.setLoading(true);
    const req = {
      appName: this.formInfoModel.formInfo.appName,
      domain: this.formInfoModel.formInfo.domain,
      localIp: this.formInfoModel.formInfo.localIp,
      homeUrl: this.formInfoModel.formInfo.homeUrl,
      refreshUrl: this.formInfoModel.formInfo.refreshUrl,
      logoUrl: this.formInfoModel.formInfo.logoUrl,
      groupId: this.formInfoModel.formInfo.groupId,
      isCoexist: this.formInfoModel.formInfo.isCoexist ? '0' : '1',
      isOpen: this.formInfoModel.formInfo.isOpen ? '0' : '1',
      tokenValidTime: this.formInfoModel.formInfo.tokenValidTime,
      refreshTokenValidTime: this.formInfoModel.formInfo.refreshTokenValidTime,
      signValidTime: this.formInfoModel.formInfo.signValidTime,
      openEmailLogin: this.formInfoModel.formInfo.openEmailLogin ? '0' : '1',
      openPhoneLogin: this.formInfoModel.formInfo.openPhoneLogin ? '0' : '1',
      openPhoneForget: this.formInfoModel.formInfo.openPhoneForget ? '0' : '1',
      openEmailForget: this.formInfoModel.formInfo.openEmailForget ? '0' : '1',
      openPhoneRegister: this.formInfoModel.formInfo.openPhoneRegister ? '0' : '1',
      openEmailRegister: this.formInfoModel.formInfo.openEmailRegister ? '0' : '1',
      showUserAuthority: this.formInfoModel.formInfo.showUserAuthority ? '0' : '1',
      status: this.formInfoModel.formInfo.status ? '0' : '1'
    };
    if (!this.createApp.appId) {
      this.appManagementService.createCurrentUserApp(req).subscribe(res => {
        this.appService.setLoading(false);
        this.createApp.isVisible = false;
        this.message.success('应用创建成功！');
        this.tableFindModel.getData(null, null);
      });
    } else {
      this.appManagementService.updateCurrentUserApp(this.createApp.appId, req).subscribe(res => {
        this.appService.setLoading(false);
        this.createApp.isVisible = false;
        this.message.success('应用修改成功！');
        this.tableFindModel.getData(null, null);
      });
    }
  }
}
