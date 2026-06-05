package com.xc.basic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>消息日志dto</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class MessageLogDto {

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "消息接收者账号")
    private String account;

    @ApiModelProperty(value = "发送内容")
    private String content;

    @ApiModelProperty(value = "消息模板主键")
    private String messageTemplateCode;

    @ApiModelProperty(value = "应用主键")
    private String appId;

    @ApiModelProperty(value = "是否成功，0：成功，1：未成功")
    private String status;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
