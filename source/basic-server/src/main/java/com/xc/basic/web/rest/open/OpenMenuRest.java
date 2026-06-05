package com.xc.basic.web.rest.open;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xc.api.basic.dto.MenuDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.basic.bean.QueryMenuBean;
import com.xc.basic.config.Constants;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.MenuEntity;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.basic.service.MenuService;
import com.xc.core.bean.QueryBean;
import com.xc.core.bean.SignBean;
import com.xc.core.enums.EffectStatus;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>开放接口，菜单</p>
 *
 * @author xc
 * @version v1.0.0
 */
@RestController
@Api(tags = {"开放接口，菜单"})
public class OpenMenuRest {
    @Autowired
    private MenuService menuService;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;
    @Autowired
    private Constants constants;

    @ApiOperation(value = "获取菜单集合", notes = "开放接口，获取菜单集合，获取的是当前应用的数据")
    @GetMapping("/open/menu_list")
    public List<MenuDto> getOpenMenuList(@ModelAttribute SignBean signBean, @RequestParam(required = false) String type,
                                         @RequestParam(required = false) String firstCode) {
        signBean.setAuthorityCode(BasicRestCode.getOpenMenuList.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        String parentNode = null;
        if (firstCode != null) {
            MenuEntity menuEntity = new MenuEntity();
            menuEntity.setAppId(appEntity.getId());
            menuEntity.setType(type);
            menuEntity.setCode(firstCode);
            MenuEntity entity = menuService.getOne(new QueryWrapper<>(menuEntity));
            if (entity != null) {
                parentNode = entity.getNode();
            } else if (constants.getRoot().equals(firstCode)) {
                parentNode = constants.getRoot();
            } else {
                return new ArrayList<>();
            }
        }
        QueryMenuBean queryMenuBean = new QueryMenuBean();
        queryMenuBean.setStatus(EffectStatus.VALID.getStatus());
        queryMenuBean.setType(type);
        queryMenuBean.setParentNode(parentNode);
        QueryBean queryBean = new QueryBean();
        queryBean.setSortField("seq");
        queryBean.setSortRule("ASC");
        List<MenuEntity> menuEntities = menuService.getAppMenuList(appEntity.getId(), queryBean, queryMenuBean);
        return ObjectUtils.convertList(menuEntities, MenuDto::new);
    }

}
