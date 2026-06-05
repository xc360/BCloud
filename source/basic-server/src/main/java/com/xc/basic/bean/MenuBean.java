package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>菜单bean</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class MenuBean {

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "菜单描述")
    private String describe;

    @ApiModelProperty(value = "菜单code")
    private String code;

    @ApiModelProperty(value = "菜单类型")
    private String type;

    @ApiModelProperty(value = "跳转地址")
    private String url;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "节点")
    private String node;

    @ApiModelProperty(value = "父级节点")
    private String parentNode;

    @ApiModelProperty(value = "排序")
    private Long seq;

    @ApiModelProperty(value = "是否隐藏，对应字典：whether，0：是，1：否")
    private String hidden;

    @ApiModelProperty(value = "状态，对应字典：effectStatus，0：有效，1：无效")
    private String status;
}
