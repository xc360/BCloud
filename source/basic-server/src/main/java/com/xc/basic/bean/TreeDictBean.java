package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>树形字典返回参数</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class TreeDictBean {

    @ApiModelProperty(value = "树形字典名称")
    private String name;

    @ApiModelProperty(value = "树形字典code")
    private String code;

    @ApiModelProperty(value = "树形字典类型")
    private String type;

    @ApiModelProperty(value = "节点")
    private String node;

    @ApiModelProperty(value = "父级id")
    private String parentNode;

    @ApiModelProperty(value = "排序")
    private Long seq;

    @ApiModelProperty(value = "状态，对应字典：effectStatus，0：有效，1：无效")
    private String status;
}
