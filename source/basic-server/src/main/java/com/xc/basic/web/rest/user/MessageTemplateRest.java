package com.xc.basic.web.rest.user;

import com.xc.api.basic.dto.MessageTemplateDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.MessageTemplateBean;
import com.xc.basic.entity.MessageTemplateEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.MessageTemplateService;
import com.xc.core.annotation.Authority;
import com.xc.core.model.TokenModel;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>需要登录权限接口，消息模板</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，消息模板"})
@RestController
public class MessageTemplateRest {
    @Autowired
    private MessageTemplateService messageTemplateService;
    @Autowired
    private AppService appService;


    @ApiOperation(value = "消息模板分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/message_template_page/{current}")
    @Authority
    public PagingDto<MessageTemplateDto> getAppMessageTemplatePage(TokenModel tokenModel, @PathVariable Integer current, @PathVariable String appId,
                                                                   @ModelAttribute PagingBean pagingBean, @ModelAttribute MessageTemplateBean messageTemplateBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppMessageTemplatePage");
        MessageTemplateEntity messageTemplateEntity = ObjectUtils.convert(new MessageTemplateEntity(), messageTemplateBean);
        messageTemplateEntity.setAppId(appId);
        return messageTemplateService.getMessageTemplatePage(current, pagingBean, messageTemplateEntity);
    }

    @ApiOperation(value = "创建消息模板")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true)
    })
    @PostMapping("/app/{appId}/message_template")
    @Authority
    public MessageTemplateDto createAppMessageTemplate(TokenModel tokenModel, @PathVariable String appId, @RequestBody MessageTemplateBean messageTemplateBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppMessageTemplate");
        MessageTemplateEntity messageTemplateEntity = ObjectUtils.convert(new MessageTemplateEntity(), messageTemplateBean);
        messageTemplateEntity.setAppId(appId);
        // 保存
        try {
            if (!messageTemplateService.save(messageTemplateEntity)) {
                throw FailCode.MESSAGE_TEMPLATE_CREATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.MESSAGE_TEMPLATE_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new MessageTemplateDto(), messageTemplateEntity);
    }

    @ApiOperation(value = "修改消息模板")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "消息模板主键", name = "messageTemplateId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/message_template/{messageTemplateId}")
    @Authority
    public MessageTemplateDto updateAppMessageTemplate(TokenModel tokenModel, @PathVariable String appId, @PathVariable String messageTemplateId,
                                                       @RequestBody MessageTemplateBean messageTemplateBean) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppMessageTemplate");
        return messageTemplateService.updateMessageTemplate(appId, messageTemplateId, messageTemplateBean);
    }


    @ApiOperation(value = "删除消息模板")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "消息模板主键", name = "messageTemplateId", paramType = "path", required = true),
    })
    @DeleteMapping("/app/{appId}/message_template/{messageTemplateId}")
    @Authority
    public void deleteAppMessageTemplate(TokenModel tokenModel, @PathVariable String appId, @PathVariable String messageTemplateId) {
        appService.verifyUserHaveApp(appId, tokenModel, "deleteAppMessageTemplate");
        messageTemplateService.deleteMessageTemplate(appId, messageTemplateId);
    }

    @ApiOperation(value = "批量创建消息模板", notes = "创建应用消息模板集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "消息模板主键", name = "messageTemplateId", paramType = "path", required = true)
    })
    @PostMapping("/app/{appId}/message_template_list")
    @Authority
    public List<MessageTemplateDto> createAppMessageTemplateList(TokenModel tokenModel, @PathVariable String appId, @RequestBody List<MessageTemplateBean> messageTemplateBeans) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppMessageTemplateList");
        return messageTemplateService.createAppMessageTemplateList(appId, messageTemplateBeans);
    }

    @ApiOperation(value = "获取消息模板集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/message_template_list")
    @Authority
    public List<MessageTemplateDto> getAppMessageTemplateList(TokenModel tokenModel, @PathVariable String appId,
                                                              @ModelAttribute QueryBean queryBean, @ModelAttribute MessageTemplateBean messageTemplateBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppMessageTemplateList");
        MessageTemplateEntity messageTemplateEntity = ObjectUtils.convert(new MessageTemplateEntity(), messageTemplateBean);
        messageTemplateEntity.setAppId(appId);
        return messageTemplateService.getMessageTemplateList(queryBean, messageTemplateEntity);
    }
}
