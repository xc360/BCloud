package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>应用参数bean</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class AppBean {

    @ApiModelProperty(value = "应用名称")
    private String appName;

    @ApiModelProperty(value = "用户组主键")
    private String groupId;

    @ApiModelProperty(value = "应用域名")
    private String domain;

    @ApiModelProperty(value = "应用IP")
    private String localIp;

    @ApiModelProperty(value = "首页地址")
    private String homeUrl;

    @ApiModelProperty(value = "刷新地址")
    private String refreshUrl;

    @ApiModelProperty(value = "logo地址")
    private String logoUrl;

    @ApiModelProperty(value = "是否公开，对应字典：whether，0：是，1：否")
    private String isOpen;

    @ApiModelProperty(value = "是否同时在线，对应字典：whether，0：是，1：否")
    private String isCoexist;

    @ApiModelProperty(value = "是否显示用户权限，对应字典：whether，0：是，1：否")
    private String showUserAuthority;

    @ApiModelProperty(value = "token有效时间")
    private Long tokenValidTime;

    @ApiModelProperty(value = "刷新token有效时间")
    private Long refreshTokenValidTime;

    @ApiModelProperty(value = "签名有效时间")
    private Long signValidTime;

    @ApiModelProperty(value = "是否打开邮箱登录，对应字典：whether，0：是，1：否")
    private String openEmailLogin;

    @ApiModelProperty(value = "是否打开手机登录，对应字典：whether，0：是，1：否")
    private String openPhoneLogin;

    @ApiModelProperty(value = "是否打开手机找回密码，对应字典：whether，0：是，1：否")
    private String openPhoneForget;

    @ApiModelProperty(value = "是否打开邮箱找回密码，对应字典：whether，0：是，1：否")
    private String openEmailForget;

    @ApiModelProperty(value = "是否打开手机注册，对应字典：whether，0：是，1：否")
    private String openPhoneRegister;

    @ApiModelProperty(value = "是否打开邮箱注册，对应字典：whether，0：是，1：否")
    private String openEmailRegister;

    @ApiModelProperty(value = "状态，对应字典：effectStatus，0：有效，1：无效")
    private String status;

    @ApiModelProperty(value = "审核状态，对应字典：auditStatus，0：未申请，1：未审核，2：已审核，3：已拒绝")
    private String auditStatus;

    @ApiModelProperty(value = "申请原因")
    private String reason;
}
