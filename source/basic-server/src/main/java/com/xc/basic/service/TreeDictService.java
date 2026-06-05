package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.basic.dto.TreeDictDto;
import com.xc.basic.bean.TreeDictBean;
import com.xc.basic.entity.TreeDictEntity;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;

import java.util.List;

/**
 * <p>树形字典Service接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface TreeDictService extends IService<TreeDictEntity> {
    /**
     * 获取树形字典分页数据
     *
     * @param current        当前页
     * @param pagingBean     分页信息
     * @param treeDictEntity 树形字典参数
     * @return 分页信息及树形字典数据
     */
    public PagingDto<TreeDictDto> getTreeDictPage(Integer current, PagingBean pagingBean, TreeDictEntity treeDictEntity);

    /**
     * 获取树形字典集合
     *
     * @param queryBean      基础查询条件
     * @param treeDictEntity 查询条件
     * @return 树形字典集合
     */
    public List<TreeDictDto> getTreeDictList(QueryBean queryBean, TreeDictEntity treeDictEntity);

    /**
     * 删除树形字典
     *
     * @param treeDictEntity 树形字典实体
     */
    public void deleteTreeDict(TreeDictEntity treeDictEntity);

    /**
     * 获取所有子集树形字典
     *
     * @param appId 应用主键
     * @param node  节点
     * @return 子集树形字典集合
     */
    public List<TreeDictEntity> getChildrenTreeDict(String appId, String node);

    /**
     * 树形字典批量添加
     *
     * @param appId         应用主键
     * @param treeDictBeans 树形字典集合
     * @return 树形字典集合
     */
    public List<TreeDictDto> createTreeDictList(String appId, List<TreeDictBean> treeDictBeans);

    /**
     * 获取树形字典集合
     *
     * @param appId     应用主键
     * @param type      树形字典类型,为null查询全部
     * @param firstCode 第一位的标识,为null查询全部
     * @return 树形字典集合
     */
    public List<TreeDictDto> getTreeDictList(String appId, String type, String firstCode);

    /**
     * 获取树形节点集合
     *
     * @param appId 应用主键
     * @param type  树形字典类型
     * @param code  树形字典标识
     * @return 树形节点集合
     */
    public List<TreeDictDto> getTreeNodeList(String appId, String type, String code);

    /**
     * 上下移动
     *
     * @param entity 树形字典实体
     * @param isUp   是否向上
     */
    public boolean move(TreeDictEntity entity, boolean isUp);
}
