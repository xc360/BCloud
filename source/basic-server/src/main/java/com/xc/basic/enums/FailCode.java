package com.xc.basic.enums;

import com.xc.core.exception.OperateException;
import com.xc.tool.utils.StringUtils;

/**
 * <p>故障代码</p>
 *
 * @author xc
 * @version v1.0
 */
public enum FailCode {
    MESSAGE_SEND_FAIL("messageSendFail", "消息发送失败，错误消息：{}"),
    ACCOUNT_LOGGED_OUT_UNAVAILABLE("accountLoggedOutUnavailable", "账号注销过无法再次注册，请联系系统管理员！"),
    OPERATE_FREQUENTLY("operateFrequently", "操作频繁，请稍后重试！"),
    // 应用
    APP_CREATE_FAIL("appCreateFail", "应用创建失败,请稍后重试！"),
    APP_UPDATE_FAIL("appUpdateFail", "应用修改失败,请稍后重试！"),
    APP_DELETE_FAIL("appDeleteFail", "应用删除失败,请稍后重试！"),
    APP_ID_ERROR("appIdError", "应用的id错误！"),
    APP_APP_ID_ERROR("appAppIdError", "应用的appId错误！"),
    APP_STATUS_INVALID("appStatusInvalid", "应用的状态无效！"),
    APP_NOT_AUDIT("appNotAudit", "应用未审核！"),
    APP_SECRET_ERROR("appSecretError", "应用秘钥错误！"),

    NOT_OVERT_APP("notOvertApp", "不是公开应用！"),
    APP_ID_NOT_NULL("appIdNotNull", "appId不能为空！"),
    APP_REFRESH_FAIL("appRefreshFail", "应用刷新失败，返回结果：{}"),
    APP_NOT_OPEN("appNotOpen", "应用没有公开！"),
    APP_CALLBACK_DOMAIN_ERROR("appDomainError", "应用回调域名错误！"),
    // 审核
    AUDIT_STATUS_ERROR("auditStatusError", "审核状态错误！"),

    // 签名
    SIGN_CLIENT_IP_ERROR("signClientIpError", "签名clientIp错误！"),
    SIGN_RANDOM_STR_NOT_NULL("signRandomStrNotNull", "签名randomStr不能为空！"),
    SIGN_TIME_STAMP_NOT_NULL("signTimeStampNotNull", "签名timeStamp不能为空！"),
    SIGN_ACCESS_ID_NOT_NULL("signAccessIdNotNull", "签名accessId不能为空！"),
    SIGN_ACCESS_ID_ERROR("signAccessIdError", "签名accessId错误！"),
    SIGN_MY_APP_ID_NOT_NULL("signMyAppIdNotNull", "签名myAppId不能为空！"),
    SIGN_MY_APP_ID_ERROR("signMyAppIdError", "签名myAppId错误！"),
    SIGN_APP_ID_NOT_NULL("signAppIdNotNull", "签名的AppId不能为空！"),
    SIGN_APP_ID_ERROR("signAppIdError", "签名的AppId错误！"),
    // 签名权限验证
    USER_AUTHORIZE_NOT_EXIST("userAuthorizeNotExist", "用户未向应用授权，应用无法访问用户信息！"),
    APP_NOT_AUTHORITY_VISIT("appNotAuthorityVisit", "应用没有访问权限！"),
    APP_NOT_USER_AUTHORITY_VISIT("appNotUserAuthorityVisit", "应用没有用户信息访问权限，请重新登录！"),

    // 应用版本
    APP_VERSION_CREATE_FAIL("appVersionCreateFail", "应用版本创建失败,请稍后重试！"),
    APP_VERSION_UPDATE_FAIL("appVersionUpdateFail", "应用版本修改失败,请稍后重试！"),
    APP_VERSION_DELETE_FAIL("appVersionDeleteFail", "应用版本删除失败,请稍后重试！"),
    APP_VERSION_ID_ERROR("appVersionIdError", "应用版本的id错误！"),
    APP_VERSION_APP_ID_ERROR("appVersionAppIdError", "应用版本的appId错误！"),
    APP_VERSION_DOC_NOT_EXIST("appVersionDocNotExist", "应用的版本文档不存在！"),
    ARTICLE_FILE_NUM_ACHIEVE_UPPER_LIMIT("articleFileNumAchieveUpperLimit", "文章文件数量达到上限！"),
    APP_VERSION_DOC_PARSER_ERROR("appVersionDocParserError", "应用版本文档解析器错误！"),
    APP_VERSION_REPEAT("appVersionRepeat", "版本号重复！"),

