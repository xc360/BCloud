package com.xc.basic.web.rest.open;

import com.xc.api.basic.bean.MessageAnalysisBean;
import com.xc.api.basic.bean.MessageSendBean;
import com.xc.api.basic.dto.MessageAnalysisDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.core.bean.SignBean;
import com.xc.api.basic.bean.SystemErrorMessageBean;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.basic.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>开放接口，消息</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"开放接口，消息"})
@RestController
public class OpenMessageRest {
    @Autowired
    private MessageService messageService;
    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    @ApiOperation(value = "创建消息", notes = "开放接口，创建消息")
    @PostMapping("/open/message")
    public void createOpenMessage(@ModelAttribute SignBean signBean, @RequestBody MessageSendBean messageSendBean) {
        signBean.setAuthorityCode(BasicRestCode.createOpenMessage.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        messageService.sendMessage(appEntity, messageSendBean);
    }

    @ApiOperation(value = "创建消息解析", notes = "开放接口，创建消息解析")
    @PostMapping("/open/message_analysis")
    public MessageAnalysisDto createOpenMessageAnalysis(@ModelAttribute SignBean signBean, @RequestBody MessageAnalysisBean messageAnalysisBean) {
        signBean.setAuthorityCode(BasicRestCode.createOpenMessageAnalysis.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        return messageService.createMessageAnalysis(appEntity.getId(), messageAnalysisBean);
    }

    @ApiOperation(value = "创建系统错误消息", notes = "开放接口，创建系统错误消息")
    @PostMapping("/open/system_error_message")
    public void createOpenSystemErrorNoticeMessage(@ModelAttribute SignBean signBean, @RequestBody SystemErrorMessageBean systemErrorMessageBean) {
        signBean.setAuthorityCode(BasicRestCode.createOpenSystemErrorNoticeMessage.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        messageService.sendSystemErrorNoticeMessage(appEntity, systemErrorMessageBean.getMessage());
    }


}
