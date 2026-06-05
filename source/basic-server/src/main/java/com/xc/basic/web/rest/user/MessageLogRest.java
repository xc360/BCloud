package com.xc.basic.web.rest.user;

import com.xc.core.bean.PagingBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.MessageLogBean;
import com.xc.basic.dto.MessageLogDto;
import com.xc.basic.entity.MessageLogEntity;
import com.xc.basic.entity.MessageTemplateEntity;
import com.xc.basic.service.AppService;
import com.xc.basic.service.MessageLogService;
import com.xc.basic.service.MessageTemplateService;
import com.xc.core.annotation.Authority;
import com.xc.core.model.TokenModel;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>需要登录权限接口，消息记录</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，消息记录"})
@RestController
public class MessageLogRest {
    @Autowired
    private MessageLogService messageLogService;
    @Autowired
    private MessageTemplateService messageTemplateService;
    @Autowired
    private AppService appService;

    @ApiOperation(value = "消息日志分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "消息主键", name = "messageTemplateId", paramType = "path", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true),
    })
    @GetMapping("/app/{appId}/message/{messageTemplateId}/log/{current}")
    @Authority
    public PagingDto<MessageLogDto> getAppMessageLogPage(TokenModel tokenModel, @PathVariable Integer current,
                                                         @PathVariable String appId, @PathVariable String messageTemplateId,
                                                         @ModelAttribute PagingBean pagingBean, @ModelAttribute MessageLogBean messageLogBean) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "getAppMessageLogPage");
        MessageTemplateEntity messageTemplateEntity = messageTemplateService.verifyMessageTemplate(appId, messageTemplateId);
        MessageLogEntity messageLogEntity = ObjectUtils.convert(new MessageLogEntity(), messageLogBean);
        messageLogEntity.setAppId(appId);
        messageLogEntity.setMessageTemplateCode(messageTemplateEntity.getCode());
        return messageLogService.getMessageLogPage(current, pagingBean, messageLogEntity);
    }
}
