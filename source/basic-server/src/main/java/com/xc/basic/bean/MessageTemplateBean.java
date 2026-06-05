package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>消息参数请求类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class MessageTemplateBean {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "消息标识")
    private String code;

    @ApiModelProperty(value = "签名")
    private String signName;

    @ApiModelProperty(value = "模板code")
    private String templateCode;

    @ApiModelProperty(value = "模板")
    private String template;

    @ApiModelProperty(value = "其他配置，json格式")
    private String otherConfig;

    @ApiModelProperty(value = "接口供应标识")
    private String apiSupplierCode;

    @ApiModelProperty(value = "状态，对应字典：effectStatus，0：有效，1：无效")
    private String status;
}
