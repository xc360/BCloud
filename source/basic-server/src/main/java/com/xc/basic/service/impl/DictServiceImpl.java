package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.basic.dto.DictDto;
import com.xc.basic.bean.DictBean;
import com.xc.basic.entity.DictEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.mapper.DictMapper;
import com.xc.basic.service.DictService;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>应用字典Service实现</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, DictEntity> implements DictService {

    @Override
    public PagingDto<DictDto> getDictPage(Integer current, PagingBean pagingBean, DictEntity dictEntity) {
        QueryWrapper<DictEntity> queryWrapper = ServiceUtils.queryLike(dictEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        IPage<DictEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        return new PagingDto<>(iPage.getTotal(), ObjectUtils.convertList(iPage.getRecords(), DictDto::new));
    }


    @Override
    @Transactional
    public List<DictDto> createAppDictList(String appId, List<DictBean> dictBeans) {
        // 删除所有字典
        DictEntity dict = new DictEntity();
        dict.setAppId(appId);
        remove(new QueryWrapper<>(dict));
        // 批量添加字典
        List<DictEntity> entities = new ArrayList<>();
        for (DictBean dictBean : dictBeans) {
            DictEntity dictEntity = ObjectUtils.convert(new DictEntity(), dictBean);
            dictEntity.setAppId(appId);
            entities.add(dictEntity);
        }
        if (entities.size() > 0) {
            if (!this.saveBatch(entities)) {
                throw FailCode.DICT_CREATE_FAIL.getOperateException();
            }
        }
        return ObjectUtils.convertList(entities, DictDto::new);
    }

    @Override
    public List<DictDto> getDictList(QueryBean queryBean, DictEntity dictEntity) {
        QueryWrapper<DictEntity> queryWrapper = ServiceUtils.queryLike(dictEntity, queryBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, queryBean);
        List<DictEntity> dictEntities = list(queryWrapper);
        return ObjectUtils.convertList(dictEntities, DictDto::new);
    }

    @Override
    @Transactional
    public boolean move(DictEntity entity, boolean isUp) {
        // 查询需要处理的权限
        DictEntity dictEntity = new DictEntity();
        dictEntity.setAppId(entity.getAppId());
        dictEntity.setType(entity.getType());
        QueryWrapper<DictEntity> queryWrapper = new QueryWrapper<>(dictEntity);
        queryWrapper.lambda().orderByAsc(DictEntity::getSeq);
        List<DictEntity> entities = this.list(queryWrapper);
        Set<Long> entitySet = entities.stream().map(DictEntity::getSeq).collect(Collectors.toSet());
        if (entitySet.size() != entities.size() || entities.get(entities.size() - 1).getSeq() != entities.size()) {
            for (int i = 0; i < entities.size(); i++) {
                DictEntity dict = entities.get(i);
                if (dict.getSeq() != i + 1) {
                    dict.setSeq(i + 1L);
                    this.updateById(dict);
                }
            }
        }
        for (int i = 0; i < entities.size(); i++) {
            DictEntity dict = entities.get(i);
            if (entity.getId().equals(dict.getId())) {
                DictEntity dict1;
                if (isUp) {
                    if (entities.size() <= i + 1) {
                        return false;
                    }
                    dict1 = entities.get(i + 1);
                } else {
                    if (i - 1 < 0) {
                        return false;
                    }
                    dict1 = entities.get(i - 1);
                }
                Long seq = dict1.getSeq();
                dict1.setSeq(dict.getSeq());
                this.updateById(dict1);
                // 修改原位置
                dict.setSeq(seq);
                this.updateById(dict);
            }
        }
        return true;
    }
}
