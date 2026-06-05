package com.xc.basic.model;

import com.xc.api.basic.bean.CaptchaBean;
import lombok.Data;

/**
 * <p>验证码频繁操作Model</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class CaptchaFrequentModel {
    /**
     * 账号
     */
    private String account;
    /**
     * 消息标识
     */
    private String messageCode;
    /**
     * 客户端ip地址
     */
    private String clientIp;
    /**
     * 图片验证码
     */
    private String captcha;
    /**
     * 图片验证码code
     */
    private String code;
}
