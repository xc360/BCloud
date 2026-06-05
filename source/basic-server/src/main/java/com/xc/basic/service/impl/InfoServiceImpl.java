package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.basic.bean.InfoBean;
import com.xc.api.basic.dto.InfoDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.entity.InfoEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.mapper.InfoMapper;
import com.xc.basic.service.InfoService;
import com.xc.core.enums.EffectStatus;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>应用信息Service实现</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
public class InfoServiceImpl extends ServiceImpl<InfoMapper, InfoEntity> implements InfoService {
    @Autowired
    private InfoMapper infoMapper;

    @Override
    public PagingDto<InfoDto> getInfoPage(Integer current, PagingBean pagingBean, InfoEntity infoEntity) {
        QueryWrapper<InfoEntity> queryWrapper = ServiceUtils.queryLike(infoEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        IPage<InfoEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        return new PagingDto<>(iPage.getTotal(), ObjectUtils.convertList(iPage.getRecords(), InfoDto::new));
    }


    @Override
    @Transactional
    public List<InfoDto> createAppInfoList(String appId, List<InfoBean> infoBeans) {
        // 删除所有字典
        InfoEntity info = new InfoEntity();
        info.setAppId(appId);
        remove(new QueryWrapper<>(info));
        // 批量添加字典
        List<InfoEntity> entities = new ArrayList<>();
        for (InfoBean infoBean : infoBeans) {
            InfoEntity infoEntity = ObjectUtils.convert(new InfoEntity(), infoBean);
            infoEntity.setAppId(appId);
            if (infoEntity.getKey() == null) {
                throw FailCode.INFO_KEY_NOT_NULL.getOperateException();
            }
            entities.add(infoEntity);
        }
        try {
            if(entities.size()>0){
                if (!this.saveBatch(entities)) {
                    throw FailCode.INFO_CREATE_FAIL.getOperateException();
                }
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.INFO_KEY_REPEAT.getOperateException();
        }
        return ObjectUtils.convertList(entities, InfoDto::new);
    }

    @Override
    public List<InfoDto> getInfoList(QueryBean queryBean, InfoEntity infoEntity) {
        QueryWrapper<InfoEntity> queryWrapper = ServiceUtils.queryLike(infoEntity, queryBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, queryBean);
        List<InfoEntity> dictEntities = list(queryWrapper);
        return ObjectUtils.convertList(dictEntities, InfoDto::new);
    }


    @Override
    public Map<String, String> getAppValidInfoMap(String id, String type) {
        InfoEntity infoEntity = new InfoEntity();
        infoEntity.setStatus(EffectStatus.VALID.getStatus());
        infoEntity.setType(type);
        infoEntity.setAppId(id);
        List<InfoEntity> infoEntities = infoMapper.selectList(new QueryWrapper<>(infoEntity));
        Map<String, String> map = new HashMap<>();
        for (InfoEntity info : infoEntities) {
            map.put(info.getKey(), info.getValue());
        }
        return map;
    }


}
