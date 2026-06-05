package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.basic.dto.MessageTemplateDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.MessageTemplateBean;
import com.xc.basic.entity.MessageLogEntity;
import com.xc.basic.entity.MessageTemplateEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.mapper.MessageTemplateMapper;
import com.xc.basic.service.MessageLogService;
import com.xc.basic.service.MessageTemplateService;
import com.xc.core.enums.EffectStatus;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>消息模板服务实现类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
@Slf4j
public class MessageTemplateServiceImpl extends ServiceImpl<MessageTemplateMapper, MessageTemplateEntity> implements MessageTemplateService {
    @Autowired
    private MessageLogService messageLogService;

    @Override
    public PagingDto<MessageTemplateDto> getMessageTemplatePage(Integer current, PagingBean pagingBean, MessageTemplateEntity messageTemplateEntity) {
        QueryWrapper<MessageTemplateEntity> queryWrapper = ServiceUtils.queryLike(messageTemplateEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        IPage<MessageTemplateEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        return new PagingDto<>(iPage.getTotal(), ObjectUtils.convertList(iPage.getRecords(), MessageTemplateDto::new));
    }

    @Override
    @Transactional
    public MessageTemplateDto updateMessageTemplate(String appId, String messageTemplateId, MessageTemplateBean messageTemplateBean) {
        MessageTemplateEntity messageTemplateEntity = verifyUpdateDelete(appId, messageTemplateId);
        // 将消息记录的模板code改成新的模板code
        if (!messageTemplateEntity.getCode().equals(messageTemplateBean.getCode())) {
            MessageLogEntity messageLogEntity = new MessageLogEntity();
            messageLogEntity.setAppId(appId);
            messageLogEntity.setMessageTemplateCode(messageTemplateEntity.getCode());
            MessageLogEntity entity = new MessageLogEntity();
            entity.setAppId(appId);
            entity.setMessageTemplateCode(messageTemplateBean.getCode());
            messageLogService.update(entity, new QueryWrapper<>(messageLogEntity));
        }
        ObjectUtils.convert(messageTemplateEntity, messageTemplateBean);
        try {
            if (!this.updateById(messageTemplateEntity)) {
                throw FailCode.MESSAGE_TEMPLATE_UPDATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.MESSAGE_TEMPLATE_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convert(new MessageTemplateDto(), messageTemplateEntity);
    }

    /**
     * 编辑删除验证
     *
     * @param appId             应用id
     * @param messageTemplateId 消息模板id
     */
    private MessageTemplateEntity verifyUpdateDelete(String appId, String messageTemplateId) {
        MessageTemplateEntity messageTemplateEntity = this.getById(messageTemplateId);
        if (messageTemplateEntity == null) {
            throw FailCode.MESSAGE_TEMPLATE_ID_ERROR.getOperateException();
        }
        if (!messageTemplateEntity.getAppId().equals(appId)) {
            throw FailCode.MESSAGE_TEMPLATE_APP_ID_ERROR.getOperateException();
        }
        return messageTemplateEntity;
    }


    @Override
    public void deleteMessageTemplate(String appId, String messageTemplateId) {
        MessageTemplateEntity messageTemplateEntity = verifyUpdateDelete(appId, messageTemplateId);
        // 删除消息日志
        MessageLogEntity messageLogEntity = new MessageLogEntity();
        messageLogEntity.setAppId(appId);
        messageLogEntity.setMessageTemplateCode(messageTemplateEntity.getCode());
        messageLogService.remove(new QueryWrapper<>(messageLogEntity));
        // 删除消息
        if (!this.removeById(messageTemplateId)) {
            throw FailCode.MESSAGE_TEMPLATE_DELETE_FAIL.getOperateException();
        }
    }

    @Override
    public MessageTemplateEntity verifyMessageTemplate(String appId, String messageTemplateId) {
        MessageTemplateEntity messageTemplateEntity = this.getById(messageTemplateId);
        if (messageTemplateEntity == null) {
            throw FailCode.MESSAGE_TEMPLATE_ID_ERROR.getOperateException();
        }
        if (!messageTemplateEntity.getAppId().equals(appId)) {
            throw FailCode.MESSAGE_TEMPLATE_APP_ID_ERROR.getOperateException();
        }
        return messageTemplateEntity;
    }


    @Override
    @Transactional
    public List<MessageTemplateDto> createAppMessageTemplateList(String appId, List<MessageTemplateBean> messageTemplateBeans) {
        // 删除消息模板
        MessageTemplateEntity messageTemplate = new MessageTemplateEntity();
        messageTemplate.setAppId(appId);
        this.remove(new QueryWrapper<>(messageTemplate));
        // 批量添加消息模板
        List<MessageTemplateEntity> entities = new ArrayList<>();
        for (MessageTemplateBean messageTemplateBean : messageTemplateBeans) {
            MessageTemplateEntity messageTemplateEntity = ObjectUtils.convert(new MessageTemplateEntity(), messageTemplateBean);
            messageTemplateEntity.setAppId(appId);
            entities.add(messageTemplateEntity);
        }
        try {
            if (entities.size() > 0) {
                if (!this.saveBatch(entities)) {
                    throw FailCode.MESSAGE_TEMPLATE_CREATE_FAIL.getOperateException();
                }
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.MESSAGE_TEMPLATE_CODE_REPEAT.getOperateException();
        }
        // 删除消息记录
        List<String> messageTemplateCodeList = messageTemplateBeans.stream().map(MessageTemplateBean::getCode).collect(Collectors.toList());
        MessageLogEntity messageLogEntity = new MessageLogEntity();
        messageLogEntity.setAppId(appId);
        QueryWrapper<MessageLogEntity> queryWrapper = new QueryWrapper<>(messageLogEntity);
        if (messageTemplateCodeList.size() > 0) {
            queryWrapper.lambda().notIn(MessageLogEntity::getMessageTemplateCode, messageTemplateCodeList);
        }
        messageLogService.remove(queryWrapper);
        return ObjectUtils.convertList(entities, MessageTemplateDto::new);
    }

    @Override
    public List<MessageTemplateDto> getMessageTemplateList(QueryBean queryBean, MessageTemplateEntity messageTemplateEntity) {
        QueryWrapper<MessageTemplateEntity> queryWrapper = ServiceUtils.queryLike(messageTemplateEntity, queryBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, queryBean);
        List<MessageTemplateEntity> messageEntities = list(queryWrapper);
        return ObjectUtils.convertList(messageEntities, MessageTemplateDto::new);
    }

    @Override
    public MessageTemplateDto getAppMessageTemplateByCode(String appId, String code, boolean verifyStatus) {
        MessageTemplateEntity message = new MessageTemplateEntity();
        message.setCode(code);
        message.setAppId(appId);
        MessageTemplateEntity messageTemplateEntity = this.getOne(new QueryWrapper<>(message));
        if (messageTemplateEntity == null) {
            throw FailCode.MESSAGE_TEMPLATE_CODE_ERROR.getOperateException();
        }
        if (verifyStatus) {
            if (EffectStatus.INVALID.getStatus().equals(messageTemplateEntity.getStatus())) {
                throw FailCode.MESSAGE_TEMPLATE_STATUS_INVALID.getOperateException();
            }
        }
        return ObjectUtils.convert(new MessageTemplateDto(), messageTemplateEntity);
    }
}
