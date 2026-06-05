package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>应用版本</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class VersionBean {

    @ApiModelProperty("版本类型，对应字典：versionType，0：网页应用，1：软件包应用，2：安卓应用")
    private String type;

    @ApiModelProperty(value = "应用版本号")
    private String appVersion;

    @ApiModelProperty(value = "安装包地址")
    private String packageUrl;

    @ApiModelProperty(value = "文档内容")
    private String docContent;

    @ApiModelProperty(value = "更新内容")
    private String updateContent;

    @ApiModelProperty(value = "用户协议")
    private String userAgreement;

    @ApiModelProperty(value = "隐私协议")
    private String privacyAgreement;

    @ApiModelProperty(value = "是否强制升级，对应字典：whether，0：是，1：否")
    private String forceUpgrade;

    @ApiModelProperty(value = "排序")
    private Long seq;

    @ApiModelProperty(value = "状态，对应字典：effectStatus，0：有效，1：无效")
    private String status;
}
