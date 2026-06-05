package com.xc.basic.web.rest.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xc.api.basic.dto.VersionDto;
import com.xc.api.file.FileApi;
import com.xc.api.file.bean.DiskSignBean;
import com.xc.api.file.config.FileConstants;
import com.xc.api.file.enums.FileRestCode;
import com.xc.api.file.utils.FileUrlUtils;
import com.xc.basic.bean.VersionBean;
import com.xc.basic.config.Constants;
import com.xc.basic.entity.VersionEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.service.AppService;
import com.xc.basic.service.VersionService;
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>需要登录权限接口，应用版本</p>
 *
 * @author xc
 * @version v1.0.0
 */
@RestController
@Api(tags = {"需要登录权限接口，应用版本"})
public class VersionRest {

    @Autowired
    private VersionService versionService;
    @Autowired
    private AppService appService;
    @Autowired
    private FileConstants fileConstants;
    @Autowired
    private Constants constants;
    @Autowired
    private FileApi fileApi;

    @ApiOperation(value = "应用版本分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true),
    })
    @GetMapping("/app/{appId}/version_page/{current}")
    @Authority
    public PagingDto<VersionDto> getAppVersionPage(TokenModel tokenModel, @PathVariable Integer current,
                                                   @PathVariable String appId, @ModelAttribute PagingBean pagingBean,
                                                   @ModelAttribute VersionBean versionBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "getAppVersionPage");
        VersionEntity versionEntity = ObjectUtils.convert(new VersionEntity(), versionBean);
        versionEntity.setAppId(appId);
        PagingDto<VersionDto> pagingDto = versionService.getVersionPage(current, pagingBean, versionEntity);
        for (VersionDto versionDto : pagingDto.getResData()) {
            versionDto.setPackageUrl(FileUrlUtils.templateTurnUrl(versionDto.getPackageUrl()));
        }
        return pagingDto;
    }

    @ApiOperation(value = "查询应用版本")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用版本主键", name = "versionId", paramType = "path", required = true)
    })
    @GetMapping("/app/{appId}/version/{versionId}")
    @Authority
    public VersionDto getAppVersion(TokenModel tokenModel, @PathVariable String appId, @PathVariable String versionId) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "getAppVersion");
        VersionEntity versionEntity = verifyUpdateDelete(appId, versionId);
        versionEntity.setDocContent(FileUrlUtils.templateTurnUrl(versionEntity.getDocContent()));
        return ObjectUtils.convert(new VersionDto(), versionEntity);
    }

    @ApiOperation(value = "创建应用版本")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
    })
    @PostMapping("/app/{appId}/version")
    @Authority
    public VersionDto createAppVersion(TokenModel tokenModel, @PathVariable String appId, @RequestBody VersionBean versionBean) {
        appService.verifyUserHaveApp(appId, tokenModel, "createAppVersion");
        // 确认文件
        versionBean.setDocContent(versionService.contentFileHandle(versionBean.getDocContent(), null));
        versionBean.setUserAgreement(versionService.contentFileHandle(versionBean.getUserAgreement(), null));
        versionBean.setPrivacyAgreement(versionService.contentFileHandle(versionBean.getPrivacyAgreement(), null));
        // 确认安装包文件
        DiskSignBean diskSignBean = new DiskSignBean(fileConstants.getDiskNo(), fileConstants.getDiskSecret(), FileRestCode.updateOpenDiskFileStatus.getCode());
        versionBean.setPackageUrl(FileUrlUtils.urlTurnTemplate(versionBean.getPackageUrl()));
        FileUrlUtils.confirmFile(fileApi, diskSignBean, null, versionBean.getPackageUrl());
        // 保存数据
        VersionEntity versionEntity = ObjectUtils.convert(new VersionEntity(), versionBean);
        versionEntity.setAppId(appId);
        // 初始化排序字段
        if (versionBean.getSeq() == null) {
            VersionEntity entity = new VersionEntity();
            entity.setAppId(appId);
            entity.setType(versionBean.getType());
            versionEntity.setSeq(versionService.count(new QueryWrapper<>(entity)) + 1);
        }
        try {
            if (!versionService.save(versionEntity)) {
                throw FailCode.APP_VERSION_CREATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.APP_VERSION_REPEAT.getOperateException();
        }
        versionEntity.setDocContent(FileUrlUtils.templateTurnUrl(versionEntity.getDocContent()));
        return ObjectUtils.convert(new VersionDto(), versionEntity);
    }

    @ApiOperation(value = "修改应用版本")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用版本主键", name = "versionId", paramType = "path", required = true),
    })
    @PutMapping("/app/{appId}/version/{versionId}")
    @Authority
    public VersionDto updateAppVersion(TokenModel tokenModel, @PathVariable String appId,
                                       @PathVariable String versionId, @RequestBody VersionBean versionBean) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppVersion");
        VersionEntity versionEntity = verifyUpdateDelete(appId, versionId);
        // 确认文件
        versionBean.setDocContent(versionService.contentFileHandle(versionBean.getDocContent(), versionEntity.getDocContent()));
        versionBean.setUserAgreement(versionService.contentFileHandle(versionBean.getUserAgreement(), versionEntity.getUpdateContent()));
        versionBean.setPrivacyAgreement(versionService.contentFileHandle(versionBean.getPrivacyAgreement(), versionEntity.getPrivacyAgreement()));
        // 确认安装包文件
        DiskSignBean diskSignBean = new DiskSignBean(fileConstants.getDiskNo(), fileConstants.getDiskSecret(), FileRestCode.updateOpenDiskFileStatus.getCode());
        versionBean.setPackageUrl(FileUrlUtils.urlTurnTemplate(versionBean.getPackageUrl()));
        FileUrlUtils.confirmFile(fileApi, diskSignBean, versionEntity.getPackageUrl(), versionBean.getPackageUrl());
        // 保存
        try {
            ObjectUtils.convert(versionEntity, versionBean);
            if (!versionService.updateById(versionEntity)) {
                throw FailCode.APP_VERSION_UPDATE_FAIL.getOperateException();
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.APP_VERSION_REPEAT.getOperateException();
        }
        versionEntity.setDocContent(FileUrlUtils.templateTurnUrl(versionEntity.getDocContent()));
        return ObjectUtils.convert(new VersionDto(), versionEntity);
    }

    /**
     * 编辑删除验证
     *
     * @param appId     应用id
     * @param versionId 应用版本id
     */
    private VersionEntity verifyUpdateDelete(String appId, String versionId) {
        VersionEntity versionEntity = versionService.getById(versionId);
        if (versionEntity == null) {
            throw FailCode.APP_VERSION_ID_ERROR.getOperateException();
        }
        if (!versionEntity.getAppId().equals(appId)) {
            throw FailCode.APP_VERSION_APP_ID_ERROR.getOperateException();
        }
        return versionEntity;
    }

    @ApiOperation(value = "删除应用版本")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用版本主键", name = "versionId", paramType = "path", required = true),
    })
    @DeleteMapping("/app/{appId}/version/{versionId}")
    @Authority
    public void deleteAppVersion(TokenModel tokenModel, @PathVariable String appId, @PathVariable String versionId) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "deleteAppVersion");
        VersionEntity versionEntity = verifyUpdateDelete(appId, versionId);
        // 删除文件
        DiskSignBean diskSignBean = new DiskSignBean(fileConstants.getDiskNo(), fileConstants.getDiskSecret(), FileRestCode.updateOpenDiskFileStatus.getCode());
        // 获取所有模板url
        List<String> fileUrls = FileUrlUtils.getMidValue(versionEntity.getDocContent(), FileUrlUtils.templateKey, constants.getArticleFileSuffixArray());
        fileUrls.addAll(FileUrlUtils.getMidValue(versionEntity.getUserAgreement(), FileUrlUtils.templateKey, constants.getArticleFileSuffixArray()));
        fileUrls.addAll(FileUrlUtils.getMidValue(versionEntity.getPrivacyAgreement(), FileUrlUtils.templateKey, constants.getArticleFileSuffixArray()));
        fileUrls.add(versionEntity.getPackageUrl());
        FileUrlUtils.confirmFile(fileApi, diskSignBean, fileUrls, new ArrayList<>());
        if (!versionService.removeById(versionId)) {
            throw FailCode.APP_VERSION_DELETE_FAIL.getOperateException();
        }
    }

    @ApiOperation(value = "上移版本")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "版本主键", name = "versionId", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/version/{versionId}/up")
    @Authority
    public void updateAppVersionUp(TokenModel tokenModel, @PathVariable String appId, @PathVariable String versionId) {
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppVersionUp");
        VersionEntity versionEntity = verifyUpdateDelete(appId, versionId);
        if (!versionService.move(versionEntity, false)) {
            throw FailCode.ALREADY_THE_FIRST_LINE.getOperateException();
        }
    }

    @ApiOperation(value = "下移版本")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "版本主键", name = "versionId", paramType = "path", required = true),
            @ApiImplicitParam(value = "应用主键", name = "appId", paramType = "path", required = true)
    })
    @PutMapping("/app/{appId}/version/{versionId}/down")
    @Authority
    public void updateAppVersionDown(TokenModel tokenModel, @PathVariable String appId, @PathVariable String versionId) {
        // 验证用户有没有这个应用的操作权限
        appService.verifyUserHaveApp(appId, tokenModel, "updateAppVersionDown");
        VersionEntity versionEntity = verifyUpdateDelete(appId, versionId);
        if (!versionService.move(versionEntity, true)) {
            throw FailCode.ALREADY_THE_LAST_LINE.getOperateException();
        }
    }
}
