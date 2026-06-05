import {Component, OnInit, ViewChild} from '@angular/core';
import {FormInfoComponent, FormInfoModel, TableFindModel} from '../../../../../../projects/xc-common/src/public-api';
import {BUTTON_CODE} from '../../../../config/button-code';
import {BasicManagementService} from '../../basic-management.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {AppService} from '../../../../app.service';
import {BasicService} from '../../../../basic/basic.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-tree-dict-list',
  templateUrl: './tree-dict-list.component.html',
  styleUrls: ['./tree-dict-list.component.scss']
})
export class TreeDictListComponent implements OnInit {
  public tableFindModel: TableFindModel;
  public formInfoModel: FormInfoModel;
  public isVisible = false;
  public listInfo = {
    isImport: true, // 是否是导入
    isVisible: false,
    content: ''
  };
  @ViewChild(FormInfoComponent, {static: true})
  public formInfoComponent: FormInfoComponent;
  public treeDictList = [];
  public trees = [];
  public appId;

  constructor(public basicManagementService: BasicManagementService,
              public basicService: BasicService,
              public message: NzMessageService,
              public modalService: NzModalService,
              public activatedRoute: ActivatedRoute,
              public appService: AppService) {
    this.tableFindModel = new TableFindModel();
    this.formInfoModel = new FormInfoModel();
    this.activatedRoute.queryParams.subscribe(queryParams => {
      if (queryParams.appId) {
        this.getBasicData(queryParams.appId);
      }
    });
  }

  ngOnInit() {
  }

  /**
   * 获取基础数据
   */
  public getBasicData(appId) {
    this.appId = appId;
    this.init();
    this.getData();
  }

  /**
   * 获取页面数据
   */
  public getData() {
    this.basicManagementService.getAppTreeDictList(this.appId, {}).subscribe(res => {
      const nodes = [];
      const array = [];
      for (const arr of res) {
        nodes.push({
          node: arr.node,
          parentNode: arr.parentNode,
          data: arr,
          title: arr.name,
          key: arr.node,
          seq: arr.seq
        });
        array.push({
          name: arr.name,
          value: arr.node
        });
      }
      this.trees = this.basicService.getTrees('root', nodes);
      this.treeDictList = array;
      for (const find of this.tableFindModel.config.finds) {
        if (find.selectTree && find.field === 'parentNode') {
          find.selectTree.trees = [{
            title: '顶级节点',
            key: 'root',
            children: this.trees
          }];
        }
      }
      for (const table of this.tableFindModel.config.tables) {
        if (table.field === 'parentNode') {
          table.options = array;
        }
      }
    });
    this.tableFindModel.getData(null, null);
  }

