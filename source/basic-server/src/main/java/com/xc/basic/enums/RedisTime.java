package com.xc.basic.enums;

/**
 * <p>redis有效期</p>
 *
 * @author xc
 * @version v1.0.0
 */
public enum RedisTime {
    /**
     * token有效期，60分钟
     */
    TOKEN_EXPIRY(3600000),
    /**
     * 刷新token有效期，120分钟
     */
    REFRESH_TOKEN_EXPIRY(7200000),
    /**
     * code有效期，15分钟
     */
    CODE_EXPIRY(900000),
    /**
     * 验证码发送间隔，60秒
     */
    CAPTCHA_SEND_INTERVAL(60000),
    /**
     * 验证码有效期,15分钟
     */
    CAPTCHA_EXPIRY(900000),
    /**
     * 验证码code有效期,15分钟
     */
    CAPTCHA_CODE_EXPIRY(900000),
    /**
     * 验证码ip地址有效期
     */
    CAPTCHA_IP_EXPIRY(86400000);

    /**
     * 时间
     */
    private long time;

    RedisTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
