package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>栏目bean</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class ColumnBean {

    @ApiModelProperty(value = "栏目名称")
    private String name;

    @ApiModelProperty(value = "栏目标签")
    private String label;

    @ApiModelProperty(value = "栏目code")
    private String code;

    @ApiModelProperty(value = "栏目描述")
    private String describe;

    @ApiModelProperty(value = "数据类型")
    private String dataType;

    @ApiModelProperty(value = "栏目url")
    private String url;

    @ApiModelProperty(value = "栏目图标")
    private String icon;

    @ApiModelProperty(value = "栏目大小")
    private Integer columnSize;

    @ApiModelProperty(value = "节点")
    private String node;

    @ApiModelProperty(value = "父节点")
    private String parentNode;

    @ApiModelProperty(value = "排序")
    private Long seq;

    @ApiModelProperty(value = "状态，对应字典：effectStatus，0：有效，1：无效")
    private String status;
}
