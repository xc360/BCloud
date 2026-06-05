import {Component, OnInit, ViewChild} from '@angular/core';
import {BasicService} from '../../../../basic/basic.service';
import {AppService} from '../../../../app.service';
import {ActivatedRoute, Router} from '@angular/router';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {BUTTON_CODE} from '../../../../config/button-code';
import {BasicManagementService} from '../../../basic-management/basic-management.service';
import {WebsiteManagementService} from '../../website-management.service';
import {TableFindModel} from '../../../../../../projects/xc-common/src/common/table-find/table-find.model';
import {FormInfoModel} from '../../../../../../projects/xc-common/src/common/form-info/form-info.model';
import {FormInfoComponent} from '../../../../../../projects/xc-common/src/common/form-info/form-info.component';

@Component({
  selector: 'app-page-list',
  templateUrl: './page-list.component.html',
  styleUrls: ['./page-list.component.scss']
})
export class PageListComponent implements OnInit {

  public tableFindModel: TableFindModel;
  public formInfoModel: FormInfoModel;
  public isVisible = false;
  @ViewChild(FormInfoComponent, {static: true})
  public formInfoComponent: FormInfoComponent;
  public dataTypeList = [];
  public columnList = [];
  public columnTrees;
  public appId;
  public listInfo = {
    isImport: true, // 是否是导入
    isVisible: false,
    type: 'data',
    content: ''
  };

  constructor(public basicService: BasicService,
              public basicManagementService: BasicManagementService,
              public websiteManagementService: WebsiteManagementService,
              public appService: AppService,
              public message: NzMessageService,
              public modalService: NzModalService,
              public activatedRoute: ActivatedRoute,
              public router: Router) {
    this.tableFindModel = new TableFindModel();
    this.formInfoModel = new FormInfoModel();
  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(queryParams => {
      if (queryParams.appId) {
        this.appId = queryParams.appId;
        this.getColumn();
      }
    });
  }

  /**
   * 栏目集合
   */
  public getColumn() {
    this.websiteManagementService.getAppColumnList(this.appId, {}).subscribe(res => {
      const nodes = [];
      for (const arr of res) {
        nodes.push({
          title: arr.name,
          node: arr.node,
          parentNode: arr.parentNode,
          key: arr.id,
          seq: arr.seq
        });
      }
      this.columnList = res;
      this.columnTrees = this.basicService.getTrees('root', nodes);
      if (this.dataTypeList && this.columnList) {
        this.init();
      }
    });
    this.websiteManagementService.getAppDataTypeList(this.appId, {
      sortField: 'seq',
      sortRule: 'asc'
    }).subscribe(res => {
      const array = [];
      for (const arr of res) {
        array.push({
          name: arr.name,
          value: arr.code
        });
      }
      this.dataTypeList = array;
      if (this.dataTypeList && this.columnList) {
        this.init();
      }
    });
  }

  /**
   *
   * 获取页面数据
   */
  public authorityList() {
    this.basicManagementService.getAppAuthorityList(this.appId, {types: ['3']}).subscribe(res => {
      const nodes = [];
      const array = [];
      for (const arr of res) {
        nodes.push({
          title: arr.name,
          node: arr.node,
          parentNode: arr.parentNode,
          key: arr.code,
          seq: arr.seq
        });
        array.push({
          name: arr.name,
          value: arr.node
        });
      }
      const trees = this.basicService.getTrees('root', nodes);
      for (const component of this.formInfoModel.formConfig.components) {
        if (component.field === 'menuCode') {
          component.selectTree.trees = trees;
        }
      }
    });
  }

