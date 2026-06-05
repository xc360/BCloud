import {Component, OnInit, ViewChild} from '@angular/core';
import {ArrayConfig, TableFindModel} from '../../../../../../projects/xc-common/src/public-api';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {AppService} from '../../../../app.service';
import {FormInfoModel} from '../../../../../../projects/xc-common/src/public-api';
import {FormInfoComponent} from '../../../../../../projects/xc-common/src/public-api';
import {BUTTON_CODE} from '../../../../config/button-code';
import {OverallSelectComponent} from '../../../../../../projects/xc-common/src/public-api';
import {BasicService} from '../../../../basic/basic.service';
import {ActivatedRoute} from '@angular/router';
import {WebsiteManagementService} from '../../website-management.service';

@Component({
  selector: 'app-menu-list',
  templateUrl: './menu-list.component.html',
  styleUrls: ['./menu-list.component.scss']
})
export class MenuListComponent implements OnInit {
  public tableFindModel: TableFindModel;
  public formInfoModel: FormInfoModel;
  public isVisible = false;
  @ViewChild(FormInfoComponent, {static: true})
  public formInfoComponent: FormInfoComponent;
  public listInfo = {
    isImport: true, // 是否是导入
    isVisible: false,
    content: ''
  };
  public appId;
  @ViewChild(OverallSelectComponent, {static: true})
  public overallSelectComponent: OverallSelectComponent;

  constructor(public websiteManagementService: WebsiteManagementService,
              public basicService: BasicService,
              public message: NzMessageService,
              public modalService: NzModalService,
              public activatedRoute: ActivatedRoute,
              public appService: AppService) {
    this.formInfoModel = new FormInfoModel();
    this.tableFindModel = new TableFindModel();
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
    this.getData();
  }

  ngOnInit() {
  }

  /**
   *
   * 获取页面数据
   */
  public getData() {
    this.websiteManagementService.getAppMenuList(this.appId, {}).subscribe(res => {
      const nodes = [];
      const array = [];
      for (const arr of res) {
        nodes.push({
          title: arr.name,
          node: arr.node,
          parentNode: arr.parentNode,
          key: arr.node,
          seq: arr.seq
        });
        array.push({
          name: arr.name,
          value: arr.node
        });
      }
      const trees = this.basicService.getTrees('root', nodes);
      for (const table of this.tableFindModel.config.tables) {
        if (table.field === 'parentNode') {
          table.options = array;
        }
      }
      for (const find of this.tableFindModel.config.finds) {
        if (find.selectTree && find.field === 'parentNode') {
          find.selectTree.trees = [{
            title: '顶级节点',
            key: 'root',
            children: trees
          }];
        }
      }
      if (this.formInfoModel.formConfig.components) {
        for (const component of this.formInfoModel.formConfig.components) {
          if (component.field === 'parentNode') {
            component.selectTree.trees = trees;
          }
        }
      }
    });
  }

  /**
   * 初始化表单
   */
  initForm(formInfo) {
    const components: Array<ArrayConfig> = [
      {
        name: '名称',
        field: 'name',
        required: true,
        input: {
          maxLength: 60
        }
      },
      {
        name: '标识',
        field: 'code',
        required: true,
        input: {
          maxLength: 60
        }
      },
      {
        name: '类型',
        field: 'type',
        required: true,
        input: {
          maxLength: 60
        }
      },
      {
        name: '父级',
        field: 'parentNode',
        selectTree: {
          trees: [],
          checkable: false
        }
      },
      {
        name: '菜单地址',
        field: 'url',
        input: {
          maxLength: 255
        }
      },
      {
        name: '图标',
        field: 'icon',
        input: {
          maxLength: 60
        }
      },
      {
        name: '是否隐藏',
        field: 'hidden',
        switch: {}
      },
      {
        name: '状态',
        field: 'status',
        switch: {}
      },
      {
        name: '描述',
        field: 'describe',
        textarea: {
          maxLength: 200,
          rows: 4
        }
      }
    ];
    this.getData();
    this.formInfoModel.initForm({components}, formInfo);
  }


