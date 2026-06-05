package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * <p>应用字典实体类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_dict")
public class DictEntity {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 字典名称
     */
    private String name;
    /**
     * 字典值
     */
    private String value;
    /**
     * 字典类型
     */
    private String type;
    /**
     * 排序
     */
    private Long seq;
    /**
     * 应用主键id
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
