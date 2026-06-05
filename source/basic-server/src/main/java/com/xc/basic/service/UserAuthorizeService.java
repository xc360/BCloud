package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.basic.bean.QueryAuthorizeBean;
import com.xc.basic.bean.UserAuthorizeBean;
import com.xc.basic.dto.UserAuthorizeAuthorityDto;
import com.xc.basic.dto.UserAuthorizeDto;
import com.xc.basic.entity.UserAuthorizeEntity;
import com.xc.core.bean.PagingBean;
import com.xc.core.dto.PagingDto;

/**
 * <p>
 * 用户授权Service
 * </p>
 *
 * @author xc
 * @since 2023-08-01
 */
public interface UserAuthorizeService extends IService<UserAuthorizeEntity> {

    /**
     * 获取用户授权分页
     *
     * @param userId            用户主键
     * @param current           当前页
     * @param pagingBean        分页信息
     * @param userAuthorizeBean 查询条件
     * @return 分页数据
     */
    public PagingDto<UserAuthorizeDto> getUserAuthorizePage(String userId, Integer current, PagingBean pagingBean, UserAuthorizeBean userAuthorizeBean);

    /**
     * 获取用户授权的权限分页
     *
     * @param userId             用户主键
     * @param current            当前页
     * @param userAuthorizeId    用户授权主键
     * @param pagingBean         分页信息
     * @param queryAuthorizeBean 查询条件
     * @return 分页数据
     */
    public PagingDto<UserAuthorizeAuthorityDto> getUserAuthorizeAuthorityPage(String userId, Integer current, String userAuthorizeId, PagingBean pagingBean, QueryAuthorizeBean queryAuthorizeBean);
}
