package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.basic.bean.UserInfoBean;
import com.xc.basic.dto.UserInfoDto;
import com.xc.basic.entity.UserInfoEntity;

import java.util.List;

/**
 * <p>用户信息服务类</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface UserInfoService extends IService<UserInfoEntity> {
    /**
     * 修改用户信息
     *
     * @param userId       用户id
     * @param userInfoBean 用户参数
     * @return 用户信息dto
     */
    public UserInfoDto updateUserInfo(String userId, UserInfoBean userInfoBean);

    /**
     * 获取用户信息集合
     *
     * @param appId 应用主键
     * @return 用户信息集合
     */
    public List<UserInfoDto> getAppUserInfoList(String appId, List<String> userIds);

    /**
     * 用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    public UserInfoEntity getUserInfoByUserId(String userId);

    /**
     * 根据用户主键删除应用
     *
     * @param userId 用户主键
     */
    public void deleteUserInfoByUserId(String userId);
}
