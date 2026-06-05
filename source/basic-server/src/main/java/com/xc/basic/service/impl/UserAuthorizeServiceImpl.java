package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.basic.dto.AuthorityDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.QueryAuthorizeBean;
import com.xc.basic.bean.UserAuthorizeBean;
import com.xc.basic.dto.UserAuthorizeAuthorityDto;
import com.xc.basic.dto.UserAuthorizeDto;
import com.xc.basic.entity.*;
import com.xc.basic.mapper.*;
import com.xc.basic.service.AppService;
import com.xc.basic.service.UserAuthorizeService;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户授权ServiceImpl
 * </p>
 *
 * @author xc
 * @since 2023-08-01
 */
@Service
public class UserAuthorizeServiceImpl extends ServiceImpl<UserAuthorizeMapper, UserAuthorizeEntity> implements UserAuthorizeService {
    @Autowired
    private UserAuthorizeMapper userAuthorizeMapper;
    @Autowired
    private AppService appService;
    @Autowired
    private UserAuthorizeAuthorityMapper userAuthorizeAuthorityMapper;
    @Autowired
    private AuthorityMapper authorityMapper;

    @Override
    public PagingDto<UserAuthorizeDto> getUserAuthorizePage(String userId, Integer current, PagingBean pagingBean, UserAuthorizeBean userAuthorizeBean) {
        UserAuthorizeEntity userAuthorizeEntity = new UserAuthorizeEntity();
        userAuthorizeEntity.setUserId(userId);
        List<UserAuthorizeEntity> appAuthorityEntities = userAuthorizeMapper.selectList(new QueryWrapper<>(userAuthorizeEntity));
        // 查询应用
        AppEntity appEntity = new AppEntity();
        appEntity.setAppName(userAuthorizeBean.getAppName());
        QueryWrapper<AppEntity> queryWrapper = ServiceUtils.queryLike(appEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        if (appAuthorityEntities.size() > 0) {
            List<String> appIds = appAuthorityEntities.stream().map(UserAuthorizeEntity::getAppId).collect(Collectors.toList());
            queryWrapper.lambda().in(AppEntity::getId, appIds);
            IPage<AppEntity> iPage = appService.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
            // 封装数据
            List<UserAuthorizeDto> appUsers = new ArrayList<>();
            for (UserAuthorizeEntity userAuthorize : appAuthorityEntities) {
                for (AppEntity app : iPage.getRecords()) {
                    if (userAuthorize.getAppId().equals(app.getId())) {
                        UserAuthorizeDto userAuthorizeDto = new UserAuthorizeDto();
                        userAuthorizeDto.setId(userAuthorize.getId());
                        userAuthorizeDto.setAppName(app.getAppName());
                        userAuthorizeDto.setAuthorizeTime(userAuthorize.getAuthorizeTime());
                        appUsers.add(userAuthorizeDto);
                    }
                }
            }
            return new PagingDto<>(iPage.getTotal(), appUsers);
        }
        return new PagingDto<>(0L, new ArrayList<>());
    }

    @Override
    public PagingDto<UserAuthorizeAuthorityDto> getUserAuthorizeAuthorityPage(String userId, Integer current, String userAuthorizeId, PagingBean pagingBean, QueryAuthorizeBean queryAuthorizeBean) {
        // 查询用户授权权限
        UserAuthorizeAuthorityEntity userAuthorizeAuthorityEntity = new UserAuthorizeAuthorityEntity();
        userAuthorizeAuthorityEntity.setUserAuthorizeId(userAuthorizeId);
        List<UserAuthorizeAuthorityEntity> userAuthorizeAuthorityEntities = userAuthorizeAuthorityMapper.selectList(new QueryWrapper<>(userAuthorizeAuthorityEntity));
        List<String> authorityIds = userAuthorizeAuthorityEntities.stream().map(UserAuthorizeAuthorityEntity::getAuthorityId).collect(Collectors.toList());
        if (authorityIds.size() == 0) {
            return new PagingDto<>(0L, new ArrayList<>());
        }
        // 查询权限
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setName(queryAuthorizeBean.getName());
        authorityEntity.setCode(queryAuthorizeBean.getCode());
        QueryWrapper<AuthorityEntity> queryWrapper = ServiceUtils.queryLike(authorityEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        queryWrapper.lambda().in(AuthorityEntity::getId, authorityIds);
        IPage<AuthorityEntity> iPage = authorityMapper.selectPage(new Page<>(current, pagingBean.getSize()), queryWrapper);
        // 数据处理
        List<UserAuthorizeAuthorityDto> userAuthorizeAuthorityList = new ArrayList<>();
        for (UserAuthorizeAuthorityEntity authorizeAuthorityEntity : userAuthorizeAuthorityEntities) {
            UserAuthorizeAuthorityDto userAuthorizeAuthorityDto = ObjectUtils.convert(new UserAuthorizeAuthorityDto(), authorizeAuthorityEntity);
            for (AuthorityEntity authorityDto : iPage.getRecords()) {
                if (authorityDto.getId().equals(authorizeAuthorityEntity.getAuthorityId())) {
                    userAuthorizeAuthorityDto.setName(authorityDto.getName());
                    userAuthorizeAuthorityDto.setCode(authorityDto.getCode());
                    userAuthorizeAuthorityDto.setAppId(authorityDto.getAppId());
                    userAuthorizeAuthorityDto.setStatus(authorityDto.getStatus());
                }
            }
            userAuthorizeAuthorityList.add(userAuthorizeAuthorityDto);
        }
        return new PagingDto<>(iPage.getTotal(), userAuthorizeAuthorityList);
    }

}
