import {Component, OnInit} from '@angular/core';
import {TableDetailModel} from '../../../../../../projects/xc-common/src/public-api';
import {HomeService} from '../../../home.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {AppService} from '../../../../app.service';
import {BasicManagementService} from '../../basic-management.service';
import {ActivatedRoute} from '@angular/router';
import {CommonTool} from '@ccxc/tool';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.scss']
})
export class UserDetailsComponent implements OnInit {

  public tableDetailModel: TableDetailModel;
  public defaultPortrait = './assets/images/user.png';
  public appId: string;
  public userId: string;

  constructor(public message: NzMessageService,
              public appService: AppService,
              public homeService: HomeService,
              public basicManagementService: BasicManagementService,
              public activatedRoute: ActivatedRoute,
              public modalService: NzModalService) {
    this.tableDetailModel = new TableDetailModel();
    this.activatedRoute.queryParams.subscribe(queryParams => {
      this.appId = queryParams.appId;
      this.userId = queryParams.id;
    });
    const COMMON_DETAIL = TableDetailModel.COMMON_DETAIL;
    this.tableDetailModel.setConfig({
      revertName: '返回',
      revertUrl: '/home/basicManage/userManage?appId=' + this.appId,
      modules: [
        {name: '基础信息', type: COMMON_DETAIL.header},
        {name: '账号', field: 'account', type: COMMON_DETAIL.block},
        {name: '邮箱', field: 'email', type: COMMON_DETAIL.block},
        {name: '手机号', field: 'phone', type: COMMON_DETAIL.block},
        {name: '个人信息', type: COMMON_DETAIL.header, blockStyle: {marginTop: '10px'}},
        {name: '用户昵称', field: 'nickName', type: COMMON_DETAIL.block},
        {name: '性别', field: 'sex', type: COMMON_DETAIL.dict, dictType: 'sex'},
        {name: '生日', field: 'birthday', type: COMMON_DETAIL.date, dateFormat: 'yyyy-MM-dd'},
        {name: '地址', field: 'regionName', type: COMMON_DETAIL.block},
        {name: '头像', field: 'portrait', type: COMMON_DETAIL.lineImg},
        {name: '个人说明', field: 'explain', type: COMMON_DETAIL.line}
      ]
    });
    this.getData();
  }

  ngOnInit() {
  }

  /**
   * 获取数据
   */
  getData() {
    this.basicManagementService.getAppUser(this.appId, this.userId).subscribe(res => {
      this.tableDetailModel.setFormInfo(res);
      const formInfo = this.tableDetailModel.formInfo;
      this.initArea(formInfo);
      if (!this.tableDetailModel.formInfo.portrait) {
        this.tableDetailModel.formInfo.portrait = this.defaultPortrait;
      }
      formInfo.birthday = formInfo.birthday ? CommonTool.turnDate(formInfo.birthday) : null;
    });
  }

  /**
   * 初始化区域
   */
  initArea(form) {
    if (form['region']) {
      this.homeService.getMyAppAreaNodeList(form['region']).subscribe(res => {
        this.tableDetailModel.formInfo['regionName'] = res.map(e => e.name).join('');
      });
    }
  }
}