    // 应用授权
    AUTHORIZE_CREATE_FAIL("authorizeCreateFail", "应用授权创建失败,请稍后重试！"),
    AUTHORIZE_UPDATE_FAIL("authorizeUpdateFail", "应用授权修改失败,请稍后重试！"),
    AUTHORIZE_DELETE_FAIL("authorizeDeleteFail", "应用授权删除失败,请稍后重试！"),
    AUTHORIZE_ID_ERROR("authorizeIdError", "应用授权主键错误！"),
    AUTHORIZE_APP_ID_ERROR("authorizeAppIdError", "应用授权appId错误！"),

    // 用户授权
    USER_AUTHORIZE_CREATE_FAIL("userAuthorizeCreateFail", "用户授权创建失败,请稍后重试！"),
    USER_AUTHORIZE_UPDATE_FAIL("userAuthorizeUpdateFail", "用户授权修改失败,请稍后重试！"),
    USER_AUTHORIZE_DELETE_FAIL("userAuthorizeDeleteFail", "用户授权删除失败,请稍后重试！"),
    USER_AUTHORIZE_ID_ERROR("userAuthorizeIdError", "用户授权主键错误！"),
    USER_AUTHORIZE_USER_ID_ERROR("userAuthorizeUserIdError", "用户授权用户主键错误！"),

    // 用户授权权限
    USER_AUTHORIZE_AUTHORITY_CREATE_FAIL("userAuthorizeAuthorityCreateFail", "用户授权权限创建失败,请稍后重试！"),
    USER_AUTHORIZE_AUTHORITY_UPDATE_FAIL("userAuthorizeAuthorityUpdateFail", "用户授权权限修改失败,请稍后重试！"),
    USER_AUTHORIZE_AUTHORITY_DELETE_FAIL("userAuthorizeAuthorityDeleteFail", "用户授权权限删除失败,请稍后重试！"),
    USER_AUTHORIZE_AUTHORITY_ID_ERROR("userAuthorizeAuthorityIdError", "用户授权权限主键错误！"),
    USER_AUTHORIZE_AUTHORITY_USER_AUTHORIZE_ID_ERROR("userAuthorizeAuthorityUserAuthorizeIdError", "用户授权权限的用户授权主键错误！"),

    // 用户组
    GROUP_CREATE_FAIL("groupCreateFail", "组创建失败,请稍后重试！"),
    GROUP_UPDATE_FAIL("groupUpdateFail", "组修改失败,请稍后重试！"),
    GROUP_DELETE_FAIL("groupDeleteFail", "组删除失败,请稍后重试!"),
    GROUP_ID_ERROR("groupIdError", "组的id错误！"),
    GROUP_APP_ID_ERROR("groupAppIdError", "组的appId错误！"),
    GROUP_CODE_REPEAT("groupCodeRepeat", "组标识重复！"),
    GROUP_CODE_ERROR("groupCodeError", "组标识错误！"),

    // 用户
    USER_CREATE_FAIL("userCreateFail", "用户创建失败,请稍后重试！"),
    USER_UPDATE_FAIL("userUpdateFail", "用户修改失败,请稍后重试！"),
    USER_DELETE_FAIL("userDeleteFail", "用户删除失败,请稍后重试！"),
    USER_PASSWORD_UPDATE_FAIL("userPasswordUpdateFail", "修改密码失败，原密码不正确！"),
    USER_ID_ERROR("userIdError", "用户的id错误！"),
    USER_ACCOUNT_ERROR("userAccountError", "用户的账号错误!"),
    USER_EXIST("userExist", "用户已存在!"),
    USER_LOCKED("userLocked", "用户已锁定，请联系管理员！"),
    USER_PASSWORD_ERROR_USER_LOCKED("userPasswordErrorUserLocked", "你输错的密码已达上限，用户已锁定，可通过找回密码解锁!"),
    USER_PASSWORD_ERROR("userPasswordError", "密码错误！"),
    USER_ACCOUNT_TYPE_ERROR("userAccountTypeError", "账号类型错误！"),
    USER_PASSWORD_NOT_NULL("userPasswordNotNull", "密码不能为空！"),
    USER_PHONE_ERROR("userPhoneError", "手机号错误！"),
    USER_EMAIL_ERROR("userEmailError", "邮箱错误！"),
    USER_AUTHORIZE_CODE_NOT_EXIST("userAuthorizeCodeNotExist", "授权code不存在！"),
    USER_PHONE_CAPTCHA_CODE_SEND_FAIL("userPhoneCaptchaCodeSendFail", "手机验证码发送失败,请联系管理员!"),
    USER_PHONE_NOT_NULL("userPhoneNotNull", "手机号不能为空！"),
    USER_MAIL_NOT_NULL("userMailNotNull", "邮箱不能为空！"),
    USER_MAIL_NOT_EXIST("userMailNotExist", "用户邮箱不存在！"),
    USER_PHONE_NOT_EXIST("userPhoneNotExist", "用户手机号不存在！"),
    USER_ACCOUNT_NOT_NULL("userAccountNotNull", "账号不能为空！"),
    USER_MAIL_REPEAT("userMailRepeat", "邮箱已存在！"),
    USER_PHONE_REPEAT("userPhoneRepeat", "手机号已存在！"),

