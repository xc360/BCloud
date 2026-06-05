package com.xc.basic.web.rest.manage;

import com.xc.api.basic.dto.AppDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.QueryAppBean;
import com.xc.basic.dto.AuditAppDto;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.core.annotation.Authority;
import com.xc.core.bean.AuditBean;
import com.xc.core.enums.AuditStatus;
import com.xc.tool.utils.ObjectUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>管理端接口，应用信息</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"管理端接口，应用信息"})
@RestController
public class ManageAppRest {
    @Autowired
    private AppService appService;

    @ApiOperation(value = "审核应用列表", notes = "获取审核应用列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true),
    })
    @GetMapping("/manage/audit_app_page/{current}")
    @Authority
    public PagingDto<AppDto> getManageAuditAppPage(@PathVariable Integer current, @ModelAttribute PagingBean pagingBean, @ModelAttribute QueryAppBean queryAppBean) {
        AppEntity appEntity = ObjectUtils.convert(new AppEntity(), queryAppBean);
        return appService.getAuditAppPage(current, pagingBean, appEntity);
    }

    @ApiOperation(value = "获取审核应用", notes = "审核应用详情")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @GetMapping("/manage/audit_app/{appId}")
    @Authority
    public AuditAppDto getManageAuditApp(@PathVariable String appId) {
        return appService.getAuditApp(appId);
    }

    @ApiOperation(value = "审核", notes = "审核应用")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @PutMapping("/manage/audit_app/{appId}")
    @Authority
    public AppDto updateManageAuditApp(@PathVariable String appId, @RequestBody AuditBean auditBean) {
        AppEntity appEntity = appService.getById(appId);
        if (appEntity == null) {
            throw FailCode.APP_ID_ERROR.getOperateException();
        }
        if (AuditStatus.REVIEWED.getStatus().equals(auditBean.getAuditStatus())) {
            // 通过
            appEntity.setApplyTime(new Date());
            appEntity.setAuditStatus(AuditStatus.REVIEWED.getStatus());
            appEntity.setReason(auditBean.getReason());
        } else if (AuditStatus.REJECTED.getStatus().equals(auditBean.getAuditStatus())) {
            // 拒绝
            appEntity.setApplyTime(new Date());
            appEntity.setAuditStatus(AuditStatus.REJECTED.getStatus());
            appEntity.setReason(auditBean.getReason());
        } else {
            throw FailCode.AUDIT_STATUS_ERROR.getOperateException();
        }
        if (!appService.updateById(appEntity)) {
            throw FailCode.APP_UPDATE_FAIL.getOperateException();
        }
        return ObjectUtils.convert(new AppDto(), appEntity);
    }
}