  /**
   * 初始化表单
   */
  initForm(formInfo) {
    this.isVisible = true;
    this.formInfoModel.initForm({
      components: [
        {
          name: '页面名称',
          field: 'name',
          required: true,
          input: {
            maxLength: 60
          }
        },
        {
          name: '页面标识',
          field: 'code',
          required: true,
          input: {
            maxLength: 60
          }
        },
        {
          name: '文件地址',
          field: 'filePath',
          required: true,
          input: {
            maxLength: 60
          }
        },
        {
          name: '数据类型',
          field: 'dataType',
          required: true,
          select: {
            options: this.dataTypeList
          }
        },
        {
          name: '菜单',
          field: 'menuCode',
          selectTree: {
            trees: []
          }
        },
        {
          name: '栏目',
          field: 'columnIds',
          selectTree: {
            trees: this.columnTrees,
            checkable: true
          }
        },
        {
          name: '重定向地址',
          field: 'redirectUrl',
          input: {
            maxLength: 60
          }
        },
        {
          name: 'seo标题',
          field: 'seoTitle',
          input: {
            maxLength: 60
          }
        },
        {
          name: 'seo关键字',
          field: 'seoKeywords',
          textarea: {
            rows: 4,
            maxLength: 255
          }
        },
        {
          name: 'seo描述',
          field: 'seoDescription',
          textarea: {
            rows: 4,
            maxLength: 255
          }
        },
        {
          name: '栏目状态',
          field: 'status',
          switch: {}
        }
      ]
    }, formInfo);
    this.authorityList();
  }

