package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>角色bean</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class RoleBean {

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色标识")
    private String code;

    @ApiModelProperty(value = "角色类型，对应字典：roleType，0：普通角色，1：基础角色")
    private String type;

    @ApiModelProperty(value = "角色描述")
    private String describe;

    @ApiModelProperty(value = "排序")
    private Long seq;

    @ApiModelProperty(value = "状态，对应字典：effectStatus，0：有效，1：无效")
    private String status;

    @ApiModelProperty(value = "权限id集合")
    private List<String> authorityIds;
}
