import {Component, OnInit} from '@angular/core';
import {HomeService} from '../../home.service';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {AppService} from '../../../app.service';
import {BUTTON_CODE} from '../../../config/button-code';
import {BasicService} from '../../../basic/basic.service';
import {TableDetailModel} from '../../../../../projects/xc-common/src/common/table-detail/table-detail.model';
import {ArrayConfig, FormInfoModel} from '../../../../../projects/xc-common/src/common/form-info/form-info.model';
import {CommonTool, FileTool, UploadTool} from '@ccxc/tool';
import {DomSanitizer} from '@angular/platform-browser';
import {FILE_FORMAT} from '../../../config/enum';

@Component({
  selector: 'app-personal-info-details',
  templateUrl: './personal-info-details.component.html',
  styleUrls: ['./personal-info-details.component.scss']
})
export class PersonalInfoDetailsComponent implements OnInit {

  public tableDetailModel: TableDetailModel;
  public formInfoModel: FormInfoModel;
  public isVisible = false;
  public userId;
  public editBasicUser: {
    isVisible: boolean
    messageCode: string
    accountType: string
    account: any
    captcha: string
    confirmCaptcha: string
    confirmType: string
  } = {
    isVisible: false,
    messageCode: null,
    accountType: '',
    account: '',
    captcha: '',
    confirmCaptcha: '',
    confirmType: ''
  };
  public captchaInfo: {
    [messageCode: string]: {
      isCaptcha
      interval
      buttonName
      code
      imgUrl,
      captcha,
    }
  } = {};
  public logOffBasic: {
    isVisible: boolean
    messageCode: string
    confirmType: string
    account: string
    captcha: string
  } = {
    isVisible: false,
    messageCode: '',
    confirmType: '',
    account: '',
    captcha: ''
  };
  public userCaptcha: {
    isVisible: boolean
    messageCode: string
    confirmType: string
    accountType: string
    account: string
    captcha: string
  } = {
    isVisible: false,
    messageCode: null,
    confirmType: '',
    accountType: '',
    account: '',
    captcha: ''
  };
  public countyOptions = [];
  public provinceOptions = [];
  public cityOptions = [];
  public defaultPortrait = './assets/images/user.png';

