package com.xc.basic.service;

import com.xc.basic.entity.AppEntity;
import org.springframework.ui.Model;

import java.util.Map;

/**
 * <p>首页Service</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface IndexService {
    /**
     * 获取应用所有信息
     */
    public Map<String, Object> getAppInfo();

    /**
     * 设置登录model信息
     *
     * @param appId 应用程序id
     */
    public AppEntity setLoginModel(Model model, String appId);
}
