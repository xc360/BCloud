package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.basic.dto.ApiSupplierDto;
import com.xc.basic.bean.ApiSupplierBean;
import com.xc.basic.entity.ApiSupplierEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.mapper.ApiSupplierMapper;
import com.xc.basic.service.ApiSupplierService;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.enums.EffectStatus;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.AesUtils;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>接口供应商服务实现</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
public class ApiSupplierServiceImpl extends ServiceImpl<ApiSupplierMapper, ApiSupplierEntity> implements ApiSupplierService {
    @Override
    public PagingDto<ApiSupplierDto> getApiSupplierPage(Integer current, PagingBean pagingBean, ApiSupplierEntity apiSupplierEntity) {
        QueryWrapper<ApiSupplierEntity> queryWrapper = ServiceUtils.queryLike(apiSupplierEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        IPage<ApiSupplierEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        return new PagingDto<>(iPage.getTotal(), ObjectUtils.convertList(iPage.getRecords(), ApiSupplierDto::new));
    }

    @Override
    @Transactional
    public List<ApiSupplierDto> createAppApiSupplierList(String appId, List<ApiSupplierBean> apiSupplierBeans) {
        // 删除所有字典
        ApiSupplierEntity apiSupplier = new ApiSupplierEntity();
        apiSupplier.setAppId(appId);
        remove(new QueryWrapper<>(apiSupplier));
        // 批量添加字典
        List<ApiSupplierEntity> entities = new ArrayList<>();
        for (ApiSupplierBean apiSupplierBean : apiSupplierBeans) {
            ApiSupplierEntity apiSupplierEntity = ObjectUtils.convert(new ApiSupplierEntity(), apiSupplierBean);
            apiSupplierEntity.setAppId(appId);
            apiSupplierEntity.setAccessId(AesUtils.decrypt(apiSupplierEntity.getAccessId(), apiSupplierEntity.getCode()));
            apiSupplierEntity.setAccessSecret(AesUtils.decrypt(apiSupplierEntity.getAccessSecret(), apiSupplierEntity.getCode()));
            entities.add(apiSupplierEntity);
        }
        try {
            if (entities.size() > 0) {
                if (!this.saveBatch(entities)) {
                    throw FailCode.API_SUPPLIER_CREATE_FAIL.getOperateException();
                }
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.API_SUPPLIER_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convertList(entities, ApiSupplierDto::new);
    }

    @Override
    public List<ApiSupplierDto> getApiSupplierList(QueryBean queryBean, ApiSupplierEntity apiSupplierEntity) {
        QueryWrapper<ApiSupplierEntity> queryWrapper = ServiceUtils.queryLike(apiSupplierEntity, queryBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, queryBean);
        List<ApiSupplierEntity> apiSupplierEntities = list(queryWrapper);
        for (ApiSupplierEntity apiSupplier : apiSupplierEntities) {
            apiSupplier.setAccessId(AesUtils.encrypt(apiSupplier.getAccessId(), apiSupplier.getCode()));
            apiSupplier.setAccessSecret(AesUtils.encrypt(apiSupplier.getAccessSecret(), apiSupplier.getCode()));
        }
        return ObjectUtils.convertList(apiSupplierEntities, ApiSupplierDto::new);
    }

    @Override
    public ApiSupplierDto getAppApiSupplierByCode(String appId, String code) {
        ApiSupplierEntity apiSupplier = new ApiSupplierEntity();
        apiSupplier.setCode(code);
        apiSupplier.setAppId(appId);
        ApiSupplierEntity apiSupplierEntity = this.getOne(new QueryWrapper<>(apiSupplier));
        if (apiSupplierEntity == null) {
            throw FailCode.API_SUPPLIER_CODE_ERROR.getOperateException();
        }
        if (EffectStatus.INVALID.getStatus().equals(apiSupplierEntity.getStatus())) {
            throw FailCode.API_SUPPLIER_STATUS_INVALID.getOperateException();
        }
        return ObjectUtils.convert(new ApiSupplierDto(), apiSupplierEntity);
    }
}
