package com.xc.basic.enums;


/**
 * <p>账号类型</p>
 *
 * @author xc
 * @version v1.0.0
 */
public enum AccountType {
    PASSWORD("password"), // 密码登录
    EMAIL("email"), // 邮箱
    PHONE("phone"); // 手机


    /**
     * 类型
     */
    private String type;

    AccountType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
