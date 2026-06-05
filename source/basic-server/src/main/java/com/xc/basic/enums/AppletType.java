package com.xc.basic.enums;

/**
 * <p>小程序登录类型</p>
 *
 * @author xc
 * @version v1.0.0
 */
public enum AppletType {
    WX("wx"), // 微信
    BD("bd"), // 百度
    ZFB("zfb"); // 支付宝
    /**
     * 类型
     */
    private String type;

    AppletType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
