package com.xc.basic.web.rest.user;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xc.api.basic.bean.InfoBean;
import com.xc.api.basic.dto.AppDto;
import com.xc.api.basic.dto.DictDto;
import com.xc.api.basic.enums.BasicRestCode;
import com.xc.api.file.bean.DiskSignBean;
import com.xc.api.file.config.FileConstants;
import com.xc.api.file.enums.FileRestCode;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.bean.SignBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.exception.OperateException;
import com.xc.core.exception.ResultException;
import com.xc.api.file.FileApi;
import com.xc.basic.bean.AppBean;
import com.xc.basic.bean.QueryAppBean;
import com.xc.basic.config.Constants;
import com.xc.basic.dto.AppSecretDto;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.core.annotation.Authority;
import com.xc.core.enums.AuditStatus;
import com.xc.core.enums.EffectStatus;
import com.xc.core.model.TokenModel;
import com.xc.api.file.utils.FileUrlUtils;
import com.xc.tool.http.exception.XcHttpException;
import com.xc.tool.utils.JSONUtils;
import com.xc.tool.utils.Md5Utils;
import com.xc.tool.utils.ObjectUtils;
import com.xc.tool.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * <p>需要登录权限接口，应用接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"需要登录权限接口，应用接口"})
@RestController
public class AppRest {

    @Autowired
    private AppService appService;
    @Autowired
    private Constants constants;
    @Autowired
    private FileConstants fileConstants;
    @Autowired
    private FileApi fileApi;

