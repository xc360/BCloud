package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * <p>小程序实体</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_applet")
public class AppletEntity {
    /***
     *关联主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 小程序类型
     */
    private String type;
    /**
     * 小程序ID
     */
    private String appletId;
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
