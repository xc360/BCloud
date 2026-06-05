import {Component, OnInit} from '@angular/core';
import {TableDetailModel} from '../../../../../../projects/xc-common/src/common/table-detail/table-detail.model';
import {ActivatedRoute, Router} from '@angular/router';
import {AppService} from '../../../../app.service';
import {NzMessageService} from 'ng-zorro-antd';
import {HomeService} from '../../../home.service';
import {AuditManagementService} from '../../audit-management.service';
import {FormInfoModel} from '../../../../../../projects/xc-common/src/common/form-info/form-info.model';
import {CommonTool, LoginTool} from '@ccxc/tool';
import {HTTP_URLS} from '../../../../config/app-http.url';
import {BUTTON_CODE} from '../../../../config/button-code';

@Component({
  selector: 'app-app-details',
  templateUrl: './app-details.component.html',
  styleUrls: ['./app-details.component.scss']
})
export class AppDetailsComponent implements OnInit {

  public formInfoModel: FormInfoModel;
  public tableDetailModel: TableDetailModel;

  public dataModel: any;
  public id;

  public isVisible = false;
  public auditStatus;
  public revertUrl = '/home/auditManage/app/list';

  public LoginTool = LoginTool;
  public BUTTON_CODE = BUTTON_CODE;

  constructor(public router: Router,
              public appService: AppService,
              public message: NzMessageService,
              public activatedRoute: ActivatedRoute,
              public homeService: HomeService,
              public auditManagementService: AuditManagementService) {
    this.formInfoModel = new FormInfoModel();
    this.tableDetailModel = new TableDetailModel();
    this.dataModel = {};
    this.activatedRoute.queryParams.subscribe(queryParams => {
      this.id = queryParams.id;
      this.auditStatus = queryParams.auditStatus;
      this.getData();
    });
  }

  ngOnInit() {
  }

  /**
   * 获取数据
   */
  public getData() {
    this.auditManagementService.getManageAuditApp(this.id).subscribe(res => {
      this.dataModel = res.appDto;
      this.dataModel['appDocUrl'] = CommonTool.analysisUrl(HTTP_URLS.appDoc, {appId: this.dataModel.id});
      this.tableDetailModel.setFormInfo(this.dataModel);
      this.initDetail(this.dataModel);
    });
  }

  /**
   * 初始化详情
   */
  public initDetail(data) {
    const COMMON_DETAIL = TableDetailModel.COMMON_DETAIL;
    this.tableDetailModel.setConfig({
      modules: [
        {name: '基础信息', type: COMMON_DETAIL.header},
        {name: '应用名称', field: 'appName', type: COMMON_DETAIL.block},
        {name: '用户名称', field: 'nickName', type: COMMON_DETAIL.block},
        {name: '状态', field: 'status', type: COMMON_DETAIL.block},
        {
          name: 'TOKEN效期', field: 'tokenValidTime', type: COMMON_DETAIL.block,
          blockStyle: {
            width: '260px'
          }
        },
        {name: '刷新TOKEN效期', field: 'refreshTokenValidTime', type: COMMON_DETAIL.block},
        {name: '创建时间', field: 'createTime', type: COMMON_DETAIL.block},
        {name: 'logo地址', field: 'logoUrl', type: COMMON_DETAIL.lineImg},
        {name: '开关', type: COMMON_DETAIL.header},
        {name: '是否公开', field: 'isOpen', type: COMMON_DETAIL.dict, dictType: 'whether'},
        {name: '同时在线', field: 'isCoexist', type: COMMON_DETAIL.dict, dictType: 'whether'},
        {name: '显示用户权限', field: 'showUserAuthority', type: COMMON_DETAIL.dict, dictType: 'whether'},
        {name: '邮箱登录', field: 'openEmailLogin', type: COMMON_DETAIL.dict, dictType: 'whether'},
        {name: '手机登录', field: 'openPhoneLogin', type: COMMON_DETAIL.dict, dictType: 'whether'},
        {name: '手机找回密码', field: 'openPhoneForget', type: COMMON_DETAIL.dict, dictType: 'whether'},
        {name: '邮箱找回密码', field: 'openEmailForget', type: COMMON_DETAIL.dict, dictType: 'whether'},
        {name: '手机注册', field: 'openPhoneRegister', type: COMMON_DETAIL.dict, dictType: 'whether'},
        {name: '邮箱注册', field: 'openEmailRegister', type: COMMON_DETAIL.dict, dictType: 'whether'},
        {name: '地址', type: COMMON_DETAIL.header},
        {name: '应用域名', field: 'domain', type: COMMON_DETAIL.block},
        {name: '首页地址', field: 'homeUrl', type: COMMON_DETAIL.line},
        {name: '刷新地址', field: 'refreshUrl', type: COMMON_DETAIL.line},
        {name: '文档地址', field: 'appDocUrl', type: COMMON_DETAIL.line},
        {name: '审核信息', type: COMMON_DETAIL.header},
        {name: '发布时间', field: 'applyTime', type: COMMON_DETAIL.block},
        {name: '审核状态', field: 'auditStatus', type: COMMON_DETAIL.dict, dictType: 'auditStatus'},
        {name: '申请原因', field: 'reason', type: COMMON_DETAIL.block}
      ]
    });
  }


  /**
   * 审核
   */
  public openAudit() {
    this.isVisible = true;
    const auditStatus = this.dataModel.auditStatus;
    if (this.dataModel.auditStatus !== '1') {
      this.initForm({whether: '1', auditStatus, type: 'audit'});
    } else {
      this.initForm({whether: '0', auditStatus, type: 'audit'});
    }
  }

  /**
   * 初始化表单
   */
  initForm(formInfo) {
    this.formInfoModel.initForm({
      components: [
        {
          name: '是否通过',
          field: 'whether',
          required: true,
          hidden: formInfo.auditStatus !== '1',
          select: {
            dictType: 'whether'
          }
        },
        {
          name: '拒绝原因(拒绝时填写)',
          field: 'reason',
          required: formInfo.whether === '1',
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
    const whether = this.formInfoModel.formInfo.whether;
    const reason = this.formInfoModel.formInfo.reason;
    if (whether === '1' && !reason) {
      this.message.warning('拒绝原因不能为空！');
      return;
    }
    const req = {
      auditStatus: whether === '1' ? '3' : '2',
      reason
    };
    this.appService.setLoading(true);
    this.auditManagementService.updateManageAuditApp(this.id, req).subscribe(res => {
      this.appService.setLoading(false);
      this.isVisible = false;
      this.message.success('操作成功！');
      this.revert();
    });
  }

  /**
   * 返回
   */
  revert() {
    this.router.navigateByUrl(this.revertUrl + '?auditStatus=' + this.auditStatus);
  }

}
