package com.xc.basic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>用户授权的应用信息</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class UserAuthorizeDto {

    @ApiModelProperty(value = "关联主键id")
    private String id;

    @ApiModelProperty(value = "应用程序名称")
    private String appName;

    @ApiModelProperty(value = "授权时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date authorizeTime;
}
