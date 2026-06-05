package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.basic.bean.TaskBean;
import com.xc.basic.dto.TaskDto;
import com.xc.basic.entity.TaskEntity;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;

import java.util.List;

/**
 * <p>
 * 任务Service
 * </p>
 *
 * @author xc
 * @since 2026-05-13
 */
public interface TaskService extends IService<TaskEntity> {
    /**
     * 获取应用任务分页数据
     *
     * @param current    当前页
     * @param pagingBean 分页参数
     * @param taskEntity 任务参数
     * @return 任务分页数据
     */
    public PagingDto<TaskDto> getTaskPage(Integer current, PagingBean pagingBean, TaskEntity taskEntity);

    /**
     * @param appId    应用主键
     * @param taskId   模板主键
     * @param taskBean 数据对象
     * @return 任务
     */
    public TaskDto updateTask(String appId, String taskId, TaskBean taskBean);

    /**
     * 删除任务
     *
     * @param appId  应用主键
     * @param taskId 任务主键
     */
    public void deleteTask(String appId, String taskId);

    /**
     * 验证关系
     *
     * @param appId  应用主键
     * @param taskId 任务主键
     */
    public TaskEntity verifyTask(String appId, String taskId);

    /**
     * 任务批量添加
     *
     * @param appId     应用主键
     * @param taskBeans 任务集合
     * @return 任务集合
     */
    public List<TaskDto> createAppTaskList(String appId, List<TaskBean> taskBeans);

    /**
     * 获取任务集合
     *
     * @param queryBean  查询信息
     * @param taskEntity 查询参数
     * @return 任务集合
     */
    public List<TaskDto> getTaskList(QueryBean queryBean, TaskEntity taskEntity);

    /**
     * 根据code获取应用任务
     *
     * @param appId        应用主键
     * @param code         模板code
     * @param verifyStatus 验证状态
     * @return 任务
     */
    public TaskDto getAppTaskByCode(String appId, String code, boolean verifyStatus);

    /**
     * 初始化任务
     */
    public void initTask();

    /**
     * 初始化任务
     *
     * @param appId 应用主键
     */
    public void initTask(String appId);

    /**
     * 初始化任务
     *
     * @param appId      应用主键
     * @param taskEntity 任务实体
     */
    public void initTask(String appId, TaskEntity taskEntity);

    /**
     * 停止任务
     *
     * @param appId    应用主键
     * @param taskCode 任务标识
     */
    public void stopTask(String appId, String taskCode);

    /**
     * 停止任务
     *
     * @param appId 应用主键
     */
    public void stopTask(String appId);

    /**
     * 执行任务
     *
     * @param taskId 任务主键
     */
    public void executeTask(String taskId);

    /**
     * 执行任务
     *
     * @param appId  应用主键
     * @param taskId 任务主键
     */
    public void executeTask(String appId, String taskId);
}
