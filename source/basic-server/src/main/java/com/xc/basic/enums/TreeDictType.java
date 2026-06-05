package com.xc.basic.enums;

/**
 * <p>应用类型</p>
 *
 * @author xc
 * @version v1.0.0
 */
public enum TreeDictType {
    AREA("area");
    /**
     * 类型
     */
    private String type;

    TreeDictType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
