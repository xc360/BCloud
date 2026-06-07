package com.example.demo.config;

import com.xc.api.basic.BasicApi;
import com.xc.core.aspect.AspectHandle;
import com.xc.core.aspect.BasicConstants;
import com.xc.core.interceptor.XcHttpInterceptorImpl;
import com.xc.core.utils.StorageUtils;
import com.xc.tool.http.XcHttp;
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

    @Bean
    public BasicApi basicApi() {
        BasicConstants basicConstants = basicConstants();
        return XcHttp.getDefault(BasicApi.class, basicConstants.getFeignUrl(), new XcHttpInterceptorImpl());
    }

    @Bean
//    @ConfigurationProperties(prefix = "xc.open.basic")
    public BasicConstants basicConstants() {
        BasicConstants basicConstants = new BasicConstants();
        basicConstants.setAppId("xc0320330512");
        basicConstants.setAppSecret("21f47c857386b4fe34816889f7277529f652413645573059");
        basicConstants.setFeignUrl("http://localhost:8811");
        basicConstants.setOpenToken(true);
        AspectHandle.setBasicConstants(basicConstants);
        StorageUtils.setUseRedis(false);
        return basicConstants;
    }
}
