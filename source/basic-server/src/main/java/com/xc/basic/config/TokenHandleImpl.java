package com.xc.basic.config;

import com.xc.basic.entity.AppEntity;
import com.xc.basic.service.AppService;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.core.aspect.BasicConstants;
import com.xc.core.aspect.TokenHandle;
import com.xc.core.dto.TokenDto;
import com.xc.core.model.TokenModel;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>token处理实现</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Component
public class TokenHandleImpl implements TokenHandle {
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;
    @Autowired
    private AppService appService;
    @Autowired
    private BasicConstants basicConstants;

    @Override
    public TokenDto getToken(String accessToken) {
        AppEntity appEntity = appService.verifyAppSecret(basicConstants.getAppId(), basicConstants.getAppSecret());
        TokenModel tokenModel = basicAuthorizeService.getToken(appEntity, null, accessToken);
        return ObjectUtils.convert(new TokenDto(), tokenModel);
    }

    @Override
    public TokenDto updateOpenToken(String refreshToken) {
        TokenModel tokenModel = basicAuthorizeService.updateToken(refreshToken);
        return ObjectUtils.convert(new TokenDto(), tokenModel);
    }
}
