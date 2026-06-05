package com.xc.basic.web.rest.user;

import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.xc.core.bean.PagingBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.QueryAuthorizeBean;
import com.xc.basic.bean.UserAuthorizeBean;
import com.xc.basic.dto.UserAuthorizeAuthorityDto;
import com.xc.basic.dto.UserAuthorizeDto;
import com.xc.basic.entity.UserAuthorizeAuthorityEntity;
import com.xc.basic.entity.UserAuthorizeEntity;
import com.xc.basic.enums.FailCode;
import com.xc.basic.mapper.UserAuthorizeAuthorityMapper;
import com.xc.basic.mapper.UserAuthorizeMapper;
import com.xc.basic.service.UserAuthorizeService;
import com.xc.core.annotation.Authority;
import com.xc.core.model.TokenModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户授权rest
 * </p>
 *
 * @author xc
 * @since 2023-08-01
 */
@Api(tags = {"需要登录权限接口，用户授权"})
@RestController
public class UserAuthorizeRest {
    @Autowired
    private UserAuthorizeMapper userAuthorizeMapper;
    @Autowired
    private UserAuthorizeAuthorityMapper userAuthorizeAuthorityMapper;
    @Autowired
    private UserAuthorizeService userAuthorizeService;

    @ApiOperation(value = "用户授权分页", notes = "用户授权分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true),
    })
    @GetMapping("/current_user/authorize_page/{current}")
    @Authority
    public PagingDto<UserAuthorizeDto> getCurrentUserAuthorizePage(TokenModel tokenModel, @PathVariable Integer current, @ModelAttribute PagingBean pagingBean, @ModelAttribute UserAuthorizeBean userAuthorizeBean) {
        return userAuthorizeService.getUserAuthorizePage(tokenModel.getUserId(), current, pagingBean, userAuthorizeBean);
    }

    @ApiOperation(value = "用户取消授权", notes = "用户取消授权")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "用户授权主键", name = "userAuthorizeId", paramType = "path", required = true),
    })
    @DeleteMapping("/current_user/authorize/{userAuthorizeId}")
    @Authority
    public void deleteCurrentUserAuthorize(TokenModel tokenModel, @PathVariable String userAuthorizeId) {
        UserAuthorizeEntity userAuthorizeEntity = userAuthorizeMapper.selectById(userAuthorizeId);
        if (userAuthorizeEntity == null) {
            throw FailCode.USER_AUTHORIZE_ID_ERROR.getOperateException();
        }
        if (!tokenModel.getUserId().equals(userAuthorizeEntity.getUserId())) {
            throw FailCode.USER_AUTHORIZE_USER_ID_ERROR.getOperateException();
        }
        if (!SqlHelper.retBool(userAuthorizeMapper.deleteById(userAuthorizeId))) {
            throw FailCode.USER_AUTHORIZE_DELETE_FAIL.getOperateException();
        }
    }

    @ApiOperation(value = "用户授权权限分页", notes = "用户授权权限分页")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "当前页", name = "current", paramType = "path", required = true),
            @ApiImplicitParam(value = "用户授权主键", name = "userAuthorizeId", paramType = "path", required = true)
    })
    @GetMapping("/current_user/authorize/{userAuthorizeId}/authority_page/{current}")
    @Authority
    public PagingDto<UserAuthorizeAuthorityDto> getCurrentUserAuthorizeAuthorityPage(TokenModel tokenModel, @PathVariable String userAuthorizeId, @PathVariable Integer current,
                                                                                     @ModelAttribute PagingBean pagingBean, @ModelAttribute QueryAuthorizeBean queryAuthorizeBean) {
        return userAuthorizeService.getUserAuthorizeAuthorityPage(tokenModel.getUserId(), current, userAuthorizeId, pagingBean, queryAuthorizeBean);
    }

    @ApiOperation(value = "用户取消权限授权", notes = "用户取消权限授权")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户token", name = "token", paramType = "header", required = true),
            @ApiImplicitParam(value = "用户授权主键", name = "userAuthorizeId", paramType = "path", required = true)
    })
    @DeleteMapping("/current_user/authorize/{userAuthorizeId}/authority/{userAuthorizeAuthorityId}")
    @Authority
    public void deleteCurrentUserAuthorizeAuthority(TokenModel tokenModel, @PathVariable String userAuthorizeId, @PathVariable String userAuthorizeAuthorityId) {
        UserAuthorizeEntity userAuthorizeEntity = userAuthorizeMapper.selectById(userAuthorizeId);
        if (userAuthorizeEntity == null) {
            throw FailCode.USER_AUTHORIZE_ID_ERROR.getOperateException();
        }
        if (!tokenModel.getUserId().equals(userAuthorizeEntity.getUserId())) {
            throw FailCode.USER_AUTHORIZE_USER_ID_ERROR.getOperateException();
        }
        UserAuthorizeAuthorityEntity userAuthorizeAuthorityEntity = userAuthorizeAuthorityMapper.selectById(userAuthorizeAuthorityId);
        if (userAuthorizeAuthorityEntity == null) {
            throw FailCode.USER_AUTHORIZE_AUTHORITY_ID_ERROR.getOperateException();
        }
        if (!userAuthorizeEntity.getId().equals(userAuthorizeAuthorityEntity.getUserAuthorizeId())) {
            throw FailCode.USER_AUTHORIZE_AUTHORITY_USER_AUTHORIZE_ID_ERROR.getOperateException();
        }
        if (!SqlHelper.retBool(userAuthorizeAuthorityMapper.deleteById(userAuthorizeAuthorityId))) {
            throw FailCode.USER_AUTHORIZE_AUTHORITY_DELETE_FAIL.getOperateException();
        }
    }
}
