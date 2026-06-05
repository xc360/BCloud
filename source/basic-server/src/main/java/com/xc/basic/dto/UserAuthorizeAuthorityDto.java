package com.xc.basic.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>用户授权权限</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class UserAuthorizeAuthorityDto {

    @ApiModelProperty(value = "权限主键")
    private String id;

    @ApiModelProperty(value = "用户授权主键")
    private String userAuthorizeId;

    @ApiModelProperty(value = "权限主键")
    private String authorityId;

    @ApiModelProperty(value = "权限名称")
    private String name;

    @ApiModelProperty(value = "权限code")
    private String code;

    @ApiModelProperty(value = "应用主键id")
    private String appId;

    @ApiModelProperty(value = "状态，对应字典：effectStatus，0：有效，1：无效")
    private String status;
}
