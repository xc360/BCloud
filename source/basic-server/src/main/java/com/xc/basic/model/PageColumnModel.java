package com.xc.basic.model;

import com.xc.basic.entity.ColumnEntity;
import com.xc.basic.entity.PageEntity;
import lombok.Data;

import java.util.List;

/**
 * <p>页面栏目model</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class PageColumnModel {
    /**
     * 页面集合
     */
    private List<PageEntity> pageEntities;
    /**
     * 栏目集合
     */
    private List<ColumnEntity> columnEntities;
}
