import {Component, OnInit} from '@angular/core';
import {TableFindModel} from '../../../../../projects/xc-common/src/public-api';
import {NzMessageService} from 'ng-zorro-antd';
import {AppService} from '../../../app.service';
import {BUTTON_CODE} from '../../../config/button-code';
import {AuthorizeManagementService} from '../authorize-management.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-authorize-list',
  templateUrl: './authorize-list.component.html',
  styleUrls: ['./authorize-list.component.scss']
})
export class AuthorizeListComponent implements OnInit {
  public tableFindModel: TableFindModel;

  constructor(public authorizeManagementService: AuthorizeManagementService,
              public appService: AppService,
              public router: Router,
              public message: NzMessageService) {
    this.init();
  }

  ngOnInit() {
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
        {name: '应用名称', field: 'appName', type: COMMON_TYPE.table.TEXT},
        {name: '授权时间', field: 'authorizeTime', type: COMMON_TYPE.table.TEXT, isSort: true},
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          operates: [
            {
              name: '详情',
              field: 'details',
              bAuthority: BUTTON_CODE.b_user_authorize_details,
              confirmFun(dataModel, that) {
                myThis.router.navigate(['/home/authorizeManage/authorizeAuthorityList'], {
                  queryParams: {
                    id: dataModel.data.id
                  }
                });
              }
            },
            {
              name: '取消授权',
              field: 'delete',
              bAuthority: BUTTON_CODE.b_user_authorize_delete,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.authorizeManagementService.deleteCurrentUserAuthorize(dataModel.data.id).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.message.success('取消成功！');
                  myThis.tableFindModel.getData(null, null);
                });
              }
            }]
        }
      ],
      finds: [
        {
          name: '应用名称', field: 'appName',
          input: {
            maxLength: 60
          }
        }
      ],
      buttons: [],
      initDataFun(formInfo, sortPage, callback, that) {// 数据处理回调 config：配置，dataModels：数据，that：当前对象
        const req = {
          appName: formInfo['appName'],
          size: sortPage.size,
          likeFields: ['appName'],
          sortField: sortPage['sortField'],
          sortRule: sortPage['sortRule']
        };
        myThis.authorizeManagementService.getCurrentUserAuthorizePage(sortPage.current, req).subscribe(res => {
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