  /**
   * 确认提交
   */
  public confirmOk() {
    this.appService.setLoading(true);
    const req = {
      node: this.formInfoModel.formInfo.node,
      name: this.formInfoModel.formInfo.name,
      describe: this.formInfoModel.formInfo.describe,
      code: this.formInfoModel.formInfo.code,
      type: this.formInfoModel.formInfo.type,
      url: this.formInfoModel.formInfo.url,
      icon: this.formInfoModel.formInfo.icon,
      parentNode: this.formInfoModel.formInfo.parentNode ? this.formInfoModel.formInfo.parentNode : 'root',
      hidden: this.formInfoModel.formInfo.hidden ? '0' : '1',
      status: this.formInfoModel.formInfo.status ? '0' : '1'
    };
    if (!this.formInfoModel.formInfo.id) {
      this.websiteManagementService.createAppMenu(this.appId, req).subscribe(res => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('创建成功！');
        this.getData();
        this.tableFindModel.getData(null, null);
      });
    } else {
      this.websiteManagementService.updateAppMenu(this.appId, this.formInfoModel.formInfo.id, req).subscribe(res => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('修改成功！');
        this.getData();
        this.tableFindModel.getData(null, null);
      });
    }
  }

  /**
   * 确认提交1
   */
  public listInfoOk() {
    const req = JSON.parse(this.listInfo.content);
    for (const r of req) {
      if (!r['node'] || !r['name'] || !r['code'] || !r['type'] || !r['parentNode']) {
        this.message.success('数据格式错误！');
        return;
      }
    }
    this.appService.setLoading(true);
    this.websiteManagementService.createAppMenuList(this.appId, ['3'], req).subscribe(res => {
      this.listInfo.isVisible = false;
      this.appService.setLoading(false);
      this.message.success('导入成功！');
      this.getData();
      this.tableFindModel.getData(null, null);
    });
  }

  /**
   * 初始化配置
   */
  public init() {
    const myThis = this;
    const COMMON_TYPE = TableFindModel.COMMON_TYPE;
    this.tableFindModel.initConfig({
      tables: [
        {name: '名称', field: 'name', type: COMMON_TYPE.table.TEXT},
        {name: '标识', field: 'code', type: COMMON_TYPE.table.TEXT},
        {name: '类型', field: 'type', type: COMMON_TYPE.table.TEXT},
        {name: '地址', field: 'url', type: COMMON_TYPE.table.TEXT},
        {name: '图标', field: 'icon', type: COMMON_TYPE.table.TEXT},
        {name: '排序', field: 'seq', type: COMMON_TYPE.table.TEXT, isSort: true},
        {name: '父级', field: 'parentNode', type: COMMON_TYPE.table.DICT_DESC},
        {name: '是否隐藏', field: 'hidden', type: COMMON_TYPE.table.DICT_DESC, dictType: 'whether'},
        {name: '状态', field: 'status', type: COMMON_TYPE.table.DICT_DESC, dictType: 'effectStatus'},
        {name: '更新时间', field: 'updateTime', type: COMMON_TYPE.table.TEXT, isSort: true},
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          operates: [
            {
              name: '编辑',
              field: 'edit',
              bAuthority: BUTTON_CODE.b_app_menu_edit,
              confirmFun(dataModel, that) {
                myThis.isVisible = true;
                myThis.initForm({
                  id: dataModel.data.id,
                  node: dataModel.data.node,
                  parentNode: dataModel.data.parentNode,
                  name: dataModel.data.name,
                  describe: dataModel.data.describe,
                  code: dataModel.data.code,
                  type: dataModel.data.type,
                  url: dataModel.data.url,
                  icon: dataModel.data.icon,
                  hidden: dataModel.data.hidden === '0',
                  status: dataModel.data.status === '0'
                });
              }
            },
            {
              name: '上移',
              field: 'up',
              bAuthority: BUTTON_CODE.b_app_menu_up,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.websiteManagementService.updateAppMenuUp(myThis.appId, dataModel.data.id).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.message.success('移动成功！');
                  myThis.getData();
                  myThis.tableFindModel.getData(null, null);
                });
              }
            },
            {
              name: '下移',
              field: 'down',
              bAuthority: BUTTON_CODE.b_app_menu_down,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.websiteManagementService.updateAppMenuDown(myThis.appId, dataModel.data.id).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.message.success('移动成功！');
                  myThis.getData();
                  myThis.tableFindModel.getData(null, null);
                });
              }
            },
            {
              name: '删除',
              field: 'delete',
              bAuthority: BUTTON_CODE.b_app_menu_delete,
              confirmFun(dataModel, that) {
                myThis.modalService.confirm({
                  nzTitle: '确认要删除吗?',
                  nzContent: '<b style="color: red;">删除后数据无法找回哦！</b>',
                  nzOkText: '确认',
                  nzOkType: 'danger',
                  nzOnOk: () => {
                    myThis.appService.setLoading(true);
                    myThis.websiteManagementService.deleteAppMenu(myThis.appId, dataModel.data.id).subscribe(res => {
                      myThis.appService.setLoading(false);
                      myThis.message.success('删除成功！');
                      myThis.getData();
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
          name: '名称',
          field: 'name',
          input: {
            maxLength: 60
          }
        },
        {
          name: '标识',
          field: 'code',
          input: {
            maxLength: 60
          }
        },
        {
          name: '类型',
          field: 'type',
          input: {
            maxLength: 60
          }
        },
        {
          name: '父级',
          field: 'parentNode',
          selectTree: {
            trees: [],
            checkable: false
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
          bAuthority: BUTTON_CODE.b_app_menu_add,
          confirmFun(formInfo, that) {
            myThis.isVisible = true;
            myThis.initForm({
              hidden: false,
              status: true,
              type: formInfo.type,
              parentNode: formInfo.parentNode
            });
          }
        },
        {
          name: '导入',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_menu_import,
          confirmFun(formInfo, that) {
            myThis.listInfo = {
              isImport: true,
              isVisible: true,
              content: ''
            };
          }
        },
        {
          name: '导出',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_menu_export,
          confirmFun(formInfo, that) {
            const req = {
              likeFields: ['name']
            };
            for (const find of that.config.finds) {
              req[find.field] = formInfo[find.field];
            }
            myThis.websiteManagementService.getAppMenuList(myThis.appId, req).subscribe(res => {
              const array = [];
              for (const data of res) {
                array.push({
                  name: data['name'],
                  node: data['node'],
                  parentNode: data['parentNode'],
                  describe: data['describe'],
                  code: data['code'],
                  type: data['type'],
                  url: data['url'],
                  icon: data['icon'],
                  seq: data['seq'],
                  hidden: data['hidden'],
                  status: data['status']
                });
              }
              myThis.listInfo = {
                isImport: false,
                isVisible: true,
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
        myThis.websiteManagementService.getAppMenuPage(myThis.appId, sortPage.current, req).subscribe(res => {
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
