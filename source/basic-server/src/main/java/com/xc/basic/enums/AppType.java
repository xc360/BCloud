package com.xc.basic.enums;

/**
 * <p>应用类型</p>
 *
 * @author xc
 * @version v1.0.0
 */
public enum AppType {
    WEB_PAGE("0"), // 网页应用
    INSTALL_PACKAGE("1"); // 安装包应用

    /**
     * 类型
     */
    private String type;

    AppType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
