package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>应用参数bean</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class AppUserBean {

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "角色主键")
    private String roleId;

    @ApiModelProperty(value = "用户组主键")
    private String groupId;

    @ApiModelProperty(value = "备注")
    private String remark;
}
