package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.basic.dto.TreeDictDto;
import com.xc.basic.bean.TreeDictBean;
import com.xc.basic.config.Constants;
import com.xc.basic.entity.TreeDictEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.mapper.TreeDictMapper;
import com.xc.basic.service.TreeDictService;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>树形字典Service实现</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Slf4j
@Service
public class TreeDictServiceImpl extends ServiceImpl<TreeDictMapper, TreeDictEntity> implements TreeDictService {

    @Autowired
    private Constants constants;

    @Override
    public PagingDto<TreeDictDto> getTreeDictPage(Integer current, PagingBean pagingBean, TreeDictEntity treeDictEntity) {
        QueryWrapper<TreeDictEntity> queryWrapper = ServiceUtils.queryLike(treeDictEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        IPage<TreeDictEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        return new PagingDto<>(iPage.getTotal(), ObjectUtils.convertList(iPage.getRecords(), TreeDictDto::new));
    }

    @Override
    public List<TreeDictDto> getTreeDictList(QueryBean queryBean, TreeDictEntity treeDictEntity) {
        QueryWrapper<TreeDictEntity> queryWrapper = ServiceUtils.queryLike(treeDictEntity, queryBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, queryBean);
        return ObjectUtils.convertList(list(queryWrapper), TreeDictDto::new);
    }

    @Override
    @Transactional
    public void deleteTreeDict(TreeDictEntity treeDictEntity) {
        // 获取所有树形字典主键
        List<TreeDictEntity> entities = getChildrenTreeDict(treeDictEntity.getAppId(), treeDictEntity.getNode());
        List<String> columnIds = entities.stream().map(TreeDictEntity::getId).collect(Collectors.toList());
        columnIds.add(treeDictEntity.getId());
        // 删除树形字典及下级树形字典
        if (!this.removeByIds(columnIds)) {
            throw FailCode.TREE_DICT_DELETE_FAIL.getOperateException();
        }
    }

    @Override
    public List<TreeDictEntity> getChildrenTreeDict(String appId, String node) {
        TreeDictEntity treeDictEntity = new TreeDictEntity();
        treeDictEntity.setAppId(appId);
        treeDictEntity.setParentNode(node);
        List<TreeDictEntity> entities = this.list(new QueryWrapper<>(treeDictEntity));
        int index = 0;
        while (index < entities.size()) {
            TreeDictEntity entity = entities.get(index);
            TreeDictEntity column = new TreeDictEntity();
            column.setParentNode(entity.getNode());
            column.setAppId(appId);
            List<TreeDictEntity> treeDictList = this.list(new QueryWrapper<>(column));
            entities.addAll(treeDictList);
            index++;
        }
        return entities;
    }

    @Override
    @Transactional
    public List<TreeDictDto> createTreeDictList(String appId, List<TreeDictBean> treeDictBeans) {
        // 删除所有树形字典
        TreeDictEntity treeDict = new TreeDictEntity();
        treeDict.setAppId(appId);
        remove(new QueryWrapper<>(treeDict));
        // 批量添加树形字典
        List<TreeDictEntity> entities = new ArrayList<>();
        for (TreeDictBean treeDictBean : treeDictBeans) {
            TreeDictEntity treeDictEntity = ObjectUtils.convert(new TreeDictEntity(), treeDictBean);
            treeDictEntity.setAppId(appId);
            entities.add(treeDictEntity);
        }
        try {
            if (entities.size() > 0) {
                if (!this.saveBatch(entities)) {
                    throw FailCode.TREE_DICT_CREATE_FAIL.getOperateException();
                }
            }
        } catch (
                DuplicateKeyException e) {
            throw FailCode.TREE_DICT_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convertList(entities, TreeDictDto::new);
    }

    @Override
    public List<TreeDictDto> getTreeDictList(String appId, String type, String firstCode) {
        String parentNode = null;
        if (firstCode != null) {
            TreeDictEntity treeDictEntity = new TreeDictEntity();
            treeDictEntity.setAppId(appId);
            treeDictEntity.setType(type);
            treeDictEntity.setCode(firstCode);
            TreeDictEntity entity = this.getOne(new QueryWrapper<>(treeDictEntity));
            if (entity != null) {
                parentNode = entity.getNode();
            } else if (constants.getRoot().equals(firstCode)) {
                parentNode = constants.getRoot();
            } else {
                return new ArrayList<>();
            }
        }
        TreeDictEntity treeDictEntity = new TreeDictEntity();
        treeDictEntity.setParentNode(parentNode);
        treeDictEntity.setType(type);
        treeDictEntity.setAppId(appId);
        return this.getTreeDictList(new QueryBean(), treeDictEntity);
    }

    @Override
    public List<TreeDictDto> getTreeNodeList(String appId, String type, String code) {
        TreeDictEntity treeDictEntity = new TreeDictEntity();
        treeDictEntity.setAppId(appId);
        treeDictEntity.setType(type);
        treeDictEntity.setCode(code);
        TreeDictEntity entity = this.getOne(new QueryWrapper<>(treeDictEntity));
        if (entity == null) {
            return new ArrayList<>();
        }
        List<TreeDictDto> treeDictList = getNodeList(appId, type, entity.getParentNode());
        treeDictList.add(ObjectUtils.convert(new TreeDictDto(), entity));
        return treeDictList;
    }

    /**
     * 查询节点集合
     *
     * @param appId 应用主键
     * @param type  类型
     * @param node  节点标识
     * @return 节点集合
     */
    private List<TreeDictDto> getNodeList(String appId, String type, String node) {
        TreeDictEntity treeDictEntity = new TreeDictEntity();
        treeDictEntity.setAppId(appId);
        treeDictEntity.setType(type);
        treeDictEntity.setNode(node);
        TreeDictEntity entity = this.getOne(new QueryWrapper<>(treeDictEntity));
        if (entity != null) {
            List<TreeDictDto> treeDictList = getNodeList(appId, type, entity.getParentNode());
            treeDictList.add(ObjectUtils.convert(new TreeDictDto(), entity));
            return treeDictList;
        }
        return new ArrayList<>();
    }


    @Override
    public boolean move(TreeDictEntity entity, boolean isUp) {
        // 查询需要处理的权限
        TreeDictEntity treeDictEntity = new TreeDictEntity();
        treeDictEntity.setAppId(entity.getAppId());
        treeDictEntity.setParentNode(entity.getParentNode());
        QueryWrapper<TreeDictEntity> queryWrapper = new QueryWrapper<>(treeDictEntity);
        queryWrapper.lambda().orderByAsc(TreeDictEntity::getSeq);
        List<TreeDictEntity> entities = this.list(queryWrapper);
        Set<Long> entitySet = entities.stream().map(TreeDictEntity::getSeq).collect(Collectors.toSet());
        if (entitySet.size() != entities.size() || entities.get(entities.size() - 1).getSeq() != entities.size()) {
            for (int i = 0; i < entities.size(); i++) {
                TreeDictEntity treeDict = entities.get(i);
                if (treeDict.getSeq() != i + 1) {
                    treeDict.setSeq(i + 1L);
                    this.updateById(treeDict);
                }
            }
        }
        for (int i = 0; i < entities.size(); i++) {
            TreeDictEntity treeDict = entities.get(i);
            if (entity.getId().equals(treeDict.getId())) {
                TreeDictEntity treeDict1;
                if (isUp) {
                    if (entities.size() <= i + 1) {
                        return false;
                    }
                    treeDict1 = entities.get(i + 1);
                } else {
                    if (i - 1 < 0) {
                        return false;
                    }
                    treeDict1 = entities.get(i - 1);
                }
                Long seq = treeDict1.getSeq();
                treeDict1.setSeq(treeDict.getSeq());
                this.updateById(treeDict1);
                // 修改原位置
                treeDict.setSeq(seq);
                this.updateById(treeDict);
            }
        }
        return true;
    }


}
