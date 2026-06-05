import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {TableFindModel} from '../../../../../projects/xc-common/src/common/table-find/table-find.model';
import {AuthorizeManagementService} from '../authorize-management.service';
import {AppService} from '../../../app.service';
import {NzMessageService} from 'ng-zorro-antd';
import {AppManagementService} from '../../app-management/app-management.service';
import {BUTTON_CODE} from '../../../config/button-code';

@Component({
  selector: 'app-authorize-authority-list',
  templateUrl: './authorize-authority-list.component.html',
  styleUrls: ['./authorize-authority-list.component.scss']
})
export class AuthorizeAuthorityListComponent implements OnInit {

  public tableFindModel: TableFindModel;
  private authorizeId;
  public appList;

  constructor(public authorizeManagementService: AuthorizeManagementService,
              public appService: AppService,
              public router: Router,
              public activatedRoute: ActivatedRoute,
              public appManagementService: AppManagementService,
              public message: NzMessageService) {
    this.activatedRoute.queryParams.subscribe(queryParams => {
      this.authorizeId = queryParams.id;
      this.appManagementService.getAppList().subscribe(res => {
        this.appList = [];
        for (const row of res) {
          this.appList.push({
            name: row['name'],
            value: row['value']
          });
        }
        this.init();
      });
    });
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
        {name: '权限名称', field: 'name', type: COMMON_TYPE.table.TEXT},
        {name: '权限标识', field: 'code', type: COMMON_TYPE.table.TEXT},
        {name: '应用主键', field: 'appId', type: COMMON_TYPE.table.DICT_DESC, options: myThis.appList},
        {name: '状态', field: 'status', type: COMMON_TYPE.table.DICT_DESC, dictType: 'effectStatus'},
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          operates: [
            {
              name: '取消授权',
              field: 'delete',
              bAuthority: BUTTON_CODE.b_user_authorize_authority_delete,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.authorizeManagementService.deleteCurrentUserAuthorizeAuthority(myThis.authorizeId, dataModel.data.id)
                  .subscribe(res => {
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
      buttons: [
        {
          name: '返回',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'rollback',
          confirmFun(formInfo, that) {
            myThis.router.navigateByUrl('/home/authorizeManage/authorizeList');
          }
        }
      ],
      initDataFun(formInfo, sortPage, callback, that) {// 数据处理回调 config：配置，dataModels：数据，that：当前对象
        const req = {
          appName: formInfo['appName'],
          size: sortPage.size,
          likeFields: [],
          sortField: sortPage['sortField'],
          sortRule: sortPage['sortRule']
        };
        myThis.authorizeManagementService.getCurrentUserAuthorizeAuthorityPage(myThis.authorizeId, sortPage.current, req).subscribe(res => {
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
