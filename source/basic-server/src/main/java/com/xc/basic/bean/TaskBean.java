package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 任务bean
 * </p>
 *
 * @author xc
 * @since 2026-05-13
 */
@Data
public class TaskBean {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "标识")
    private String code;

    @ApiModelProperty(value = "cron表达式")
    private String cron;

    @ApiModelProperty(value = "请求url")
    private String url;
    
    @ApiModelProperty(value = "任务描述")
    private String describe;

    @ApiModelProperty(value = "状态，对应字典：effectStatus，0：有效，1：无效")
    private String status;
}
