package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.basic.dto.DeletedUserDto;
import com.xc.basic.entity.DeletedUserEntity;

import java.util.List;

/**
 * <p>已删除用户Service</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface DeletedUserService extends IService<DeletedUserEntity> {
    /**
     * 获取已删除用户集合
     *
     * @param appId 应用主键
     * @return 已删除用户集合
     */
    public List<DeletedUserDto> getAppDeletedUserList(String appId);

    /**
     * 获取已删除用户集合
     *
     * @return 已删除用户集合
     */
    public List<DeletedUserDto> getAppDeletedUserList();
}
