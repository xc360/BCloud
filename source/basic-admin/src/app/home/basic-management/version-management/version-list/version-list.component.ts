import {Component, OnInit, ViewChild} from '@angular/core';
import {
  FormInfoComponent,
  FormInfoModel,
  OverallSelectComponent,
  TableFindModel
} from '../../../../../../projects/xc-common/src/public-api';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {ActivatedRoute, Router} from '@angular/router';
import {AppService} from '../../../../app.service';
import {BUTTON_CODE} from '../../../../config/button-code';
import {HTTP_URLS} from '../../../../config/app-http.url';
import {HomeService} from '../../../home.service';
import {LoginTool, CommonTool, UploadTool, FileTool} from '@ccxc/tool';
import {BasicManagementService} from '../../basic-management.service';

@Component({
  selector: 'app-version-list',
  templateUrl: './version-list.component.html',
  styleUrls: ['./version-list.component.scss']
})
export class VersionListComponent implements OnInit {

  public tableFindModel: TableFindModel;
  public formInfoModel: FormInfoModel;
  public versionId;
  public isVisible = false;
  @ViewChild(FormInfoComponent, {static: true})
  public formInfoComponent: FormInfoComponent;
  public appId;
  @ViewChild(OverallSelectComponent, {static: true})
  public overallSelectComponent: OverallSelectComponent;

