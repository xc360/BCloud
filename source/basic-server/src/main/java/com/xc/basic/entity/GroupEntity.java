package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * <p>用户组实体</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_group")
public class GroupEntity {
    /**
     * 用户组主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 用户组名称
     */
    private String name;
    /**
     * 用户组标识
     */
    private String code;
    /**
     * 用户组描述
     */
    @TableField("`describe`")
    private String describe;

    /**
     * 应用主键id
     */
    private String appId;
    /**
     * 状态
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
     * 版本
     */
    @Version
    private Integer version;

}
