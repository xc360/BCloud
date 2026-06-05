package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.xc.api.basic.dto.PageDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.PageBean;
import com.xc.basic.bean.PageColumnBean;
import com.xc.basic.dto.PageColumnDto;
import com.xc.basic.entity.ColumnEntity;
import com.xc.basic.entity.PageColumnEntity;
import com.xc.basic.entity.PageEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.mapper.PageColumnMapper;
import com.xc.basic.mapper.PageMapper;
import com.xc.basic.service.ColumnService;
import com.xc.basic.service.PageService;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>页面服务类实现</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Slf4j
@Service
public class PageServiceImpl extends ServiceImpl<PageMapper, PageEntity> implements PageService {

    @Autowired
    private PageColumnMapper pageColumnMapper;
    @Autowired
    private ColumnService columnService;

    @Override
    public PagingDto<PageDto> getPagePage(Integer current, PagingBean pagingBean, PageEntity pageEntity) {
        QueryWrapper<PageEntity> queryWrapper = ServiceUtils.queryData(pagingBean, pageEntity);
        IPage<PageEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        List<String> pageIds = iPage.getRecords().stream().map(PageEntity::getId).collect(Collectors.toList());
        List<PageColumnEntity> columnEntities = new ArrayList<>();
        if (pageIds.size() > 0) {
            QueryWrapper<PageColumnEntity> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(PageColumnEntity::getPageId, pageIds);
            columnEntities = pageColumnMapper.selectList(wrapper);
        }
        List<PageDto> pageList = new ArrayList<>();
        for (PageEntity page : iPage.getRecords()) {
            PageDto pageDto = ObjectUtils.convert(new PageDto(), page);
            pageDto.setColumnIds(new ArrayList<>());
            for (PageColumnEntity columnEntity : columnEntities) {
                if (page.getId().equals(columnEntity.getPageId())) {
                    pageDto.getColumnIds().add(columnEntity.getColumnId());
                }
            }
            pageList.add(pageDto);
        }
        return new PagingDto<>(iPage.getTotal(), pageList);
    }

    @Override
    public List<PageDto> getPageList(QueryBean queryBean, PageEntity pageEntity) {
        QueryWrapper<PageEntity> queryWrapper = ServiceUtils.queryLike(pageEntity, queryBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, queryBean);
        List<PageEntity> pageEntities = this.list(queryWrapper);
        List<PageDto> pageList = ObjectUtils.convertList(pageEntities, PageDto::new);
        List<String> pageIds = pageEntities.stream().map(PageEntity::getId).collect(Collectors.toList());
        // 获取栏目关联
        if (pageIds.size() > 0) {
            QueryWrapper<PageColumnEntity> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(PageColumnEntity::getPageId, pageIds);
            List<PageColumnEntity> authorityEntities = pageColumnMapper.selectList(wrapper);
            for (PageDto pageDto : pageList) {
                List<String> columnIds = new ArrayList<>();
                for (PageColumnEntity authorityEntity : authorityEntities) {
                    if (pageDto.getId().equals(authorityEntity.getPageId())) {
                        columnIds.add(authorityEntity.getColumnId());
                    }
                }
                pageDto.setColumnIds(columnIds);
            }
        }
        return pageList;
    }

