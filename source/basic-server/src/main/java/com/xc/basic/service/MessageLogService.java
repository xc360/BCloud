package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.core.bean.PagingBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.dto.MessageLogDto;
import com.xc.basic.entity.MessageLogEntity;

/**
 * <p>消息记录服务类</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface MessageLogService extends IService<MessageLogEntity> {
    /**
     * 获取应用消息日志分页数据
     *
     * @param current          当前页
     * @param pagingBean       分页消息日志
     * @param messageLogEntity 应用消息参数
     * @return 分页消息日志及应用消息日志数据
     */
    public PagingDto<MessageLogDto> getMessageLogPage(Integer current, PagingBean pagingBean, MessageLogEntity messageLogEntity);


}
