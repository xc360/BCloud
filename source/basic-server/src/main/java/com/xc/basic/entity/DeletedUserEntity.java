package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * <p>已删除用户实体</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_deleted_user")
public class DeletedUserEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 访问ID
     */
    private String accessId;
    /**
     * 访问秘钥
     */
    private String accessSecret;
    /**
     * 登录失败次数
     */
    private Integer failRecord;
    /**
     * 是否是初始化管理员，对应字典：whether，0：是，1：否
     */
    private String initialAdmin;
    /**
     * 删除时间 不能为空
     */
    private Date deleteTime;
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
