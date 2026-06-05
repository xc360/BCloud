package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.file.bean.DiskSignBean;
import com.xc.api.file.config.FileConstants;
import com.xc.api.file.enums.FileRestCode;
import com.xc.api.file.FileApi;
import com.xc.basic.bean.UserInfoBean;
import com.xc.basic.dto.UserInfoDto;
import com.xc.basic.entity.*;
import com.xc.basic.enums.FailCode;
import com.xc.basic.mapper.*;
import com.xc.basic.service.AppService;
import com.xc.basic.service.UserInfoService;
import com.xc.api.file.utils.FileUrlUtils;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>用户信息服务实现类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoEntity> implements UserInfoService {

    @Autowired
    private FileApi fileApi;
    @Autowired
    private FileConstants fileConstants;
    @Autowired
    private UserAuthorizeMapper userAuthorizeMapper;
    @Autowired
    private UserAuthorizeAuthorityMapper userAuthorizeAuthorityMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserGroupMapper userGroupMapper;
    @Autowired
    private AppletMapper appletMapper;
    @Autowired
    private AppService appService;

    @Override
    public UserInfoDto updateUserInfo(String userId, UserInfoBean userInfoBean) {
        UserInfoEntity userInfoEntity = getUserInfoByUserId(userId);
        // 保存头像图片
        DiskSignBean diskSignBean = new DiskSignBean(fileConstants.getDiskNo(), fileConstants.getDiskSecret(), FileRestCode.updateOpenDiskFileStatus.getCode());
        if (userInfoEntity == null) {
            // 设置文件为有效
            String templateUrl = FileUrlUtils.urlTurnTemplate(userInfoBean.getPortrait());
            FileUrlUtils.confirmFile(fileApi, diskSignBean, null, templateUrl);
            userInfoBean.setPortrait(templateUrl);
            // 保存用户信息
            userInfoEntity = new UserInfoEntity();
            userInfoEntity.setUserId(userId);
            ObjectUtils.convert(userInfoEntity, userInfoBean);
            if (!save(userInfoEntity)) {
                throw FailCode.USER_INFO_CREATE_FAIL.getOperateException();
            }
        } else {
            String templateUrl = FileUrlUtils.urlTurnTemplate(userInfoBean.getPortrait());
            FileUrlUtils.confirmFile(fileApi, diskSignBean, userInfoEntity.getPortrait(), templateUrl);
            userInfoBean.setPortrait(templateUrl);
            // 修改用户信息
            ObjectUtils.convert(userInfoEntity, userInfoBean);
            if (!updateById(userInfoEntity)) {
                throw FailCode.USER_INFO_UPDATE_FAIL.getOperateException();
            }
        }
        return ObjectUtils.convert(new UserInfoDto(), userInfoEntity);
    }

    @Override
    public List<UserInfoDto> getAppUserInfoList(String appId, List<String> userIds) {
        UserAuthorizeEntity userAuthorizeEntity = new UserAuthorizeEntity();
        userAuthorizeEntity.setAppId(appId);
        QueryWrapper<UserAuthorizeEntity> wrapper = new QueryWrapper<>(userAuthorizeEntity);
        if (userIds != null) {
            wrapper.lambda().in(UserAuthorizeEntity::getUserId, userIds);
        }
        List<UserAuthorizeEntity> appUserEntities = userAuthorizeMapper.selectList(wrapper);
        List<String> ids = appUserEntities.stream().map(UserAuthorizeEntity::getUserId).collect(Collectors.toList());
        if (ids.size() > 0) {
            QueryWrapper<UserInfoEntity> queryWrapper = new QueryWrapper<>(new UserInfoEntity());
            queryWrapper.lambda().in(UserInfoEntity::getUserId, ids);
            List<UserInfoEntity> userEntities = this.list(queryWrapper);
            return ObjectUtils.convertList(userEntities, UserInfoDto::new);
        }
        return new ArrayList<>();
    }

    @Override
    public UserInfoEntity getUserInfoByUserId(String userId) {
        UserInfoEntity userInfo = new UserInfoEntity();
        userInfo.setUserId(userId);
        return this.getOne(new QueryWrapper<>(userInfo));
    }

    @Override
    @Transactional
    public void deleteUserInfoByUserId(String userId) {
        // 删除用户信息
        UserInfoEntity entity = new UserInfoEntity();
        entity.setUserId(userId);
        this.remove(new QueryWrapper<>(entity));
        // 删除用户与应用的关联
        UserAuthorizeEntity userAuthorizeEntity = new UserAuthorizeEntity();
        userAuthorizeEntity.setUserId(userId);
        QueryWrapper<UserAuthorizeEntity> queryWrapper = new QueryWrapper<>(userAuthorizeEntity);
        List<UserAuthorizeEntity> userAuthorizeEntities = userAuthorizeMapper.selectList(queryWrapper);
        userAuthorizeMapper.delete(new QueryWrapper<>(userAuthorizeEntity));
        // 删除用户授权的权限
        for (UserAuthorizeEntity userAuthorize : userAuthorizeEntities) {
            UserAuthorizeAuthorityEntity userAuthorizeAuthorityEntity = new UserAuthorizeAuthorityEntity();
            userAuthorizeAuthorityEntity.setUserAuthorizeId(userAuthorize.getId());
            userAuthorizeAuthorityMapper.delete(new QueryWrapper<>(userAuthorizeAuthorityEntity));
        }
        // 删除用户跟角色关联
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setUserId(userId);
        userRoleMapper.delete(new QueryWrapper<>(userRoleEntity));
        // 删除用户与用户组的关联
        UserGroupEntity userGroupEntity = new UserGroupEntity();
        userGroupEntity.setUserId(userId);
        userGroupMapper.delete(new QueryWrapper<>(userGroupEntity));
        // 删除用户小程序
        AppletEntity appletEntity = new AppletEntity();
        appletEntity.setUserId(userId);
        appletMapper.delete(new QueryWrapper<>(appletEntity));
        // 删除应用信息
        appService.deleteAppByUserId(userId);
    }
}
