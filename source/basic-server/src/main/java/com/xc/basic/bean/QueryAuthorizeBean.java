package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>授权查询bean</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class QueryAuthorizeBean {

    @ApiModelProperty(value = "权限名称")
    private String name;

    @ApiModelProperty(value = "权限标识")
    private String code;

}
