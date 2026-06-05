import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {HTTP_URLS} from '../../config/app-http.url';
import {CommonTool} from '@ccxc/tool';

@Injectable({
  providedIn: 'root'
})
export class WebsiteManagementService {

  constructor(private http: HttpClient) {
  }

  // region*******************************菜单管理-开始***************************************
  /**
   * 获取应用的菜单分页数据
   */
  public getAppMenuPage(appId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppMenuPage, {appId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 创建应用的菜单
   */
  public createAppMenu(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppMenu, {appId});
    return this.http.post(url, req);
  }

  /**
   * 修改应用的菜单
   */
  public updateAppMenu(appId, menuId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppMenu, {appId, menuId});
    return this.http.put(url, req);
  }

  /**
   * 删除应用的菜单
   */
  public deleteAppMenu(appId, menuId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.deleteAppMenu, {appId, menuId});
    return this.http.delete(url);
  }

  /**
   * 创建应用菜单集合
   */
  public createAppMenuList(appId, types, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.createAppMenuList, {appId});
    url = CommonTool.analysisParam(url, {types});
    return this.http.post(url, req);
  }

  /**
   * 获取应用的所有菜单
   * 根据条件获取当前用户的应用下的所有菜单
   */
  public getAppMenuList(appId, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppMenuList, {appId});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }

  /**
   * 上移菜单
   */
  public updateAppMenuUp(appId, menuId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppMenuUp, {appId, menuId});
    return this.http.put(url, {});
  }

  /**
   * 下移菜单
   */
  public updateAppMenuDown(appId, menuId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppMenuDown, {appId, menuId});
    return this.http.put(url, {});
  }

  // endregion*******************************菜单管理-结束***************************************
  // region*******************************页面管理-开始***************************************
  /**
   * 页面分页
   */
  public getAppPagePage(appId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppPagePage, {appId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 创建页面
   */
  public createAppPage(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppPage, {appId});
    return this.http.post(url, req);
  }

  /**
   * 修改页面
   */
  public updateAppPage(appId, pageId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppPage, {appId, pageId});
    return this.http.put(url, req);
  }

  /**
   * 删除页面
   */
  public deleteAppPage(appId, pageId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.deleteAppPage, {appId, pageId});
    return this.http.delete<any>(url);
  }

  /**
   * 页面集合
   */
  public getAppPageList(appId, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppPageList, {appId});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }

  /**
   * 批量创建页面
   */
  public createAppPageList(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppPageList, {appId});
    return this.http.post(url, req);
  }

  /**
   * 获取的页面栏目关联集合
   */
  public getAppPageColumnRelationList(appId, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppPageColumnRelationList, {appId});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }

  /**
   * 获取的页面栏目关联集合
   */
  public createAppPageColumnRelationList(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppPageColumnRelationList, {appId});
    return this.http.post(url, req);
  }

  // endregion*******************************页面管理-结束***************************************
  // region*******************************数据类型-开始***************************************
  /**
   * 数据类型分页
   */
  public getAppDataTypePage(appId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppDataTypePage, {appId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 数据类型集合
   */
  public getAppDataTypeList(appId, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppDataTypeList, {appId});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }

  /**
   * 创建数据类型
   */
  public createAppDataType(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppDataType, {appId});
    return this.http.post(url, req);
  }

  /**
   * 修改数据类型
   */
  public updateAppDataType(appId, dataTypeId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppDataType, {appId, dataTypeId});
    return this.http.put(url, req);
  }

  /**
   * 删除数据类型
   */
  public deleteAppDataType(appId, dataTypeId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.deleteAppDataType, {appId, dataTypeId});
    return this.http.delete<any>(url);
  }

  /**
   * 批量创建数据类型
   */
  public createAppDataTypeList(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppDataTypeList, {appId});
    return this.http.post(url, req);
  }


  /**
   * 上移栏目
   */
  public updateAppDataTypeUp(appId, dataTypeId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppDataTypeUp, {appId, dataTypeId});
    return this.http.put(url, {});
  }

  /**
   * 下移栏目
   */
  public updateAppDataTypeDown(appId, dataTypeId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppDataTypeDown, {appId, dataTypeId});
    return this.http.put(url, {});
  }

  // endregion*******************************数据类型-结束***************************************
  // region*******************************栏目管理-开始***************************************
  /**
   * 栏目分页
   */
  public getAppColumnPage(appId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppColumnPage, {appId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 查询栏目
   */
  public getAppColumn(appId, columnId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.getAppColumn, {appId, columnId});
    return this.http.get(url);
  }

  /**
   * 创建栏目
   */
  public createAppColumn(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppColumn, {appId});
    return this.http.post(url, req);
  }

  /**
   * 修改栏目
   */
  public updateAppColumn(appId, columnId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppColumn, {appId, columnId});
    return this.http.put(url, req);
  }

  /**
   * 删除栏目
   */
  public deleteAppColumn(appId, columnId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.deleteAppColumn, {appId, columnId});
    return this.http.delete<any>(url);
  }

  /**
   * 获取栏目集合
   */
  public getAppColumnList(appId, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppColumnList, {appId});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }

  /**
   * 上移栏目
   */
  public updateAppColumnUp(appId, columnId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppColumnUp, {appId, columnId});
    return this.http.put(url, {});
  }

  /**
   * 下移栏目
   */
  public updateAppColumnDown(appId, columnId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppColumnDown, {appId, columnId});
    return this.http.put(url, {});
  }

  /**
   * 批量创建栏目
   */
  public createAppColumnList(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppColumnList, {appId});
    return this.http.post(url, req);
  }

  // endregion*******************************栏目管理-结束***************************************
}
