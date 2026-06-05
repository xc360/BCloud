package com.xc.basic.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>用户组，用户，角色</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class GroupRoleDto {

    @ApiModelProperty(value = "用户组id集合")
    public List<String> groupIds;

    @ApiModelProperty(value = "角色id集合")
    public List<String> roleIds;

    @ApiModelProperty(value = "用户组标识")
    private String groupCode;

    @ApiModelProperty(value = "角色标识")
    private String roleCode;

    @ApiModelProperty(value = "备注")
    private String remark;
}

