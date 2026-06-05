import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {HTTP_URLS} from '../../config/app-http.url';
import {CommonTool} from '@ccxc/tool';

@Injectable({
  providedIn: 'root'
})
export class BasicManagementService {
  constructor(private http: HttpClient) {
  }

  // region*******************************用户组管理-开始***************************************
  /**
   * 获取应用的用户组分页数据
   */
  public getAppGroupPage(appId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppGroupPage, {appId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }


  /**
   * 创建应用的用户组
   */
  public createAppGroup(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppGroup, {appId});
    return this.http.post(url, req);
  }

  /**
   * 创建应用的用户组
   */
  public updateAppGroup(appId, groupId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppGroup, {appId, groupId});
    return this.http.put(url, req);
  }

  /**
   * 查询应用的用户组
   */
  public getAppGroup(appId, groupId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.getAppGroup, {appId, groupId});
    return this.http.get(url);
  }

  /**
   * 删除应用的组
   */
  public deleteAppGroup(appId, groupId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.deleteAppGroup, {appId, groupId});
    return this.http.delete(url);
  }

  /**
   * 获取应用的用户组
   */
  public getAppGroupList(appId, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppGroupList, {appId});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }

  /**
   * 创建用户组集合
   */
  public createAppGroupList(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppGroupList, {appId});
    return this.http.post(url, req);
  }

  /**
   * 获取的用户组角色关联集合
   */
  public getAppGroupRoleRelationList(appId, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppGroupRoleRelationList, {appId});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }

  /**
   * 创建用户组角色关联集合
   */
  public createAppGroupRoleRelationList(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppGroupRoleRelationList, {appId});
    return this.http.post(url, req);
  }

  // endregion*******************************用户组管理-结束***************************************
  // region*******************************用户管理-开始***************************************
  /**
   * 获取应用的用户组分页数据
   */
  public getAppUserPage(appId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppUserPage, {appId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 用户的组和角色信息
   */
  public getUserGroupRole(appId, userId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.getUserGroupRole, {appId, userId});
    return this.http.get<any>(url);
  }

  /**
   * 用户的组和角色信息
   */
  public updateUserGroupRole(appId, userId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateUserGroupRole, {appId, userId});
    return this.http.post<any>(url, req);
  }

  /**
   * 获取应用的用户
   */
  public getAppUser(appId, userId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.getAppUser, {appId, userId});
    return this.http.get<any>(url);
  }

  // endregion*******************************用户管理-结束***************************************
  // region*******************************角色管理-开始***************************************

  /**
   * 获取应用的角色分页数据
   */
  public getAppRolePage(appId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppRolePage, {appId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 创建应用角色
   */
  public createAppRole(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppRole, {appId});
    return this.http.post(url, req);
  }

  /**
   * 修改应用的角色
   */
  public updateAppRole(appId, roleId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppRole, {appId, roleId});
    return this.http.put(url, req);
  }

  /**
   * 查询应用的角色
   */
  public getAppRole(appId, roleId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.getAppRole, {appId, roleId});
    return this.http.get(url);
  }

  /**
   * 删除应用的角色
   */
  public deleteAppRole(appId, roleId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.deleteAppRole, {appId, roleId});
    return this.http.delete(url);
  }

  /**
   * 获取角色集合字典
   */
  public getAppRoleList(appId, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppRoleList, {appId});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }

  /**
   * 创建角色集合
   */
  public createAppRoleList(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppRoleList, {appId});
    return this.http.post(url, req);
  }

  /**
   * 上移字典
   */
  public updateAppRoleUp(appId, roleId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppRoleUp, {appId, roleId});
    return this.http.put(url, {});
  }

  /**
   * 下移字典
   */
  public updateAppRoleDown(appId, roleId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppRoleDown, {appId, roleId});
    return this.http.put(url, {});
  }

  /**
   * 创建角色权限关联集合
   */
  public createAppRoleAuthorityRelationList(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppRoleAuthorityRelationList, {appId});
    return this.http.post(url, req);
  }

  /**
   * 获取的角色权限关联集合
   */
  public getAppRoleAuthorityRelationList(appId, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppRoleAuthorityRelationList, {appId});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }

  // endregion*******************************角色管理-结束***************************************
  // region*******************************权限管理-开始***************************************
  /**
   * 获取应用的权限分页数据
   */
  public getAppAuthorityPage(appId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppAuthorityPage, {appId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 创建应用的权限
   */
  public createAppAuthority(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppAuthority, {appId});
    return this.http.post(url, req);
  }

  /**
   * 修改应用的权限
   */
  public updateAppAuthority(appId, authorityId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppAuthority, {appId, authorityId});
    return this.http.put(url, req);
  }

  /**
   * 删除应用的权限
   */
  public deleteAppAuthority(appId, authorityId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.deleteAppAuthority, {appId, authorityId});
    return this.http.delete(url);
  }

  /**
   * 创建应用权限集合
   */
  public createAppAuthorityList(appId, types, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.createAppAuthorityList, {appId});
    url = CommonTool.analysisParam(url, {types});
    return this.http.post(url, req);
  }

  /**
   * 获取应用的所有权限
   * 根据条件获取当前用户的应用下的所有权限
   */
  public getAppAuthorityList(appId, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppAuthorityList, {appId});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }

  /**
   * 上移权限
   */
  public updateAppAuthorityUp(appId, authorityId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppAuthorityUp, {appId, authorityId});
    return this.http.put(url, {});
  }

  /**
   * 下移权限
   */
  public updateAppAuthorityDown(appId, authorityId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppAuthorityDown, {appId, authorityId});
    return this.http.put(url, {});
  }

  // endregion*******************************权限管理-结束***************************************
  // region*******************************信息管理-开始***************************************

  /**
   * 获取应用信息分页数据
   */
  public getAppInfoPage(appId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppInfoPage, {appId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 创建应用信息
   */
  public createAppInfo(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppInfo, {appId});
    return this.http.post(url, req);
  }

  /**
   * 修改应用信息
   */
  public updateAppInfo(appId, infoId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppInfo, {appId, infoId});
    return this.http.put(url, req);
  }

  /**
   * 删除应用的信息
   */
  public deleteAppInfo(appId, infoId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.deleteAppInfo, {appId, infoId});
    return this.http.delete<any>(url);
  }

  /**
   * 创建应用信息集合
   */
  public createAppInfoList(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppInfoList, {appId});
    return this.http.post(url, req);
  }

  /**
   * 获取应用的信息集合
   */
  public getAppInfoList(appId, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppInfoList, {appId});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }

  // endregion*******************************信息管理-结束***************************************
  // region*******************************字典管理-开始***************************************
  /**
   * 获取应用字典分页数据
   */
  public getAppDictPage(appId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppDictPage, {appId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 创建应用字典
   */
  public createAppDict(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppDict, {appId});
    return this.http.post(url, req);
  }

  /**
   * 修改应用字典
   */
  public updateAppDict(appId, dictId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppDict, {appId, dictId});
    return this.http.put(url, req);
  }

  /**
   * 删除应用的字典
   */
  public deleteAppDict(appId, dictId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.deleteAppDict, {appId, dictId});
    return this.http.delete<any>(url);
  }

  /**
   * 创建应用字典集合
   */
  public createAppDictList(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppDictList, {appId});
    return this.http.post(url, req);
  }

  /**
   * 获取应用的字典集合
   */
  public getAppDictList(appId, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppDictList, {appId});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }

  /**
   * 上移字典
   */
  public updateAppDictUp(appId, dictId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppDictUp, {appId, dictId});
    return this.http.put(url, {});
  }

  /**
   * 下移字典
   */
  public updateAppDictDown(appId, dictId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppDictDown, {appId, dictId});
    return this.http.put(url, {});
  }

  // endregion*******************************字典管理-结束***************************************
  // region*******************************树形字典-开始***************************************
  /**
   * 获取树形字典分页数据
   */
  public getAppTreeDictPage(appId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppTreeDictPage, {appId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 创建树形字典
   */
  public createAppTreeDict(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppTreeDict, {appId});
    return this.http.post(url, req);
  }

  /**
   * 修改树形字典
   */
  public updateAppTreeDict(appId, treeDictId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppTreeDict, {appId, treeDictId});
    return this.http.put(url, req);
  }

  /**
   * 删除树形字典
   */
  public deleteAppTreeDict(appId, treeDictId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.deleteAppTreeDict, {appId, treeDictId});
    return this.http.delete<any>(url);
  }

  /**
   * 上移树形字典
   */
  public updateAppTreeDictUp(appId, treeDictId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppTreeDictUp, {appId, treeDictId});
    return this.http.put(url, {});
  }

  /**
   * 下移树形字典
   */
  public updateAppTreeDictDown(appId, treeDictId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppTreeDictDown, {appId, treeDictId});
    return this.http.put(url, {});
  }

  /**
   * 批量创建树形字典
   */
  public createAppTreeDictList(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppTreeDictList, {appId});
    return this.http.post(url, req);
  }

  /**
   * 获取树形字典集合
   */
  public getAppTreeDictList(appId, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppTreeDictList, {appId});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }

  // endregion*******************************树形字典-结束***************************************
  // region*******************************应用版本-开始***************************************

  /**
   * 应用版本分页
   */
  public getAppVersionPage(appId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppVersionPage, {appId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 创建应用版本
   */
  public createAppVersion(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppVersion, {appId});
    return this.http.post(url, req);
  }

  /**
   * 修改应用版本
   */
  public updateAppVersion(appId, versionId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppVersion, {appId, versionId});
    return this.http.put(url, req);
  }

  /**
   * 删除应用版本
   */
  public deleteAppVersion(appId, versionId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.deleteAppVersion, {appId, versionId});
    return this.http.delete<any>(url);
  }

  /**
   * 获取应用版本信息
   */
  public getAppVersion(appId, versionId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.getAppVersion, {appId, versionId});
    return this.http.get<any>(url);
  }

  /**
   * 上移版本
   */
  public updateAppVersionUp(appId, versionId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppVersionUp, {appId, versionId});
    return this.http.put(url, {});
  }

  /**
   * 下移版本
   */
  public updateAppVersionDown(appId, versionId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppVersionDown, {appId, versionId});
    return this.http.put(url, {});
  }

  // endregion*******************************应用版本-结束***************************************
  // region*******************************任务管理-开始***************************************

  /**
   * 应用任务分页
   */
  public getAppTaskPage(appId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppTaskPage, {appId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }

  /**
   * 创建应用任务
   */
  public createAppTask(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppTask, {appId});
    return this.http.post(url, req);
  }

  /**
   * 修改应用任务
   */
  public updateAppTask(appId, taskId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppTask, {appId, taskId});
    return this.http.put(url, req);
  }

  /**
   * 删除应用任务
   */
  public deleteAppTask(appId, taskId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.deleteAppTask, {appId, taskId});
    return this.http.delete<any>(url);
  }

  /**
   * 批量创建应用任务
   */
  public createAppTaskList(appId, req) {
    const url = CommonTool.analysisUrl(HTTP_URLS.createAppTaskList, {appId});
    return this.http.post(url, req);
  }

  /**
   * 获取任务集合
   */
  public getAppTaskList(appId, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppTaskList, {appId});
    url = CommonTool.analysisParam(url, req);
    return this.http.get<any>(url);
  }

  /**
   * 初始化任务
   */
  public updateAppTaskInit(appId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppTaskInit, {appId});
    return this.http.put(url, {});
  }

  /**
   * 执行任务
   */
  public updateAppTaskExecute(appId, taskId) {
    const url = CommonTool.analysisUrl(HTTP_URLS.updateAppTaskExecute, {appId, taskId});
    return this.http.put(url, {});
  }

  /**
   * 应用任务日志分页
   */
  public getAppTaskLogPage(appId, taskId, current, req) {
    let url = CommonTool.analysisUrl(HTTP_URLS.getAppTaskLogPage, {appId, taskId, current});
    url = CommonTool.analysisParam(url, req);
    return this.http.get(url);
  }


  // endregion*******************************任务管理-结束***************************************
  // region*******************************其他管理-开始***************************************
  /**
   * 获取选中的id集合
   */
  public getSelectedIds(authorityIds, authorityList) {
    const array = [];
    for (const id of authorityIds) {
      for (const authority of authorityList) {
        if (id === authority.id) {
          array.push(authority);
        }
      }
    }
    const ids = [];
    for (const arr of array) {
      let bool = true;
      for (const authority of array) {
        if (arr.node === authority.parentNode) {
          bool = false;
        }
      }
      if (bool) {
        ids.push(arr.id);
      }
    }
    return ids;
  }

  /**
   * 获取提交权限id
   */
  public getSubmitIds(authorityIds, authorityList) {
    const array = [];
    for (const id of authorityIds) {
      for (const authority of authorityList) {
        if (id === authority.id) {
          array.push(authority);
        }
      }
    }
    let parentIds = [];
    let childrenIds = [];
    for (let i = 0; i < array.length; i++) {
      parentIds = parentIds.concat(this.getParentNodes(array[i], authorityList));
      childrenIds = childrenIds.concat(this.getChildrenIds(array[i].node, authorityList));
    }
    // 父级id去重
    parentIds = parentIds.filter((item, index) => {
      return parentIds.indexOf(item) === index;
    });
    return authorityIds.concat(parentIds).concat(childrenIds);
  }

  /**
   * 获取子级id集合
   */
  private getChildrenIds(parentNode, authorityList) {
    let array = [];
    for (const authority of authorityList) {
      if (authority.parentNode === parentNode) {
        array = array.concat(this.getChildrenIds(authority.node, authorityList));
        array.push(authority.id);
      }
    }
    return array;
  }

  /**
   * 获取所有的父级id
   */
  private getParentNodes(auth, authorityList) {
    let array = [];
    for (const authority of authorityList) {
      if (authority.node === auth.parentNode) {
        if (authority.parentNode === 'root') {
          return [authority.id];
        } else {
          array = array.concat(this.getParentNodes(authority, authorityList));
          array.push(authority.id);
        }
      }
    }
    return array;
  }

  // endregion*******************************其他管理-结束***************************************
}
