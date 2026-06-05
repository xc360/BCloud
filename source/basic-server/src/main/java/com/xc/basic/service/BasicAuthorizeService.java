package com.xc.basic.service;

import cn.hutool.json.JSONObject;
import com.xc.api.basic.bean.*;
import com.xc.api.basic.dto.LoginDto;
import com.xc.api.basic.dto.SignDto;
import com.xc.api.basic.dto.UserSignDto;
import com.xc.api.basic.dto.VerifyAccountDto;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.UserEntity;
import com.xc.basic.model.CodeModel;
import com.xc.core.bean.SignBean;
import com.xc.core.model.TokenModel;

import java.util.List;
import java.util.Map;

/**
 * <p>授权接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface BasicAuthorizeService {
    /**
     * 登录，获取token
     *
     * @param loginBean   登录参数
     * @param openCaptcha 打开验证码，true：打开，false：关闭
     * @return 返回code信息
     */
    public LoginDto login(AppEntity appEntity, LoginBean loginBean, boolean openCaptcha);

    /**
     * 获取code信息
     *
     * @param code code
     * @return code信息
     */
    public CodeModel getCode(String code);

    /**
     * 获取token令牌信息
     *
     * @param code        用户code
     * @param accessToken 用户token
     * @return token令牌信息
     */
    public TokenModel getToken(AppEntity appEntity, String code, String accessToken);

    /**
     * 获取token令牌信息
     *
     * @param appEntity  应用信息
     * @param userEntity 用户信息
     * @return token令牌信息
     */
    public TokenModel getToken(AppEntity appEntity, UserEntity userEntity);

    /**
     * 刷新应用TOKEN的权限
     *
     * @param appEntity 应用实体
     */
    public void updateAppTokenAuthority(AppEntity appEntity);

    /**
     * 刷新用户授权
     *
     * @param appEntity 应用实体
     */
    public void updateAppUserAuthorize(AppEntity appEntity);

    /**
     * 创建应用用户权限关联
     *
     * @param appEntity    应用信息
     * @param userId       用户主键
     * @param authorityIds 权限主键集合
     */
    public void createAppUserAuthority(AppEntity appEntity, String userId, List<String> authorityIds);

    /**
     * 更新token
     *
     * @param refreshToken 用户token
     * @return token信息
     */
    public TokenModel updateToken(String refreshToken);

    /**
     * 删除token信息
     *
     * @param appId  应用主键
     * @param userId 用户主键
     */
    public void deleteUserToken(String appId, String userId);

    /**
     * 删除token信息
     *
     * @param userId 用户主键
     */
    public void deleteUserToken(String userId);

    /**
     * 删除token信息
     *
     * @param token 用户token
     */
    public void deleteToken(String token);

    /**
     * 删除token信息
     *
     * @param tokenModel token信息
     */
    public void deleteToken(TokenModel tokenModel);

    /**
     * 注册
     *
     * @param registerBean 注册参数信息
     */
    public void register(RegisterBean registerBean);


    /**
     * 小程序登录接口
     *
     * @param appEntity  应用信息
     * @param appletBean 登录参数
     * @return 登录成功code信息
     */
    public TokenModel createAppletToken(AppEntity appEntity, AppletBean appletBean);

    /**
     * 找回密码
     *
     * @param forgetBean 找回密码需要参数
     */
    public void forgetPassword(ForgetBean forgetBean);

    /**
     * 验证账号
     *
     * @param accountType 账号类型
     * @param account     账号
     */
    public VerifyAccountDto verifyAccount(String accountType, String account);


    /**
     * 验证签名
     *
     * @param signBean 应用签名参数
     * @return 应用信息
     */
    public AppEntity verifySign(SignBean signBean);

    /**
     * 验证签名
     *
     * @param signBean 应用签名参数
     * @return 应用信息
     */
    public AppEntity verifySign(SignBean signBean, SignDto<JSONObject> signDto);

    /**
     * 验证用户签名
     *
     * @param userSignBean 用户签名参数
     * @return 用户签名信息
     */
    public UserEntity verifyUserSign(UserSignBean userSignBean, UserSignDto<JSONObject> userSignDto);

    /**
     * 获取上传文件签名
     *
     * @return 上传文件签名
     */
    public Map<String, Object> getUploadFileSign(String userId);
}
