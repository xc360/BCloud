package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>权限bean</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class AuthorityBean {

    @ApiModelProperty(value = "权限名称")
    private String name;

    @ApiModelProperty(value = "权限描述")
    private String describe;

    @ApiModelProperty(value = "权限code")
    private String code;

    @ApiModelProperty(value = "权限类型，对应字典：authorityType，0：菜单，1：接口，2：按钮，3：开放菜单，4：应用权限，5：用户信息权限")
    private String type;

    @ApiModelProperty(value = "跳转地址")
    private String url;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "节点")
    private String node;

    @ApiModelProperty(value = "父级节点")
    private String parentNode;

    @ApiModelProperty(value = "排序")
    private Long seq;

    @ApiModelProperty(value = "是否隐藏，对应字典：whether，0：是，1：否")
    private String hidden;

    @ApiModelProperty(value = "状态，对应字典：effectStatus，0：有效，1：无效")
    private String status;
}
