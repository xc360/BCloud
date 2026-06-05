import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CommonTool} from '@ccxc/tool';
import {HTTP_URLS} from '../../config/app-http.url';

@Injectable({
  providedIn: 'root'
})
export class AuthorizeManagementService {

  constructor(private http: HttpClient) {
  }

  /**
   * 用户授权分页
   */
  public getCurrentUserAuthorizePage(current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getCurrentUserAuthorizePage, {current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 用户取消授权
   */
  public deleteCurrentUserAuthorize(userAuthorizeId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.deleteCurrentUserAuthorize, {userAuthorizeId});
    return this.http.delete(url);
  }


  /**
   * 用户授权权限分页
   */
  public getCurrentUserAuthorizeAuthorityPage(userAuthorizeId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getCurrentUserAuthorizeAuthorityPage, {userAuthorizeId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 用户取消权限授权
   */
  public deleteCurrentUserAuthorizeAuthority(userAuthorizeId, userAuthorizeAuthorityId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.deleteCurrentUserAuthorizeAuthority, {userAuthorizeId, userAuthorizeAuthorityId});
    return this.http.delete(url);
  }
}
