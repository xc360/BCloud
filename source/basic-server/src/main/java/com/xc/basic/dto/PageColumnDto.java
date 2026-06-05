package com.xc.basic.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>页面栏目关联dto</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class PageColumnDto {

    @ApiModelProperty(value = "页面标识")
    private String pageCode;

    @ApiModelProperty(value = "栏目标识")
    private String columnCode;
}
