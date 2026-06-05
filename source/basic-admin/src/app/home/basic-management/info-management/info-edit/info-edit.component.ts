import {Component, OnInit} from '@angular/core';
import {ArrayConfig, FormInfoModel} from '../../../../../../projects/xc-common/src/public-api';
import {BasicManagementService} from '../../basic-management.service';
import {AppService} from '../../../../app.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-info-edit',
  templateUrl: './info-edit.component.html',
  styleUrls: ['./info-edit.component.scss']
})
export class InfoEditComponent implements OnInit {

  public formInfoModel: FormInfoModel;
  public appId: any;
  public infoList;

  constructor(public basicManagementService: BasicManagementService,
              public message: NzMessageService,
              public modalService: NzModalService,
              public activatedRoute: ActivatedRoute,
              public appService: AppService,
              public router: Router) {
    this.formInfoModel = new FormInfoModel();
    this.activatedRoute.queryParams.subscribe(queryParams => {
      this.appId = queryParams.appId;
    });
  }

  ngOnInit(): void {
    this.initForm();
  }

  /**
   * 初始化表单
   */
  initForm() {
    const components: Array<ArrayConfig> = [];
    const formInfo = {};
    this.basicManagementService.getAppInfoList(this.appId, {}).subscribe(res => {
      this.infoList = res;
      for (const data of res) {
        components.push({
          name: data['describe'],
          field: data['key'],
          textarea: {rows: 4, maxLength: 1000}
        });
        formInfo[data['key']] = data['value'];
      }
      this.formInfoModel.initForm({components}, formInfo);
    });
  }

  /**
   * 提交
   */
  submit() {
    for (const data of this.infoList) {
      data['value'] = this.formInfoModel.formInfo[data['key']];
    }
    const req = this.infoList;
    for (const r of req) {
      if (!r['key'] || !r['value'] || !r['type'] || !r['status']) {
        this.message.success('数据格式错误！');
        return;
      }
    }
    this.appService.setLoading(true);
    this.basicManagementService.createAppInfoList(this.appId, req).subscribe(res => {
      this.appService.setLoading(false);
      this.message.success('保存成功！');
    });
  }

  /**
   * 返回
   */
  public revert() {
    this.router.navigateByUrl('/home/basicManage/infoManage/infoList?appId=' + this.appId);
  }
}