  /**
   * 初始化表单
   */
  initForm(formInfo) {
    this.formInfoModel.initForm({
      components: [
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
            trees: this.trees,
            checkable: false
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
          name: '状态',
          field: 'status',
          switch: {}
        }
      ]
    }, formInfo);
  }

  /**
   * 确认提交
   */
  confirmOk() {
    this.appService.setLoading(true);
    const req = {
      name: this.formInfoModel.formInfo.name,
      code: this.formInfoModel.formInfo.code,
      type: this.formInfoModel.formInfo.type,
      node: this.formInfoModel.formInfo.node,
      parentNode: this.formInfoModel.formInfo.parentNode ? this.formInfoModel.formInfo.parentNode : 'root',
      seq: this.formInfoModel.formInfo.seq,
      status: this.formInfoModel.formInfo.status ? '0' : '1'
    };
    if (!this.formInfoModel.formInfo.id) {
      this.basicManagementService.createAppTreeDict(this.appId, req).subscribe(res => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('创建成功！');
        this.getData();
      });
    } else {
      this.basicManagementService.updateAppTreeDict(this.appId, this.formInfoModel.formInfo.id, req).subscribe(res => {
        this.appService.setLoading(false);
        this.isVisible = false;
        this.message.success('修改成功！');
        this.getData();
      });
    }

  }

  /**
   * 确认提交1
   */
  public listInfoOk() {
    const req = JSON.parse(this.listInfo.content);
    for (const r of req) {
      if (!r['name'] || !r['code'] || !r['type'] || !r['node'] || !r['parentNode'] || !r['status']) {
        this.message.success('数据格式错误！');
        return;
      }
    }
    this.appService.setLoading(true);
    this.basicManagementService.createAppTreeDictList(this.appId, req).subscribe(res => {
      this.listInfo.isVisible = false;
      this.appService.setLoading(false);
      this.message.success('导入成功！');
      this.getData();
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
        {name: '父级', field: 'parentNode', type: COMMON_TYPE.table.DICT_DESC},
        {name: '排序', field: 'seq', type: COMMON_TYPE.table.TEXT, isSort: true},
        {name: '状态', field: 'status', type: COMMON_TYPE.table.DICT_DESC, dictType: 'effectStatus'},
        {name: '更新时间', field: 'updateTime', type: COMMON_TYPE.table.TEXT, isSort: true},
        {
          name: '操作', field: '', type: COMMON_TYPE.table.OPERATE,
          operates: [
            {
              name: '编辑',
              field: 'edit',
              bAuthority: BUTTON_CODE.b_app_tree_dict_edit,
              confirmFun(dataModel, that) {
                myThis.isVisible = true;
                myThis.initForm({
                  id: dataModel.data.id,
                  name: dataModel.data['name'],
                  code: dataModel.data['code'],
                  type: dataModel.data['type'],
                  node: dataModel.data['node'],
                  parentNode: dataModel.data['parentNode'],
                  seq: dataModel.data['seq'],
                  status: dataModel.data['status'] === '0'
                });
              }
            },
            {
              name: '上移',
              field: 'up',
              bAuthority: BUTTON_CODE.b_app_tree_dict_up,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.basicManagementService.updateAppTreeDictUp(myThis.appId, dataModel.data.id).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.message.success('移动成功！');
                  myThis.tableFindModel.getData(null, null);
                  myThis.getData();
                });
              }
            },
            {
              name: '下移',
              field: 'down',
              bAuthority: BUTTON_CODE.b_app_tree_dict_down,
              confirmFun(dataModel, that) {
                myThis.appService.setLoading(true);
                myThis.basicManagementService.updateAppTreeDictDown(myThis.appId, dataModel.data.id).subscribe(res => {
                  myThis.appService.setLoading(false);
                  myThis.message.success('移动成功！');
                  myThis.tableFindModel.getData(null, null);
                  myThis.getData();
                });
              }
            },
            {
              name: '删除',
              field: 'delete',
              bAuthority: BUTTON_CODE.b_app_tree_dict_delete,
              confirmFun(dataModel, that) {
                myThis.modalService.confirm({
                  nzTitle: '确认要删除吗?',
                  nzContent: '<b style="color: red;">删除后数据无法找回哦！</b>',
                  nzOkText: '确认',
                  nzOkType: 'danger',
                  nzOnOk: () => {
                    myThis.appService.setLoading(true);
                    myThis.basicManagementService.deleteAppTreeDict(myThis.appId, dataModel.data.id).subscribe(res => {
                      myThis.appService.setLoading(false);
                      myThis.message.success('删除成功！');
                      myThis.tableFindModel.getData(null, null);
                      myThis.getData();
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
          bAuthority: BUTTON_CODE.b_app_tree_dict_add,
          confirmFun(formInfo, that) {
            myThis.isVisible = true;
            myThis.initForm({
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
          bAuthority: BUTTON_CODE.b_app_tree_dict_import,
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
          bAuthority: BUTTON_CODE.b_app_tree_dict_export,
          confirmFun(formInfo, that) {
            const req = {
              likeFields: ['name']
            };
            for (const find of that.config.finds) {
              req[find.field] = formInfo[find.field];
            }
            myThis.basicManagementService.getAppTreeDictList(myThis.appId, req).subscribe(res => {
              const array = [];
              for (const data of res) {
                array.push({
                  name: data['name'],
                  code: data['code'],
                  type: data['type'],
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
          likeFields: ['name'],
          sortField: sortPage['sortField'],
          sortRule: sortPage['sortRule']
        };
        for (const find of that.config.finds) {
          req[find.field] = formInfo[find.field];
        }
        myThis.basicManagementService.getAppTreeDictPage(myThis.appId, sortPage.current, req).subscribe(res => {
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
