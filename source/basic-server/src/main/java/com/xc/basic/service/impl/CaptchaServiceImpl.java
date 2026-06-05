package com.xc.basic.service.impl;

import com.xc.api.basic.bean.CaptchaBean;
import com.xc.api.basic.bean.MessageSendBean;
import com.xc.api.basic.dto.CaptchaDto;
import com.xc.core.dto.ImageCaptchaDto;
import com.xc.basic.config.Constants;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.UserEntity;
import com.xc.basic.enums.AccountType;
import com.xc.basic.enums.FailCode;
import com.xc.basic.enums.MessageCode;
import com.xc.basic.model.CaptchaFrequentModel;
import com.xc.basic.service.CaptchaService;
import com.xc.basic.service.MessageService;
import com.xc.basic.service.UserService;
import com.xc.basic.enums.RedisPrefix;
import com.xc.basic.enums.RedisTime;
import com.xc.core.enums.Whether;
import com.xc.core.utils.RedisUtils;
import com.xc.core.utils.VerifyCodeUtils;
import com.xc.tool.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.TreeMap;
import java.util.UUID;

/**
 * <p>验证码服务实现类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Slf4j
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private MessageService messageService;
    @Autowired
    private Constants constants;
    @Lazy
    @Autowired
    private UserService userService;

    @Override
    public void sendCaptcha(AppEntity appEntity, String account, String key, String messageCode, String accountType) {
        // key设置默认值
        if (key == null || "".equals(key)) {
            key = messageCode + account;
        }
        // 发送验证码
        if (AccountType.EMAIL.getType().equals(accountType)) {
            sendEmailCaptcha(appEntity, account, key, messageCode);
        } else if (AccountType.PHONE.getType().equals(accountType)) {
            sendPhoneCaptcha(appEntity, account, key, messageCode);
        } else {
            throw FailCode.USER_ACCOUNT_TYPE_ERROR.getOperateException();
        }
    }

    @Override
    public String getCaptchaAccount(String userId, String accountType) {
        UserEntity userEntity = userService.getById(userId);
        if (AccountType.EMAIL.getType().equals(accountType)) {
            if (userEntity.getEmail() != null && !userEntity.getEmail().equals("")) {
                return userEntity.getEmail();
            }
            throw FailCode.USER_MAIL_NOT_EXIST.getOperateException();
        } else if (AccountType.PHONE.getType().equals(accountType)) {
            if (userEntity.getPhone() != null && !"".equals(userEntity.getPhone())) {
                return userEntity.getPhone();
            }
            throw FailCode.USER_PHONE_NOT_EXIST.getOperateException();
        } else {
            throw FailCode.USER_ACCOUNT_TYPE_ERROR.getOperateException();
        }
    }

    @Override
    public boolean verifyCaptcha(String key, String captcha) {
        return this.verifyCaptcha(key, captcha, true);
    }

    @Override
    public boolean verifyCaptcha(String key, String captcha, boolean openError) {
        this.verifyCaptchaNum(key, true);
        return this.verifyCaptcha(key, captcha, openError, true);
    }

    @Override
    public boolean verifyCaptcha(String key, String captcha, boolean openError, boolean isDelete) {
        String code = RedisUtils.get(RedisPrefix.CAPTCHA.getKey() + key);
        if (captcha == null || !captcha.equalsIgnoreCase(code)) {
            log.info("redis的验证码：{}", code);
            if (openError) {
                throw FailCode.CAPTCHA_ERROR.getOperateException();
            } else {
                return false;
            }
        }
        if (isDelete) {
            RedisUtils.delete(RedisPrefix.CAPTCHA.getKey() + key);
        }
        return true;
    }

    @Override
    public boolean verifyCaptchaNum(String key, boolean openError) {
        // 验证流程是否正常
        String numKey = RedisPrefix.VERIFY_NUM.getKey() + key;
        Integer num = RedisUtils.get(numKey);
        if (num == null) {
            num = 0;
        }
        if (num < 5) {
            num++;
            RedisUtils.set(numKey, num, RedisTime.CAPTCHA_EXPIRY.getTime());
        } else {
            RedisUtils.delete(numKey);
            RedisUtils.delete(RedisPrefix.CAPTCHA.getKey() + key);
            if (openError) {
                throw FailCode.CAPTCHA_VERIFY_NUM_TOO_MUCH.getOperateException();
            }
            return false;
        }
        return true;
    }

    @Override
    public CaptchaDto verifyFrequent(CaptchaFrequentModel captchaFrequentModel, CaptchaDto captchaDto) {
        String clientIp = captchaFrequentModel.getClientIp();
        String messageCode = captchaFrequentModel.getMessageCode();
        String account = captchaFrequentModel.getAccount();
        String captcha = captchaFrequentModel.getCaptcha();
        String code = captchaFrequentModel.getCode();
        String captchaKey = RedisPrefix.CAPTCHA_NUM.getKey() + messageCode + account;
        // 频繁发送验证
        String data = RedisUtils.get(captchaKey);
        if (data == null) {
            data = captchaKey;
            RedisUtils.set(captchaKey, data, RedisTime.CAPTCHA_SEND_INTERVAL.getTime());
        } else {
            throw FailCode.CAPTCHA_SEND_SENDING_TOO_OFTEN.getOperateException();
        }
        // 验证流程是否正常
        String key = RedisPrefix.CAPTCHA_IP.getKey() + messageCode + clientIp;
        Integer num = RedisUtils.get(key);
        if (num == null) {
            num = 0;
        }
        if (num < 3) {
            num++;
            RedisUtils.set(key, num, RedisTime.CAPTCHA_IP_EXPIRY.getTime());
        } else {
            // 验证数字验证码
            if (!this.verifyCaptchaNum(key, false)) {
                // 数值验证码错误不验证其频繁发送
                RedisUtils.delete(captchaKey);
                captchaDto.setImgCaptchaInvalid(Whether.YES.getValue());
            }
            if (!verifyCaptcha(code, captcha, false, true)) {
                // 数值验证码错误不验证其频繁发送
                RedisUtils.delete(captchaKey);
                captchaDto.setImgCaptchaError(Whether.YES.getValue());
            }
            RedisUtils.delete(key);
        }

        return captchaDto;
    }

    @Override
    public CaptchaDto createUserCaptcha(AppEntity appEntity, String userId, String messageCode, String accountType) {
        String account = this.getCaptchaAccount(userId, accountType);
        // 验证账号是否正确
        if (AccountType.EMAIL.getType().equals(accountType)) {
            if (!account.contains(".") || !account.contains("@")) {
                throw FailCode.USER_EMAIL_ERROR.getOperateException();
            }
        } else if (AccountType.PHONE.getType().equals(accountType)) {
            if (account.length() != 11 || !account.startsWith("1")) {
                throw FailCode.USER_PHONE_ERROR.getOperateException();
            }
        } else {
            throw FailCode.USER_ACCOUNT_TYPE_ERROR.getOperateException();
        }
        // 防止频繁发送
        CaptchaFrequentModel captchaFrequentModel = new CaptchaFrequentModel();
        captchaFrequentModel.setAccount(account);
        captchaFrequentModel.setMessageCode(messageCode);
        CaptchaDto captchaDto = this.verifyFrequent(captchaFrequentModel, new CaptchaDto());
        // 发送验证码
        this.sendCaptcha(appEntity, account, messageCode + userId, messageCode, accountType);
        return captchaDto;
    }

    @Override
    public void getUserCaptcha(String userId, String messageCode, String captcha, String isDelete) {
        String key = messageCode + userId;
        this.verifyCaptchaNum(key, true);
        this.verifyCaptcha(key, captcha, true, Whether.YES.getValue().equals(isDelete));
    }

    @Override
    public void verifyUserExist(String messageCode, String account) {
        if (MessageCode.EMAIL_REGISTER.getCode().equals(messageCode) ||
                MessageCode.PHONE_REGISTER.getCode().equals(messageCode) ||
                MessageCode.PHONE_UPDATE.getCode().equals(messageCode) ||
                MessageCode.EMAIL_UPDATE.getCode().equals(messageCode)) {
            // 验证用户手机是否存在，存在则报已存在
            UserEntity userEntity = userService.getUserByEmailOrPhone(account);
            if (userEntity != null) {
                throw FailCode.USER_EXIST.getOperateException();
            }
        } else if (MessageCode.EMAIL_FORGET_PASSWORD.getCode().equals(messageCode) ||
                MessageCode.PHONE_FORGET_PASSWORD.getCode().equals(messageCode)) {
            // 验证账户是否存在，不存在报不存在
            UserEntity userEntity = userService.getUserByEmailOrPhone(account);
            if (userEntity == null) {
                throw FailCode.USER_ACCOUNT_ERROR.getOperateException();
            }
        }
    }

    @Override
    public CaptchaDto createCaptcha(AppEntity appEntity, String messageCode, String accountType, CaptchaBean captchaBean) {
        CaptchaDto captchaDto = new CaptchaDto();
        String clientIp = captchaBean.getClientIp();
        if (clientIp == null) {
            throw FailCode.CAPTCHA_CLIENT_IP_NOT_NULL.getOperateException();
        }
        String account = captchaBean.getAccount();
        if (account == null || account.equals("")) {
            throw FailCode.USER_ACCOUNT_NOT_NULL.getOperateException();
        }
        // 验证账号是否正确
        if (AccountType.EMAIL.getType().equals(accountType)) {
            if (!account.contains(".") || !account.contains("@")) {
                throw FailCode.USER_EMAIL_ERROR.getOperateException();
            }
        } else if (AccountType.PHONE.getType().equals(accountType)) {
            if (account.length() != 11 || !account.startsWith("1")) {
                throw FailCode.USER_PHONE_ERROR.getOperateException();
            }
        } else {
            throw FailCode.USER_ACCOUNT_TYPE_ERROR.getOperateException();
        }
        verifyUserExist(messageCode, account);
        // 封装防止频繁发送对象
        CaptchaFrequentModel captchaFrequentModel = new CaptchaFrequentModel();
        captchaFrequentModel.setAccount(account);
        captchaFrequentModel.setMessageCode(messageCode);
        captchaFrequentModel.setClientIp(clientIp);
        captchaFrequentModel.setCaptcha(captchaBean.getCaptcha());
        captchaFrequentModel.setCode(captchaBean.getCode());
        // 验证频繁
        captchaDto = this.verifyFrequent(captchaFrequentModel, captchaDto);
        // 发送验证码
        this.sendCaptcha(appEntity, account, null, messageCode, accountType);
        return captchaDto;
    }

    @Override
    public void getCaptcha(String account, String messageCode, String captcha, String isDelete) {
        String key = messageCode + account;
        this.verifyCaptchaNum(key, true);
        this.verifyCaptcha(key, captcha, true, Whether.YES.getValue().equals(isDelete));
    }

    @Override
    public ImageCaptchaDto getImgCaptcha(String captcha) {
        if (captcha == null) {
            captcha = VerifyCodeUtils.generateVerifyCode(4);
        }
        String code = UUID.randomUUID().toString();
        RedisUtils.set(RedisPrefix.CAPTCHA.getKey() + code, captcha, RedisTime.CAPTCHA_EXPIRY.getTime());
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            VerifyCodeUtils.outputImage(200, 80, byteArrayOutputStream, captcha);
            return new ImageCaptchaDto(code, byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw FailCode.CAPTCHA_GAIN_FAIL.getOperateException();
    }

    /**
     * 发送邮箱验证码
     */
    private void sendEmailCaptcha(AppEntity appEntity, String account, String key, String messageCode) {
        // 存入redis
        String code = StringUtils.secureRandomNum(6);
        RedisUtils.set(RedisPrefix.CAPTCHA.getKey() + key, code, RedisTime.CAPTCHA_EXPIRY.getTime());
        // 发送验证码
        MessageSendBean messageSendBean = new MessageSendBean();
        messageSendBean.setAccount(account);
        messageSendBean.setCode(messageCode);
        if (constants.getOpenEmailCaptcha()) {
            TreeMap<String, String> data = new TreeMap<>();
            data.put("code", code);
            messageSendBean.setData(data);
            messageService.sendMessage(appEntity, messageSendBean);
        } else {
            log.info("邮箱验证码为：{}", code);
        }
    }

    /**
     * 发送手机验证码
     */
    private void sendPhoneCaptcha(AppEntity appEntity, String account, String key, String messageCode) {
        // 存入redis
        String code = StringUtils.secureRandomNum(6);
        RedisUtils.set(RedisPrefix.CAPTCHA.getKey() + key, code, RedisTime.CAPTCHA_EXPIRY.getTime());
        // 发送验证码
        MessageSendBean messageSendBean = new MessageSendBean();
        messageSendBean.setAccount(account);
        messageSendBean.setCode(messageCode);
        if (constants.getOpenPhoneCaptcha()) {
            TreeMap<String, String> data = new TreeMap<>();
            data.put("code", code);
            messageSendBean.setData(data);
            messageService.sendMessage(appEntity, messageSendBean);
        } else {
            log.info("手机验证码为：{}", code);
        }
    }
}
