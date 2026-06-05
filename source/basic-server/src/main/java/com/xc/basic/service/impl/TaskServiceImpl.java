package com.xc.basic.service.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.basic.bean.TaskBean;
import com.xc.basic.bean.TaskLogBean;
import com.xc.basic.dto.TaskDto;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.TaskLogEntity;
import com.xc.basic.entity.TaskEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.enums.RedisPrefix;
import com.xc.basic.enums.TaskLogType;
import com.xc.basic.mapper.TaskMapper;
import com.xc.basic.service.AppService;
import com.xc.basic.service.TaskLogService;
import com.xc.basic.service.TaskService;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.bean.SignBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.enums.EffectStatus;
import com.xc.core.enums.Whether;
import com.xc.core.exception.OperateException;
import com.xc.core.exception.ResultException;
import com.xc.core.utils.RedisUtils;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.http.exception.XcHttpException;
import com.xc.tool.utils.JSONUtils;
import com.xc.tool.utils.ObjectUtils;
import com.xc.tool.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

/**
 * <p>任务ServiceImpl</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
@Slf4j
public class TaskServiceImpl extends ServiceImpl<TaskMapper, TaskEntity> implements TaskService {
    @Autowired
    private TaskLogService taskLogService;
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;
    @Autowired
    private AppService appService;
    // 关键：正在运行的任务
    private final Map<String, Map<String, ScheduledFuture<?>>> futureMap = new ConcurrentHashMap<>();

    @Override
    public PagingDto<TaskDto> getTaskPage(Integer current, PagingBean pagingBean, TaskEntity taskEntity) {
        QueryWrapper<TaskEntity> queryWrapper = ServiceUtils.queryLike(taskEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        IPage<TaskEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        return new PagingDto<>(iPage.getTotal(), ObjectUtils.convertList(iPage.getRecords(), TaskDto::new));
    }

    @Override
    @Transactional
    public TaskDto updateTask(String appId, String taskId, TaskBean taskBean) {
        TaskEntity taskEntity = verifyUpdateDelete(appId, taskId);
        // 将任务记录的模板code改成新的模板code
        if (!taskEntity.getCode().equals(taskBean.getCode())) {
            TaskLogEntity taskLogEntity = new TaskLogEntity();
            taskLogEntity.setAppId(appId);
            taskLogEntity.setTaskCode(taskEntity.getCode());
            TaskLogEntity entity = new TaskLogEntity();
            entity.setAppId(appId);
            entity.setTaskCode(taskBean.getCode());
            taskLogService.update(entity, new QueryWrapper<>(taskLogEntity));
        }
        ObjectUtils.convert(taskEntity, taskBean);
        try {
            if (!this.updateById(taskEntity)) {
                throw FailCode.TASK_UPDATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.TASK_CODE_REPEAT.getOperateException();
        }
        this.initTask(appId, taskEntity);
        return ObjectUtils.convert(new TaskDto(), taskEntity);
    }

    /**
     * 编辑删除验证
     *
     * @param appId  应用id
     * @param taskId 任务模板id
     */
    private TaskEntity verifyUpdateDelete(String appId, String taskId) {
        TaskEntity taskEntity = this.getById(taskId);
        if (taskEntity == null) {
            throw FailCode.TASK_ID_ERROR.getOperateException();
        }
        if (!taskEntity.getAppId().equals(appId)) {
            throw FailCode.TASK_APP_ID_ERROR.getOperateException();
        }
        return taskEntity;
    }


    @Override
    public void deleteTask(String appId, String taskId) {
        TaskEntity taskEntity = verifyUpdateDelete(appId, taskId);
        this.stopTask(appId, taskEntity.getCode());
        // 删除任务日志
        TaskLogEntity taskLogEntity = new TaskLogEntity();
        taskLogEntity.setAppId(appId);
        taskLogEntity.setTaskCode(taskEntity.getCode());
        taskLogService.remove(new QueryWrapper<>(taskLogEntity));
        // 删除任务
        if (!this.removeById(taskId)) {
            throw FailCode.TASK_DELETE_FAIL.getOperateException();
        }
    }

    @Override
    public TaskEntity verifyTask(String appId, String taskId) {
        TaskEntity taskEntity = this.getById(taskId);
        if (taskEntity == null) {
            throw FailCode.TASK_ID_ERROR.getOperateException();
        }
        if (!taskEntity.getAppId().equals(appId)) {
            throw FailCode.TASK_APP_ID_ERROR.getOperateException();
        }
        return taskEntity;
    }


    @Override
    @Transactional
    public List<TaskDto> createAppTaskList(String appId, List<TaskBean> taskBeans) {
        // 删除任务模板
        TaskEntity task = new TaskEntity();
        task.setAppId(appId);
        this.remove(new QueryWrapper<>(task));
        // 批量添加任务模板
        List<TaskEntity> entities = new ArrayList<>();
        for (TaskBean taskBean : taskBeans) {
            TaskEntity taskEntity = ObjectUtils.convert(new TaskEntity(), taskBean);
            taskEntity.setAppId(appId);
            entities.add(taskEntity);
        }
        try {
            if (entities.size() > 0) {
                if (!this.saveBatch(entities)) {
                    throw FailCode.TASK_CREATE_FAIL.getOperateException();
                }
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.TASK_CODE_REPEAT.getOperateException();
        }
        // 删除任务记录
        List<String> taskCodeList = taskBeans.stream().map(TaskBean::getCode).collect(Collectors.toList());
        TaskLogEntity taskLogEntity = new TaskLogEntity();
        taskLogEntity.setAppId(appId);
        QueryWrapper<TaskLogEntity> queryWrapper = new QueryWrapper<>(taskLogEntity);
        if (taskCodeList.size() > 0) {
            queryWrapper.lambda().notIn(TaskLogEntity::getTaskCode, taskCodeList);
        }
        taskLogService.remove(queryWrapper);
        return ObjectUtils.convertList(entities, TaskDto::new);
    }

    @Override
    public List<TaskDto> getTaskList(QueryBean queryBean, TaskEntity taskEntity) {
        QueryWrapper<TaskEntity> queryWrapper = ServiceUtils.queryLike(taskEntity, queryBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, queryBean);
        List<TaskEntity> messageEntities = list(queryWrapper);
        return ObjectUtils.convertList(messageEntities, TaskDto::new);
    }

    @Override
    public TaskDto getAppTaskByCode(String appId, String code, boolean verifyStatus) {
        TaskEntity message = new TaskEntity();
        message.setCode(code);
        message.setAppId(appId);
        TaskEntity taskEntity = this.getOne(new QueryWrapper<>(message));
        if (taskEntity == null) {
            throw FailCode.TASK_CODE_ERROR.getOperateException();
        }
        if (verifyStatus) {
            if (EffectStatus.INVALID.getStatus().equals(taskEntity.getStatus())) {
                throw FailCode.TASK_STATUS_INVALID.getOperateException();
            }
        }
        return ObjectUtils.convert(new TaskDto(), taskEntity);
    }

    @Override
    public void initTask() {
        AppEntity appEntity = new AppEntity();
        appEntity.setStatus(EffectStatus.VALID.getStatus());
        List<AppEntity> appEntities = appService.list(new QueryWrapper<>(appEntity));
        for (AppEntity entity : appEntities) {
            initTask(entity.getId());
        }
    }

    @Override
    public void initTask(String appId) {
        stopTask(appId);
        // 关闭app的所有任务
        Map<String, ScheduledFuture<?>> map = futureMap.get(appId);
        if (map == null) {
            map = new ConcurrentHashMap<>();
        }
        // 创建任务
        TaskEntity entity = new TaskEntity();
        entity.setAppId(appId);
        entity.setStatus(EffectStatus.VALID.getStatus());
        List<TaskEntity> taskEntities = this.list(new QueryWrapper<>(entity));
        for (TaskEntity taskEntity : taskEntities) {
            map.put(taskEntity.getCode(), taskScheduler.schedule(() -> {
                executeTask(taskEntity.getId());
            }, new CronTrigger(taskEntity.getCron())));
        }
        futureMap.put(appId, map);
        log.info("任务初始化完成，appId：{}，任务数量：{}", appId, map.size());
    }

    @Override
    public void initTask(String appId, TaskEntity taskEntity) {
        stopTask(appId, taskEntity.getCode());
        // 关闭app的所有任务
        Map<String, ScheduledFuture<?>> map = futureMap.get(appId);
        if (map == null) {
            map = new ConcurrentHashMap<>();
        }
        map.put(taskEntity.getCode(), taskScheduler.schedule(() -> {
            executeTask(taskEntity.getId());
        }, new CronTrigger(taskEntity.getCron())));
        futureMap.put(appId, map);
        log.info("任务初始化完成，appId：{}，任务数量：{}", appId, map.size());
    }

    @Override
    public void stopTask(String appId, String taskCode) {
        Map<String, ScheduledFuture<?>> map = futureMap.get(appId);
        if (map != null) {
            ScheduledFuture<?> obj = map.get(taskCode);
            if (obj != null) {
                obj.cancel(true);
                map.remove(taskCode);
            }
        }
    }

    @Override
    public void stopTask(String appId) {
        Map<String, ScheduledFuture<?>> map = futureMap.get(appId);
        if (map != null) {
            for (ScheduledFuture<?> obj : map.values()) {
                obj.cancel(true);
            }
            map.clear();
        }
    }

    @Override
    public void executeTask(String taskId) {
        TaskEntity taskEntity = this.getById(taskId);
        if (taskEntity == null) {
            throw FailCode.TASK_ID_ERROR.getOperateException();
        }
        // Redis加锁
        String lockKey = RedisPrefix.TASK.getKey() + taskEntity.getCode();
        RedisUtils.lock(lockKey);
        // 封装创建参数
        TaskLogBean taskLogBean = new TaskLogBean();
        String code = getTaskLogCode(taskEntity.getCron(), taskEntity.getCode());
        taskLogBean.setCode(code);
        taskLogBean.setType(TaskLogType.AUTO.getType());
        taskLogBean.setTaskCode(taskEntity.getCode());
        taskLogBean.setUrl(taskEntity.getUrl());
        // 检测任务是否已经执行
        TaskLogEntity taskLogEntity = new TaskLogEntity();
        taskLogEntity.setAppId(taskEntity.getAppId());
        taskLogEntity.setTaskCode(taskEntity.getCode());
        taskLogEntity.setCode(code);
        TaskLogEntity entity = taskLogService.getOne(new QueryWrapper<>(taskLogEntity));
        if (entity != null && Whether.YES.getValue().equals(entity.getStatus())) {
            RedisUtils.unlock(lockKey);
            return;
        }
        long duration;
        String url = taskEntity.getUrl();
        long time = System.currentTimeMillis();
        try {
            AppEntity appEntity = appService.getById(taskEntity.getAppId());
            Map<String, Object> signMap = new HashMap<>();
            signMap.put("taskCode", taskEntity.getCode());
            SignBean signBean = new SignBean(appEntity.getAppId(), appEntity.getAppSecret(), BasicRestCode.task.getCode(), signMap);
            url = StringUtils.analysisParam(url, signBean);
            HttpResponse httpResponse = HttpUtil.createGet(url).execute();
            if (httpResponse.isOk()) {
                duration = System.currentTimeMillis() - time;
                taskLogBean.setDuration(duration);
                taskLogBean.setStatus(Whether.YES.getValue());
                saveTaskLog(entity, taskLogBean, taskEntity.getAppId());
                RedisUtils.unlock(lockKey);
            } else {
                duration = System.currentTimeMillis() - time;
                taskLogBean.setDuration(duration);
                taskLogBean.setStatus(Whether.NO.getValue());
                taskLogBean.setErrorMsg(httpResponse.body());
                saveTaskLog(entity, taskLogBean, taskEntity.getAppId());
                RedisUtils.unlock(lockKey);
                // 抛出异常
                if (httpResponse.getStatus() == 400) {
                    String json = httpResponse.body();
                    ResultException resultException = JSONUtils.getObjectByString(json, ResultException.class);
                    throw new OperateException(resultException.getCode(), resultException.getMessage());
                } else {
                    throw FailCode.APP_REFRESH_FAIL.getOperateException();
                }
            }
        } catch (Exception e) {
            duration = System.currentTimeMillis() - time;
            taskLogBean.setDuration(duration);
            taskLogBean.setStatus(Whether.NO.getValue());
            taskLogBean.setErrorMsg(e.getMessage());
            saveTaskLog(entity, taskLogBean, taskEntity.getAppId());
            RedisUtils.unlock(lockKey);
        }
    }

    @Override
    public void executeTask(String appId, String taskId) {
        TaskEntity taskEntity = this.getById(taskId);
        if (taskEntity == null) {
            throw FailCode.TASK_ID_ERROR.getOperateException();
        }
        if (!taskEntity.getAppId().equals(appId)) {
            throw FailCode.TASK_APP_ID_ERROR.getOperateException();
        }
        // 检测任务是否已经执行
        TaskLogBean taskLogBean = new TaskLogBean();
        String code = "task_" + taskEntity.getCode() + "_" + System.currentTimeMillis();
        taskLogBean.setCode(code);
        taskLogBean.setType(TaskLogType.MANUAL.getType());
        taskLogBean.setTaskCode(taskEntity.getCode());
        taskLogBean.setUrl(taskEntity.getUrl());
        long duration;
        String url = taskEntity.getUrl();
        long time = System.currentTimeMillis();
        try {
            AppEntity appEntity = appService.getById(taskEntity.getAppId());
            Map<String, Object> signMap = new HashMap<>();
            signMap.put("taskCode", taskEntity.getCode());
            SignBean signBean = new SignBean(appEntity.getAppId(), appEntity.getAppSecret(), BasicRestCode.task.getCode(), signMap);
            url = StringUtils.analysisParam(url, signBean);
            HttpResponse httpResponse = HttpUtil.createGet(url).execute();
            if (httpResponse.isOk()) {
                duration = System.currentTimeMillis() - time;
                taskLogBean.setDuration(duration);
                taskLogBean.setStatus(Whether.YES.getValue());
                saveTaskLog(null, taskLogBean, taskEntity.getAppId());
            } else {
                duration = System.currentTimeMillis() - time;
                taskLogBean.setDuration(duration);
                taskLogBean.setStatus(Whether.NO.getValue());
                taskLogBean.setErrorMsg(httpResponse.body());
                saveTaskLog(null, taskLogBean, taskEntity.getAppId());
                // 抛出异常
                if (httpResponse.getStatus() == 400) {
                    String json = httpResponse.body();
                    ResultException resultException = JSONUtils.getObjectByString(json, ResultException.class);
                    throw new OperateException(resultException.getCode(), resultException.getMessage());
                } else {
                    throw FailCode.TASK_EXECUTE_FAIL.getOperateException();
                }
            }
        } catch (Exception e) {
            duration = System.currentTimeMillis() - time;
            taskLogBean.setDuration(duration);
            taskLogBean.setStatus(Whether.NO.getValue());
            taskLogBean.setErrorMsg(e.getMessage());
            saveTaskLog(null, taskLogBean, taskEntity.getAppId());
        }
    }

    /**
     * 保存任务日志
     *
     * @param entity      实体
     * @param taskLogBean 参数
     * @param appId       应用主键
     */
    private void saveTaskLog(TaskLogEntity entity, TaskLogBean taskLogBean, String appId) {
        if (entity == null) {
            entity = ObjectUtils.convert(new TaskLogEntity(), taskLogBean);
            entity.setAppId(appId);
            taskLogService.save(entity);
        } else {
            ObjectUtils.convert(entity, taskLogBean);
            taskLogService.updateById(entity);
        }
    }

    /**
     * 生成日志标识
     *
     * @param cron     cron表达式
     * @param taskCode 任务标识
     * @return 日志标识
     */
    private String getTaskLogCode(String cron, String taskCode) {
        CronExpression cronExp = CronExpression.parse(cron); // 解析 cron
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextTime = cronExp.next(now); // 解析下次执行时间
        if (nextTime != null) {
            LocalDateTime nextNextTime = cronExp.next(nextTime); // 解析下下次执行时间
            long intervalSec = ChronoUnit.SECONDS.between(nextTime, nextNextTime); // 计算执行间隔（秒）
            LocalDateTime thisTime = nextTime.minusSeconds(intervalSec);
            return "task_" + taskCode + "_" + thisTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(); // 解析成毫秒
        }
        return "task_" + taskCode + "_" + System.currentTimeMillis();
    }
}
