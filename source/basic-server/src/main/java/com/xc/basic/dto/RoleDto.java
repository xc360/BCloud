package com.xc.basic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>角色返回参数</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class RoleDto {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色标识")
    private String code;

    @ApiModelProperty(value = "角色描述")
    private String describe;

    @ApiModelProperty(value = "角色类型，对应字典：roleType，0：普通角色，1：基础角色")
    private String type;

    @ApiModelProperty(value = "排序")
    private Long seq;

    @ApiModelProperty(value = "应用id")
    private String appId;

    @ApiModelProperty(value = "状态，对应字典：effectStatus，0：有效，1：无效")
    private String status;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "权限id集合")
    private List<String> authorityIds;
}
