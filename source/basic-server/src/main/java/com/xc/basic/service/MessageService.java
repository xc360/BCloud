package com.xc.basic.service;

import com.xc.api.basic.bean.MessageAnalysisBean;
import com.xc.api.basic.bean.MessageSendBean;
import com.xc.api.basic.dto.MessageAnalysisDto;
import com.xc.basic.entity.AppEntity;

import java.util.TreeMap;

/**
 * <p>消息服务</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface MessageService {
    /**
     * 发送消息
     *
     * @param appEntity       应用信息
     * @param messageSendBean 参数
     */
    public void sendMessage(AppEntity appEntity, MessageSendBean messageSendBean);

    /**
     * 系统异常通知消息
     *
     * @param appEntity 应用信息
     * @param message   消息内容
     */
    public void sendSystemErrorNoticeMessage(AppEntity appEntity, String message);

    /**
     * 创建消息解析
     *
     * @param appId               应用主键
     * @param messageAnalysisBean 消息参数
     * @return 解析后的消息对象
     */
    public MessageAnalysisDto createMessageAnalysis(String appId, MessageAnalysisBean messageAnalysisBean);

    /**
     * 消息解析
     *
     * @param template 模板
     * @param data     数据
     * @return 内容
     */
    public String messageAnalysis(String template, TreeMap<String, String> data);
}
