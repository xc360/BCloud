package com.xc.basic.enums;

/**
 * <p>状态</p>
 *
 * @author xc
 * @version v1.0.0
 */
public enum UserStatus {
    NORMAL("0"),// 正常
    LOCKING("1");// 锁定


    /**
     * 状态
     */
    private String status;

    UserStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
