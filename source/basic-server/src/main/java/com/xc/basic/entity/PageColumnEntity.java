package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>栏目权限关联实体</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_page__column")
public class PageColumnEntity {

    /**
     * 关联主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 栏目id
     */
    private String columnId;
    /**
     * 页面主键
     */
    private String pageId;

}
