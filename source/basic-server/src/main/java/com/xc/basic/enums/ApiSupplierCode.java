package com.xc.basic.enums;

/**
 * <p>接口供应商标识</p>
 *
 * @author xc
 * @version v1.0.0
 */
public enum ApiSupplierCode {
    ALI_SMS("ali_sms"), // 阿里短信
    QQ_SMS("qq_sms"), // 腾讯短信
    ZT_SMS("zt_sms"), // 助通科技
    QQ_MAIL("qq_mail"); // 腾讯邮箱

    /**
     * 标识
     */
    private String code;

    ApiSupplierCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
