package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>用户信息bean</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class UserInfoBean {

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "头像")
    private String portrait;

    @ApiModelProperty(value = "个人说明")
    private String explain;

    @ApiModelProperty(value = "性别：对应字典表的sex：0：女，1：男")
    private String sex;

    @ApiModelProperty(value = "生日")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;

    @ApiModelProperty(value = "区域")
    private String region;
}
