package com.xc.basic.model;

import lombok.Data;

/**
 * <p>阿里短信验证码模型</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class AliCaptchaModel {
    /**
     * 请求ID
     */
    private String RequestId;
    /**
     * 状态码的描述。
     */
    private String Message;
    /**
     * 请求状态码。
     */
    private String Code;
    /**
     * 短信发送总条数。
     */
    private String TotalCount;
}
