package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.core.bean.PagingBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.dto.MessageLogDto;
import com.xc.basic.entity.MessageLogEntity;
import com.xc.basic.mapper.MessageLogMapper;
import com.xc.basic.service.MessageLogService;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.stereotype.Service;

/**
 * <p>消息记录服务实现类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
public class MessageLogServiceImpl extends ServiceImpl<MessageLogMapper, MessageLogEntity> implements MessageLogService {
    @Override
    public PagingDto<MessageLogDto> getMessageLogPage(Integer current, PagingBean pagingBean, MessageLogEntity messageLogEntity) {
        QueryWrapper<MessageLogEntity> queryWrapper = ServiceUtils.queryLike(messageLogEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        IPage<MessageLogEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        return new PagingDto<>(iPage.getTotal(), ObjectUtils.convertList(iPage.getRecords(), MessageLogDto::new));
    }
}