    @Override
    @Transactional
    public List<PageDto> createPageList(String appId, List<PageBean> pageBeans) {
        // 删除查询页面
        PageEntity entity = new PageEntity();
        entity.setAppId(appId);
        List<PageEntity> pageEntities = this.list(new QueryWrapper<>(entity));
        List<String> pageIds = pageEntities.stream().map(PageEntity::getId).collect(Collectors.toList());
        if (pageIds.size() > 0) {
            // 删除栏目关联
            QueryWrapper<PageColumnEntity> pageWrapper = new QueryWrapper<>(new PageColumnEntity());
            pageWrapper.lambda().in(PageColumnEntity::getPageId, pageIds);
            pageColumnMapper.delete(pageWrapper);
        }
        // 删除所有页面
        remove(new QueryWrapper<>(entity));
        // 批量添加页面
        List<PageEntity> entities = new ArrayList<>();
        for (PageBean pageBean : pageBeans) {
            PageEntity pageEntity = ObjectUtils.convert(new PageEntity(), pageBean);
            pageEntity.setAppId(appId);
            entities.add(pageEntity);
        }
        try {
            if (entities.size() > 0) {
                if (!this.saveBatch(entities)) {
                    throw FailCode.PAGE_CREATE_FAIL.getOperateException();
                }
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.PAGE_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convertList(entities, PageDto::new);
    }

    @Override
    @Transactional
    public PageDto createPage(String appId, PageBean pageBean) {
        PageEntity pageEntity = ObjectUtils.convert(new PageEntity(), pageBean);
        pageEntity.setAppId(appId);
        try {
            if (!this.save(pageEntity)) {
                throw FailCode.PAGE_CREATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.PAGE_CODE_REPEAT.getOperateException();
        }
        PageDto pageDto = ObjectUtils.convert(new PageDto(), pageEntity);
        pageDto.setColumnIds(createPageColumn(pageEntity.getId(), pageBean.getColumnIds()));
        return pageDto;
    }

    @Override
    @Transactional
    public PageDto updatePage(String appId, String pageId, PageBean pageBean) {
        PageEntity pageEntity = this.getById(pageId);
        if (pageEntity == null) {
            throw FailCode.PAGE_ID_ERROR.getOperateException();
        }
        if (!pageEntity.getAppId().equals(appId)) {
            throw FailCode.PAGE_APP_ID_ERROR.getOperateException();
        }
        ObjectUtils.convert(pageEntity, pageBean);
        // 保存数据
        try {
            if (!this.updateById(pageEntity)) {
                throw FailCode.PAGE_UPDATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.PAGE_CODE_REPEAT.getOperateException();
        }
        PageDto pageDto = ObjectUtils.convert(new PageDto(), pageEntity);
        pageDto.setColumnIds(createPageColumn(pageEntity.getId(), pageBean.getColumnIds()));
        return pageDto;
    }

    @Override
    @Transactional
    public void deletePage(String appId, String pageId) {
        PageEntity pageEntity = this.getById(pageId);
        if (pageEntity == null) {
            throw FailCode.PAGE_ID_ERROR.getOperateException();
        }
        if (!pageEntity.getAppId().equals(appId)) {
            throw FailCode.PAGE_APP_ID_ERROR.getOperateException();
        }
        if (!this.removeById(pageId)) {
            throw FailCode.PAGE_DELETE_FAIL.getOperateException();
        }
        // 删除栏目关联
        PageColumnEntity pageColumnEntity = new PageColumnEntity();
        pageColumnEntity.setPageId(pageId);
        pageColumnMapper.delete(new QueryWrapper<>(pageColumnEntity));
    }

    @Override
    public List<PageColumnDto> createAppPageColumnRelationList(String appId, List<PageColumnBean> pageColumnBeans) {
        // 删除页面栏目
        deletePageColumn(appId);
        // 查询页面
        List<String> pageCodes = pageColumnBeans.stream().map(PageColumnBean::getPageCode).collect(Collectors.toList());
        List<PageEntity> pageEntities = new ArrayList<>();
        if (pageCodes.size() > 0) {
            PageEntity pageEntity = new PageEntity();
            pageEntity.setAppId(appId);
            QueryWrapper<PageEntity> pageWrapper = new QueryWrapper<>(pageEntity);
            pageWrapper.lambda().in(PageEntity::getCode, pageCodes);
            pageEntities = this.list(pageWrapper);
        }
        // 查询栏目
        List<String> columnCodes = pageColumnBeans.stream().map(PageColumnBean::getColumnCode).collect(Collectors.toList());
        List<ColumnEntity> columnEntities = new ArrayList<>();
        if (columnCodes.size() > 0) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setAppId(appId);
            QueryWrapper<ColumnEntity> columnWrapper = new QueryWrapper<>(columnEntity);
            columnWrapper.lambda().in(ColumnEntity::getCode, columnCodes);
            columnEntities = columnService.list(columnWrapper);
        }
        // 处理数据
        List<PageColumnDto> columnRelationList = new ArrayList<>();
        for (PageColumnBean pageColumnBean : pageColumnBeans) {
            String columnId = null;
            for (ColumnEntity entity : columnEntities) {
                if (entity.getCode().equals(pageColumnBean.getColumnCode())) {
                    columnId = entity.getId();
                }
            }
            String pageId = null;
            for (PageEntity entity : pageEntities) {
                if (entity.getCode().equals(pageColumnBean.getPageCode())) {
                    pageId = entity.getId();
                }
            }
            if (columnId != null && pageId != null) {
                PageColumnEntity pageColumn = new PageColumnEntity();
                pageColumn.setPageId(pageId);
                pageColumn.setColumnId(columnId);
                if (!SqlHelper.retBool(pageColumnMapper.insert(pageColumn))) {
                    throw FailCode.PAGE_AND_COLUMN_RELATION_CREATE_FAIL.getOperateException();
                }
                columnRelationList.add(ObjectUtils.convert(new PageColumnDto(), pageColumnBean));
            }
        }
        return columnRelationList;
    }

    @Override
    public List<PageColumnDto> getAppPageColumnRelationList(String appId) {
        List<PageColumnEntity> pageColumnEntities = pageColumnMapper.selectList(new QueryWrapper<>());
        // 查询页面
        List<String> pageIds = pageColumnEntities.stream().map(PageColumnEntity::getPageId).collect(Collectors.toList());
        List<PageEntity> pageEntities = new ArrayList<>();
        if (pageIds.size() > 0) {
            PageEntity pageEntity = new PageEntity();
            pageEntity.setAppId(appId);
            QueryWrapper<PageEntity> pageWrapper = new QueryWrapper<>(pageEntity);
            pageWrapper.lambda().in(PageEntity::getId, pageIds);
            pageEntities = this.list(pageWrapper);
        }
        // 查询栏目
        List<String> columnIds = pageColumnEntities.stream().map(PageColumnEntity::getColumnId).collect(Collectors.toList());
        List<ColumnEntity> columnEntities = new ArrayList<>();
        if (columnIds.size() > 0) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setAppId(appId);
            QueryWrapper<ColumnEntity> columnWrapper = new QueryWrapper<>(columnEntity);
            columnWrapper.lambda().in(ColumnEntity::getId, columnIds);
            columnEntities = columnService.list(columnWrapper);
        }
        // 处理数据
        List<PageColumnDto> columnRelationList = new ArrayList<>();
        for (PageColumnEntity pageColumnEntity : pageColumnEntities) {
            String columnCode = null;
            for (ColumnEntity entity : columnEntities) {
                if (entity.getId().equals(pageColumnEntity.getColumnId())) {
                    columnCode = entity.getCode();
                }
            }
            String pageCode = null;
            for (PageEntity entity : pageEntities) {
                if (entity.getId().equals(pageColumnEntity.getPageId())) {
                    pageCode = entity.getCode();
                }
            }
            if (columnCode != null && pageCode != null) {
                PageColumnDto pageColumnDto = new PageColumnDto();
                pageColumnDto.setPageCode(pageCode);
                pageColumnDto.setColumnCode(columnCode);
                columnRelationList.add(pageColumnDto);
            }
        }
        return columnRelationList;
    }

    /**
     * 获取页面栏目Model
     *
     * @param appId 应用主键
     */
    private void deletePageColumn(String appId) {
        // 删除查询页面
        PageEntity pageEntity = new PageEntity();
        pageEntity.setAppId(appId);
        List<PageEntity> pageEntities = this.list(new QueryWrapper<>(pageEntity));
        List<String> pageIds = pageEntities.stream().map(PageEntity::getId).collect(Collectors.toList());
        if (pageIds.size() > 0) {
            QueryWrapper<PageColumnEntity> pageWrapper = new QueryWrapper<>(new PageColumnEntity());
            pageWrapper.lambda().in(PageColumnEntity::getPageId, pageIds);
            pageColumnMapper.delete(pageWrapper);
        }
        // 删除查询栏目
        ColumnEntity columnEntity = new ColumnEntity();
        columnEntity.setAppId(appId);
        List<ColumnEntity> columnEntities = columnService.list(new QueryWrapper<>(columnEntity));
        List<String> columnIds = columnEntities.stream().map(ColumnEntity::getId).collect(Collectors.toList());
        if (columnIds.size() > 0) {
            QueryWrapper<PageColumnEntity> columnWrapper = new QueryWrapper<>(new PageColumnEntity());
            columnWrapper.lambda().in(PageColumnEntity::getColumnId, columnIds);
            pageColumnMapper.delete(columnWrapper);
        }
    }

    /**
     * 创建用户组权限
     *
     * @return 关联成功的角色id集合
     */
    private List<String> createPageColumn(String pageId, List<String> columnIds) {
        // 删除所有角色
        PageColumnEntity pageColumnEntity = new PageColumnEntity();
        pageColumnEntity.setPageId(pageId);
        pageColumnMapper.delete(new QueryWrapper<>(pageColumnEntity));
        if (columnIds != null) {
            for (String columnId : columnIds) {
                PageColumnEntity pageColumn = new PageColumnEntity();
                pageColumn.setPageId(pageId);
                pageColumn.setColumnId(columnId);
                if (!SqlHelper.retBool(pageColumnMapper.insert(pageColumn))) {
                    throw FailCode.PAGE_AND_COLUMN_RELATION_CREATE_FAIL.getOperateException();
                }
            }
            return columnIds;
        }
        return new ArrayList<>();
    }
}
