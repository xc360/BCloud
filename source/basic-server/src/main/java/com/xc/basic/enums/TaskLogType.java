package com.xc.basic.enums;

import com.sun.media.sound.FFT;

/**
 * <p>
 *
 * </p>
 *
 * @author xc
 * @since 2026-05-13
 */
public enum TaskLogType {
    MANUAL("0"),
    AUTO("1");

    /**
     * 类型
     */
    private final String type;

    TaskLogType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
