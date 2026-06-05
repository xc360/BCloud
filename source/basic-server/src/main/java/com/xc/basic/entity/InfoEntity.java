package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * <p>应用信息实体</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_info")
public class InfoEntity {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 信息key
     */
    @TableField("`key`")
    private String key;
    /**
     * 应用主键value
     */
    private String value;
    /**
     * 信息描述
     */
    @TableField("`describe`")
    private String describe;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 信息类型，对应字典：infoType，0：客户端信息，1：后台信息
     */
    private String type;
    /**
     * 状态，对应字典：effectStatus，0：有效，1：无效
     */
    private String status;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 乐观锁
     */
    @Version
    private Integer version;
}
