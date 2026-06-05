package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>公开应用bean</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class QueryOvertAppBean {

    @ApiModelProperty(value = "应用程序名称")
    private String appName;

}
