package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>接口供应商bean</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class ApiSupplierBean {

    @ApiModelProperty(value = "接口供应商名称")
    private String name;

    @ApiModelProperty(value = "接口供应商标识")
    private String code;

    @ApiModelProperty(value = "访问id")
    private String accessId;

    @ApiModelProperty(value = "访问秘钥")
    private String accessSecret;

    @ApiModelProperty(value = "其他配置，json格式")
    private String otherConfig;

    @ApiModelProperty(value = "状态，对应字典：effectStatus，0：有效，1：无效")
    private String status;
}
