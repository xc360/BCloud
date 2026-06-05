package com.xc.basic.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 任务返回参数
 * </p>
 *
 * @author xc
 * @since 2026-05-13
 */
@Data
public class TaskDto {

    @ApiModelProperty(value = "主键")
    private String id;

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

    @ApiModelProperty(value = "应用主键")
    private String appId;

    @ApiModelProperty(value = "状态，对应字典：effectStatus，0：有效，1：无效")
    private String status;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "乐观锁")
    private Integer version;
}
