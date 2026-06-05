package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>审核权限bean</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthorizeAuthorityBean extends QueryAuthorizeBean {

    @ApiModelProperty(value = "权限的appId")
    private String authorityAppId;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;
}

