package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.basic.dto.AppletDto;
import com.xc.basic.entity.AppletEntity;

/**
 * <p>小程序服务类</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface AppletService extends IService<AppletEntity> {
    /**
     * 获取小程序信息
     *
     * @param appId      应用主键
     * @param appletType 小程序类型
     * @param appletId   小程序id
     * @return 小程序信息
     */
    public AppletDto getAppAppletByCode(String appId, String appletType, String appletId);

    /**
     * 获取小程序信息
     *
     * @param appId      应用主键
     * @param appletType 小程序类型
     * @param userId     小程序id
     * @return 小程序信息
     */
    public AppletDto getAppAppletByUser(String appId, String appletType, String userId);
}
