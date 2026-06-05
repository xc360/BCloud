package com.xc.basic.web.rest.user;

import com.xc.basic.bean.TaskLogBean;
import com.xc.basic.dto.TaskLogDto;
import com.xc.basic.entity.MessageTemplateEntity;
import com.xc.basic.entity.TaskEntity;
import com.xc.basic.entity.TaskLogEntity;
import com.xc.basic.service.AppService;
import com.xc.basic.service.MessageTemplateService;
import com.xc.basic.service.TaskLogService;
import com.xc.basic.service.TaskService;
import com.xc.core.annotation.Authority;
import com.xc.core.bean.PagingBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.model.TokenModel;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>需要登录权限接口，任务记录</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，任务记录"})
@RestController
public class TaskLogRest {
    @Autowired
    private TaskLogService taskLogService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private AppService appService;

    @ApiOperation(value = "任务日志分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "任务主键", name = "taskId", paramType = "path", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true),
    })
    @GetMapping("/app/{appId}/task/{taskId}/log/{current}")
    @Authority
    public PagingDto<TaskLogDto> getAppTaskLogPage(TokenModel tokenModel, @PathVariable Integer current,
                                                   @PathVariable String appId, @PathVariable String taskId,
                                                   @ModelAttribute PagingBean pagingBean, @ModelAttribute TaskLogBean taskLogBean) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "getAppTaskLogPage");
        TaskEntity taskEntity = taskService.verifyTask(appId, taskId);
        TaskLogEntity taskLogEntity = ObjectUtils.convert(new TaskLogEntity(), taskLogBean);
        taskLogEntity.setAppId(appId);
        taskLogEntity.setTaskCode(taskEntity.getCode());
        return taskLogService.getTaskLogPage(current, pagingBean, taskLogEntity);
    }
}
