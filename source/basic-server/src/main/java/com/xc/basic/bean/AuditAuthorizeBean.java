package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 审核权限
 * </p>
 *
 * @author xc
 * @since 2023-08-01
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AuditAuthorizeBean extends QueryAuthorizeBean {

    @ApiModelProperty(value = "应用主键")
    private String appId;

    @ApiModelProperty(value = "权限应用主键")
    private String authorityAppId;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;
}
