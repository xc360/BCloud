package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>应用程序实体</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_app")
public class AppEntity {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 应用程序名称
     */
    private String appName;
    /**
     * 应用程序ID
     */
    private String appId;
    /**
     * 应用程序秘钥
     */
    private String appSecret;
    /**
     * 用户主键
     */
    private String userId;
    /**
     * 用户组主键
     */
    private String groupId;
    /**
     * 应用域名
     */
    private String domain;
    /**
     * 应用IP
     */
    private String localIp;
    /**
     * 首页地址
     */
    private String homeUrl;
    /**
     * 刷新地址
     */
    private String refreshUrl;
    /**
     * logo地址
     */
    private String logoUrl;
    /**
     * 是否公开，对应字典：whether，0：是，1：否
     */
    private String isOpen;
    /**
     * 是否同时在线，对应字典：whether，0：是，1：否
     */
    private String isCoexist;
    /**
     * 是否显示用户权限，对应字典：whether，0：是，1：否
     */
    private String showUserAuthority;
    /**
     * token有效时间
     */
    private Long tokenValidTime;
    /**
     * 签名有效时间
     */
    private Long signValidTime;
    /**
     * 刷新token有效时间
     */
    private Long refreshTokenValidTime;
    /**
     * 是否打开邮箱登录，对应字典：whether，0：是，1：否
     */
    private String openEmailLogin;
    /**
     * 是否打开手机登录，对应字典：whether，0：是，1：否
     */
    private String openPhoneLogin;
    /**
     * 是否打开手机找回密码，对应字典：whether，0：是，1：否
     */
    private String openPhoneForget;
    /**
     * 是否打开邮箱找回密码，对应字典：whether，0：是，1：否
     */
    private String openEmailForget;
    /**
     * 是否打开手机注册，对应字典：whether，0：是，1：否
     */
    private String openPhoneRegister;
    /**
     * 是否打开邮箱注册，对应字典：whether，0：是，1：否
     */
    private String openEmailRegister;
    /**
     * 状态，对应字典：effectStatus，0：有效，1：无效
     */
    private String status;
    /**
     * 审核状态，对应字典：auditStatus，0：未申请，1：未审核，2：已审核，3：已拒绝
     */
    private String auditStatus;
    /**
     * 申请，拒绝，取消原因
     */
    private String reason;
    /**
     * 发布时间
     */
    private Date applyTime;
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


    public AppEntity() {

    }

    public AppEntity(String userId) {
        this.userId = userId;
    }
}
