import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {HTTP_URLS} from '../../config/app-http.url';
import {CommonTool} from '@ccxc/tool';

@Injectable({
  providedIn: 'root'
})
export class ApiManagementService {
  constructor(private http: HttpClient) {
  }

  /***********************************接口供应商******************************************/
  /**
   * 消息分页
   */
  public getAppApiSupplierPage(appId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppApiSupplierPage, {appId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }

  /**
   * 创建消息
   */
  public createAppApiSupplier(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppApiSupplier, {appId});
    return this.http.post(url, req);
  }

  /**
   * 修改消息
   */
  public updateAppApiSupplier(appId, apiSupplierId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppApiSupplier, {appId, apiSupplierId});
    return this.http.put(url, req);
  }

  /**
   * 删除消息
   */
  public deleteAppApiSupplier(appId, apiSupplierId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.deleteAppApiSupplier, {appId, apiSupplierId});
    return this.http.delete<any>(url);
  }

  /**
   * 创建应用消息集合
   */
  public createAppApiSupplierList(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppApiSupplierList, {appId});
    return this.http.post(url, req);
  }

  /**
   * 获取应用的消息集合
   */
  public getAppApiSupplierList(appId, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppApiSupplierList, {appId});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }


  /***********************************消息模板******************************************/

  /**
   * 消息分页
   */
  public getAppMessageTemplatePage(appId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppMessageTemplatePage, {appId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 创建消息
   */
  public createAppMessageTemplate(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppMessageTemplate, {appId});
    return this.http.post(url, req);
  }

  /**
   * 修改消息
   */
  public updateAppMessageTemplate(appId, messageTemplateId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppMessageTemplate, {appId, messageTemplateId});
    return this.http.put(url, req);
  }

  /**
   * 删除消息
   */
  public deleteAppMessageTemplate(appId, messageTemplateId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.deleteAppMessageTemplate, {appId, messageTemplateId});
    return this.http.delete<any>(url);
  }

  /**
   * 创建应用消息集合
   */
  public createAppMessageTemplateList(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppMessageTemplateList, {appId});
    return this.http.post(url, req);
  }

  /**
   * 获取应用的消息集合
   */
  public getAppMessageTemplateList(appId, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppMessageTemplateList, {appId});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }

  /***********************************消息日志******************************************/
  /**
   * 消息日志分页
   */
  public getAppMessageLogPage(appId, messageTemplateId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppMessageLogPage, {appId, messageTemplateId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

}
