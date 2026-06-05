package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.basic.dto.ColumnDto;
import com.xc.basic.bean.ColumnBean;
import com.xc.basic.entity.ColumnEntity;
import com.xc.basic.entity.PageColumnEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.mapper.ColumnMapper;
import com.xc.basic.mapper.PageColumnMapper;
import com.xc.basic.service.ColumnService;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>栏目实现类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
public class ColumnServiceImpl extends ServiceImpl<ColumnMapper, ColumnEntity> implements ColumnService {
    @Autowired
    private PageColumnMapper pageColumnMapper;

    @Override
    public PagingDto<ColumnDto> getColumnPage(Integer current, PagingBean pagingBean, ColumnEntity columnEntity) {
        QueryWrapper<ColumnEntity> queryWrapper = ServiceUtils.queryLike(columnEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        IPage<ColumnEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        return new PagingDto<>(iPage.getTotal(), ObjectUtils.convertList(iPage.getRecords(), ColumnDto::new));
    }

    @Override
    public ColumnDto getColumn(ColumnEntity columnEntity) {
        PageColumnEntity columnAuthorityEntity = new PageColumnEntity();
        columnAuthorityEntity.setColumnId(columnEntity.getId());
        List<PageColumnEntity> entities = pageColumnMapper.selectList(new QueryWrapper<>(columnAuthorityEntity));
        List<String> pageIds = new ArrayList<>();
        for (PageColumnEntity columnAuthority : entities) {
            pageIds.add(columnAuthority.getPageId());
        }
        ColumnDto columnDto = ObjectUtils.convert(new ColumnDto(), columnEntity);
        columnDto.setPageIds(pageIds);
        return columnDto;
    }

    @Override
    public ColumnDto createColumn(ColumnEntity columnEntity) {
        if (columnEntity.getCode() == null) {
            throw FailCode.COLUMN_CODE_NOT_NULL.getOperateException();
        }
        try {
            columnEntity.setNode(IdWorker.getIdStr());
            if (!this.save(columnEntity)) {
                throw FailCode.COLUMN_CREATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.COLUMN_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new ColumnDto(), columnEntity);
    }

    @Override
    public ColumnDto updateColumn(ColumnEntity columnEntity) {
        if (columnEntity.getCode() == null) {
            throw FailCode.COLUMN_CODE_NOT_NULL.getOperateException();
        }
        // 验证是否在下级
        List<ColumnEntity> entities = this.getChildrenColumn(columnEntity.getAppId(), columnEntity.getNode());
        entities.add(columnEntity);
        for (ColumnEntity entity1 : entities) {
            if (entity1.getNode().equals(columnEntity.getParentNode())) {
                throw FailCode.NOT_CHILDREN_ADD.getOperateException();
            }
        }
        try {
            if (!this.updateById(columnEntity)) {
                throw FailCode.COLUMN_UPDATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.COLUMN_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new ColumnDto(), columnEntity);
    }

    @Override
    @Transactional
    public void deleteColumn(ColumnEntity columnEntity) {
        // 获取所有栏目主键
        List<ColumnEntity> entities = getChildrenColumn(columnEntity.getAppId(), columnEntity.getNode());
        List<String> columnIds = entities.stream().map(ColumnEntity::getId).collect(Collectors.toList());
        columnIds.add(columnEntity.getId());
        // 删除页面和栏目关联
        QueryWrapper<PageColumnEntity> pageColumnWrapper = new QueryWrapper<>(new PageColumnEntity());
        pageColumnWrapper.lambda().in(PageColumnEntity::getColumnId, columnEntity.getId());
        pageColumnMapper.delete(pageColumnWrapper);
        // 删除栏目及下级栏目
        if (!this.removeByIds(columnIds)) {
            throw FailCode.COLUMN_DELETE_FAIL.getOperateException();
        }
    }

    @Override
    public List<ColumnEntity> getChildrenColumn(String appId, String node) {
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setAppId(appId);
        columnEntity.setParentNode(node);
        List<ColumnEntity> entities = this.list(new QueryWrapper<>(columnEntity));
        int index = 0;
        while (index < entities.size()) {
            ColumnEntity entity = entities.get(index);
            ColumnEntity column = new ColumnEntity();
            column.setParentNode(entity.getNode());
            column.setAppId(appId);
            List<ColumnEntity> columnList = this.list(new QueryWrapper<>(column));
            entities.addAll(columnList);
            index++;
        }
        return entities;
    }

    @Override
    public List<ColumnDto> getColumnList(QueryBean queryBean, ColumnEntity columnEntity) {
        QueryWrapper<ColumnEntity> queryWrapper = ServiceUtils.queryLike(columnEntity, queryBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, queryBean);
        List<ColumnDto> columnList = ObjectUtils.convertList(list(queryWrapper), ColumnDto::new);
        List<String> columnIds = columnList.stream().map(ColumnDto::getId).collect(Collectors.toList());
        // 获取栏目关联
        if (columnIds.size() > 0) {
            QueryWrapper<PageColumnEntity> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(PageColumnEntity::getColumnId, columnIds);
            List<PageColumnEntity> authorityEntities = pageColumnMapper.selectList(wrapper);
            for (ColumnDto columnDto : columnList) {
                List<String> pageIds = new ArrayList<>();
                for (PageColumnEntity authorityEntity : authorityEntities) {
                    if (columnDto.getId().equals(authorityEntity.getColumnId())) {
                        pageIds.add(authorityEntity.getPageId());
                    }
                }
                columnDto.setPageIds(pageIds);
            }
        }
        return columnList;
    }

    @Override
    @Transactional
    public List<ColumnDto> createColumnList(String appId, List<ColumnBean> columnBeans) {
        // 删除查询栏目
        ColumnEntity entity = new ColumnEntity();
        entity.setAppId(appId);
        List<ColumnEntity> columnEntities = this.list(new QueryWrapper<>(entity));
        List<String> columnIds = columnEntities.stream().map(ColumnEntity::getId).collect(Collectors.toList());
        if (columnIds.size() > 0) {
            // 删除页面关联
            QueryWrapper<PageColumnEntity> pageWrapper = new QueryWrapper<>(new PageColumnEntity());
            pageWrapper.lambda().in(PageColumnEntity::getColumnId, columnIds);
            pageColumnMapper.delete(pageWrapper);
        }
        // 删除所有栏目
        remove(new QueryWrapper<>(entity));
        // 批量添加栏目
        List<ColumnEntity> entities = new ArrayList<>();
        for (ColumnBean columnBean : columnBeans) {
            ColumnEntity columnEntity = ObjectUtils.convert(new ColumnEntity(), columnBean);
            columnEntity.setAppId(appId);
            entities.add(columnEntity);
        }
        try {
            if (entities.size() > 0) {
                if (!this.saveBatch(entities)) {
                    throw FailCode.COLUMN_CREATE_FAIL.getOperateException();
                }
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.COLUMN_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convertList(entities, ColumnDto::new);
    }

    @Override
    @Transactional
    public boolean move(ColumnEntity entity, boolean isUp) {
        // 查询需要处理的权限
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setAppId(entity.getAppId());
        columnEntity.setParentNode(entity.getParentNode());
        QueryWrapper<ColumnEntity> queryWrapper = new QueryWrapper<>(columnEntity);
        queryWrapper.lambda().orderByAsc(ColumnEntity::getSeq);
        List<ColumnEntity> entities = this.list(queryWrapper);
        Set<Long> entitySet = entities.stream().map(ColumnEntity::getSeq).collect(Collectors.toSet());
        if (entitySet.size() != entities.size() || entities.get(entities.size() - 1).getSeq() != entities.size()) {
            for (int i = 0; i < entities.size(); i++) {
                ColumnEntity column = entities.get(i);
                if (column.getSeq() != i + 1) {
                    column.setSeq(i + 1L);
                    this.updateById(column);
                }
            }
        }
        for (int i = 0; i < entities.size(); i++) {
            ColumnEntity column = entities.get(i);
            if (entity.getId().equals(column.getId())) {
                ColumnEntity column1;
                if (isUp) {
                    if (entities.size() <= i + 1) {
                        return false;
                    }
                    column1 = entities.get(i + 1);
                } else {
                    if (i - 1 < 0) {
                        return false;
                    }
                    column1 = entities.get(i - 1);
                }
                Long seq = column1.getSeq();
                column1.setSeq(column.getSeq());
                this.updateById(column1);
                // 修改原位置
                column.setSeq(seq);
                this.updateById(column);
            }
        }
        return true;
    }
}
