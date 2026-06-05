import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CommonTool} from '@ccxc/tool';
import {HTTP_URLS} from '../../config/app-http.url';

@Injectable({
  providedIn: 'root'
})
export class AuditManagementService {

  constructor(private http: HttpClient) {
  }

  /**
   * 获取应用的分页数据
   */
  public getManageAuditAppPage(current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getManageAuditAppPage, {current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 获取审核应用
   */
  public getManageAuditApp(appId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.getManageAuditApp, {appId});
    return this.http.get<any>(url);
  }

  /**
   * 审核应用
   */
  public updateManageAuditApp(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateManageAuditApp, {appId});
    return this.http.put(url, req);
  }

  /**
   * 审核权限列表分页查询
   */
  public getAuditAppAuthorizePage(current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAuditAppAuthorizePage, {current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }

  /**
   * 获取审核应用权限
   */
  public getAuditAppAuthorize(authorizeId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.getAuditAppAuthorize, {authorizeId});
    return this.http.get<any>(url);
  }

  /**
   * 审核应用权限
   */
  public updateAuditAppAuthorize(authorizeId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAuditAppAuthorize, {authorizeId});
    return this.http.put(url, req);
  }
}
