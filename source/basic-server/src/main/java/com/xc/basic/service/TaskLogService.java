package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.basic.dto.TaskLogDto;
import com.xc.basic.entity.TaskLogEntity;
import com.xc.core.bean.PagingBean;
import com.xc.core.dto.PagingDto;

/**
 * <p>
 * 任务记录Service
 * </p>
 *
 * @author xc
 * @since 2026-05-13
 */
public interface TaskLogService extends IService<TaskLogEntity> {
    /**
     * 获取应用任务日志分页数据
     *
     * @param current       当前页
     * @param pagingBean    分页消息日志
     * @param taskLogEntity 应用任务参数
     * @return 分页任务日志数据
     */
    public PagingDto<TaskLogDto> getTaskLogPage(Integer current, PagingBean pagingBean, TaskLogEntity taskLogEntity);
}
