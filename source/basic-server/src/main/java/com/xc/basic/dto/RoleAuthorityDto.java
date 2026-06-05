package com.xc.basic.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>角色和权限关联dto</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class RoleAuthorityDto {

    @ApiModelProperty(value = "角色标识")
    private String roleCode;

    @ApiModelProperty(value = "权限标识")
    private String authorityCode;
}
