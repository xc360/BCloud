package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>用户信息bean</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class UserBean {

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "头像")
    private String portrait;

    @ApiModelProperty(value = "个人说明")
    private String explain;

    @ApiModelProperty(value = "性别：对应字典：sex，0：女，1：男")
    private String sex;

    @ApiModelProperty(value = "年龄")
    private Integer age;
}
