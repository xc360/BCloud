package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>页面栏目关联bean</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class PageColumnBean {

    @ApiModelProperty(value = "页面标识")
    private String pageCode;

    @ApiModelProperty(value = "栏目标识")
    private String columnCode;
}
