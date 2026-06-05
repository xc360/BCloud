import {Injectable} from '@angular/core';
import {CommonTool, FileTool, LoginTool, UploadTool} from '@ccxc/tool';
import {ArrayConfig, FormInfoModel} from '../../../../projects/xc-common/src/common/form-info/form-info.model';
import {HomeService} from '../home.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {AppService} from '../../app.service';
import {BasicService} from '../../basic/basic.service';
import {FILE_FORMAT} from '../../config/enum';

@Injectable({
  providedIn: 'root'
})
export class PersonalInfoService {
  public formInfoModel: FormInfoModel;
  public isVisible = false;
  public countyOptions = [];
  public provinceOptions = [];
  public cityOptions = [];
  public defaultPortrait = './assets/images/user.png';

  constructor(public homeService: HomeService,
              public message: NzMessageService,
              public appService: AppService,
              public basicService: BasicService,
              public modalService: NzModalService) {
    this.formInfoModel = new FormInfoModel();
  }

  /**
   * 初始化表单
   */
  public initForm(form) {
    if (this.defaultPortrait === form.portrait) {
      form.portrait = '';
    }
    const myThis = this;
    this.formInfoModel.initForm({
      components: [
        {
          name: '用户昵称',
          field: 'nickName',
          required: true,
          input: {
            maxLength: 60
          }
        },
        {
          name: '性别',
          field: 'sex',
          required: true,
          select: {
            dictType: 'sex'
          }
        },
        {
          name: '生日',
          field: 'birthday',
          required: true,
          date: {
            disabledDate: (current: Date) => {
              return current.getTime() > new Date().getTime();
            }
          }
        },
        {
          name: '地址',
          field: 'address',
          required: true,
          linkageSelect: {
            children: [
              {
                name: '省/地区',
                field: 'province',
                options: this.provinceOptions,
                initOptionFun($event, formInfo, that) {
                  if ($event) {
                    for (const item of that.selectItem) {
                      if (item.field === 'province') {
                        const array = [];
                        for (const option of item.options) {
                          if (option.status === '0') {
                            array.push(option);
                          }
                        }
                        item.options = array;
                      }
                    }
                  }
                },
                confirmFun(formInfo, that) {
                  formInfo.county = null;
                  formInfo.city = null;
                  if (formInfo.province) {
                    myThis.homeService.getMyAppAreaList(formInfo.province).subscribe(res => {
                      const array = [];
                      for (const arr of res) {
                        if (arr.status === '0') {
                          arr.value = arr.code;
                          array.push(arr);
                        }
                      }
                      for (const item of that.selectItem) {
                        if (item.field === 'city') {
                          item.options = array;
                        }
                        if (item.field === 'county') {
                          item.options = [];
                        }
                      }
                      if (array.length === 1) {
                        formInfo['city'] = array[0].value;
                        myThis.homeService.getMyAppAreaList(array[0].value).subscribe(data => {
                          const list = [];
                          for (const arr of data) {
                            if (arr.status === '0') {
                              arr.value = arr.code;
                              list.push(arr);
                            }
                          }
                          for (const item of that.selectItem) {
                            if (item.field === 'county') {
                              item.options = list;
                            }
                          }
                        });
                      }
                    });
                  }
                }
              },
              {
                name: '市',
                field: 'city',
                options: this.cityOptions,
                confirmFun(formInfo, that) {
                  formInfo.county = null;
                  if (formInfo.city) {
                    myThis.homeService.getMyAppAreaList(formInfo.city).subscribe(res => {
                      const array = [];
                      for (const arr of res) {
                        if (arr.status === '0') {
                          arr.value = arr.code;
                          array.push(arr);
                        }
                      }
                      for (const item of that.selectItem) {
                        if (item.field === 'county') {
                          item.options = array;
                        }
                      }
                    });
                  }
                }
              },
              {
                name: '县/区',
                field: 'county',
                options: this.countyOptions
              }
            ]
          }
        },
        {
          name: '头像',
          field: 'portrait',
          uploadPicture: {
            clickFun(formInfo, component) {
              myThis.uploadLogo(formInfo, component);
            }
          }
        },
        {
          name: '个人说明',
          field: 'explain',
          textarea: {
            rows: 5,
            maxLength: 255
          }
        }
      ]
    }, JSON.parse(JSON.stringify(form)));
  }

