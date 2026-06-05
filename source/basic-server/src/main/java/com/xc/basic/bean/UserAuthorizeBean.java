package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>应用权限查询参数</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class UserAuthorizeBean {

    @ApiModelProperty(value = "应用名称")
    private String appName;
}
