package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * <p>角色实体</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_role")
public class RoleEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色标识
     */
    private String code;
    /**
     * 角色描述
     */
    @TableField("`describe`")
    private String describe;
    /**
     * 角色类型，对应字典：roleType，0：普通角色，1：基础角色
     */
    private String type;
    /**
     * 排序
     */
    private Long seq;
    /**
     * 应用id
     */
    private String appId;
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