    @ApiOperation(value = "应用分页", notes = "获取当前用户应用分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true),
    })
    @GetMapping("/current_user/app_page/{current}")
    @Authority
    public PagingDto<AppDto> getCurrentUserAppPage(TokenModel tokenModel, @PathVariable Integer current, @ModelAttribute PagingBean pagingBean, @ModelAttribute QueryAppBean queryAppBean) {
        PagingDto<AppDto> pagingDto = appService.getUserAppPage(current, tokenModel, pagingBean, ObjectUtils.convert(new AppEntity(), queryAppBean));
        for (AppDto appDto : pagingDto.getResData()) {
            appDto.setLogoUrl(FileUrlUtils.templateTurnUrl(appDto.getLogoUrl()));
        }
        return pagingDto;
    }

    @ApiOperation(value = "创建应用", notes = "创建当前用户的应用")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
    })
    @PostMapping("/current_user/app")
    @Authority
    public AppDto createCurrentUserApp(TokenModel tokenModel, @RequestBody AppBean appBean) {
        // 确认文件
        DiskSignBean diskSignBean = new DiskSignBean(fileConstants.getDiskNo(), fileConstants.getDiskSecret(), FileRestCode.updateOpenDiskFileStatus.getCode());
        String templateUrl = FileUrlUtils.urlTurnTemplate(appBean.getLogoUrl());
        FileUrlUtils.confirmFile(fileApi, diskSignBean, null, templateUrl);
        appBean.setLogoUrl(templateUrl);
        // 创建应用
        AppEntity appEntity = ObjectUtils.convert(new AppEntity(), appBean);
        appEntity.setAppId(StringUtils.generateOnlyNum(constants.getAppIdPrefix()));
        appEntity.setAppSecret(Md5Utils.getSaltMd5(StringUtils.random(10)));
        appEntity.setUserId(tokenModel.getUserId());
        appEntity.setAuditStatus(AuditStatus.NOT_APPLIED.getStatus());
        if (!appService.save(appEntity)) {
            throw FailCode.APP_CREATE_FAIL.getOperateException();
        }
        appEntity.setLogoUrl(FileUrlUtils.templateTurnUrl(appEntity.getLogoUrl()));
        return ObjectUtils.convert(new AppDto(), appEntity);
    }

    @ApiOperation(value = "修改应用", notes = "修改应用,状态为0/发布应用,状态为1")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
    })
    @PutMapping("/current_user/app/{appId}")
    @Authority
    public AppDto updateCurrentUserApp(TokenModel tokenModel, @PathVariable String appId, @RequestBody AppBean appBean) {
        AppEntity appEntity = appService.verifyUserHaveApp(appId, tokenModel, "updateCurrentUserApp");
        if (AuditStatus.APPLIED.getStatus().equals(appBean.getAuditStatus()) ||
                AuditStatus.NOT_APPLIED.getStatus().equals(appBean.getAuditStatus())) {
            if (AuditStatus.APPLIED.getStatus().equals(appBean.getAuditStatus())) {
                appEntity.setApplyTime(new Date());
            }
        } else {
            appEntity.setAuditStatus(null);
            appEntity.setReason(null);
            appEntity.setApplyTime(null);
        }
        // 确认文件
        if (appBean.getLogoUrl() != null) {
            DiskSignBean diskSignBean = new DiskSignBean(fileConstants.getDiskNo(), fileConstants.getDiskSecret(), FileRestCode.updateOpenDiskFileStatus.getCode());
            String templateUrl = FileUrlUtils.urlTurnTemplate(appBean.getLogoUrl());
            FileUrlUtils.confirmFile(fileApi, diskSignBean, appEntity.getLogoUrl(), templateUrl);
            appBean.setLogoUrl(templateUrl);
        }
        // 保存数据
        ObjectUtils.convert(appEntity, appBean);
        if (!appService.updateById(appEntity)) {
            throw FailCode.APP_UPDATE_FAIL.getOperateException();
        }
        return ObjectUtils.convert(new AppDto(), appEntity);
    }

    @ApiOperation(value = "删除应用", notes = "删除当前用户的应用")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
    })
    @DeleteMapping("/current_user/app/{appId}")
    @Authority
    public void deleteCurrentUserApp(TokenModel tokenModel, @PathVariable String appId) {
        AppEntity appEntity = appService.verifyUserHaveApp(appId, tokenModel, "deleteCurrentUserApp");
        appService.deleteApp(appEntity);
    }

    @ApiOperation(value = "获取应用秘钥", notes = "获取当前用户应用的秘钥")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @GetMapping("/current_user/app/{appId}/secret")
    @Authority
    public AppSecretDto getAppSecret(TokenModel tokenModel, @PathVariable String appId) {
        AppEntity appEntity = appService.verifyUserHaveApp(appId, tokenModel, "getAppSecret");
        AppSecretDto appSecretDto = new AppSecretDto();
        appSecretDto.setAppSecret(appEntity.getAppSecret());
        return appSecretDto;
    }

    @ApiOperation(value = "更新应用秘钥", notes = "更新当前用户应用的秘钥")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @PutMapping("/current_user/app/{appId}/secret")
    @Authority
    public AppSecretDto updateAppSecret(TokenModel tokenModel, @PathVariable String appId) {
        AppEntity appEntity = appService.verifyUserHaveApp(appId, tokenModel, "updateAppSecret");
        appEntity.setAppSecret(Md5Utils.getSaltMd5(StringUtils.random(10)));
        if (!appService.updateById(appEntity)) {
            throw FailCode.APP_UPDATE_FAIL.getOperateException();
        }
        AppSecretDto appSecretDto = new AppSecretDto();
        appSecretDto.setAppSecret(appEntity.getAppSecret());
        return appSecretDto;
    }

    @ApiOperation(value = "获取当前用户的应用集合")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @GetMapping("/current_user/app_list")
    @Authority
    public List<AppDto> getCurrentUserAppList(TokenModel tokenModel, @ModelAttribute QueryBean queryBean, @ModelAttribute InfoBean infoBean) {
        return ObjectUtils.convertList(appService.getUserAppList(queryBean, tokenModel, ObjectUtils.convert(new AppEntity(), infoBean)), AppDto::new);
    }

    @ApiOperation(value = "获取应用集合")
    @GetMapping("/app_list")
    @Authority
    public List<DictDto> getAppList() {
        AppEntity app = new AppEntity();
        app.setStatus(EffectStatus.VALID.getStatus());
        app.setAuditStatus(AuditStatus.REVIEWED.getStatus());
        List<AppEntity> appEntities = appService.list(new QueryWrapper<>(app));
        List<DictDto> dtoList = new ArrayList<>();
        for (AppEntity appEntity : appEntities) {
            DictDto dictDto = new DictDto();
            dictDto.setName(appEntity.getAppName());
            dictDto.setValue(appEntity.getId());
            dtoList.add(dictDto);
        }
        return dtoList;
    }

    @ApiOperation(value = "应用刷新", notes = "应用刷新")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
    })
    @PutMapping("/current_user/app_refresh/{appId}")
    @Authority
    public void updateCurrentUserAppRefresh(TokenModel tokenModel, @PathVariable String appId) {
        AppEntity appEntity = appService.verifyUserHaveApp(appId, tokenModel, "updateCurrentUserAppRefresh");
        if (appEntity.getRefreshUrl() != null) {
            String[] urls = appEntity.getRefreshUrl().split(",");
            for (String url : urls) {
                SignBean signBean = new SignBean(appEntity.getAppId(), appEntity.getAppSecret(), BasicRestCode.install.getCode());
                url = StringUtils.analysisParam(url, signBean);
                HttpResponse httpResponse = HttpUtil.createGet(url).execute();
                if (!httpResponse.isOk()) {
                    if (httpResponse.getStatus() == 400) {
                        String json = httpResponse.body();
                        ResultException resultException = JSONUtils.getObjectByString(json, ResultException.class);
                        throw new OperateException(resultException.getCode(), resultException.getMessage());
                    } else {
                        throw FailCode.APP_REFRESH_FAIL.getOperateException(httpResponse.body());
                    }
                }
            }
        }
    }
}
