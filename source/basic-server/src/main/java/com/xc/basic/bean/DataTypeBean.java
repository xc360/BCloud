package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>数据类型参数</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class DataTypeBean {

    @ApiModelProperty(value = "数据类型名称")
    private String name;

    @ApiModelProperty(value = "数据类型标识")
    private String code;

    @ApiModelProperty(value = "排序")
    private Long seq;

    @ApiModelProperty(value = "状态，对应字典：effectStatus，0：有效，1：无效")
    private String status;
}