  constructor(public message: NzMessageService,
              public modalService: NzModalService,
              public basicManagementService: BasicManagementService,
              public router: Router,
              public homeService: HomeService,
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
    const that = this;
    this.formInfoModel.initForm({
      components: [
        {
          name: '版本类型',
          field: 'type',
          required: true,
          select: {
            dictType: 'versionType'
          }
        },
        {
          name: '版本',
          field: 'appVersion',
          required: true,
          input: {
            maxLength: 60
          }
        },
        {
          name: '更新内容',
          field: 'updateContent',
          required: true,
          textarea: {
            rows: 6,
            maxLength: 2000000
          }
        },
        {
          name: '下载地址',
          field: 'packageUrl',
          uploadInput: {
            isUpload: true,
            confirmFun(formInfo, component) {
              that.uploadPackage();
            }
          }
        },
        {
          name: '排序',
          field: 'seq',
          inputNumber: {
            max: 999999999
          }
        },
        {
          name: '是否强制升级',
          field: 'forceUpgrade',
          switch: {}
        },
        {
          name: '应用状态',
          field: 'status',
          switch: {}
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
      type: this.formInfoModel.formInfo.type,
      appVersion: this.formInfoModel.formInfo.appVersion,
      packageUrl: this.formInfoModel.formInfo.packageUrl,
      updateContent: this.formInfoModel.formInfo.updateContent,
      seq: this.formInfoModel.formInfo.seq,
      forceUpgrade: this.formInfoModel.formInfo.forceUpgrade ? '0' : '1',
      status: this.formInfoModel.formInfo.status ? '0' : '1'
    };
    if (!this.versionId) {
      this.basicManagementService.createAppVersion(this.appId, req).subscribe(res => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('创建成功！');
        this.tableFindModel.getData(null, null);
      });
    } else {
      this.basicManagementService.updateAppVersion(this.appId, this.versionId, req).subscribe(res => {
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
        {name: '版本类型', field: 'type', type: COMMON_TYPE.table.DICT_DESC, dictType: 'versionType'},
        {name: '版本号', field: 'appVersion', type: COMMON_TYPE.table.TEXT, isSort: true},
        {
          name: '安装包地址', field: 'packageUrl', type: COMMON_TYPE.table.TEXT, tdStyle: {
            width: '320px'
          }
        },
        {
          name: '文档地址', field: 'appDocUrl', type: COMMON_TYPE.table.JUMP,
          tdStyle: {
            width: '320px'
          }
        },
        {name: '排序', field: 'seq', type: COMMON_TYPE.table.TEXT, isSort: true},
        {name: '状态', field: 'status', type: COMMON_TYPE.table.DICT_DESC, dictType: 'effectStatus'},
        {name: '更新时间', field: 'updateTime', type: COMMON_TYPE.table.TEXT, isSort: true},
        {name: '创建时间', field: 'createTime', type: COMMON_TYPE.table.TEXT, isSort: true},
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          operates: [
            {
              name: '编辑',
              field: 'edit',
              bAuthority: BUTTON_CODE.b_app_version_edit,
              confirmFun(dataModel, that) {
                myThis.isVisible = true;
                myThis.versionId = dataModel.data.id;
                myThis.formInfoModel.formInfo = {
                  type: dataModel.data.type,
                  appVersion: dataModel.data.appVersion,
                  packageUrl: dataModel.data.packageUrl,
                  updateContent: dataModel.data.updateContent,
                  seq: dataModel.data.seq,
                  forceUpgrade: dataModel.data.forceUpgrade === '0',
                  status: dataModel.data['status'] === '0'
                };
              }
            },
            {
              name: '上移',
              field: 'up',
              bAuthority: BUTTON_CODE.b_app_version_up,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.basicManagementService.updateAppVersionUp(myThis.appId, dataModel.data.id).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.message.success('移动成功！');
                  myThis.tableFindModel.getData(null, null);
                });
              }
            },
            {
              name: '下移',
              field: 'down',
              bAuthority: BUTTON_CODE.b_app_version_down,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.basicManagementService.updateAppVersionDown(myThis.appId, dataModel.data.id).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.message.success('移动成功！');
                  myThis.tableFindModel.getData(null, null);
                });
              }
            },
            {
              name: '版本文档',
              field: 'version',
              bAuthority: BUTTON_CODE.b_app_version_doc,
              confirmFun(dataModel, that) {
                myThis.router.navigate(['/home/basicManage/versionManage/versionDoc'], {
                  queryParams: {
                    id: dataModel.data.id,
                    appId: myThis.appId,
                    field: 'docContent'
                  }
                });
              }
            },
            {
              name: '用户协议',
              field: 'userAgreement',
              bAuthority: BUTTON_CODE.b_app_version_doc,
              confirmFun(dataModel, that) {
                myThis.router.navigate(['/home/basicManage/versionManage/versionDoc'], {
                  queryParams: {
                    id: dataModel.data.id,
                    appId: myThis.appId,
                    field: 'userAgreement'
                  }
                });
              }
            },
            {
              name: '隐私协议',
              field: 'userAgreement',
              bAuthority: BUTTON_CODE.b_app_version_doc,
              confirmFun(dataModel, that) {
                myThis.router.navigate(['/home/basicManage/versionManage/versionDoc'], {
                  queryParams: {
                    id: dataModel.data.id,
                    appId: myThis.appId,
                    field: 'privacyAgreement'
                  }
                });
              }
            },
            {
              name: '删除',
              field: 'delete',
              bAuthority: BUTTON_CODE.b_app_version_delete,
              confirmFun(dataModel, that) {
                myThis.modalService.confirm({
                  nzTitle: '确认要删除吗?',
                  nzContent: '<b style="color: red;">删除后数据无法找回哦！</b>',
                  nzOkText: '确认',
                  nzOkType: 'danger',
                  nzOnOk: () => {
                    myThis.appService.setLoading(true);
                    myThis.basicManagementService.deleteAppVersion(myThis.appId, dataModel.data.id).subscribe(res => {
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
          name: '版本类型',
          field: 'type',
          select: {
            dictType: 'versionType'
          }
        },
        {
          name: '版本号',
          field: 'appVersion',
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
          bAuthority: BUTTON_CODE.b_app_version_add,
          confirmFun(formInfo, that) {
            myThis.isVisible = true;
            myThis.versionId = null;
            myThis.formInfoModel.formInfo = {status: true};
          }
        }
      ],
      initDataFun(formInfo, sortPage, callback, that) {// 数据处理回调 config：配置，dataModels：数据，that：当前对象
        const req = {
          size: sortPage.size,
          likeFields: ['appVersion'],
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
        myThis.basicManagementService.getAppVersionPage(myThis.appId, sortPage.current, req).subscribe(res => {
          const array = res['resData'].map((data) => {
            data['appDocUrl'] = CommonTool.analysisUrl(HTTP_URLS.appDoc, {appId: myThis.appId, versionId: data.id});
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
   * 上传软件包
   */
  public uploadPackage() {
    const $this = this;
    $this.formInfoModel.formInfo.packageUrl = '';
    const input = document.createElement('input');
    input.style.display = 'none';
    input.type = 'file';
    input.setAttribute('multiple', '');
    const event = document.createEvent('MouseEvents');
    event.initEvent('click', true, true); // 这里的click可以换成你想触发的行为
    input.dispatchEvent(event); // 这里的clickME可以换成你想触发行为的DOM结点
    input.onchange = (e) => {
      $this.appService.setLoading(true, '上传中...');
      const file = e.target['files'][0];
      const tokenInfo = LoginTool.getToken();
      $this.uploadFolderFile(file, {
        folderPath: '/app/' + tokenInfo.userId + '/package',
        fileName: FileTool.randomFileName(file.name)
      }, (downloadUrl) => {
        $this.appService.setLoading(false);
        $this.formInfoModel.formInfo.packageUrl = downloadUrl;
      });
    };
  }

  /**
   * 上传文件数据处理
   */
  public uploadFolderFile(file, config, fun) {
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
      xcUploadTool.start().then(httpRes => {
        fun(httpRes.downloadUrl);
      }).catch(() => {
        myThis.appService.setLoading(false);
        myThis.message.error('文件上传失败！');
      });
    });
  }
}