    // 用户信息
    USER_INFO_CREATE_FAIL("userInfoCreateFail", "用户信息创建失败,请稍后重试！"),
    USER_INFO_UPDATE_FAIL("userInfoUpdateFail", "用户信息修改失败,请稍后重试!"),

    // 已删除用户
    DELETED_USER_CREATE_FAIL("deletedUserCreateFail", "已删除用户信息创建失败,请稍后重试！"),

    // 角色
    ROLE_UPDATE_FAIL("roleUpdateFail", "角色创建失败,请稍后重试！"),
    ROLE_CREATE_FAIL("roleCreateFail", "角色修改失败,请稍后重试！"),
    ROLE_DELETE_FAIL("roleDeleteFail", "角色删除失败,请稍后重试!"),
    ROLE_ID_ERROR("roleIdError", "角色的id错误！"),
    ROLE_APP_ID_ERROR("roleAppIdError", "角色的appId错误！"),
    ROLE_CODE_REPEAT("roleCodeRepeat", "角色标识重复！"),
    ROLE_CODE_ERROR("roleCodeError", "角色的标识错误！"),

    // 组和用户关联
    GROUP_AND_USER_RELATION_CREATE_FAIL("groupAndUserRelationCreateFail", "用户和组关联创建失败,请稍后重试！"),

    // 组和角色关联
    GROUP_AND_ROLE_RELATION_CREATE_FAIL("groupAndRoleRelationCreateFail", "角色和组关联创建失败,请稍后重试！"),

    // 用户和角色关联
    USER_AND_ROLE_RELATION_CREATE_FAIL("userAndRoleRelationCreateFail", "用户和角色关联创建失败,请稍后重试！"),

    // 角色和权限关联
    ROLE_AND_AUTHORITY_RELATION_CREATE_FAIL("roleAndAuthorityRelationCreateFail", "角色和权限关联创建失败,请稍后重试！"),

    // 权限
    AUTHORITY_CREATE_FAIL("authorityCreateFail", "权限创建失败,请稍后重试!"),
    AUTHORITY_UPDATE_FAIL("authorityUpdateFail", "权限修改失败,请稍后重试!"),
    AUTHORITY_DELETE_FAIL("authorityDeleteFail", "权限删除失败,请稍后重试!"),
    AUTHORITY_CODE_REPEAT("authorityCodeRepeat", "权限的标识重复！"),
    AUTHORITY_ID_ERROR("authorityIdError", "权限的id错误！"),
    AUTHORITY_APP_ID_ERROR("authorityAppIdError", "权限的appId错误！"),
    AUTHORITY_CODE_ERROR("authorityCodeError", "权限标识错误！"),
    // 字典
    DICT_CREATE_FAIL("dictCreateFail", "字典创建失败,请稍后重试！"),
    DICT_UPDATE_FAIL("dictUpdateFail", "字典修改失败,请稍后重试！"),
    DICT_DELETE_FAIL("dictDeleteFail", "字典删除失败,请稍后重试!"),
    DICT_ID_ERROR("dictIdError", "字典的id错误！"),
    DICT_APP_ID_ERROR("dictAppIdError", "字典的appId错误！"),
    DICT_TYPE_VALUE_REPEAT("dictTypeValueRepeat", "字典类型和值重复！"),

    // 信息
    INFO_CREATE_FAIL("infoCreateFail", "信息创建失败,请稍后重试！"),
    INFO_UPDATE_FAIL("infoUpdateFail", "信息修改失败,请稍后重试！"),
    INFO_DELETE_FAIL("infoDeleteFail", "信息删除失败,请稍后重试!"),
    INFO_KEY_REPEAT("infoKeyRepeat", "信息的key重复！"),
    INFO_ID_ERROR("infoIdError", "信息的id错误！"),
    INFO_APP_ID_ERROR("infoAppIdError", "信息的appId错误！"),
    INFO_KEY_NOT_NULL("infoKeyNotNull", "信息的key不能为空！"),

