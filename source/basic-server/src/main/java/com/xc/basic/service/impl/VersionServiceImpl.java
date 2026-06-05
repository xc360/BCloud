package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.basic.dto.UserVersionDto;
import com.xc.api.basic.dto.VersionDto;
import com.xc.api.file.FileApi;
import com.xc.api.file.bean.DiskSignBean;
import com.xc.api.file.config.FileConstants;
import com.xc.api.file.enums.FileRestCode;
import com.xc.api.file.utils.FileUrlUtils;
import com.xc.basic.config.Constants;
import com.xc.basic.entity.VersionEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.mapper.VersionMapper;
import com.xc.basic.service.VersionService;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.enums.EffectStatus;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>应用版本服务实现类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
public class VersionServiceImpl extends ServiceImpl<VersionMapper, VersionEntity> implements VersionService {
    @Autowired
    private FileConstants fileConstants;
    @Autowired
    private Constants constants;
    @Autowired
    private FileApi fileApi;

    @Override
    public PagingDto<VersionDto> getVersionPage(Integer current, PagingBean pagingBean, VersionEntity versionEntity) {
        QueryWrapper<VersionEntity> queryWrapper = ServiceUtils.queryData(pagingBean, versionEntity);
        addVersionSelect(queryWrapper, false);
        IPage<VersionEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        return new PagingDto<>(iPage.getTotal(), ObjectUtils.convertList(iPage.getRecords(), VersionDto::new));
    }

    @Override
    public PagingDto<UserVersionDto> getOvertVersionPage(Integer current, PagingBean pagingBean, VersionEntity versionEntity) {
        QueryWrapper<VersionEntity> queryWrapper = ServiceUtils.queryData(pagingBean, versionEntity);
        addVersionSelect(queryWrapper, false);
        IPage<VersionEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        return new PagingDto<>(iPage.getTotal(), ObjectUtils.convertList(iPage.getRecords(), UserVersionDto::new));
    }

    @Override
    public List<UserVersionDto> getOvertVersionList(VersionEntity versionEntity) {
        QueryWrapper<VersionEntity> queryWrapper = new QueryWrapper<>(versionEntity);
        addVersionSelect(queryWrapper, false);
        List<VersionEntity> entities = this.list(queryWrapper);
        return ObjectUtils.convertList(entities, UserVersionDto::new);
    }

    @Override
    public VersionDto getNewestVersion(String appId, String type, String version) {
        QueryBean queryBean = new QueryBean();
        queryBean.setSortField("seq");
        queryBean.setSortRule("DESCEND");
        VersionEntity versionEntity = new VersionEntity();
        versionEntity.setAppId(appId);
        versionEntity.setStatus(EffectStatus.VALID.getStatus());
        versionEntity.setType(type);
        if (version != null) {
            versionEntity.setAppVersion(version);
        }
        QueryWrapper<VersionEntity> queryWrapper = ServiceUtils.querySort(new QueryWrapper<>(versionEntity), queryBean);
        IPage<VersionEntity> iPage = this.page(new Page<>(1, 1), queryWrapper);
        if (iPage.getRecords().size() > 0) {
            VersionDto versionDto = ObjectUtils.convert(new VersionDto(), iPage.getRecords().get(0));
            versionDto.setPackageUrl(FileUrlUtils.templateTurnUrl(versionDto.getPackageUrl()));
            return versionDto;
        }
        return null;
    }

    @Override
    public List<VersionDto> getVersionList(QueryBean queryBean, VersionEntity versionEntity) {
        QueryWrapper<VersionEntity> queryWrapper = new QueryWrapper<>(versionEntity);
        ServiceUtils.querySort(queryWrapper, queryBean);
        addVersionSelect(queryWrapper, false);
        List<VersionEntity> columnEntities = this.list(queryWrapper);
        return ObjectUtils.convertList(columnEntities, VersionDto::new);
    }

