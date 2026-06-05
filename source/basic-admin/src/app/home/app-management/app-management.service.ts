import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {HTTP_URLS} from '../../config/app-http.url';
import {CommonTool} from '@ccxc/tool';

@Injectable({
  providedIn: 'root'
})
export class AppManagementService {

  constructor(private http: HttpClient) {
  }

  /**
   * 获取当前用户应用的分页数据
   */
  public getCurrentUserAppPage(current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getCurrentUserAppPage, {current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 创建当前用户的应用
   */
  public createCurrentUserApp(req) {
    return this.http.post(HTTP_URLS.createCurrentUserApp, req);
  }

  /**
   * 发布应用
   */
  public updateCurrentUserApp(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateCurrentUserApp, {appId});
    return this.http.put(url, req);
  }

  /**
   * 删除当前用户的应用
   */
  public deleteCurrentUserApp(appId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.deleteCurrentUserApp, {appId});
    return this.http.delete(url);
  }

  /**
   * 刷新应用
   */
  public updateCurrentUserAppRefresh(appId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateCurrentUserAppRefresh, {appId});
    return this.http.put(url, {});
  }

  /**
   * 获取当前用户应用的秘钥
   */
  public getAppSecret(appId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.getAppSecret, {appId});
    return this.http.get(url);
  }

  /**
   * 获取当前用户应用的秘钥
   */
  public updateAppSecret(appId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppSecret, {appId});
    return this.http.put(url, {});
  }

  /**
   * 获取当前用户应用拥有的的权限
   */
  public getAppAuthorizeAuthorityPage(appId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppAuthorizeAuthorityPage, {appId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 创建当前用户应用拥有的的权限
   */
  public updateAppAuthorize(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppAuthorize, {appId});
    return this.http.put(url, req);
  }

  /**
   * 获取应用集合字典
   */
  public getAppList() {
    return this.http.get<any>(HTTP_URLS.getAppList);
  }

  /**
   * 获取用户组集合
   */
  public getMyAppUserGroupList() {
    return this.http.get<any>(HTTP_URLS.getMyAppUserGroupList);
  }
}