  constructor(public homeService: HomeService,
              public message: NzMessageService,
              public appService: AppService,
              public basicService: BasicService,
              private sanitizer: DomSanitizer,
              public modalService: NzModalService) {
    this.tableDetailModel = new TableDetailModel();
    this.formInfoModel = new FormInfoModel();
    const that = this;
    const COMMON_DETAIL = TableDetailModel.COMMON_DETAIL
    this.tableDetailModel.setConfig({
      revertName: '返回',
      revertUrl: '/home',
      modules: [
        {name: '基础信息', type: COMMON_DETAIL.header},
        {
          name: '账号', field: 'account', type: COMMON_DETAIL.block,
          operate: {
            name: '注销',
            style: {marginLeft: '5px'},
            bAuthority: BUTTON_CODE.b_delete_user,
            confirmFun(formInfo) {
              that.modalService.confirm({
                nzTitle: '确认要注销吗?',
                nzContent: '<b style="color: red;">注销以后将删除你在本平台的所有数据哦！</b>',
                nzOkText: '确认',
                nzOkType: 'danger',
                nzOnOk: () => {
                  if (formInfo.phone) {
                    that.logOffBasic = {
                      isVisible: true,
                      messageCode: 'phone_unsubscribe',
                      confirmType: 'phone',
                      account: formInfo.phone,
                      captcha: ''
                    };
                  } else if (formInfo.email) {
                    that.logOffBasic = {
                      isVisible: true,
                      messageCode: 'email_unsubscribe',
                      confirmType: 'email',
                      account: formInfo.email,
                      captcha: ''
                    };
                  } else {
                    that.message.info('该账号未绑定手机或邮箱，无法注销！');
                  }
                  const messageCode = that.logOffBasic.messageCode;
                  if (that.captchaInfo[messageCode]) {
                    that.captchaInfo[messageCode].isCaptcha = true;
                    that.captchaInfo[messageCode].buttonName = '获取验证码';
                    clearInterval(that.captchaInfo[messageCode].interval);
                  }
                },
                nzCancelText: '取消'
              });
            }
          }
        },
        {
          name: '邮箱', field: 'email', type: COMMON_DETAIL.block,
          operate: {
            name: '修改邮箱',
            style: {marginLeft: '5px'},
            bAuthority: BUTTON_CODE.b_update_email,
            confirmFun(formInfo) {
              if (formInfo.email) {
                that.userCaptcha = {
                  messageCode: 'email_update_email',
                  isVisible: true,
                  account: formInfo.email,
                  captcha: '',
                  confirmType: 'email',
                  accountType: 'email'
                };
              } else if (formInfo.phone) {
                that.userCaptcha = {
                  messageCode: 'phone_update_email',
                  isVisible: true,
                  account: formInfo.phone,
                  captcha: '',
                  confirmType: 'phone',
                  accountType: 'email'
                };
              } else {
                that.editBasicUser = {
                  messageCode: 'email_update',
                  isVisible: true,
                  account: '',
                  captcha: '',
                  confirmCaptcha: null,
                  confirmType: null,
                  accountType: 'email'
                };
              }
              const messageCode = that.editBasicUser.messageCode;
              if (that.captchaInfo[messageCode]) {
                that.captchaInfo[messageCode].isCaptcha = true;
                that.captchaInfo[messageCode].buttonName = '获取验证码';
                clearInterval(that.captchaInfo[messageCode].interval);
              }
            }
          }
        },
        {
          name: '手机号', field: 'phone', type: COMMON_DETAIL.block,
          operate: {
            name: '修改手机号',
            style: {marginLeft: '5px'},
            bAuthority: BUTTON_CODE.b_update_phone,
            confirmFun(formInfo) {
              if (formInfo.phone) {
                that.userCaptcha = {
                  messageCode: 'phone_update_phone',
                  isVisible: true,
                  account: formInfo.phone,
                  captcha: '',
                  confirmType: 'phone',
                  accountType: 'phone'
                };
              } else if (formInfo.email) {
                that.userCaptcha = {
                  messageCode: 'email_update_phone',
                  isVisible: true,
                  account: formInfo.email,
                  captcha: '',
                  confirmType: 'email',
                  accountType: 'phone'
                };
              } else {
                that.editBasicUser = {
                  messageCode: 'phone_update',
                  isVisible: true,
                  account: '',
                  captcha: '',
                  confirmCaptcha: null,
                  confirmType: null,
                  accountType: 'phone'
                };
              }
              const messageCode = that.editBasicUser.messageCode;
              if (that.captchaInfo[messageCode]) {
                that.captchaInfo[messageCode].isCaptcha = true;
                that.captchaInfo[messageCode].buttonName = '获取验证码';
                clearInterval(that.captchaInfo[messageCode].interval);
              }
            }
          }
        },
        {name: '初始化管理员', field: 'initialAdmin', type: COMMON_DETAIL.dict, dictType: 'whether'},
        {
          name: '个人信息', type: COMMON_DETAIL.header,
          blockStyle: {marginTop: '10px'},
          operate: {
            name: '修改资料',
            bAuthority: BUTTON_CODE.b_update_user_info,
            style: {fontSize: '14px', marginLeft: '5px'},
            confirmFun(formInfo) {
              that.isVisible = true;
              that.initForm(that.tableDetailModel.formInfo);
            }
          }
        },
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
   * 切换验证码
   */
  switchImg(messageCode) {
    this.homeService.getMyAppImgCaptcha().subscribe(data => {
      this.captchaInfo[messageCode].code = data['code'];
      const url = URL.createObjectURL(CommonTool.dataURItoBlob(data['imgBytes']));
      this.captchaInfo[messageCode].imgUrl = this.sanitizer.bypassSecurityTrustUrl(url);
    });
  }

  /**
   * 获取数据
   */
  getData() {
    this.homeService.getMyAppUser().subscribe(res => {
      this.userId = res.id;
      this.tableDetailModel.setFormInfo(res);
      this.homeService.getCurrentUserInfo().subscribe(form => {
        Object.assign(this.tableDetailModel.formInfo, form ? form : {});
        const formInfo = this.tableDetailModel.formInfo;
        if (this.tableDetailModel.formInfo['email']) {
          this.tableDetailModel.setModule('email', 'operate.name', '修改邮箱');
        } else {
          this.tableDetailModel.setModule('email', 'operate.name', '设置邮箱');
        }
        if (this.tableDetailModel.formInfo['phone']) {
          this.tableDetailModel.setModule('phone', 'operate.name', '修改手机号');
        } else {
          this.tableDetailModel.setModule('phone', 'operate.name', '设置手机号');
        }
        this.initArea(formInfo);
        if (!this.tableDetailModel.formInfo.portrait) {
          this.tableDetailModel.formInfo.portrait = this.defaultPortrait;
        }
        formInfo.birthday = formInfo.birthday ? CommonTool.turnDate(formInfo.birthday) : null;
      });
    });
  }

  /**
   * 初始化区域
   */
  initArea(form) {
    if (form['region']) {
      this.homeService.getMyAppAreaNodeList(form['region']).subscribe(res => {
        this.tableDetailModel.formInfo['regionName'] = res.map(e => e.name).join('');
        for (const arr of res) {
          arr.value = arr.code;
        }
        if (res.length > 0) {
          const obj = res[0];
          this.tableDetailModel.formInfo['province'] = obj.value;
          this.provinceOptions = [obj];
        }
        if (res.length > 1) {
          const obj = res[1];
          this.tableDetailModel.formInfo['city'] = obj.value;
          this.cityOptions = [obj];

        }
        if (res.length > 2) {
          const obj = res[2];
          this.tableDetailModel.formInfo['county'] = obj.value;
          this.countyOptions = [obj];
        }
      });
    }
  }

  /**
   * 初始化表单
   */
  initForm(form) {
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
                    myThis.homeService.getMyAppAreaList('root').subscribe(res => {
                      const array = [];
                      for (const option of res) {
                        option.value = option.code;
                        if (option.status === '0') {
                          array.push(option);
                        }
                      }
                      this.options = array;
                    });
                  }
                },
                confirmFun(formInfo, that) {
                  formInfo.county = null;
                  formInfo.city = null;
                  for (const item of that.selectItem) {
                    if (item.field === 'city') {
                      item.options = [];
                    }
                    if (item.field === 'county') {
                      item.options = [];
                    }
                  }
                }
              },
              {
                name: '市',
                field: 'city',
                options: this.cityOptions,
                initOptionFun($event, formInfo, that) {
                  if ($event) {
                    myThis.homeService.getMyAppAreaList(formInfo.province).subscribe(res => {
                      const array = [];
                      for (const option of res) {
                        option.value = option.code;
                        if (option.status === '0') {
                          array.push(option);
                        }
                      }
                      this.options = array;
                    });
                  }
                },
                confirmFun(formInfo, that) {
                  formInfo.county = null;
                  for (const item of that.selectItem) {
                    if (item.field === 'county') {
                      item.options = [];
                    }
                  }
                }
              },
              {
                name: '县/区',
                field: 'county',
                options: this.countyOptions,
                initOptionFun($event, formInfo, that) {
                  if ($event) {
                    myThis.homeService.getMyAppAreaList(formInfo.city).subscribe(res => {
                      const array = [];
                      for (const option of res) {
                        option.value = option.code;
                        if (option.status === '0') {
                          array.push(option);
                        }
                      }
                      this.options = array;
                    });
                  }
                }
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
      $this.uploadFolderFile(file, {
        folderPath: '/user/' + $this.userId + '/logo',
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
   * 提交
   */
  submit(formInfo) {
    const req = {
      nickName: formInfo['nickName'],
      portrait: formInfo['portrait'],
      explain: formInfo['explain'],
      sex: formInfo['sex'],
      birthday: CommonTool.turnDate(formInfo['birthday']),
      region: formInfo['county'] ? formInfo['county'] : ''
    };
    this.appService.setLoading(true);
    this.homeService.updateCurrentUserInfo(req).subscribe(res => {
      this.isVisible = false;
      this.getData();
      this.appService.setLoading(false);
      this.message.success('编辑成功！');
    });
  }

  /**
   * 获取验证码
   */
  public sendCaptcha(accountType, messageCode, account) {
    if (!this.captchaInfo[messageCode]) {
      this.captchaInfo[messageCode] = {
        isCaptcha: true,
        interval: null,
        buttonName: '获取验证码',
        code: null,
        imgUrl: null,
        captcha: null
      };
    }
    if (this.captchaInfo[messageCode].isCaptcha) {
      this.captchaInfo[messageCode].isCaptcha = false;
      this.appService.setLoading(true);
      const req = {account};
      if (this.captchaInfo[messageCode].code) {
        req['code'] = this.captchaInfo[messageCode].code;
        req['captcha'] = this.captchaInfo[messageCode].captcha;
      }
      this.homeService.createMyAppCaptcha(messageCode, accountType, req).subscribe(res => {
        this.appService.setLoading(false);
        if (res['openImgCaptcha']) {
          this.captchaInfo[messageCode].isCaptcha = true;
          if (this.captchaInfo[messageCode].code) {
            this.message.error('图片验证码错误！');
            return;
          }
          this.switchImg(messageCode);
        } else {
          this.captchaSendSuccess(messageCode);
        }
      }, () => {
        this.captchaInfo[messageCode].isCaptcha = true;
        this.switchImg(messageCode);
      });
    }
  }

  /**
   * 验证码发送成功
   */
  public captchaSendSuccess(messageCode) {
    let index = 60;
    this.captchaInfo[messageCode].interval = setInterval(() => {
      index--;
      if (index <= 0) {
        this.captchaInfo[messageCode].isCaptcha = true;
        this.captchaInfo[messageCode].buttonName = '获取验证码';
        clearInterval(this.captchaInfo[messageCode].interval);
      } else {
        this.captchaInfo[messageCode].buttonName = index + '秒后重试';
      }
    }, 1000);
  }

  /**
   * 获取验证码
   */
  public sendUserCaptcha(messageCode, confirmType) {
    if (!this.captchaInfo[messageCode]) {
      this.captchaInfo[messageCode] = {
        isCaptcha: true,
        interval: null,
        buttonName: '获取验证码',
        code: null,
        imgUrl: null,
        captcha: null
      };
    }
    if (this.captchaInfo[messageCode].isCaptcha) {
      this.captchaInfo[messageCode].isCaptcha = false;
      this.appService.setLoading(true);
      this.homeService.createMyAppUserCaptcha(messageCode, confirmType).subscribe(res => {
        this.appService.setLoading(false);
        this.captchaSendSuccess(messageCode);
      }, () => {
        this.captchaInfo[messageCode].isCaptcha = true;
      });
    }
  }

  /**
   * 修改手机号/邮箱确认提交
   */
  public confirmOk(data) {
    if (data.accountType === 'phone') {
      this.appService.setLoading(true);
      this.homeService.updateMyAppUserPhone({
        phone: data.account,
        captcha: data.captcha,
        confirmCaptcha: data.confirmCaptcha,
        confirmAccountType: data.confirmType
      }).subscribe(res => {
        this.editBasicUser.isVisible = false;
        this.appService.setLoading(false);
        this.tableDetailModel.formInfo.phone = res.phone;
        this.tableDetailModel.setModule('phone', 'operate.name', '修改手机号');
        this.message.success('操作成功！');
      });
    } else {
      this.appService.setLoading(true);
      this.homeService.updateMyAppUserMail({
        email: data.account,
        captcha: data.captcha,
        confirmCaptcha: data.confirmCaptcha,
        confirmAccountType: data.confirmType
      }).subscribe(res => {
        this.editBasicUser.isVisible = false;
        this.appService.setLoading(false);
        this.tableDetailModel.formInfo.email = res.email;
        this.tableDetailModel.setModule('email', 'operate.name', '修改邮箱');
        this.message.success('操作成功！');
      });
    }
  }

  /**
   * 切换手机号或邮箱
   */
  public switchMode(data) {
    const user = this.tableDetailModel.formInfo;
    if (data.confirmType === 'email') {
      if (!user.phone) {
        this.message.success('你的手机号为空，无法切换，请先绑定手机号！');
        return;
      }
      data.confirmType = 'phone';
      data.account = user.phone;
    } else if (data.confirmType === 'phone') {
      if (!user.email) {
        this.message.success('你的邮箱为空，无法切换，请先绑定邮箱！');
        return;
      }
      data.confirmType = 'email';
      data.account = user.email;
    }
    if (data.messageCode === 'phone_unsubscribe') {
      data.messageCode = 'email_unsubscribe';
    } else if (data.messageCode === 'email_unsubscribe') {
      data.messageCode = 'phone_unsubscribe';
    } else if (data.messageCode === 'email_update_email') {
      data.messageCode = 'phone_update_email';
    } else if (data.messageCode === 'phone_update_email') {
      data.messageCode = 'email_update_email';
    } else if (data.messageCode === 'phone_update_phone') {
      data.messageCode = 'email_update_phone';
    } else if (data.messageCode === 'email_update_phone') {
      data.messageCode = 'phone_update_phone';
    }
  }


  /**
   * 确认注销
   */
  public logOffConfirmOk(logOffBasic) {
    this.homeService.deleteMyAppUser(logOffBasic.captcha, logOffBasic.confirmType).subscribe(res => {
      this.logOffBasic.isVisible = false;
      this.appService.setLoading(false);
      this.message.success('操作成功！');
      this.basicService.logOut();
    });
  }

  /**
   * 验证用户
   */
  public verifyUser(data) {
    this.homeService.getMyAppUserCaptcha(data.messageCode, data.captcha, '1').subscribe(res => {
      this.userCaptcha.isVisible = false;
      if (data.accountType === 'email') {
        this.editBasicUser = {
          messageCode: 'email_update',
          isVisible: true,
          account: '',
          captcha: '',
          confirmCaptcha: data.captcha,
          confirmType: data.confirmType,
          accountType: 'email'
        };
      } else if (data.accountType === 'phone') {
        this.editBasicUser = {
          messageCode: 'phone_update',
          isVisible: true,
          account: '',
          captcha: '',
          confirmCaptcha: data.captcha,
          confirmType: data.confirmType,
          accountType: 'phone'
        };
      }
    });
  }
}
