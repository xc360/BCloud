package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>接口供应商</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_api_supplier")
public class ApiSupplierEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 接口供应商名称
     */
    private String name;
    /**
     * 接口供应商标识
     */
    private String code;
    /**
     * 访问id
     */
    private String accessId;
    /**
     * 访问秘钥
     */
    private String accessSecret;
    /**
     * 其他配置，json格式
     */
    private String otherConfig;
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
