package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * <p>页面实体</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_page")
public class PageEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 页面名称
     */
    private String name;
    /**
     * 页面标识
     */
    private String code;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * seo标题
     */
    private String seoTitle;
    /**
     * seo关键字
     */
    private String seoKeywords;
    /**
     * seo描述
     */
    private String seoDescription;
    /**
     * 页面文件地址
     */
    private String filePath;
    /**
     * 页面重定向地址
     */
    private String redirectUrl;
    /**
     * 菜单标识
     */
    private String menuCode;
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
