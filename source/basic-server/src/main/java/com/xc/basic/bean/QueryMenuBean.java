
package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>查询菜单参数类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class QueryMenuBean {

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "菜单描述")
    private String describe;

    @ApiModelProperty(value = "菜单code")
    private String code;

    @ApiModelProperty(value = "菜单类型")
    private String type;

    @ApiModelProperty(value = "父级节点")
    private String parentNode;

    @ApiModelProperty(value = "状态，对应字典：effectStatus，0：有效，1：无效")
    private String status;
}
