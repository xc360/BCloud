package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>页面参数</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class PageBean {

    @ApiModelProperty(value = "页面名称")
    private String name;

    @ApiModelProperty(value = "页面标识")
    private String code;

    @ApiModelProperty(value = "数据类型")
    private String dataType;

    @ApiModelProperty(value = "seo标题")
    private String seoTitle;

    @ApiModelProperty(value = "seo关键字")
    private String seoKeywords;

    @ApiModelProperty(value = "seo描述")
    private String seoDescription;

    @ApiModelProperty(value = "页面文件地址")
    private String filePath;

    @ApiModelProperty(value = "页面重定向地址")
    private String redirectUrl;

    @ApiModelProperty(value = "菜单标识")
    private String menuCode;

    @ApiModelProperty(value = "状态，对应字典：effectStatus，0：有效，1：无效")
    private String status;

    @ApiModelProperty(value = "栏目主键集合")
    private List<String> columnIds;
}