  /**
   * 提交
   */
  public submit(formInfo) {
    const req = {
      nickName: formInfo['nickName'],
      portrait: formInfo['portrait'],
      explain: formInfo['explain'],
      sex: formInfo['sex'],
      birthday: CommonTool.turnDate(formInfo['birthday']),
      province: formInfo['province'] ? formInfo['province'] : '',
      city: formInfo['city'] ? formInfo['city'] : '',
      county: formInfo['county'] ? formInfo['county'] : ''
    };
    this.appService.setLoading(true);
    this.homeService.updateCurrentUserInfo(req).subscribe(res => {
      this.isVisible = false;
      this.appService.setLoading(false);
      this.message.success('保存成功！');
    });
  }

  /**
   * 上传logo
   */
  public uploadLogo(formInfo, component) {
    const $this = this;
    $this.formInfoModel.formInfo.portrait = '';
    const input = document.createElement('input');
    input.style.display = 'none';
    input.type = 'file';
    input.setAttribute('multiple', '');
    const event = document.createEvent('MouseEvents');
    event.initEvent('click', true, true); // 这里的click可以换成你想触发的行为
    input.dispatchEvent(event); // 这里的clickME可以换成你想触发行为的DOM结点
    input.onchange = (e) => {
      const file = e.target['files'][0];
      let bool = false;
      for (const suffix of FILE_FORMAT.imageFormat) {
        if (file.type.indexOf(suffix) !== -1) {
          bool = true;
        }
      }
      if (!bool) {
        $this.message.error('文件只能为图片格式！');
        return;
      }
      $this.appService.setLoading(true, '上传中...');
      const tokenInfo = LoginTool.getToken();
      $this.uploadFolderFile(file, {
        folderPath: '/user/' + tokenInfo.userId + '/logo',
        fileName: FileTool.randomFileName(file.name)
      }, component, (downloadUrl) => {
        $this.appService.setLoading(false);
        $this.formInfoModel.formInfo.portrait = downloadUrl;
      });
    };
  }

  /**
   * 上传文件数据处理
   */
  public uploadFolderFile(file, config, component: ArrayConfig, fun) {
    const myThis = this;
    this.homeService.getMyAppUploadFileSign().subscribe(res => {
      const xcUploadTool = new UploadTool(null);
      xcUploadTool.setConfig({
        uploadUrl: res['uploadUrl'], // 上传的url地址
        resumed: '0',
        sign: res,
        md5Loading: null,
        uploadLoading: null
      });
      xcUploadTool.setFileData({file, fileName: config.fileName, folderPath: config.folderPath});
      if (component) {
        component.uploadPicture.loading = true;
      }
      xcUploadTool.start().then(httpRes => {
        if (component) {
          component.uploadPicture.loading = false;
        }
        fun(httpRes.downloadUrl);
      }).catch(() => {
        if (component) {
          component.uploadPicture.loading = false;
        }
        myThis.appService.setLoading(false);
        myThis.message.error('文件上传失败！');
      });
    });
  }

  /**
   * 关闭弹窗
   */
  public cancel() {
    this.isVisible = false;
    this.perfectData();
  }

  /**
   * 提示修改资料
   */
  public perfectData() {
    this.initArea();
    this.modalService.info({
      nzTitle: '提示',
      nzContent: '<p>你还没完善个人信息，请先完善个人信息！</p>',
      nzOkText: '完善资料',
      nzCancelText: '退出登录',
      nzOnOk: () => {
        this.initForm({});
        this.isVisible = true;
      },
      nzOnCancel: () => {
        this.basicService.logOut();
      }
    });
  }

  /**
   * 初始化区域
   */
  initArea() {
    this.homeService.getMyAppAreaList('root').subscribe(res => {
      for (const arr of res) {
        arr.value = arr.code;
      }
      this.provinceOptions = res;
    });
  }

}
