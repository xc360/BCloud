package com.xc.basic.service.impl;

import com.xc.api.basic.dto.AppDto;
import com.xc.api.basic.dto.InfoDto;
import com.xc.core.bean.QueryBean;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.InfoEntity;
import com.xc.basic.service.AppService;
import com.xc.basic.service.IndexService;
import com.xc.basic.service.InfoService;
import com.xc.core.aspect.BasicConstants;
import com.xc.core.enums.EffectStatus;
import com.xc.api.basic.enums.InfoType;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

/**
 * <p>首页ServiceImpl</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    private BasicConstants basicConstants;
    @Autowired
    private InfoService infoService;
    @Autowired
    private AppService appService;

    @Override
    public Map<String, Object> getAppInfo() {
        // 查询app
        AppEntity appEntity = appService.verifyAppSecret(basicConstants.getAppId(), basicConstants.getAppSecret());
        AppDto appDto = ObjectUtils.convert(new AppDto(), appEntity);
        // 查询APP信息
        InfoEntity infoEntity = new InfoEntity();
        infoEntity.setAppId(appDto.getId());
        infoEntity.setType(InfoType.CLIENT_INFO.getType());
        infoEntity.setStatus(EffectStatus.VALID.getStatus());
        List<InfoDto> infoList = infoService.getInfoList(new QueryBean(), infoEntity);
        //数据转换
        Map<String, Object> objectMap = ObjectUtils.convertMap(appDto);
        for (InfoDto infoDto : infoList) {
            objectMap.put(infoDto.getKey(), infoDto.getValue());
        }
        return objectMap;
    }

    @Override
    public AppEntity setLoginModel(Model model, String appId) {
        // 应用信息
        AppEntity appEntity = appService.getValidAppByAppId(appId);
        model.addAttribute("appName", appEntity.getAppName());
        model.addAttribute("app", ObjectUtils.convert(new AppDto(), appEntity));
        return appEntity;
    }
}
