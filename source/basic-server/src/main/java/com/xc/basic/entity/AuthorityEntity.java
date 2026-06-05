package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * <p>权限实体</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_authority")
public class AuthorityEntity {
    /**
     * 用户主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 权限名称
     */
    private String name;
    /**
     * 权限code
     */
    private String code;
    /**
     * 权限类型，对应字典：authorityType，0：菜单，1：接口，2：按钮，3：开放菜单，4：应用权限，5：用户信息权限
     */
    private String type;
    /**
     * 跳转地址
     */
    private String url;
    /**
     * 图标
     */
    private String icon;

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
     * 是否隐藏，对应字典：whether，0：是，1：否
     */
    private String hidden;
    /**
     * 应用主键id
     */
    private String appId;
    /**
     * 状态，对应字典：effectStatus，0：有效，1：无效
     */
    private String status;
    /**
     * 权限备注
     */
    @TableField("`describe`")
    private String describe;
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
