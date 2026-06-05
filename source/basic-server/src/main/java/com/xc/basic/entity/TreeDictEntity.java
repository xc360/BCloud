package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>树形字典实体类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_tree_dict")
public class TreeDictEntity {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 树形字典名称
     */
    private String name;
    /**
     * 树形字典code
     */
    private String code;
    /**
     * 树形字典类型
     */
    private String type;
    /**
     * 节点
     */
    private String node;
    /**
     * 父级节点
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
