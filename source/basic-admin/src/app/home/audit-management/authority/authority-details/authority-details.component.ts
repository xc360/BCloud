import {Component, OnInit} from '@angular/core';
import {FormInfoModel} from '../../../../../../projects/xc-common/src/common/form-info/form-info.model';
import {TableDetailModel} from '../../../../../../projects/xc-common/src/common/table-detail/table-detail.model';
import {ActivatedRoute, Router} from '@angular/router';
import {AppService} from '../../../../app.service';
import {NzMessageService} from 'ng-zorro-antd';
import {HomeService} from '../../../home.service';
import {AuditManagementService} from '../../audit-management.service';
import {AppManagementService} from '../../../app-management/app-management.service';

@Component({
  selector: 'app-authority-details',
  templateUrl: './authority-details.component.html',
  styleUrls: ['./authority-details.component.scss']
})
export class AuthorityDetailsComponent implements OnInit {
  public formInfoModel: FormInfoModel;
  public tableDetailModel: TableDetailModel;

  public dataModel: any;
  public id;

  public isVisible = false;
  public auditStatus;
  public revertUrl = '/home/auditManage/authority/list';

  public appList;
  public authorityAppList;

  constructor(public router: Router,
              public appService: AppService,
              public message: NzMessageService,
              public activatedRoute: ActivatedRoute,
              public homeService: HomeService,
              public appManagementService: AppManagementService,
              public auditManagementService: AuditManagementService) {
    this.formInfoModel = new FormInfoModel();
    this.tableDetailModel = new TableDetailModel();
    this.dataModel = {};
    this.activatedRoute.queryParams.subscribe(queryParams => {
      this.id = queryParams.id;
      this.auditStatus = queryParams.auditStatus;
      this.appManagementService.getAppList().subscribe(res => {
        this.appList = [];
        for (const row of res) {
          this.appList.push({
            name: row['name'],
            value: row['value']
          });
        }
        if (this.authorityAppList && this.appList) {
          this.getData();
        }
      });
      this.homeService.getCurrentUserAppList({}).subscribe(res => {
        this.authorityAppList = [];
        for (const data of res) {
          this.authorityAppList.push({
            name: data.appName,
            value: data.id
          });
        }
        if (this.authorityAppList && this.appList) {
          this.getData();
        }
      });
    });
  }

  ngOnInit() {
  }

  /**
   * 获取数据
   */
  public getData() {
    this.auditManagementService.getAuditAppAuthorize(this.id).subscribe(res => {
      this.dataModel = res;
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
        {name: '权限名称', field: 'authorityName', type: COMMON_DETAIL.block},
        {name: '权限标识', field: 'authorityCode', type: COMMON_DETAIL.block},
        {name: '权限类型', field: 'authorityType', type: COMMON_DETAIL.dict, dictType: 'authorityType'},
        {name: '权限应用', field: 'authorityAppId', type: COMMON_DETAIL.dict, options: this.appList},
        {name: '申请应用', field: 'appId', type: COMMON_DETAIL.dict, options: this.authorityAppList},
        {name: '审核信息', type: COMMON_DETAIL.header},
        {name: '申请时间', field: 'applyTime', type: COMMON_DETAIL.block},
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
    this.auditManagementService.updateAuditAppAuthorize(this.id, req).subscribe(res => {
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
