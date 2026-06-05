import {Component, OnInit, ViewChild} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {AppService} from '../../../../app.service';
import {BUTTON_CODE} from '../../../../config/button-code';
import {BasicService} from '../../../../basic/basic.service';
import {ActivatedRoute, Router} from '@angular/router';
import {BasicManagementService} from '../../../basic-management/basic-management.service';
import {WebsiteManagementService} from '../../website-management.service';
import {TableFindModel} from '../../../../../../projects/xc-common/src/common/table-find/table-find.model';
import {FormInfoModel} from '../../../../../../projects/xc-common/src/common/form-info/form-info.model';
import {FormInfoComponent} from '../../../../../../projects/xc-common/src/common/form-info/form-info.component';
import {
  OverallSelectComponent
} from '../../../../../../projects/xc-common/src/common/overall-select/overall-select.component';

@Component({
  selector: 'app-column-list',
  templateUrl: './column-list.component.html',
  styleUrls: ['./column-list.component.scss']
})
export class ColumnListComponent implements OnInit {

  public tableFindModel: TableFindModel;
  public formInfoModel: FormInfoModel;
  public isVisible = false;
  @ViewChild(FormInfoComponent, {static: true})
  public formInfoComponent: FormInfoComponent;
  @ViewChild(OverallSelectComponent, {static: true})
  public overallSelectComponent: OverallSelectComponent;
  public columnList = [];
  public dataTypeList = [];
  public columnTrees;
  public appId;
  public listInfo = {
    isImport: true, // 是否是导入
    isVisible: false,
    content: ''
  };

  constructor(public router: Router,
              public basicManagementService: BasicManagementService,
              public websiteManagementService: WebsiteManagementService,
              public message: NzMessageService,
              public modalService: NzModalService,
              public basicService: BasicService,
              public activatedRoute: ActivatedRoute,
              public appService: AppService) {
    this.formInfoModel = new FormInfoModel();
    this.tableFindModel = new TableFindModel();
    this.activatedRoute.queryParams.subscribe(queryParams => {
      if (queryParams.appId) {
        this.appId = queryParams.appId;
        this.getBasicData();
      }
    });
  }

  /**
   * 获取基础数据
   */
  public getBasicData() {
    this.getData(true);
  }

