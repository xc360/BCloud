package com.xc.basic.service;


import com.xc.api.basic.bean.CaptchaBean;
import com.xc.api.basic.dto.CaptchaDto;
import com.xc.core.dto.ImageCaptchaDto;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.model.CaptchaFrequentModel;

/**
 * <p>验证码服务接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface CaptchaService {
    /**
     * 发送验证码
     *
     * @param appEntity   应用信息
     * @param account     账号
     * @param key         存储的key
     * @param messageCode 消息标识
     * @param accountType 账号类型
     */
    public void sendCaptcha(AppEntity appEntity, String account, String key, String messageCode, String accountType);

    /**
     * 验证码处理
     */
    public String getCaptchaAccount(String userId, String accountType);

    /**
     * 验证验证码
     *
     * @param key     获取code的key
     * @param captcha 验证码
     */
    public boolean verifyCaptcha(String key, String captcha);

    /**
     * 验证验证码
     *
     * @param key       获取code的key
     * @param captcha   验证码
     * @param openError 开启错误，为true直接报异常，为false，返回true/false
     * @return 返回是否通过，true：通过，false：未通过
     */
    public boolean verifyCaptcha(String key, String captcha, boolean openError);

    /**
     * 验证验证码
     *
     * @param key       获取code的key
     * @param captcha   验证码
     * @param openError 开启错误，为true直接报异常，为false，返回true/false
     * @param isDelete  是否验证通过立即删除验证码，为true立即删除，false：不删除，等待过期，验证超过五次
     * @return 返回是否通过，true：通过，false：未通过
     */
    public boolean verifyCaptcha(String key, String captcha, boolean openError, boolean isDelete);

    /**
     * 验证验证码验证次数
     *
     * @param key       获取code的key
     * @param openError 开启错误，为true直接报异常，为false，返回true/false
     * @return 返回是否通过，true：通过，false：未通过
     */
    public boolean verifyCaptchaNum(String key, boolean openError);

    /**
     * 验证是否频繁操作，频繁操作报频繁操作
     *
     * @param captchaFrequentModel 验证频繁操作
     * @param captchaDto           验证码返回对象
     */
    public CaptchaDto verifyFrequent(CaptchaFrequentModel captchaFrequentModel, CaptchaDto captchaDto);

    /**
     * 创建用户验证码
     *
     * @param appEntity   应用信息
     * @param userId      用户主键
     * @param messageCode 消息标识
     * @param accountType 账号类型
     */
    public CaptchaDto createUserCaptcha(AppEntity appEntity, String userId, String messageCode, String accountType);

    /**
     * 获取用户验证码信息，验证验证码
     *
     * @param userId      用户主键
     * @param messageCode 消息标识
     * @param captcha     验证码
     * @param isDelete    是否删除验证码
     */
    public void getUserCaptcha(String userId, String messageCode, String captcha, String isDelete);

    /**
     * 验证用户手机是否存在，存在则报已存在
     * 验证账户是否存在，不存在报不存在
     *
     * @param messageCode 消息标识
     * @param account     账号
     */
    public void verifyUserExist(String messageCode, String account);

    /**
     * 创建验证码
     *
     * @param appEntity   应用信息
     * @param messageCode 消息标识
     * @param accountType 账号类型
     * @param captchaBean 验证码参数
     * @return 验证码信息
     */
    public CaptchaDto createCaptcha(AppEntity appEntity, String messageCode, String accountType, CaptchaBean captchaBean);

    /**
     * 获取验证码信息，验证验证码
     *
     * @param account     账号
     * @param messageCode 消息标识
     * @param captcha     验证码
     * @param isDelete    是否删除验证码
     */
    public void getCaptcha(String account, String messageCode, String captcha, String isDelete);

    /**
     * 获取图片验证码
     *
     * @param captcha 验证码
     * @return 图片和code
     */
    public ImageCaptchaDto getImgCaptcha(String captcha);
}
