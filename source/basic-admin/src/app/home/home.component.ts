import {HttpClient} from '@angular/common/http';
import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {NzDropdownMenuComponent, NzIconService, NzMessageService, NzModalService} from 'ng-zorro-antd';
import {environment} from '../../environments/environment';
import {AppService} from '../app.service';
import {HomeService} from './home.service';
import {BasicService} from '../basic/basic.service';
import {BUTTON_CODE} from '../config/button-code';
import {OverallSelectComponent} from '../../../projects/xc-common/src/public-api';
import {PersonalInfoService} from './personal-info/personal-info.service';
import {LoginTool, CommonTool, DictTool} from '@ccxc/tool';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  public menus: Array<any>;
  public isCollapsed: boolean;
  public isPassWordVisible: boolean;
  public setupPasswordVisible: boolean;
  public passwordInfo: {
    password: string
    newPassword: string
    againPassword: string,
  };
  public passwordConfig: {
    password: boolean,
    newPassword: boolean
    againPassword: boolean
    passwordType: string,
  };
  public isUpdateSecret: boolean;
  public url = environment.config.url + '/share/page/';
  public tokenInfo: any;
  public userInfo: any;
  public BUTTON_CODE = BUTTON_CODE;
  @ViewChild(OverallSelectComponent, {static: true})
  public overallSelectComponent: OverallSelectComponent;
  public appId; // 应用主键
  public appList = [];
  @ViewChild(OverallSelectComponent, {static: true})
  public menuDom: NzDropdownMenuComponent;
  public secretInfo: { isVisible: boolean, accessId: string, accessSecret: string };

  constructor(private router: Router,
              private http: HttpClient,
              public appService: AppService,
              public homeService: HomeService,
              public basicService: BasicService,
              public iconService: NzIconService,
              private message: NzMessageService,
              public activatedRoute: ActivatedRoute,
              public modalService: NzModalService,
              public personalInfoService: PersonalInfoService) {
    this.secretInfo = {isVisible: false, accessId: '', accessSecret: ''};
    // 获取应用信息
    let iconUrl = environment.config.iconUrl;
    if (!iconUrl) {
      iconUrl = './assets/icon/iconfont.js';
    }
    this.iconService.fetchFromIconfont({
      scriptUrl: iconUrl
    });
    this.tokenInfo = LoginTool.getToken();
    // 获取用户信息
    this.userInfo = {};
    this.homeService.getMyAppUser().subscribe((res) => {
      this.userInfo = res;
      this.homeService.getCurrentUserInfo().subscribe(data => {
        if (data) {
          Object.assign(this.userInfo, data ? data : {});
        } else {
          this.personalInfoService.perfectData();
        }
      });
    });
    this.isPassWordVisible = false;
    this.setupPasswordVisible = false;
    this.passwordInfo = {password: '', newPassword: '', againPassword: ''};
    this.passwordConfig = {password: false, newPassword: false, againPassword: false, passwordType: ''};
    this.isUpdateSecret = false;
  }

  ngOnInit() {
    // 查询用户应用
    const that = this;
    const config = {
      openFun: (initOptionsFun) => {
        // 查询用户应用
        that.homeService.getCurrentUserAppList({}).subscribe(res => {
          that.appList = res;
          const array = [{name: '无', value: ''}];
          for (const data of res) {
            array.push({
              name: data.appName,
              value: data.id
            });
          }
          initOptionsFun(array);
        });
      }
    };
    this.overallSelectComponent.init(config);
  }

  /**
   * 初始化基础数据
   */
  public initBasicData($event) {
    if (this.appId !== $event) {
      this.appId = $event;
      const that = this;
      if (this.appId) {
        const app = this.appList.find((data) => data.id === this.appId);
        LoginTool.setGroupId(app.groupId);
      }
      this.basicService.getMyAppMenuList().subscribe(menus => {
        // 菜单处理
        const menuList = that.basicService.menuHandle(menus);
        if (menuList) {
          // 菜单存储
          that.menus = menuList;
          const appId = this.activatedRoute.snapshot.queryParams.appId;
          if (this.appId && appId !== this.appId) {
            that.jumpApp('/home');
          }
        }
      });
    }
  }

  /**
   * 验证按钮权限
   */
  public verifyButtonAuthority(code) {
    return LoginTool.verifyAuthority(code);
  }

  /**
   * 打开菜单
   */
  public toggleCollapsed() {
    this.isCollapsed = !this.isCollapsed;
  }

  /**
   * 修改密码关闭
   */
  public modifyPassWordCancel() {
    this.isPassWordVisible = false;
    this.passwordInfo = {password: '', newPassword: '', againPassword: ''};
    this.passwordConfig = {password: false, newPassword: false, againPassword: false, passwordType: ''};
  }

  /**
   * 修改密码提交
   */
  public modifyPassWordOk(data) {
    if (data.newPassword === data.password) {
      this.message.warning('新密码不能和旧密码相同！');
      return;
    }
    this.appService.setLoading(true);
    this.homeService.updateMyAppUserPassword({
      newPassword: data.newPassword,
      password: data.password ? data.password : null
    }).subscribe(() => {
      this.passwordInfo = {
        password: '',
        newPassword: '',
        againPassword: ''
      };
      this.userInfo['passwordExist'] = true;
      this.appService.setLoading(false);
      this.message.success('密码修改成功！');
      this.isPassWordVisible = false;
      this.basicService.logOut();
    });
  }

  /**
   * 退出登录
   */
  public logOut() {
    this.basicService.logOut();
  }

  /**
   * 跳转到个人信息
   */
  public jumpPersonal(url) {
    this.router.navigateByUrl(url);
  }

  /**
   * 获取访问秘钥
   */
  public getAccessSecret() {
    this.appService.setLoading(true);
    this.homeService.getCurrentUserAccessSecret().subscribe(res => {
      this.appService.setLoading(false);
      this.secretInfo = {
        accessId: this.userInfo.accessId,
        isVisible: true,
        accessSecret: res['accessSecret']
      };
    });
  }

  /**
   * 更新访问秘钥
   */
  public updateAccessSecret(secretInfo) {
    this.modalService.confirm({
      nzTitle: '确定要更新秘钥吗?',
      nzContent: '<b style="color: red;">更新秘钥后无法回退原秘钥哦！</b>',
      nzOkText: '确认',
      nzOkType: 'danger',
      nzOnOk: () => {
        this.appService.setLoading(true);
        this.homeService.updateCurrentUserAccessSecret().subscribe(res => {
          this.appService.setLoading(false);
          this.message.success('更新秘钥成功！');
          secretInfo['accessSecret'] = res['accessSecret'];
        });
      },
      nzCancelText: '取消'
    });
  }

  /**
   * 跳转
   */
  public jumpApp(url) {
    if (url) {
      const httpUrl = CommonTool.analysisParam(url, {appId: this.appId});
      this.router.navigateByUrl(httpUrl);
    }
  }
}
