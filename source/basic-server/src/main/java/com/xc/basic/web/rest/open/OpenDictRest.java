package com.xc.basic.web.rest.open;

import com.xc.api.basic.dto.DictDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.core.bean.QueryBean;
import com.xc.core.bean.SignBean;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.DictEntity;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.basic.service.DictService;
import com.xc.core.enums.EffectStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>开放接口，字典</p>
 *
 * @author xc
 * @version v1.0.0
 */
@RestController
@Api(tags = {"开放接口，字典"})
public class OpenDictRest {
    @Autowired
    private DictService dictService;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @ApiOperation(value = "获取字典集合", notes = "开放接口，根据类型获取字典集合,不传类型获取全部，获取的是当前应用的数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "字典类型,为null查询全部", name = "type", paramType = "query"),
    })
    @GetMapping("/open/dict_list")
    public List<DictDto> getOpenDictList(@ModelAttribute SignBean signBean, @RequestParam(required = false) String type) {
        signBean.setAuthorityCode(BasicRestCode.getOpenDictList.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        DictEntity dictEntity = new DictEntity();
        dictEntity.setAppId(appEntity.getId());
        dictEntity.setType(type);
        dictEntity.setStatus(EffectStatus.VALID.getStatus());
        return dictService.getDictList(new QueryBean("seq", "ASC"), dictEntity);
    }
}
