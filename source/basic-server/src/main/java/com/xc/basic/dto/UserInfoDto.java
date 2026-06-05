package com.xc.basic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>用户名信息</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class UserInfoDto {

    @ApiModelProperty(value = "用户主键")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "头像")
    private String portrait;

    @ApiModelProperty(value = "个人说明")
    private String explain;

    @ApiModelProperty(value = "性别：对应字典：sex，0：女，1：男")
    private String sex;

    @ApiModelProperty(value = "生日")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date birthday;

    @ApiModelProperty(value = "区域")
    private String region;

    @ApiModelProperty(value = "创建时间 不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间 不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
