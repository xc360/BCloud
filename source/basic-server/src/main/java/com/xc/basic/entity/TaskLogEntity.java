package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 任务记录实体
 * </p>
 *
 * @author xc
 * @since 2026-05-13
 */
@Data
@TableName("xc_task_log")
public class TaskLogEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 记录标识
     */
    private String code;
    /**
     * 执行类型：0：手动执行，1：自动执行
     */
    private String type;
    /**
     * 任务标识
     */
    private String taskCode;
    /**
     * 请求url
     */
    private String url;
    /**
     * 执行耗时（毫秒）
     */
    private Long duration;
    /**
     * 异常信息
     */
    private String errorMsg;
    /**
     * 应用主键
     */
    private String appId;
    /**
     * 是否成功，0：成功，1：未成功
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