    // 树形字典
    TREE_DICT_CREATE_FAIL("treeDictCreateFail", "树形字典创建失败,请稍后重试！"),
    TREE_DICT_UPDATE_FAIL("treeDictUpdateFail", "树形字典修改失败,请稍后重试！"),
    TREE_DICT_DELETE_FAIL("treeDictDeleteFail", "树形字典删除失败,请稍后重试！"),
    TREE_DICT_ID_ERROR("treeDictIdError", "树形字典的id错误！"),
    TREE_DICT_APP_ID_ERROR("treeDictAppIdError", "树形字典的appId错误！"),
    TREE_DICT_CODE_REPEAT("treeDictCodeRepeat", "树形字典标识重复！"),

    // 任务
    TASK_ID_ERROR("taskIdError", "任务主键错误!"),
    TASK_CODE_ERROR("taskCodeError", "任务标识错误!"),
    TASK_APP_ID_ERROR("taskAppIdError", "任务应用主键错误!"),
    TASK_STATUS_INVALID("taskStatusInvalid", "任务的状态无效!"),
    TASK_CREATE_FAIL("taskCreateFail", "任务创建失败,请稍后重试！"),
    TASK_CODE_REPEAT("taskCodeRepeat", "任务标识重复,请稍后重试！"),
    TASK_UPDATE_FAIL("taskUpdateFail", "任务修改失败,请稍后重试！"),
    TASK_DELETE_FAIL("taskDeleteFail", "任务删除失败,请稍后重试！"),
    TASK_EXECUTE_FAIL("taskExecuteFail", "任务执行失败！"),


    // 验证码
    CAPTCHA_SEND_SENDING_TOO_OFTEN("captchaSendSendingTooOften", "验证码发送过于频繁,请刷新重试！"),
    CAPTCHA_ERROR("captchaError", "验证码错误！"),
    CONFIRM_CAPTCHA_ERROR("confirmCaptchaError", "确认验证码错误！"),
    CAPTCHA_CODE_NOT_NULL("captchaCodeNotNull", "验证码code不能为空！"),
    CAPTCHA_GAIN_FAIL("captchaGainFail", "验证码获取失败！"),
    CAPTCHA_VERIFY_NUM_TOO_MUCH("captchaVerifyNumTooMuch", "验证码校验次数过多，请重新获取验证码!"),
    CAPTCHA_CLIENT_IP_NOT_NULL("captchaClientIpNotNull", "验证码客户端ip不能为空！"),

    // 小程序
    APPLET_CREATE_FAIL("appletCreateFail", "小程序创建失败,请稍后重试！"),
    APPLET_UPDATE_FAIL("appletUpdateFail", "小程序修改失败,请稍后重试"),
    // 其他
    NOT_INTERFACE_DATA_AUTHORITY("notInterfaceDataAuthority", "你没有该接口的数据权限，请联系管理员关联用户组！"),
    ALREADY_THE_LAST_LINE("alreadyTheLastLine", "已经是最后一行了！"),
    ALREADY_THE_FIRST_LINE("alreadyTheFirstLine", "已经是第一行了！"),
    NOT_CHILDREN_ADD("notChildrenAdd", "不能向子集添加！"),
    YOU_NOT_SET_PASSWORD("youNotSetPassword", "你还未设置密码，请选择其他登录方式！"),
    /****************************接口管理*********************************/

    // 接口供应商
    API_SUPPLIER_ID_ERROR("apiSupplierIdError", "接口供应商主键错误!"),
    API_SUPPLIER_CODE_ERROR("apiSupplierCodeError", "接口供应商标识错误!"),
    API_SUPPLIER_APP_ID_ERROR("apiSupplierAppIdError", "接口供应商应用主键错误!"),
    API_SUPPLIER_STATUS_INVALID("apiSupplierStatusInvalid", "接口供应商的状态无效!"),
    API_SUPPLIER_CREATE_FAIL("apiSupplierCreateFail", "接口供应商创建失败,请稍后重试！"),
    API_SUPPLIER_CODE_REPEAT("apiSupplierCodeRepeat", "接口供应商标识重复,请稍后重试！"),
    API_SUPPLIER_UPDATE_FAIL("apiSupplierUpdateFail", "接口供应商修改失败,请稍后重试！"),
    API_SUPPLIER_DELETE_FAIL("apiSupplierDeleteFail", "接口供应商删除失败,请稍后重试！"),

