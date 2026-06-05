package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>应用查询bean</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class QueryAppBean {

    @ApiModelProperty(value = "应用程序名称")
    private String appName;

    @ApiModelProperty(value = "应用程序id")
    private String appId;

    @ApiModelProperty(value = "审核状态，对应字典：auditStatus，0：未申请，1：未审核，2：已审核，3：已拒绝")
    private String auditStatus;
}
