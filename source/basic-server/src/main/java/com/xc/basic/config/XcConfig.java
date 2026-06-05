package com.xc.basic.config;

import com.xc.api.file.FileApi;
import com.xc.api.file.config.FileConstants;
import com.xc.api.file.utils.FileUrlUtils;
import com.xc.core.aspect.AspectHandle;
import com.xc.core.aspect.AuthorityHandle;
import com.xc.core.aspect.BasicConstants;
import com.xc.core.aspect.TokenHandle;
import com.xc.core.interceptor.XcHttpInterceptorImpl;
import com.xc.tool.http.XcHttp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Xc的配置
 * </p>
 *
 * @author xc
 * @since 2026-05-29
 */
@Configuration
public class XcConfig {

    @Autowired
    private ConfigurableApplicationContext context;

    @Bean
    @ConfigurationProperties(prefix = "xc.open.file")
    public FileConstants fileConstants() {
        return new FileConstants();
    }

    @Bean
    @ConfigurationProperties(prefix = "xc.open.basic")
    public BasicConstants basicConstants() {
        BasicConstants basicConstants = new BasicConstants();
        AspectHandle.setBasicConstants(basicConstants);
        return basicConstants;
    }

    @Bean
    public FileApi fileApi() {
        FileConstants fileConstants = fileConstants();
        return XcHttp.getDefault(FileApi.class, fileConstants.getFeignUrl(), new XcHttpInterceptorImpl());
    }

    public void init() {
        // 添加统计适配器
        StatisticsAdaptorImpl statisticsAdaptor = context.getBeanFactory().getBean(StatisticsAdaptorImpl.class);
        AuthorityHandle.setStatisticsAdaptor(statisticsAdaptor);
        // 添加token处理
        TokenHandle tokenHandle = context.getBeanFactory().getBean(TokenHandle.class);
        AuthorityHandle.setTokenHandle(tokenHandle);
        // 设置文件url解析
        FileConstants fileConstants = context.getBeanFactory().getBean(FileConstants.class);
        FileUrlUtils.setConfig(fileConstants.getFixedPath(), fileConstants.getDiskNo(), fileConstants.getFileUrl());
    }
}
