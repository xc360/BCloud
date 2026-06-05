package com.xc.basic.enums;

/**
 * <p>消息标识</p>
 *
 * @author xc
 * @version v1.0.0
 */
public enum MessageCode {
    EMAIL_EXCEPTION_NOTICE("email_exception_notice"), // 邮件发送系统异常通知
    PHONE_EXCEPTION_NOTICE("phone_exception_notice"), // 手机发送系统异常通知
    PHONE_LOGIN("phone_login"), // 手机号登录
    PHONE_REGISTER("phone_register"), // 手机号注册
    PHONE_FORGET_PASSWORD("phone_forget_password"), // 手机号找回密码
    PHONE_UPDATE("phone_update"), // 手机号修改
    PHONE_UNSUBSCRIBE("phone_unsubscribe"), // 手机号注销
    PHONE_UPDATE_PHONE("phone_update_phone"), // 手机修改手机
    PHONE_UPDATE_EMAIL("phone_update_email"), // 手机修改邮箱
    EMAIL_LOGIN("email_login"), // 邮箱登录
    EMAIL_REGISTER("email_register"), // 邮箱注册
    EMAIL_FORGET_PASSWORD("email_forget_password"), // 邮箱找回密码
    EMAIL_UPDATE("email_update"), // 邮箱修改
    EMAIL_UNSUBSCRIBE("email_unsubscribe"), // 邮箱注销
    EMAIL_UPDATE_PHONE("email_update_phone"), // 邮箱修改手机
    EMAIL_UPDATE_EMAIL("email_update_email"); // 邮箱修改邮箱

    /**
     * 类型
     */
    private String code;

    MessageCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
