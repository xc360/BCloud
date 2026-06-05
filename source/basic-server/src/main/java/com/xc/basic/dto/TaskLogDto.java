package com.xc.basic.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 任务记录返回参数
 * </p>
 *
 * @author xc
 * @since 2026-05-13
 */
@Data
public class TaskLogDto {

    @ApiModelProperty(value = "主键")
    private String id;

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

    @ApiModelProperty(value = "乐观锁")
    private Integer version;
}
