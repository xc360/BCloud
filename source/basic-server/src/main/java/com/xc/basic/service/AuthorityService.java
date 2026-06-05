package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.basic.dto.AuthorityDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.AuthorityBean;
import com.xc.basic.bean.QueryAuthorityBean;
import com.xc.basic.dto.AuthorizeDto;
import com.xc.basic.entity.AuthorityEntity;
import com.xc.basic.entity.UserEntity;

import java.util.List;

/**
 * <p>权限服务接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface AuthorityService extends IService<AuthorityEntity> {

    /**
     * 获取权限分页数据
     *
     * @param current            当前页
     * @param pagingBean         分页信息
     * @param queryAuthorityBean 权限参数
     * @return 分页信息及角色数据
     */
    public PagingDto<AuthorityDto> getAppAuthorityPage(String appId, Integer current, PagingBean pagingBean, QueryAuthorityBean queryAuthorityBean);

    /**
     * 获取应用的用户的有效权限集合
     *
     * @param appId      应用主键id
     * @param userEntity 用户信息
     * @return 权限集合
     */
    public List<AuthorityEntity> getAppUserValidAuthorityList(String appId, UserEntity userEntity);


    /**
     * 获取应用的，用户的，组的，有效权限集合
     *
     * @param appId      应用主键id
     * @param userEntity 用户信息
     * @param groupIds   组id集合
     * @return 权限集合
     */
    public List<AuthorityEntity> getAppUserGroupValidAuthorityList(String appId, UserEntity userEntity, List<String> groupIds);


    /**
     * 获取应用的有效权限集合
     *
     * @param id 主键id
     * @return 权限集合
     */
    public List<AuthorityEntity> getAppValidAuthorityList(String id, String appId, String type);

    /**
     * 删除权限
     *
     * @param authorityEntity 条件
     */
    public void deleteAuthority(AuthorityEntity authorityEntity);

    /**
     * 创建应用权限集合
     *
     * @param id             应用id
     * @param authorityBeans 权限集合
     * @return 返回权限集合
     */
    public List<AuthorityDto> createAppAuthorityList(String id, List<AuthorityBean> authorityBeans);

    /**
     * 获取权限集合
     *
     * @param appId              应用主键
     * @param queryAuthorityBean 权限查询条件
     * @return 权限集合
     */
    public List<AuthorityEntity> getAppAuthorityList(String appId, QueryBean queryBean, QueryAuthorityBean queryAuthorityBean);

    /**
     * 获取应用权限分页数据
     *
     * @param current         当前页
     * @param pagingBean      分页信息
     * @param authorityEntity 查询条件
     * @return 分页数据
     */
    public PagingDto<AuthorityEntity> getAppAuthorityPage(Integer current, PagingBean pagingBean, List<String> types, AuthorityEntity authorityEntity);

    /**
     * 获取子集的所有权限
     *
     * @param appId 应用主键
     * @param node  父节点
     * @return 权限集合
     */
    public List<AuthorityEntity> getChildrenAuthority(String appId, String node);

    /**
     * 上下移动
     *
     * @param entity 权限实体
     * @param isUp   是否向上
     */
    public boolean move(AuthorityEntity entity, boolean isUp);
}
