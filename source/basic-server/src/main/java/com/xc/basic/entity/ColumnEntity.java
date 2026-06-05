package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * <p>栏目实体</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_column")
public class ColumnEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 栏目名称
     */
    private String name;
    /**
     * 栏目标签
     */
    private String label;
    /**
     * 栏目描述
     */
    @TableField("`describe`")
    private String describe;
    /**
     * 栏目标识
     */
    private String code;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 栏目url
     */
    private String url;
    /**
     * 栏目图标
     */
    private String icon;
    /**
     * 栏目大小
     */
    private Integer columnSize;
    /**
     * 节点
     */
    private String node;
    /**
     * 父节点
     */
    private String parentNode;
    /**
     * 排序
     */
    private Long seq;
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
