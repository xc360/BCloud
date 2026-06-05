import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {HTTP_URLS} from '../config/app-http.url';
import {CommonTool} from '@ccxc/tool';
import {NzMessageService} from 'ng-zorro-antd';

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  public appInfo = {};

  constructor(private http: HttpClient,
              private message: NzMessageService) {

  }

  /**
   * 复制
   */
  public copy(data) {
    const input = document.createElement('input');
    input.value = data;
    document.body.appendChild(input);
    input.select();
    if (document.execCommand('Copy')) {
      this.message.success('复制成功！');
    } else {
      this.message.success('复制失败！');
    }
    input.style.display = 'none';
  }

  /**
   * 设置app
   */
  public setAppInfo(appInfo) {
    this.appInfo = appInfo;
  }

  /**
   * 更具key获取数据
   */
  public getAppInfoValue(key) {
    return this.appInfo[key];
  }

  /**
   * 修改密码
   */
  public updateMyAppUserPassword(req: { newPassword, password }): Observable<any> {
    return this.http.put<any>(HTTP_URLS.updateMyAppUserPassword, req);
  }

  /**
   * 获取用户信息
   */
  public getMyAppUser(): Observable<any> {
    return this.http.get<any>(HTTP_URLS.getMyAppUser);
  }

  /**
   * 获取当前用户信息
   */
  public getCurrentUserInfo() {
    return this.http.get<any>(HTTP_URLS.getCurrentUserInfo);
  }

  /**
   * 修改当前用户信息
   */
  public updateCurrentUserInfo(req) {
    return this.http.put<any>(HTTP_URLS.updateCurrentUserInfo, req);
  }

  /**
   * 获取区域集合
   */
  public getMyAppAreaList(firstCode) {
    const url = CommonTool.analysisParam(HTTP_URLS.getMyAppAreaList, {firstCode});
    return this.http.get<any>(url);
  }

  /**
   * 区域节点集合
   */
  public getMyAppAreaNodeList(code) {
    const url = CommonTool.analysisParam(HTTP_URLS.getMyAppAreaNodeList, {code});
    return this.http.get<any>(url);
  }

  /**
   * 获取图片验证码
   */
  public getMyAppImgCaptcha() {
    return this.http.get<any>(HTTP_URLS.getMyAppImgCaptcha);
  }

  /**
   * 发送验证码
   */
  public createMyAppCaptcha(messageCode, accountType, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createMyAppCaptcha, {messageCode, accountType});
    return this.http.post<any>(url, req);
  }

  /**
   * 发送用户验证码
   */
  public createMyAppUserCaptcha(messageCode, accountType) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createMyAppUserCaptcha, {messageCode, accountType});
    return this.http.post<any>(url, {});
  }

  /**
   * 获取用户验证码信息
   */
  public getMyAppUserCaptcha(messageCode, captcha, isDelete) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getMyAppUserCaptcha, {messageCode});
    url = CommonTool.analysisParam(url, {captcha, isDelete});
    return this.http.get<any>(url);
  }

  /**
   * 修改用户手机号
   */
  public updateMyAppUserMail(req) {
    return this.http.put<any>(HTTP_URLS.updateMyAppUserMail, req);
  }

  /**
   * 修改用户邮箱
   */
  public updateMyAppUserPhone(req) {
    return this.http.put<any>(HTTP_URLS.updateMyAppUserPhone, req);
  }

  /**
   * 注销账号
   */
  public deleteMyAppUser(captcha, accountType) {
    const url = CommonTool.analysisParam(HTTP_URLS.deleteMyAppUser, {captcha, accountType});
    return this.http.delete<any>(url);
  }

  /**
   * 获取应用集合
   */
  public getCurrentUserAppList(req) {
    const url = CommonTool.analysisParam(HTTP_URLS.getCurrentUserAppList, req);
    return this.http.get<any>(url);
  }

  /**
   * 创建app文件
   */
  public getMyAppUploadFileSign() {
    return this.http.get<any>(HTTP_URLS.getMyAppUploadFileSign);
  }

  /**
   * 获取字典集合
   */
  public getMyAppDictList() {
    return this.http.get(HTTP_URLS.getMyAppDictList);
  }

  /**
   * 获取当前用户访问秘钥
   */
  public getCurrentUserAccessSecret() {
    return this.http.get(HTTP_URLS.getCurrentUserAccessSecret);
  }

  /**
   * 更新当前用户访问秘钥
   */
  public updateCurrentUserAccessSecret() {
    return this.http.put(HTTP_URLS.updateCurrentUserAccessSecret, {});
  }

}
