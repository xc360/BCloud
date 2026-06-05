package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.basic.dto.AppDto;
import com.xc.api.basic.dto.UserAppDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.dto.AuditAppDto;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.UserAuthorizeEntity;
import com.xc.core.model.TokenModel;

import java.util.List;


/**
 * <p>应用程序服务类</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface AppService extends IService<AppEntity> {

    /**
     * 获取当前用户拥有的应用分页数据
     *
     * @param current    当前页
     * @param tokenModel token信息
     * @param pagingBean 分页信息
     * @param appEntity  查询条件
     * @return 分页数据
     */
    public PagingDto<AppDto> getUserAppPage(Integer current, TokenModel tokenModel, PagingBean pagingBean, AppEntity appEntity);

    /**
     * 审核应用分页数据
     *
     * @param current    当前页
     * @param pagingBean 分页信息
     * @param appEntity  查询条件
     * @return 分页数据
     */
    public PagingDto<AppDto> getAuditAppPage(Integer current, PagingBean pagingBean, AppEntity appEntity);

    /**
     * 分页查询显示的应用
     *
     * @param current    当前第几页
     * @param pagingBean 分页条件
     * @param appEntity  查询条件
     * @return 分页数据
     */
    public PagingDto<AppDto> getOvertAppPage(Integer current, PagingBean pagingBean, AppEntity appEntity);

    /**
     * 根据应用ID获取有效的应用信息
     *
     * @param appId 应用ID
     * @return 应用信息
     */
    public AppEntity getValidAppByAppId(String appId);

    /**
     * 根据应用ID获取已审核(默认有效)的应用信息
     *
     * @param appId 应用ID
     * @return 应用信息
     */
    public AppEntity getReviewedAppByAppId(String appId);

    /**
     * 删除应用id
     *
     * @param appEntity 应用信息
     */
    public void deleteApp(AppEntity appEntity);

    /**
     * 根据用户主键删除应用
     *
     * @param userId 用户主键
     */
    public void deleteAppByUserId(String userId);

    /**
     * 获取应用集合
     *
     * @param queryBean 基础条件
     * @param appEntity 查询条件
     * @return 应用集合
     */
    public List<AppEntity> getAppList(QueryBean queryBean, AppEntity appEntity);

    /**
     * 获取用户的应用集合
     *
     * @param queryBean  基础条件
     * @param tokenModel token信息
     * @param appEntity  查询条件
     * @return 应用集合
     */
    public List<AppEntity> getUserAppList(QueryBean queryBean, TokenModel tokenModel, AppEntity appEntity);

    /**
     * 验证用户是否拥有该应用
     *
     * @param appId         应用id
     * @param tokenModel    token信息
     * @param authorityCode 权限code
     * @return 应用实体
     */
    public AppEntity verifyUserHaveApp(String appId, TokenModel tokenModel, String authorityCode);

    /**
     * 验证应用是否公开应用或者验证用户是否拥有该应用
     *
     * @param appId  应用id
     * @param userId 用户主键
     * @return 应用实体
     */
    public AppEntity verifyAppIsOpen(String appId, String userId);

    /**
     * 验证应用秘钥
     *
     * @param appId 应用的appId
     * @return 应用信息
     */
    public AppEntity verifyAppSecret(String appId, String appSecret);

    /**
     * 获取给应用授权了的用户主键集合
     *
     * @param appId 应用主键
     * @return 用户主键集合
     */
    public List<String> getAuthorizeAppUserIds(String appId, List<String> userIds);

    /**
     * 获取给应用授权了的用户主键集合
     *
     * @param appId  应用主键
     * @param userId 用户主键
     * @return 用户主键集合
     */
    public UserAuthorizeEntity getAppUser(String appId, String userId);

    /**
     * 审核应用详情
     *
     * @param appId 应用主键
     * @return 审核应用详情
     */
    public AuditAppDto getAuditApp(String appId);

    /**
     * 获取用户的应用信息集合
     *
     * @param userId    用户主键
     * @param appEntity 查询条件
     * @return 分页数据
     */
    public List<UserAppDto> getAppListByUserId(String userId, AppEntity appEntity, String versionType);

    /**
     * 验证应用ip
     *
     * @param ip    IP地址
     * @param appIp 应用IP地址
     */
    public void verifyAppIp(String ip, String appIp);

    /**
     * 验证应用域名
     *
     * @param domain    域名
     * @param appDomain 应用域名
     */
    public void verifyAppDomain(String domain, String appDomain);
}
