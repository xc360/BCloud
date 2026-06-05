package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.basic.dto.ColumnDto;
import com.xc.basic.bean.ColumnBean;
import com.xc.basic.entity.ColumnEntity;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;

import java.util.List;

/**
 * <p>栏目管理</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface ColumnService extends IService<ColumnEntity> {

    /**
     * 获取应用栏目分页数据
     *
     * @param current      当前页
     * @param pagingBean   分页栏目
     * @param columnEntity 应用栏目参数
     * @return 分页栏目及应用栏目数据
     */
    public PagingDto<ColumnDto> getColumnPage(Integer current, PagingBean pagingBean, ColumnEntity columnEntity);

    /**
     * 查询栏目
     *
     * @param columnEntity 栏目参数
     * @return 栏目信息
     */
    public ColumnDto getColumn(ColumnEntity columnEntity);

    /**
     * 创建应用栏目
     *
     * @param columnEntity 栏目参数
     * @return 栏目信息
     */
    public ColumnDto createColumn(ColumnEntity columnEntity);

    /**
     * 修改应用栏目
     *
     * @param columnEntity 栏目参数
     * @return 栏目信息
     */
    public ColumnDto updateColumn(ColumnEntity columnEntity);

    /**
     * 删除栏目
     *
     * @param columnEntity 栏目实体
     */
    public void deleteColumn(ColumnEntity columnEntity);

    /**
     * 获取所有子集栏目
     *
     * @param appId 应用主键
     * @param node  节点
     * @return 子集栏目集合
     */
    public List<ColumnEntity> getChildrenColumn(String appId, String node);

    /**
     * 获取栏目集合
     *
     * @param queryBean    基础条件
     * @param columnEntity 栏目查询条件
     * @return 权限集合
     */
    public List<ColumnDto> getColumnList(QueryBean queryBean, ColumnEntity columnEntity);

    /**
     * 栏目批量添加
     *
     * @param appId       应用id
     * @param columnBeans 栏目集合
     * @return 栏目集合
     */
    public List<ColumnDto> createColumnList(String appId, List<ColumnBean> columnBeans);

    /**
     * 上下移动
     *
     * @param entity 栏目实体
     * @param isUp   是否向上
     */
    public boolean move(ColumnEntity entity, boolean isUp);
}