  /**
   * 获取栏目
   */
  public getData(bool) {
    this.websiteManagementService.getAppColumnList(this.appId, {}).subscribe(res => {
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
      this.columnTrees = this.basicService.getTrees('root', nodes);
      this.columnList = array;
      if (this.dataTypeList && this.columnList && bool) {
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
      if (this.dataTypeList && this.columnList && bool) {
        this.init();
      }
    });
  }

  ngOnInit() {
  }

  /**
   * 初始化表单
   */
  initForm(formInfo) {
    this.formInfoModel.initForm({
      components: [
        {
          name: '栏目名称',
          field: 'name',
          required: true,
          input: {
            maxLength: 60
          }
        },
        {
          name: '栏目标识',
          field: 'code',
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
          name: '栏目大小',
          field: 'columnSize',
          required: true,
          inputNumber: {}
        },
        {
          name: '栏目标签',
          field: 'label',
          input: {
            maxLength: 60
          }
        },
        {
          name: '栏目url',
          field: 'url',
          input: {
            maxLength: 60
          }
        },
        {
          name: '父级',
          field: 'parentNode',
          selectTree: {
            trees: this.columnTrees
          }
        },
        {
          name: '栏目图标',
          field: 'icon',
          input: {
            maxLength: 60
          }
        },
        {
          name: '排序',
          field: 'seq',
          input: {
            maxLength: 9
          }
        },
        {
          name: '栏目状态',
          field: 'status',
          switch: {}
        },
        {
          name: '描述',
          field: 'describe',
          textarea: {
            rows: 4,
            maxLength: 255
          }
        }
      ]
    }, formInfo);
  }

  /**
   * 初始化配置
   */
  public init() {
    const myThis = this;
    const COMMON_TYPE = TableFindModel.COMMON_TYPE;
    this.tableFindModel.initConfig({
      tables: [
        {name: '栏目名称', field: 'name', type: COMMON_TYPE.table.TEXT},
        {name: '栏目标签', field: 'label', type: COMMON_TYPE.table.TEXT},
        {name: '栏目标识', field: 'code', type: COMMON_TYPE.table.TEXT},
        {name: '数据类型', field: 'dataType', type: COMMON_TYPE.table.DICT_DESC, options: this.dataTypeList},
        {name: '父级', field: 'parentNode', type: COMMON_TYPE.table.DICT_DESC},
        {name: '排序', field: 'seq', type: COMMON_TYPE.table.TEXT, isSort: true},
        {name: '栏目大小', field: 'columnSize', type: COMMON_TYPE.table.TEXT},
        {name: '状态', field: 'status', type: COMMON_TYPE.table.DICT_DESC, dictType: 'effectStatus'},
        {name: '更新时间', field: 'updateTime', type: COMMON_TYPE.table.TEXT, isSort: true},
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          operates: [
            {
              name: '编辑',
              field: 'edit',
              bAuthority: BUTTON_CODE.b_app_column_edit,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.websiteManagementService.getAppColumn(myThis.appId, dataModel.data.id).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.isVisible = true;
                  myThis.initForm({
                    id: dataModel.data.id,
                    name: dataModel.data['name'],
                    label: dataModel.data['label'],
                    code: dataModel.data['code'],
                    dataType: dataModel.data['dataType'],
                    url: dataModel.data['url'],
                    icon: dataModel.data['icon'],
                    columnSize: dataModel.data['columnSize'],
                    describe: dataModel.data['describe'],
                    parentNode: dataModel.data['parentNode'],
                    seq: dataModel.data.seq,
                    status: dataModel.data['status'] === '0'
                  });
                });
              }
            },
            {
              name: '上移',
              field: 'up',
              bAuthority: BUTTON_CODE.b_app_column_up,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.websiteManagementService.updateAppColumnUp(myThis.appId, dataModel.data.id).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.message.success('移动成功！');
                  myThis.getData(false);
                  myThis.tableFindModel.getData(null, null);
                });
              }
            },
            {
              name: '下移',
              field: 'down',
              bAuthority: BUTTON_CODE.b_app_column_down,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.websiteManagementService.updateAppColumnDown(myThis.appId, dataModel.data.id).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.message.success('移动成功！');
                  myThis.getData(false);
                  myThis.tableFindModel.getData(null, null);
                });
              }
            },
            {
              name: '删除',
              field: 'delete',
              bAuthority: BUTTON_CODE.b_app_column_delete,
              confirmFun(dataModel, that) {
                myThis.modalService.confirm({
                  nzTitle: '确认要删除吗?',
                  nzContent: '<b style="color: red;">删除后数据无法找回哦！</b>',
                  nzOkText: '确认',
                  nzOkType: 'danger',
                  nzOnOk: () => {
                    myThis.appService.setLoading(true);
                    myThis.websiteManagementService.deleteAppColumn(myThis.appId, dataModel.data.id).subscribe(res => {
                      myThis.appService.setLoading(false);
                      myThis.message.success('删除成功！');
                      myThis.getData(false);
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
          name: '栏目名称',
          field: 'name',
          input: {
            maxLength: 60
          }
        },
        {
          name: '栏目标签',
          field: 'label',
          input: {
            maxLength: 60
          }
        },
        {
          name: '栏目标识',
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
          name: '父级',
          field: 'parentNode',
          selectTree: {
            trees: [{
              title: '顶级节点',
              key: 'root',
              children: this.columnTrees
            }]
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
          bAuthority: BUTTON_CODE.b_app_column_add,
          confirmFun(formInfo, that) {
            myThis.isVisible = true;
            myThis.initForm({
              status: true,
              columnSize: '1',
              dataType: formInfo.dataType,
              parentNode: formInfo.parentNode
            });
          }
        },
        {
          name: '导入',
          type: COMMON_TYPE.button.MIDDLING,
          icon: 'plus-circle',
          bAuthority: BUTTON_CODE.b_app_column_import,
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
          bAuthority: BUTTON_CODE.b_app_column_export,
          confirmFun(formInfo, that) {
            const req = {
              likeFields: ['name']
            };
            for (const find of that.config.finds) {
              req[find.field] = formInfo[find.field];
            }
            myThis.websiteManagementService.getAppColumnList(myThis.appId, req).subscribe(res => {
              const array = [];
              for (const data of res) {
                array.push({
                  name: data['name'],
                  label: data['label'],
                  code: data['code'],
                  describe: data['describe'],
                  dataType: data['dataType'],
                  url: data['url'],
                  icon: data['icon'],
                  columnSize: data['columnSize'],
                  node: data['node'],
                  parentNode: data['parentNode'],
                  seq: data['seq'],
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
          likeFields: ['name', 'label'],
          sortField: sortPage['sortField'],
          sortRule: sortPage['sortRule']
        };
        for (const find of that.config.finds) {
          req[find.field] = formInfo[find.field];
        }
        myThis.websiteManagementService.getAppColumnPage(myThis.appId, sortPage.current, req).subscribe(res => {
          const array = res['resData'].map((data) => {
            return {data};
          });
          callback(array, {total: res['total']});
        });
      },
      frontPaging: false,
      frontSort: false
    });
    for (const find of this.tableFindModel.config.finds) {
      if (find.selectTree && find.field === 'parentNode') {
        find.selectTree.trees = [{
          title: '顶级节点',
          key: 'root',
          children: myThis.columnTrees
        }];
      }
    }
    for (const table of this.tableFindModel.config.tables) {
      if (table.field === 'parentNode') {
        table.options = myThis.columnList;
      }
    }
  }

  /**
   * 确认提交1
   */
  public listInfoOk() {
    const req = JSON.parse(this.listInfo.content);
    for (const r of req) {
      if (!r['name'] || !r['code'] || !r['dataType'] || !r['columnSize'] || !r['node'] || !r['parentNode']) {
        this.message.success('数据格式错误！');
        return;
      }
    }
    this.appService.setLoading(true);
    this.websiteManagementService.createAppColumnList(this.appId, req).subscribe(res => {
      this.listInfo.isVisible = false;
      this.appService.setLoading(false);
      this.message.success('导入成功！');
      this.tableFindModel.getData(null, null);
    });
  }

  /**
   * 确认提交
   */
  confirmOk() {
    this.appService.setLoading(true);
    const req = {
      name: this.formInfoModel.formInfo.name,
      label: this.formInfoModel.formInfo.label,
      code: this.formInfoModel.formInfo.code,
      dataType: this.formInfoModel.formInfo.dataType,
      url: this.formInfoModel.formInfo.url,
      icon: this.formInfoModel.formInfo.icon,
      columnSize: this.formInfoModel.formInfo.columnSize,
      describe: this.formInfoModel.formInfo.describe,
      parentNode: this.formInfoModel.formInfo.parentNode ? this.formInfoModel.formInfo.parentNode : 'root',
      seq: this.formInfoModel.formInfo.seq,
      status: this.formInfoModel.formInfo.status ? '0' : '1'
    };
    if (!this.formInfoModel.formInfo.id) {
      this.websiteManagementService.createAppColumn(this.appId, req).subscribe(res => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('栏目创建成功！');
        this.getData(false);
        this.tableFindModel.getData(null, null);
      });
    } else {
      this.websiteManagementService.updateAppColumn(this.appId, this.formInfoModel.formInfo.id, req).subscribe(res => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('栏目修改成功！');
        this.getData(false);
        this.tableFindModel.getData(null, null);
      });
    }
  }
}
