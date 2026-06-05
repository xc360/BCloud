import {environment} from '../../environments/environment';

const PATH = environment.config.url;
const BASIC = PATH + environment.config.basicPath; // 基础服务
export const HTTP_URLS = {

  // 我的应用
  getMyApp: BASIC + '/my_app',
  getMyAppToken: BASIC + '/my_app/token', // 获取应用的token信息
  deleteMyAppToken: BASIC + '/my_app/token', // 删除应用的token信息
  getMyAppMenuList: BASIC + '/my_app/menu_list', // 获取应用的菜单信息集合
  getMyAppUser: BASIC + '/my_app/user', // 获取我的应用的用户信息
  updateMyAppUserPassword: BASIC + '/my_app/user/password', // 修改我的应用的用户密码
  getMyAppDictList: BASIC + '/my_app/dict_list', // 获取我的应用的字典集合
  updateMyAppToken: BASIC + '/my_app/token/{refreshToken}', // 更新应用的token信息
  getMyAppAreaList: BASIC + '/my_app/area_list', // 获取区域集合
  getMyAppAreaNodeList: BASIC + '/my_app/area_node_list', // 区域节点集合
  createMyAppUserCaptcha: BASIC + '/my_app/user_captcha/{messageCode}/{accountType}', // 发送用户验证码
  getMyAppUserCaptcha: BASIC + '/my_app/user_captcha/{messageCode}', // 获取用户验证码信息
  createMyAppCaptcha: BASIC + '/my_app/captcha/{messageCode}/{accountType}', //  发送验证码
  getMyAppImgCaptcha: BASIC + '/my_app/img_captcha', // 获取图片验证码

  updateMyAppUserMail: BASIC + '/my_app/user/email', // 修改用户手机号
  updateMyAppUserPhone: BASIC + '/my_app/user/phone',  // 修改用户邮箱
  deleteMyAppUser: BASIC + '/my_app/user', // 注销账号
  getMyAppUserGroupList: BASIC + '/my_app/user/group_list', // 获取用户的用户组集合
  getMyAppUploadFileSign: BASIC + '/my_app/upload_file/sign', // 获取上传文件签名
  // 应用管理
  getCurrentUserAppPage: BASIC + '/current_user/app_page/{current}', // 获取当前用户应用的分页数据
  createCurrentUserApp: BASIC + '/current_user/app', // 创建当前用户的应用
  updateCurrentUserApp: BASIC + '/current_user/app/{appId}', // 修改当前用户的应用
  deleteCurrentUserApp: BASIC + '/current_user/app/{appId}', // 删除当前用户的应用
  getAppSecret: BASIC + '/current_user/app/{appId}/secret', // 获取当前用户应用的秘钥
  updateAppSecret: BASIC + '/current_user/app/{appId}/secret', // 更新当前用户应用的秘钥
  getCurrentUserAppList: BASIC + '/current_user/app_list', // 获取应用集合字典
  getAppList: BASIC + '/app_list', // 获取应用集合
  updateCurrentUserAppRefresh: BASIC + '/current_user/app_refresh/{appId}', // 刷新应用
  getCurrentUserAccessSecret: BASIC + '/current_user/access_secret', // 获取当前用户访问秘钥
  updateCurrentUserAccessSecret: BASIC + '/current_user/access_secret', // 更新当前用户访问秘钥
  // 应用文档
  appDoc: BASIC + '/show/app/{appId}/version/{versionId}', // 应用文档
  // 用户组管理
  getAppGroupPage: BASIC + '/app/{appId}/group_page/{current}', // 获取应用的用户组分页数据
  createAppGroup: BASIC + '/app/{appId}/group', // 创建应用的用户组
  updateAppGroup: BASIC + '/app/{appId}/group/{groupId}', // 修改应用的用户组
  getAppGroup: BASIC + '/app/{appId}/group/{groupId}', // 查询应用的用户组
  deleteAppGroup: BASIC + '/app/{appId}/group/{groupId}', // 删除应用的组
  getAppGroupList: BASIC + '/app/{appId}/group_list', // 获取用户组集合
  createAppGroupList: BASIC + '/app/{appId}/group_list', // 创建用户组集合
  getAppGroupRoleRelationList: BASIC + '/app/{appId}/group_role_relation_list', // 获取的用户组角色关联集合
  createAppGroupRoleRelationList: BASIC + '/app/{appId}/group_role_relation_list', // 创建用户组角色关联集合
  // 用户管理
  getAppUserPage: BASIC + '/app/{appId}/user_page/{current}', // 获取应用的用户组分页数据
  getUserGroupRole: BASIC + '/app/{appId}/user/{userId}/group_role', // 用户的组和角色信息
  updateUserGroupRole: BASIC + '/app/{appId}/user/{userId}/group_role', // 创建用户组和角色
  getAppUser: BASIC + '/app/{appId}/user/{userId}', // 获取应用的用户
  // 用户信息
  getCurrentUserInfo: BASIC + '/current_user/info', // 获取当前用户信息
  updateCurrentUserInfo: BASIC + '/current_user/info', // 修改当前用户信息
  // 角色管理
  getAppRolePage: BASIC + '/app/{appId}/role_page/{current}', // 获取应用的角色分页数据
  createAppRole: BASIC + '/app/{appId}/role', // 创建应用角色
  updateAppRole: BASIC + '/app/{appId}/role/{roleId}', // 修改应用的角色
  getAppRole: BASIC + '/app/{appId}/role/{roleId}', // 查询应用的角色
  deleteAppRole: BASIC + '/app/{appId}/role/{roleId}', // 删除应用的角色
  getAppRoleList: BASIC + '/app/{appId}/role_list', // 获取角色集合
  createAppRoleList: BASIC + '/app/{appId}/role_list', // 创建角色集合
  updateAppRoleUp: BASIC + '/app/{appId}/role/{roleId}/up', // 上移角色
  updateAppRoleDown: BASIC + '/app/{appId}/role/{roleId}/down', // 下移角色
  createAppRoleAuthorityRelationList: BASIC + '/app/{appId}/role_authority_relation_list', // 创建角色权限关联集合
  getAppRoleAuthorityRelationList: BASIC + '/app/{appId}/role_authority_relation_list', // 获取的角色权限关联集合
  // 权限管理
  getAppAuthorityPage: BASIC + '/app/{appId}/authority_page/{current}', // 获取应用的权限分页数据
  createAppAuthority: BASIC + '/app/{appId}/authority', // 创建应用的权限
  updateAppAuthority: BASIC + '/app/{appId}/authority/{authorityId}', // 修改应用的权限
  deleteAppAuthority: BASIC + '/app/{appId}/authority/{authorityId}', // 删除应用的权限
  createAppAuthorityList: BASIC + '/app/{appId}/authority_list', // 创建应用权限集合
  getAppAuthorityList: BASIC + '/app/{appId}/authority_list', // 获取应用的所有权限
  updateAppAuthorityUp: BASIC + '/app/{appId}/authority/{authorityId}/up', // 上移权限
  updateAppAuthorityDown: BASIC + '/app/{appId}/authority/{authorityId}/down', // 下移权限
  // 权限审核
  getAuditAuthorityPage: BASIC + '/audit_authority_page/{current}', // 审核应用权限分页
  updateAuditAuthority: BASIC + '/audit_authority/{authorityId}', // 审核权限
  // 授权管理
  getAuthorizeAppPage: BASIC + '/authorize_app_page/{current}', // 授权应用分页
  deleteAuthorizeApp: BASIC + '/authorize_app/{appId}', // 取消授权
  // 应用审核
  getManageAuditAppPage: BASIC + '/manage/audit_app_page/{current}', // 审核应用列表
  getManageAuditApp: BASIC + '/manage/audit_app/{appId}', // 获取审核应用
  updateManageAuditApp: BASIC + '/manage/audit_app/{appId}', // 审核应用
  // 应用授权
  getAppAuthorizeAuthorityPage: BASIC + '/app/{appId}/authorize/authority_page/{current}', // 应用授权权限分页
  updateAppAuthorize: BASIC + '/app/{appId}/authorize', // 更新应用授权
  getAuditAppAuthorizePage: BASIC + '/audit_app_authorize_page/{current}', // 审核应用权限分页
  getAuditAppAuthorize: BASIC + '/audit_app_authorize/{authorizeId}', // 获取审核应用权限
  updateAuditAppAuthorize: BASIC + '/audit_app_authorize/{authorizeId}', // 审核应用权限
  // 用户授权
  getCurrentUserAuthorizePage: BASIC + '/current_user/authorize_page/{current}', // 用户授权分页
  deleteCurrentUserAuthorize: BASIC + '/current_user/authorize/{userAuthorizeId}', // 用户取消授权
  getCurrentUserAuthorizeAuthorityPage: BASIC + '/current_user/authorize/{userAuthorizeId}/authority_page/{current}', // 用户授权权限分页
  deleteCurrentUserAuthorizeAuthority: BASIC + '/current_user/authorize/{userAuthorizeId}/authority/{userAuthorizeAuthorityId}', // 用户取消权限授权
  // 信息管理
  getAppInfoPage: BASIC + '/app/{appId}/info_page/{current}', // 获取应用信息分页数据
  createAppInfo: BASIC + '/app/{appId}/info', // 创建应用信息
  updateAppInfo: BASIC + '/app/{appId}/info/{infoId}', // 修改应用信息
  deleteAppInfo: BASIC + '/app/{appId}/info/{infoId}', // 删除应用的信息
  createAppInfoList: BASIC + '/app/{appId}/info_list', // 创建应用信息集合
  getAppInfoList: BASIC + '/app/{appId}/info_list', // 获取应用的信息集合
  // 字典管理
  getAppDictPage: BASIC + '/app/{appId}/dict_page/{current}', // 获取应用字典分页数据
  createAppDict: BASIC + '/app/{appId}/dict', // 创建应用字典
  updateAppDict: BASIC + '/app/{appId}/dict/{dictId}', // 修改应用字典
  deleteAppDict: BASIC + '/app/{appId}/dict/{dictId}', // 删除应用字典
  createAppDictList: BASIC + '/app/{appId}/dict_list', // 创建应用字典集合
  getAppDictList: BASIC + '/app/{appId}/dict_list', // 获取应用的字典集合
  updateAppDictUp: BASIC + '/app/{appId}/dict/{dictId}/up', // 上移字典
  updateAppDictDown: BASIC + '/app/{appId}/dict/{dictId}/down', // 下移字典
  // 树形字典管理
  getAppTreeDictPage: BASIC + '/app/{appId}/tree_dict_page/{current}', // 获取树形字典分页数据
  createAppTreeDict: BASIC + '/app/{appId}/tree_dict', // 创建树形字典
  updateAppTreeDict: BASIC + '/app/{appId}/tree_dict/{treeDictId}', // 修改树形字典
  deleteAppTreeDict: BASIC + '/app/{appId}/tree_dict/{treeDictId}', // 删除树形字典
  createAppTreeDictList: BASIC + '/app/{appId}/tree_dict_list', // 批量创建树形字典
  getAppTreeDictList: BASIC + '/app/{appId}/tree_dict_list', // 获取树形字典集合
  updateAppTreeDictUp: BASIC + '/app/{appId}/tree_dict/{treeDictId}/up', // 上移树形字典
  updateAppTreeDictDown: BASIC + '/app/{appId}/tree_dict/{treeDictId}/down', // 下移树形字典
  // 接口供应商
  getAppApiSupplierPage: BASIC + '/app/{appId}/api_supplier_page/{current}', // 接口供应商分页
  createAppApiSupplier: BASIC + '/app/{appId}/api_supplier', // 创建接口供应商
  updateAppApiSupplier: BASIC + '/app/{appId}/api_supplier/{apiSupplierId}', // 修改接口供应商
  deleteAppApiSupplier: BASIC + '/app/{appId}/api_supplier/{apiSupplierId}', // 删除接口供应商
  createAppApiSupplierList: BASIC + '/app/{appId}/api_supplier_list', // 批量创建接口供应商
  getAppApiSupplierList: BASIC + '/app/{appId}/api_supplier_list', // 获取接口供应商集合
  // 消息模板
  getAppMessageTemplatePage: BASIC + '/app/{appId}/message_template_page/{current}', // 消息模板分页
  createAppMessageTemplate: BASIC + '/app/{appId}/message_template', // 创建消息模板
  updateAppMessageTemplate: BASIC + '/app/{appId}/message_template/{messageTemplateId}', // 修改消息模板
  deleteAppMessageTemplate: BASIC + '/app/{appId}/message_template/{messageTemplateId}', // 删除消息模板
  createAppMessageTemplateList: BASIC + '/app/{appId}/message_template_list', // 批量创建消息模板
  getAppMessageTemplateList: BASIC + '/app/{appId}/message_template_list', // 获取消息模板集合
  // 消息日志
  getAppMessageLogPage: BASIC + '/app/{appId}/message/{messageTemplateId}/log/{current}', // 消息日志分页
  // 应用版本
  getAppVersionPage: BASIC + '/app/{appId}/version_page/{current}', // 应用版本分页
  createAppVersion: BASIC + '/app/{appId}/version', // 创建应用版本
  updateAppVersion: BASIC + '/app/{appId}/version/{versionId}', // 修改应用版本
  deleteAppVersion: BASIC + '/app/{appId}/version/{versionId}', // 删除应用版本
  getAppVersion: BASIC + '/app/{appId}/version/{versionId}', // 获取应用版本
  updateAppVersionUp: BASIC + '/app/{appId}/version/{versionId}/up', // 上移版本
  updateAppVersionDown: BASIC + '/app/{appId}/version/{versionId}/down', // 下移版本
  // 任务
  getAppTaskPage: BASIC + '/app/{appId}/task_page/{current}', // 任务分页
  createAppTask: BASIC + '/app/{appId}/task', // 创建任务
  updateAppTask: BASIC + '/app/{appId}/task/{taskId}', // 修改任务
  deleteAppTask: BASIC + '/app/{appId}/task/{taskId}', // 删除任务
  createAppTaskList: BASIC + '/app/{appId}/task_list', // 批量创建任务
  getAppTaskList: BASIC + '/app/{appId}/task_list', // 获取任务集合
  updateAppTaskInit: BASIC + '/app/{appId}/task_init', // 初始化任务
  updateAppTaskExecute: BASIC + '/app/{appId}/task/{taskId}/execute', // 执行任务
  // 任务日志
  getAppTaskLogPage: BASIC + '/app/{appId}/task/{taskId}/log/{current}', // 任务日志分页
  /****************************网站基础*********************************/
  // 菜单管理
  getAppMenuPage: BASIC + '/app/{appId}/menu_page/{current}', // 获取应用的菜单分页数据
  createAppMenu: BASIC + '/app/{appId}/menu', // 创建应用的菜单
  updateAppMenu: BASIC + '/app/{appId}/menu/{menuId}', // 修改应用的菜单
  deleteAppMenu: BASIC + '/app/{appId}/menu/{menuId}', // 删除应用的菜单
  createAppMenuList: BASIC + '/app/{appId}/menu_list', // 创建应用菜单集合
  getAppMenuList: BASIC + '/app/{appId}/menu_list', // 获取应用的所有菜单
  updateAppMenuUp: BASIC + '/app/{appId}/menu/{menuId}/up', // 上移菜单
  updateAppMenuDown: BASIC + '/app/{appId}/menu/{menuId}/down', // 下移菜单
  // 页面管理
  getAppPagePage: BASIC + '/app/{appId}/page_page/{current}', // 页面分页
  createAppPage: BASIC + '/app/{appId}/page', // 创建页面
  updateAppPage: BASIC + '/app/{appId}/page/{pageId}', // 修改页面
  deleteAppPage: BASIC + '/app/{appId}/page/{pageId}', // 删除页面
  getAppPageList: BASIC + '/app/{appId}/page_list', // 页面集合
  createAppPageList: BASIC + '/app/{appId}/page_list', // 批量创建页面
  createAppPageColumnRelationList: BASIC + '/app/{appId}/page_column_relation_list', // 创建页面栏目关联集合
  getAppPageColumnRelationList: BASIC + '/app/{appId}/page_column_relation_list', // 获取的页面栏目关联集合
  // 数据类型管理
  getAppDataTypePage: BASIC + '/app/{appId}/data_type_page/{current}', // 数据类型分页
  getAppDataTypeList: BASIC + '/app/{appId}/data_type_list', // 数据类型集合
  createAppDataType: BASIC + '/app/{appId}/data_type', // 创建数据类型
  updateAppDataType: BASIC + '/app/{appId}/data_type/{dataTypeId}', // 修改数据类型
  deleteAppDataType: BASIC + '/app/{appId}/data_type/{dataTypeId}', // 删除数据类型
  createAppDataTypeList: BASIC + '/app/{appId}/data_type_list', // 批量创建数据类型
  updateAppDataTypeUp: BASIC + '/app/{appId}/data_type/{dataTypeId}/up', // 上移数据类型
  updateAppDataTypeDown: BASIC + '/app/{appId}/data_type/{dataTypeId}/down', // 下移数据类型
  // 栏目管理
  getAppColumnPage: BASIC + '/app/{appId}/column_page/{current}', // 栏目分页
  getAppColumn: BASIC + '/app/{appId}/column/{columnId}', // 查询栏目
  createAppColumn: BASIC + '/app/{appId}/column', // 创建栏目
  updateAppColumn: BASIC + '/app/{appId}/column/{columnId}', // 修改栏目
  deleteAppColumn: BASIC + '/app/{appId}/column/{columnId}', // 删除栏目
  getAppColumnList: BASIC + '/app/{appId}/column_list', // 获取栏目集合
  updateAppColumnUp: BASIC + '/app/{appId}/column/{columnId}/up', // 上移栏目
  updateAppColumnDown: BASIC + '/app/{appId}/column/{columnId}/down', // 下移栏目
  createAppColumnList: BASIC + '/app/{appId}/column_list' // 批量创建栏目
};
