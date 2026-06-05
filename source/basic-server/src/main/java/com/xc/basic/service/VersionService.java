package com.xc.basic.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.basic.dto.VersionDto;
import com.xc.basic.entity.DataTypeEntity;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.api.basic.dto.UserVersionDto;
import com.xc.basic.entity.VersionEntity;

import java.util.List;

/**
 * <p>应用版本服务接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface VersionService extends IService<VersionEntity> {
    /**
     * 获取应用版本分页数据
     *
     * @param current       当前页
     * @param pagingBean    分页信息
     * @param versionEntity 应用版本参数
     * @return 分页信息及应用信息数据
     */
    public PagingDto<VersionDto> getVersionPage(Integer current, PagingBean pagingBean, VersionEntity versionEntity);

    /**
     * 公开版本分页
     *
     * @param current       当前第几页
     * @param pagingBean    分页基础参数信息
     * @param versionEntity 查询条件
     * @return 版分页数据
     */
    public PagingDto<UserVersionDto> getOvertVersionPage(Integer current, PagingBean pagingBean, VersionEntity versionEntity);

    /**
     * 获取公开版本集合
     *
     * @param versionEntity 查询条件
     * @return 版本集合数据
     */
    public List<UserVersionDto> getOvertVersionList(VersionEntity versionEntity);

    /**
     * 获取最新文档
     *
     * @param appId   应用ID
     * @param version 可以为空
     * @return 最新文档
     */
    public VersionDto getNewestVersion(String appId, String type, String version);

    /**
     * 获取版本集合
     *
     * @param queryBean     查询信息
     * @param versionEntity 查询参数
     * @return 版本集合
     */
    public List<VersionDto> getVersionList(QueryBean queryBean, VersionEntity versionEntity);

    /**
     * 添加版本的查询返回字段
     *
     * @param queryWrapper 版本的queryWrapper
     */
    public void addVersionSelect(QueryWrapper<VersionEntity> queryWrapper, boolean isContent);

    /**
     * 上下移动
     *
     * @param entity 版本实体
     * @param isUp   是否向上
     */
    public boolean move(VersionEntity entity, boolean isUp);

    /**
     * 内容文件处理
     *
     * @param newContent 新的内容
     * @param oldContent 旧的内容
     * @return 处理后的内容
     */
    public String contentFileHandle(String newContent, String oldContent);
}
