package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.basic.dto.MessageTemplateDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.MessageTemplateBean;
import com.xc.basic.entity.MessageTemplateEntity;
import com.xc.core.model.TokenModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>消息模板服务</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface MessageTemplateService extends IService<MessageTemplateEntity> {

    /**
     * 获取应用消息模板分页数据
     *
     * @param current               当前页
     * @param pagingBean            分页参数
     * @param messageTemplateEntity 消息模板参数
     * @return 消息模板分页数据
     */
    public PagingDto<MessageTemplateDto> getMessageTemplatePage(Integer current, PagingBean pagingBean, MessageTemplateEntity messageTemplateEntity);

    /**
     * @param appId               应用主键
     * @param messageTemplateId   模板主键
     * @param messageTemplateBean 数据对象
     * @return 消息模板
     */
    public MessageTemplateDto updateMessageTemplate(String appId, String messageTemplateId, MessageTemplateBean messageTemplateBean);

    /**
     * 删除消息模板
     *
     * @param appId             应用主键
     * @param messageTemplateId 消息模板主键
     */
    public void deleteMessageTemplate(String appId, String messageTemplateId);

    /**
     * 验证关系
     *
     * @param appId             应用主键
     * @param messageTemplateId 消息模板主键
     */
    public MessageTemplateEntity verifyMessageTemplate(String appId, String messageTemplateId);

    /**
     * 消息模板批量添加
     *
     * @param appId                应用主键
     * @param messageTemplateBeans 消息模板集合
     * @return 消息模板集合
     */
    public List<MessageTemplateDto> createAppMessageTemplateList(String appId, List<MessageTemplateBean> messageTemplateBeans);

    /**
     * 获取消息模板集合
     *
     * @param queryBean             查询信息
     * @param messageTemplateEntity 查询参数
     * @return 消息模板集合
     */
    public List<MessageTemplateDto> getMessageTemplateList(QueryBean queryBean, MessageTemplateEntity messageTemplateEntity);

    /**
     * 根据code获取应用消息模板
     *
     * @param appId        应用主键
     * @param code         模板code
     * @param verifyStatus 验证状态
     * @return 消息模板
     */
    public MessageTemplateDto getAppMessageTemplateByCode(String appId, String code, boolean verifyStatus);
}
