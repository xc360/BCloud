package com.xc.basic.enums;

/**
 * <p>角色类型</p>
 *
 * @author xc
 * @version v1.0.0
 */
public enum RoleType {
    ORDINARY_ROLE("0"), // 普通角色
    BASIC_ROLE("1"); // 基础角色


    /**
     * 类型
     */
    private String type;

    RoleType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
