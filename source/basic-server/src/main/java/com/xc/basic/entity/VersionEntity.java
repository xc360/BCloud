package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>版本</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_version")
public class VersionEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 版本类型，对应字典：versionType，0：网页应用，1：软件包应用，2：安卓应用
     */
    private String type;
    /**
     * 应用版本号
     */
    private String appVersion;
    /**
     * 安装包地址
     */
    private String packageUrl;
    /**
     * 文档地址
     */
    private String docContent;
    /**
     * 更新内容
     */
    private String updateContent;
    /**
     * 用户协议
     */
    private String userAgreement;
    /**
     * 隐私协议
     */
    private String privacyAgreement;
    /**
     * 是否强制升级，对应字典：whether，0：是，1：否
     */
    private String forceUpgrade;
    /**
     * 排序
     */
    private Long seq;
    /**
     * 应用主键
     */
    private String appId;
    /**
     * 状态，对应字典：effectStatus，0：有效，1：无效
     */
    private String status;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 乐观锁
     */
    @Version
    private Integer version;

}
