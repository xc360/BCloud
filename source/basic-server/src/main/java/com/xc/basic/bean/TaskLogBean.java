package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 任务记录bean
 * </p>
 *
 * @author xc
 * @since 2026-05-13
 */
@Data
public class TaskLogBean {

    @ApiModelProperty(value = "记录标识")
    private String code;

    @ApiModelProperty(value = "执行类型：0：手动执行，1：自动执行")
    private String type;

    @ApiModelProperty(value = "任务标识")
    private String taskCode;

    @ApiModelProperty(value = "请求url")
    private String url;

    @ApiModelProperty(value = "执行耗时（毫秒）")
    private Long duration;

    @ApiModelProperty(value = "异常信息")
    private String errorMsg;

    @ApiModelProperty(value = "是否成功，0：成功，1：未成功")
    private String status;
}
