package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.basic.dto.TaskLogDto;
import com.xc.basic.entity.TaskLogEntity;
import com.xc.basic.mapper.TaskLogMapper;
import com.xc.basic.service.TaskLogService;
import com.xc.core.bean.PagingBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务记录ServiceImpl
 * </p>
 *
 * @author xc
 * @since 2026-05-13
 */
@Service
public class TaskLogServiceImpl extends ServiceImpl<TaskLogMapper, TaskLogEntity> implements TaskLogService {
    @Override
    public PagingDto<TaskLogDto> getTaskLogPage(Integer current, PagingBean pagingBean, TaskLogEntity taskLogEntity) {
        QueryWrapper<TaskLogEntity> queryWrapper = ServiceUtils.queryLike(taskLogEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        IPage<TaskLogEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        return new PagingDto<>(iPage.getTotal(), ObjectUtils.convertList(iPage.getRecords(), TaskLogDto::new));
    }
}
