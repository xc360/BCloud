package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.basic.dto.DataTypeDto;
import com.xc.basic.bean.DataTypeBean;
import com.xc.basic.entity.DataTypeEntity;
import com.xc.basic.entity.DictEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.mapper.DataTypeMapper;
import com.xc.basic.service.DataTypeService;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>数据类型接口实现</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
public class DataTypeServiceImpl extends ServiceImpl<DataTypeMapper, DataTypeEntity> implements DataTypeService {
    @Override
    public PagingDto<DataTypeDto> getDataTypePage(Integer current, PagingBean pagingBean, DataTypeEntity dataTypeEntity) {
        QueryWrapper<DataTypeEntity> queryWrapper = ServiceUtils.queryLike(dataTypeEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        IPage<DataTypeEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        return new PagingDto<>(iPage.getTotal(), ObjectUtils.convertList(iPage.getRecords(), DataTypeDto::new));
    }

    @Override
    public List<DataTypeDto> getDataTypeList(QueryBean queryBean, DataTypeEntity dataTypeEntity) {
        QueryWrapper<DataTypeEntity> queryWrapper = ServiceUtils.queryLike(dataTypeEntity, queryBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, queryBean);
        List<DataTypeEntity> dataTypeEntities = this.list(queryWrapper);
        return ObjectUtils.convertList(dataTypeEntities, DataTypeDto::new);
    }

    @Override
    @Transactional
    public List<DataTypeDto> createDataTypeList(String appId, List<DataTypeBean> dataTypeBeans) {
        // 删除所有数据类型
        DataTypeEntity entity = new DataTypeEntity();
        entity.setAppId(appId);
        remove(new QueryWrapper<>(entity));
        // 批量添加数据类型
        List<DataTypeEntity> entities = new ArrayList<>();
        for (DataTypeBean dataTypeBean : dataTypeBeans) {
            DataTypeEntity dataTypeEntity = ObjectUtils.convert(new DataTypeEntity(), dataTypeBean);
            dataTypeEntity.setAppId(appId);
            entities.add(dataTypeEntity);
        }
        try {
            if (entities.size() > 0) {
                if (!this.saveBatch(entities)) {
                    throw FailCode.DATA_TYPE_CREATE_FAIL.getOperateException();
                }
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.DATA_TYPE_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convertList(entities, DataTypeDto::new);
    }

    @Override
    @Transactional
    public boolean move(DataTypeEntity entity, boolean isUp) {
        // 查询需要处理的权限
        DataTypeEntity dataTypeEntity = new DataTypeEntity();
        dataTypeEntity.setAppId(entity.getAppId());
        QueryWrapper<DataTypeEntity> queryWrapper = new QueryWrapper<>(dataTypeEntity);
        queryWrapper.lambda().orderByAsc(DataTypeEntity::getSeq);
        List<DataTypeEntity> entities = this.list(queryWrapper);
        Set<Long> entitySet = entities.stream().map(DataTypeEntity::getSeq).collect(Collectors.toSet());
        if (entitySet.size() != entities.size() || entities.get(entities.size() - 1).getSeq() != entities.size()) {
            for (int i = 0; i < entities.size(); i++) {
                DataTypeEntity dataType = entities.get(i);
                if (dataType.getSeq() != i + 1) {
                    dataType.setSeq(i + 1L);
                    this.updateById(dataType);
                }
            }
        }
        for (int i = 0; i < entities.size(); i++) {
            DataTypeEntity dataType = entities.get(i);
            if (entity.getId().equals(dataType.getId())) {
                DataTypeEntity dataType1;
                if (isUp) {
                    if (entities.size() <= i + 1) {
                        return false;
                    }
                    dataType1 = entities.get(i + 1);
                } else {
                    if (i - 1 < 0) {
                        return false;
                    }
                    dataType1 = entities.get(i - 1);
                }
                Long seq = dataType1.getSeq();
                dataType1.setSeq(dataType.getSeq());
                this.updateById(dataType1);
                // 修改原位置
                dataType.setSeq(seq);
                this.updateById(dataType);
            }
        }
        return true;
    }
}
