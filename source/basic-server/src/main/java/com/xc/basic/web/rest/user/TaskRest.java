package com.xc.basic.web.rest.user;

import com.xc.basic.bean.TaskBean;
import com.xc.basic.dto.TaskDto;
import com.xc.basic.entity.TaskEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.TaskService;
import com.xc.core.annotation.Authority;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.model.TokenModel;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>需要登录权限接口，任务</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，任务"})
@RestController
public class TaskRest {
    @Autowired
    private TaskService taskService;
    @Autowired
    private AppService appService;


    @ApiOperation(value = "任务分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/task_page/{current}")
    @Authority
    public PagingDto<TaskDto> getAppTaskPage(TokenModel tokenModel, @PathVariable Integer current, @PathVariable String appId,
                                             @ModelAttribute PagingBean pagingBean, @ModelAttribute TaskBean taskBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppTaskPage");
        TaskEntity taskEntity = ObjectUtils.convert(new TaskEntity(), taskBean);
        taskEntity.setAppId(appId);
        return taskService.getTaskPage(current, pagingBean, taskEntity);
    }

    @ApiOperation(value = "创建任务")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true)
    })
    @PostMapping("/app/{appId}/task")
    @Authority
    public TaskDto createAppTask(TokenModel tokenModel, @PathVariable String appId, @RequestBody TaskBean taskBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppTask");
        TaskEntity taskEntity = ObjectUtils.convert(new TaskEntity(), taskBean);
        taskEntity.setAppId(appId);
        // 保存
        try {
            if (!taskService.save(taskEntity)) {
                throw FailCode.TASK_CREATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.TASK_CODE_REPEAT.getOperateException();
        }
        taskService.initTask(appId, taskEntity);
        return ObjectUtils.convert(new TaskDto(), taskEntity);
    }

    @ApiOperation(value = "修改任务")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "任务主键", name = "taskId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/task/{taskId}")
    @Authority
    public TaskDto updateAppTask(TokenModel tokenModel, @PathVariable String appId, @PathVariable String taskId,
                                 @RequestBody TaskBean taskBean) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppTask");
        return taskService.updateTask(appId, taskId, taskBean);
    }


    @ApiOperation(value = "删除任务")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "任务主键", name = "taskId", paramType = "path", required = true),
    })
    @DeleteMapping("/app/{appId}/task/{taskId}")
    @Authority
    public void deleteAppTask(TokenModel tokenModel, @PathVariable String appId, @PathVariable String taskId) {
        appService.verifyUserHaveApp(appId, tokenModel, "deleteAppTask");
        taskService.deleteTask(appId, taskId);
    }

    @ApiOperation(value = "批量创建任务", notes = "创建应用任务集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "任务主键", name = "taskId", paramType = "path", required = true)
    })
    @PostMapping("/app/{appId}/task_list")
    @Authority
    public List<TaskDto> createAppTaskList(TokenModel tokenModel, @PathVariable String appId, @RequestBody List<TaskBean> taskBeans) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppTaskList");
        return taskService.createAppTaskList(appId, taskBeans);
    }

    @ApiOperation(value = "获取任务集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/task_list")
    @Authority
    public List<TaskDto> getAppTaskList(TokenModel tokenModel, @PathVariable String appId,
                                        @ModelAttribute QueryBean queryBean, @ModelAttribute TaskBean taskBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppTaskList");
        TaskEntity taskEntity = ObjectUtils.convert(new TaskEntity(), taskBean);
        taskEntity.setAppId(appId);
        return taskService.getTaskList(queryBean, taskEntity);
    }


    @ApiOperation(value = "初始化任务")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "任务主键", name = "taskId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/task_init")
    @Authority
    public void updateAppTaskInit(TokenModel tokenModel, @PathVariable String appId) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppTaskInit");
        taskService.initTask(appId);
    }

    @ApiOperation(value = "执行任务")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "任务主键", name = "taskId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/task/{taskId}/execute")
    @Authority
    public void updateAppTaskExecute(TokenModel tokenModel, @PathVariable String appId, @PathVariable String taskId) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppTaskExecute");
        taskService.executeTask(appId, taskId);
    }
}