  /**
   * 初始化配置
   */
  public init() {
    const myThis = this;
    const COMMON_TYPE = TableFindModel.COMMON_TYPE;
    const config = {
      tables: [
        {name: '页面名称', field: 'name', type: COMMON_TYPE.table.TEXT},
        {name: '页面标识', field: 'code', type: COMMON_TYPE.table.TEXT},
        {name: '数据类型', field: 'dataType', type: COMMON_TYPE.table.DICT_DESC, options: this.dataTypeList},
        {name: '状态', field: 'status', type: COMMON_TYPE.table.DICT_DESC, dictType: 'effectStatus'},
        {name: '更新时间', field: 'updateTime', type: COMMON_TYPE.table.TEXT, isSort: true},
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          operates: [
            {
              name: '编辑',
              field: 'edit',
              bAuthority: BUTTON_CODE.b_app_page_edit,
              confirmFun(dataModel, that) {
                myThis.initForm({
                  id: dataModel.data.id,
                  name: dataModel.data.name,
                  code: dataModel.data.code,
                  dataType: dataModel.data.dataType,
                  seoTitle: dataModel.data.seoTitle,
                  seoKeywords: dataModel.data.seoKeywords,
                  seoDescription: dataModel.data.seoDescription,
                  filePath: dataModel.data.filePath,
                  redirectUrl: dataModel.data.redirectUrl,
                  menuCode: dataModel.data.menuCode,
                  status: dataModel.data['status'] === '0',
                  columnIds: myThis.basicManagementService.getSelectedIds(dataModel.data.columnIds, myThis.columnList)
                });
              }
            },
            {
              name: '删除',
              field: 'delete',
              bAuthority: BUTTON_CODE.b_app_page_delete,
              confirmFun(dataModel, that) {
                myThis.modalService.confirm({
                  nzTitle: '确认要删除吗?',
                  nzContent: '<b style="color: red;">删除后数据无法找回哦！</b>',
                  nzOkText: '确认',
                  nzOkType: 'danger',
                  nzOnOk: () => {
                    myThis.appService.setLoading(true);
                    myThis.websiteManagementService.deleteAppPage(myThis.appId, dataModel.data.id).subscribe(res => {
                      myThis.appService.setLoading(false);
                      myThis.message.success('删除成功！');
                      myThis.getColumn();
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
          name: '页面名称',
          field: 'name',
          input: {
            maxLength: 60
          }
        },
        {
          name: '页面标识',
          field: 'code',
          input: {
            maxLength: 60
          }
        },
        {
          name: '数据类型',
          field: 'dataType',
          select: {
            options: this.dataTypeList
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
          name: '添加',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_page_add,
          confirmFun(formInfo, that) {
            myThis.initForm({status: true});
          }
        },
        {
          name: '导入',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_page_import,
          confirmFun(formInfo, that) {
            myThis.listInfo = {
              isImport: true,
              isVisible: true,
              type: 'data',
              content: ''
            };
          }
        },
        {
          name: '导出',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_page_export,
          confirmFun(formInfo, that) {
            const req = {
              likeFields: ['name']
            };
            for (const find of that.config.finds) {
              req[find.field] = formInfo[find.field];
            }
            myThis.websiteManagementService.getAppPageList(myThis.appId, req).subscribe(res => {
              const array = [];
              for (const data of res) {
                array.push({
                  name: data['name'],
                  code: data['code'],
                  dataType: data['dataType'],
                  seoTitle: data['seoTitle'],
                  seoKeywords: data['seoKeywords'],
                  seoDescription: data['seoDescription'],
                  filePath: data['filePath'],
                  redirectUrl: data['redirectUrl'],
                  menuCode: data['menuCode'],
                  status: data['status']
                });
              }
              myThis.listInfo = {
                isImport: false,
                isVisible: true,
                type: 'data',
                content: JSON.stringify(array)
              };
            });
          }
        },
        {
          name: '关联导入',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_page_column_relation_import,
          confirmFun(formInfo, that) {
            myThis.listInfo = {
              isImport: true,
              isVisible: true,
              type: 'relation',
              content: ''
            };
          }
        },
        {
          name: '关联导出',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_page_column_relation_export,
          confirmFun(formInfo, that) {
            myThis.websiteManagementService.getAppPageColumnRelationList(myThis.appId, {}).subscribe(res => {
              const array = [];
              for (const data of res) {
                array.push({
                  pageCode: data['pageCode'],
                  columnCode: data['columnCode']
                });
              }
              myThis.listInfo = {
                isImport: false,
                isVisible: true,
                type: 'relation',
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
        myThis.websiteManagementService.getAppPagePage(myThis.appId, sortPage.current, req).subscribe(res => {
          const array = res['resData'].map((data) => {
            return {data};
          });
          callback(array, {total: res['total']});
        });
      },
      frontPaging: false,
      frontSort: false
    };
    this.tableFindModel.initConfig(config);
  }

  /**
   * 确认提交1
   */
  public listInfoOk() {
    const req = JSON.parse(this.listInfo.content);
    if (this.listInfo.type === 'data') {
      for (const r of req) {
        if (!r['name'] || !r['code'] || !r['status']) {
          this.message.success('数据格式错误！');
          return;
        }
      }
      this.appService.setLoading(true);
      this.websiteManagementService.createAppPageList(this.appId, req).subscribe(res => {
        this.listInfo.isVisible = false;
        this.appService.setLoading(false);
        this.message.success('导入成功！');
        this.tableFindModel.getData(null, null);
      });
    } else {
      for (const r of req) {
        if (!r['pageCode'] || !r['columnCode']) {
          this.message.success('数据格式错误！');
          return;
        }
      }
      this.appService.setLoading(true);
      this.websiteManagementService.createAppPageColumnRelationList(this.appId, req).subscribe(res => {
        this.listInfo.isVisible = false;
        this.appService.setLoading(false);
        this.message.success('导入成功！');
        this.tableFindModel.getData(null, null);
      });
    }
  }

  /**
   * 确定
   */
  confirmOk() {
    this.appService.setLoading(true);
    const req = {
      name: this.formInfoModel.formInfo.name,
      code: this.formInfoModel.formInfo.code,
      dataType: this.formInfoModel.formInfo.dataType,
      seoTitle: this.formInfoModel.formInfo.seoTitle,
      seoKeywords: this.formInfoModel.formInfo.seoKeywords,
      seoDescription: this.formInfoModel.formInfo.seoDescription,
      filePath: this.formInfoModel.formInfo.filePath,
      redirectUrl: this.formInfoModel.formInfo.redirectUrl,
      menuCode: this.formInfoModel.formInfo.menuCode ? this.formInfoModel.formInfo.menuCode : '',
      status: this.formInfoModel.formInfo.status ? '0' : '1',
      columnIds: []
    };
    const columnIds = this.formInfoModel.formInfo.columnIds;
    if (columnIds) {
      req['columnIds'] = this.basicManagementService.getSubmitIds(columnIds, this.columnList);
    }
    if (!this.formInfoModel.formInfo.id) {
      this.websiteManagementService.createAppPage(this.appId, req).subscribe(() => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('页面创建成功！');
        this.getColumn();
        this.tableFindModel.getData(null, null);
      });
    } else {
      this.websiteManagementService.updateAppPage(this.appId, this.formInfoModel.formInfo.id, req).subscribe(() => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('页面修改成功！');
        this.getColumn();
        this.tableFindModel.getData(null, null);
      });
    }
  }
}
