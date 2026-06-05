package com.xc.basic.enums;

/**
 * <p>redis前缀</p>
 *
 * @author xc
 * @version v1.0.0
 */
public enum RedisPrefix {
    CODE("code:"), // code
    ACCESS("access:"), // token
    REFRESH("refresh:"), // 刷新
    CAPTCHA("captcha:"),// 验证码
    CAPTCHA_NUM("captchaNum:"), // 账号发送验证码次数
    CAPTCHA_CODE("captchaCode:"), // 验证码code
    CAPTCHA_IP("captchaIp:"), // 验证码id地址
    VERIFY_NUM("verifyNum:"), // 验证码验证次数
    TASK("task:"), // 任务
    ;

    /**
     * redis前缀
     */
    private final String key;

    RedisPrefix(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
