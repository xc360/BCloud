package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>查询权限参数类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class QueryAuthorityBean {

    @ApiModelProperty(value = "权限名称")
    private String name;

    @ApiModelProperty(value = "权限描述")
    private String describe;

    @ApiModelProperty(value = "权限code")
    private String code;

    @ApiModelProperty(value = "权限类型，对应字典：authorityType，0：菜单，1：接口，2：按钮，3：开放菜单，4：应用权限，5：用户信息权限")
    private String type;

    @ApiModelProperty(value = "父级节点")
    private String parentNode;

    @ApiModelProperty(value = "权限类型集合")
    private List<String> types;

    @ApiModelProperty(value = "状态，对应字典：effectStatus，0：有效，1：无效")
    private String status;
}
