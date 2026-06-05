package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * <p>消息实体</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_message_template")
public class MessageTemplateEntity {
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
     * 消息标识
     */
    private String code;
    /**
     * 签名
     */
    private String signName;
    /**
     * 模板code
     */
    private String templateCode;
    /**
     * 模板
     */
    private String template;
    /**
     * 其他配置，json格式
     */
    private String otherConfig;
    /**
     * 接口供应标识
     */
    private String apiSupplierCode;
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
