package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * <p>消息记录实体</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_message_log")
public class MessageLogEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 消息接收者账号
     */
    private String account;
    /**
     * 发送内容
     */
    private String content;
    /**
     * 消息模板主键
     */
    private String messageTemplateCode;
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
