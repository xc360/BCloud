package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 任务实体
 * </p>
 *
 * @author xc
 * @since 2026-05-13
 */
@Data
@TableName("xc_task")
public class TaskEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 标识
     */
    private String code;
    /**
     * cron表达式
     */
    private String cron;
    /**
     * 请求url
     */
    private String url;
    /**
     * 任务描述
     */
    @TableField("`describe`")
    private String describe;
    /**
     * 应用主键
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