    // 消息模板
    MESSAGE_TEMPLATE_ID_ERROR("messageTemplateIdError", "消息模板主键错误!"),
    MESSAGE_TEMPLATE_CODE_ERROR("messageTemplateCodeError", "消息模板标识错误!"),
    MESSAGE_TEMPLATE_APP_ID_ERROR("messageTemplateAppIdError", "消息模板应用主键错误!"),
    MESSAGE_TEMPLATE_STATUS_INVALID("messageTemplateStatusInvalid", "消息模板的状态无效!"),
    MESSAGE_TEMPLATE_CREATE_FAIL("messageTemplateCreateFail", "消息模板创建失败,请稍后重试！"),
    MESSAGE_TEMPLATE_CODE_REPEAT("messageTemplateCodeRepeat", "消息模板标识重复,请稍后重试！"),
    MESSAGE_TEMPLATE_UPDATE_FAIL("messageTemplateUpdateFail", "消息模板修改失败,请稍后重试！"),
    MESSAGE_TEMPLATE_DELETE_FAIL("messageTemplateDeleteFail", "消息模板删除失败,请稍后重试！"),
    MESSAGE_TEMPLATE_OTHER_CONFIG_NOT_NULL("messageTemplateOtherConfigNotNull", "消息的其他配置不能为空！"),
    MESSAGE_TEMPLATE_LACK_SMS_SDK_APP_ID_ERROR("messageTemplateLackSmsSdkAppIdError", "消息模板的其他配置缺少SmsSdkAppid！"),

    /****************************网站基础*********************************/
    // 菜单
    MENU_CREATE_FAIL("menuCreateFail", "菜单创建失败,请稍后重试!"),
    MENU_UPDATE_FAIL("menuUpdateFail", "菜单修改失败,请稍后重试!"),
    MENU_DELETE_FAIL("menuDeleteFail", "菜单删除失败,请稍后重试!"),
    MENU_CODE_REPEAT("menuCodeRepeat", "菜单的标识重复！"),
    MENU_ID_ERROR("menuIdError", "菜单的id错误！"),
    MENU_APP_ID_ERROR("menuAppIdError", "菜单的appId错误！"),

    // 栏目
    COLUMN_CREATE_FAIL("columnCreateFail", "栏目创建失败,请稍后重试！"),
    COLUMN_UPDATE_FAIL("columnUpdateFail", "栏目修改失败,请稍后重试！"),
    COLUMN_DELETE_FAIL("columnDeleteFail", "栏目删除失败,请稍后重试!"),
    COLUMN_ID_ERROR("columnIdError", "栏目的id错误！"),
    COLUMN_CODE_REPEAT("columnCodeRepeat", "栏目标识重复！"),
    COLUMN_CODE_NOT_NULL("columnCodeNotNull", "栏目的code不能为空！"),
    COLUMN_APP_ID_ERROR("columnAppIdError", "栏目的appId错误！"),

    // 数据类型
    DATA_TYPE_CREATE_FAIL("dataTypeCreateFail", "数据类型创建失败，请稍后重试！"),
    DATA_TYPE_UPDATE_FAIL("dataTypeUpdateFail", "数据类型修改失败，请稍后重试！"),
    DATA_TYPE_DELETE_FAIL("dataTypeDeleteFail", "数据类型删除失败，请稍后重试！"),
    DATA_TYPE_CODE_REPEAT("dataTypeCodeRepeat", "数据类型标识重复！"),
    DATA_TYPE_ID_ERROR("dataTypeIdError", "数据类型的id错误！"),
    DATA_TYPE_APP_ID_ERROR("dataTypeAppIdError", "数据类型的appId错误！"),

    // 页面
    PAGE_CREATE_FAIL("pageCreateFail", "页面创建失败，请稍后重试！"),
    PAGE_UPDATE_FAIL("pageUpdateFail", "页面修改失败，请稍后重试！"),
    PAGE_DELETE_FAIL("pageDeleteFail", "页面删除失败，请稍后重试！"),
    PAGE_ID_ERROR("pageIdError", "页面的id错误！"),
    PAGE_APP_ID_ERROR("pageAppIdError", "页面的appId错误！"),
    PAGE_CODE_REPEAT("pageCodeRepeat", "页面标识重复！"),
    PAGE_AND_COLUMN_RELATION_CREATE_FAIL("pageAndColumnRelationCreateFail", "页面和栏目关联创建失败,请稍后重试！");


    /**
     * 错误code
     */
    private String code;
    /**
     * 消息
     */
    private String message;

    FailCode() {
    }

    FailCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public OperateException getOperateException() {
        return new OperateException(this.code, this.getMessage());
    }

    public OperateException getOperateException(String... array) {
        String message = StringUtils.analysisTemplate(this.getMessage(), array);
        return new OperateException(this.code, message);
    }
}
