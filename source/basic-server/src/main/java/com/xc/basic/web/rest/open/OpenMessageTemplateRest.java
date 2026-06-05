package com.xc.basic.web.rest.open;

import com.xc.api.basic.dto.MessageTemplateDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.core.bean.SignBean;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.basic.service.MessageTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>开放接口，消息模板</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"开放接口，消息模板"})
@RestController
public class OpenMessageTemplateRest {
    @Autowired
    private MessageTemplateService messageTemplateService;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @ApiOperation(value = "获取消息模板", notes = "开放接口，获取消息模板")
    @GetMapping("/open/message_template")
    public MessageTemplateDto getOpenMessageTemplate(@ModelAttribute SignBean signBean, @RequestParam String code) {
        signBean.setAuthorityCode(BasicRestCode.getOpenMessageTemplate.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        return messageTemplateService.getAppMessageTemplateByCode(appEntity.getId(), code, true);
    }
}
