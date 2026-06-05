package com.xc.basic.web.rest.open;

import com.xc.api.basic.dto.VersionDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.api.file.utils.FileUrlUtils;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.VersionEntity;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.basic.service.VersionService;
import com.xc.core.bean.QueryBean;
import com.xc.core.bean.SignBean;
import com.xc.core.enums.EffectStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>开放接口，应用版本</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"开放接口，应用版本"})
@RestController
public class OpenVersionRest {

    @Autowired
    private VersionService versionService;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @ApiOperation(value = "获取版本集合", notes = "开放接口，获取的是当前应用的数据")
    @GetMapping("/open/version_list")
    public List<VersionDto> getOpenVersionList(@ModelAttribute SignBean signBean) {
        signBean.setAuthorityCode(BasicRestCode.getOpenVersionList.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        VersionEntity versionEntity = new VersionEntity();
        versionEntity.setAppId(appEntity.getId());
        versionEntity.setStatus(EffectStatus.VALID.getStatus());
        List<VersionDto> versionList = versionService.getVersionList(new QueryBean(), versionEntity);
        for (VersionDto versionDto : versionList) {
            versionDto.setPackageUrl(FileUrlUtils.templateTurnUrl(versionDto.getPackageUrl()));
        }
        return versionList;
    }

    @ApiOperation(value = "获取最新版本", notes = "开放接口，获取最新版本")
    @GetMapping("/open/newest_version")
    public VersionDto getOpenNewestVersion(@ModelAttribute SignBean signBean, @RequestParam String type) {
        signBean.setAuthorityCode(BasicRestCode.getOpenNewestVersion.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        return versionService.getNewestVersion(appEntity.getId(), type, null);
    }
}