    @Override
    public void addVersionSelect(QueryWrapper<VersionEntity> queryWrapper, boolean isContent) {
        if (isContent) {
            queryWrapper.lambda().select(
                    VersionEntity::getId,
                    VersionEntity::getType,
                    VersionEntity::getAppVersion,
                    VersionEntity::getPackageUrl,
                    VersionEntity::getDocContent,
                    VersionEntity::getUserAgreement,
                    VersionEntity::getPrivacyAgreement,
                    VersionEntity::getUpdateContent,
                    VersionEntity::getForceUpgrade,
                    VersionEntity::getSeq,
                    VersionEntity::getAppId,
                    VersionEntity::getStatus,
                    VersionEntity::getCreateTime,
                    VersionEntity::getUpdateTime,
                    VersionEntity::getVersion
            );
        } else {
            queryWrapper.lambda().select(
                    VersionEntity::getId,
                    VersionEntity::getType,
                    VersionEntity::getAppVersion,
                    VersionEntity::getPackageUrl,
                    VersionEntity::getUpdateContent,
                    VersionEntity::getForceUpgrade,
                    VersionEntity::getSeq,
                    VersionEntity::getAppId,
                    VersionEntity::getStatus,
                    VersionEntity::getCreateTime,
                    VersionEntity::getUpdateTime,
                    VersionEntity::getVersion
            );
        }
    }

    @Override
    @Transactional
    public boolean move(VersionEntity entity, boolean isUp) {
        // 查询需要处理的权限
        VersionEntity versionEntity = new VersionEntity();
        versionEntity.setAppId(entity.getAppId());
        versionEntity.setType(entity.getType());
        QueryWrapper<VersionEntity> queryWrapper = new QueryWrapper<>(versionEntity);
        queryWrapper.lambda().orderByAsc(VersionEntity::getSeq);
        List<VersionEntity> entities = this.list(queryWrapper);
        Set<Long> entitySet = entities.stream().map(VersionEntity::getSeq).collect(Collectors.toSet());
        if (entitySet.size() != entities.size() || entities.get(entities.size() - 1).getSeq() != entities.size()) {
            for (int i = 0; i < entities.size(); i++) {
                VersionEntity version = entities.get(i);
                if (version.getSeq() != i + 1) {
                    version.setSeq(i + 1L);
                    this.updateById(version);
                }
            }
        }
        for (int i = 0; i < entities.size(); i++) {
            VersionEntity version = entities.get(i);
            if (entity.getId().equals(version.getId())) {
                VersionEntity version1;
                if (isUp) {
                    if (entities.size() <= i + 1) {
                        return false;
                    }
                    version1 = entities.get(i + 1);
                } else {
                    if (i - 1 < 0) {
                        return false;
                    }
                    version1 = entities.get(i - 1);
                }
                Long seq = version1.getSeq();
                version1.setSeq(version.getSeq());
                this.updateById(version1);
                // 修改原位置
                version.setSeq(seq);
                this.updateById(version);
            }
        }
        return true;
    }

    @Override
    public String contentFileHandle(String newContent, String oldContent) {
        // 确认文件
        DiskSignBean diskSignBean = new DiskSignBean(fileConstants.getDiskNo(), fileConstants.getDiskSecret(), FileRestCode.updateOpenDiskFileStatus.getCode());
        List<String> newFileUrls = FileUrlUtils.getMidValue(newContent, fileConstants.getFileUrl(), constants.getArticleFileSuffixArray());
        if (newFileUrls.size() > constants.getArticleFileMaxNum()) {
            throw FailCode.ARTICLE_FILE_NUM_ACHIEVE_UPPER_LIMIT.getOperateException();
        }
        for (String newFileUrl : newFileUrls) {
            String templateUrl = FileUrlUtils.templateTurnUrl(newFileUrl);
            newContent = newContent.replace(newFileUrl, templateUrl);
        }
        newFileUrls = newFileUrls.stream().map(FileUrlUtils::urlTurnTemplate).collect(Collectors.toList());
        List<String> oldFileUrls = new ArrayList<>();
        if (oldContent != null) {
            oldFileUrls = FileUrlUtils.getMidValue(oldContent, FileUrlUtils.templateKey, constants.getArticleFileSuffixArray());
        }
        FileUrlUtils.confirmFile(fileApi, diskSignBean, oldFileUrls, newFileUrls);
        return newContent;
    }

}
