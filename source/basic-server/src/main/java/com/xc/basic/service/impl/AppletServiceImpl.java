package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.basic.dto.AppletDto;
import com.xc.basic.entity.AppletEntity;
import com.xc.basic.mapper.AppletMapper;
import com.xc.basic.service.AppletService;
import com.xc.tool.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>小程序服务实现类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Slf4j
@Service
public class AppletServiceImpl extends ServiceImpl<AppletMapper, AppletEntity> implements AppletService {

    @Override
    public AppletDto getAppAppletByCode(String appId, String appletType, String appletId) {
        AppletEntity appletEntity = new AppletEntity();
        appletEntity.setAppId(appId);
        appletEntity.setType(appletType);
        appletEntity.setAppletId(appletId);
        AppletEntity entity = this.getOne(new QueryWrapper<>(appletEntity));
        if (entity != null) {
            return ObjectUtils.convert(new AppletDto(), entity);
        }
        return null;
    }

    @Override
    public AppletDto getAppAppletByUser(String appId, String appletType, String userId) {
        AppletEntity appletEntity = new AppletEntity();
        appletEntity.setAppId(appId);
        appletEntity.setType(appletType);
        appletEntity.setUserId(userId);
        AppletEntity entity = this.getOne(new QueryWrapper<>(appletEntity));
        if (entity != null) {
            return ObjectUtils.convert(new AppletDto(), entity);
        }
        return null;
    }
}
