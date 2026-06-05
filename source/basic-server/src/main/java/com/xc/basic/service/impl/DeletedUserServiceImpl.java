package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.basic.dto.DeletedUserDto;
import com.xc.basic.entity.DeletedUserEntity;
import com.xc.basic.mapper.DeletedUserMapper;
import com.xc.basic.service.AppService;
import com.xc.basic.service.DeletedUserService;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>已删除用户Service实现类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
public class DeletedUserServiceImpl extends ServiceImpl<DeletedUserMapper, DeletedUserEntity> implements DeletedUserService {
    @Autowired
    private AppService appService;
    @Autowired
    private DeletedUserMapper deletedUserMapper;

    @Override
    public List<DeletedUserDto> getAppDeletedUserList(String appId) {
        List<String> userIds = appService.getAuthorizeAppUserIds(appId, null);
        if (userIds.size() > 0) {
            QueryWrapper<DeletedUserEntity> queryWrapper = new QueryWrapper<>(new DeletedUserEntity());
            queryWrapper.lambda().in(DeletedUserEntity::getUserId, userIds);
            List<DeletedUserEntity> deletedUserEntities = deletedUserMapper.selectList(queryWrapper);
            return ObjectUtils.convertList(deletedUserEntities, DeletedUserDto::new);
        }
        return new ArrayList<>();
    }

    @Override
    public List<DeletedUserDto> getAppDeletedUserList() {
        List<DeletedUserEntity> deletedUserEntities = deletedUserMapper.selectList(new QueryWrapper<>());
        return ObjectUtils.convertList(deletedUserEntities, DeletedUserDto::new);
    }
}
