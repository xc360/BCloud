package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>用户组参数类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class GroupBean {

    @ApiModelProperty(value = "用户组名称")
    private String name;

    @ApiModelProperty(value = "用户组标识")
    private String code;

    @ApiModelProperty(value = "用户组描述")
    private String describe;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "角色id集合,添加，修改是使用")
    private List<String> roleIds;
}
