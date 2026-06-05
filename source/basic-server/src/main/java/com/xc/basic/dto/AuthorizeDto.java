package com.xc.basic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>获取app秘钥</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class AuthorizeDto {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "应用主键")
    private String appId;

    @ApiModelProperty(value = "权限应用主键")
    private String authorityAppId;

    @ApiModelProperty(value = "权限主键")
    private String authorityId;

    @ApiModelProperty(value = "权限名称")
    private String authorityName;

    @ApiModelProperty(value = "权限标识")
    private String authorityCode;

    @ApiModelProperty(value = "权限类型，对应字典：authorityType，0：菜单，1：接口，2：按钮，3：开放菜单，4：应用权限，5：用户信息权限")
    private String authorityType;

    @ApiModelProperty(value = "审核状态，对应字典：auditStatus，0：未申请，1：未审核，2：已审核，3：已拒绝")
    private String auditStatus;

    @ApiModelProperty(value = "申请，拒绝，取消原因")
    private String reason;

    @ApiModelProperty(value = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;
}
