package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>查询公开的版本信息</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class QueryOvertVersionBean {

    @ApiModelProperty(value = "版本号")
    private String appVersion;
}
