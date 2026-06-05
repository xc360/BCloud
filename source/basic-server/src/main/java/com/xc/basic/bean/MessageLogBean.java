package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>消息日志请求参数</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class MessageLogBean {

    @ApiModelProperty(value = "消息接收者账号")
    private String account;

    @ApiModelProperty(value = "发送内容")
    private String content;

    @ApiModelProperty(value = "是否成功，0：成功，1：未成功")
    private String status;
}
