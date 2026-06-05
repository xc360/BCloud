package com.xc.basic.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.sun.mail.util.MailSSLSocketFactory;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.xc.api.basic.bean.MessageAnalysisBean;
import com.xc.api.basic.bean.MessageSendBean;
import com.xc.api.basic.dto.ApiSupplierDto;
import com.xc.api.basic.dto.MessageAnalysisDto;
import com.xc.api.basic.dto.MessageTemplateDto;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.entity.MessageLogEntity;
import com.xc.basic.entity.UserEntity;
import com.xc.basic.enums.ApiSupplierCode;
import com.xc.basic.enums.FailCode;
import com.xc.basic.enums.MessageCode;
import com.xc.basic.model.AliCaptchaModel;
import com.xc.basic.service.*;
import com.xc.core.enums.EffectStatus;
import com.xc.core.enums.Whether;
import com.xc.tool.utils.JSONUtils;
import com.xc.tool.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeMap;

/**
 * <p>消息服务实现</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageTemplateService messageTemplateService;
    @Autowired
    private ApiSupplierService apiSupplierService;
    @Autowired
    private MessageLogService messageLogService;
    @Lazy
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void sendMessage(AppEntity appEntity, MessageSendBean messageSendBean) {
        MessageTemplateDto messageTemplateDto = messageTemplateService.getAppMessageTemplateByCode(appEntity.getId(), messageSendBean.getCode(), true);
        sendMessage(appEntity, messageSendBean.getAccount(), messageSendBean.getData(), messageTemplateDto, true);
    }

    @Override
    public void sendSystemErrorNoticeMessage(AppEntity appEntity, String message) {
        String appId = appEntity.getId();
        try {
            // 查询用户信息
            UserEntity userEntity = userService.getById(appEntity.getUserId());
            // 查询邮件模板
            if (userEntity.getEmail() != null && !"".equals(userEntity.getEmail())) {
                MessageTemplateDto emailTemplateDto = messageTemplateService.getAppMessageTemplateByCode(appId, MessageCode.EMAIL_EXCEPTION_NOTICE.getCode(), false);
                if (EffectStatus.VALID.getStatus().equals(emailTemplateDto.getStatus())) {
                    // 封装数据
                    TreeMap<String, String> data = new TreeMap<>();
                    data.put("message", message);
                    // 发送通知邮件
                    sendMessage(appEntity, userEntity.getEmail(), data, emailTemplateDto, false);
                }
            }
            // 查询手机模板
            if (userEntity.getPhone() != null && !"".equals(userEntity.getPhone())) {
                MessageTemplateDto phoneTemplateDto = messageTemplateService.getAppMessageTemplateByCode(appId, MessageCode.PHONE_EXCEPTION_NOTICE.getCode(), false);
                if (EffectStatus.VALID.getStatus().equals(phoneTemplateDto.getStatus())) {
                    // 封装数据
                    TreeMap<String, String> data = new TreeMap<>();
                    data.put("message", message);
                    // 发送通知邮件
                    sendMessage(appEntity, userEntity.getPhone(), data, phoneTemplateDto, false);
                }
            }
        } catch (Exception e) {
            log.error("发送系统异常通知失败！", e);
        }
    }

    @Override
    public MessageAnalysisDto createMessageAnalysis(String appId, MessageAnalysisBean messageAnalysisBean) {
        MessageTemplateDto messageTemplateDto = messageTemplateService.getAppMessageTemplateByCode(appId, messageAnalysisBean.getCode(), true);
        String context = messageAnalysis(messageTemplateDto.getTemplate(), messageAnalysisBean.getData());
        MessageAnalysisDto messageAnalysisDto = new MessageAnalysisDto();
        messageAnalysisDto.setContent(context);
        ObjectUtils.convert(messageAnalysisDto, messageTemplateDto);
        return messageAnalysisDto;
    }

    @Override
    public String messageAnalysis(String template, TreeMap<String, String> data) {
        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        Context context = new Context();
        context.setVariables(new HashMap<>(data));
        return springTemplateEngine.process(template, context);
    }

    /**
     * 发送消息
     *
     * @param appEntity          应用信息
     * @param account            账号
     * @param data               数据
     * @param messageTemplateDto 模板信息
     * @param openErrorNotice    是否开始错误通知
     */
    private void sendMessage(AppEntity appEntity, String account, TreeMap<String, String> data, MessageTemplateDto messageTemplateDto, boolean openErrorNotice) {
        MessageLogEntity messageLogEntity = new MessageLogEntity();
        messageLogEntity.setAccount(account);
        messageLogEntity.setAppId(messageTemplateDto.getAppId());
        messageLogEntity.setMessageTemplateCode(messageTemplateDto.getCode());
        messageLogEntity.setStatus(Whether.NO.getValue());
        String json = JSONUtils.getStringByObject(data);
        ApiSupplierDto apiSupplierDto = apiSupplierService.getAppApiSupplierByCode(appEntity.getId(), messageTemplateDto.getApiSupplierCode());
        if (ApiSupplierCode.ALI_SMS.getCode().equals(apiSupplierDto.getCode())) {
            messageLogEntity.setContent("模板Code:" + messageTemplateDto.getTemplateCode() + ",数据：" + json);
            messageLogService.save(messageLogEntity);
            aliSMS(appEntity, messageTemplateDto, apiSupplierDto, account, data, openErrorNotice);
        } else if (ApiSupplierCode.QQ_SMS.getCode().equals(apiSupplierDto.getCode())) {
            messageLogEntity.setContent("模板Code:" + messageTemplateDto.getTemplateCode() + ",数据：" + json);
            messageLogService.save(messageLogEntity);
            qqSMS(appEntity, messageTemplateDto, apiSupplierDto, account, data, openErrorNotice);
        } else if (ApiSupplierCode.QQ_MAIL.getCode().equals(apiSupplierDto.getCode())) {
            messageLogEntity.setContent("模板:" + messageTemplateDto.getTemplate() + ",数据：" + json);
            messageLogService.save(messageLogEntity);
            qqMail(appEntity, messageTemplateDto, apiSupplierDto, account, data, openErrorNotice);
        } else if (ApiSupplierCode.ZT_SMS.getCode().equals(apiSupplierDto.getCode())) {
            messageLogEntity.setContent("模板:" + messageTemplateDto.getTemplate() + ",数据：" + json);
            messageLogService.save(messageLogEntity);
            ztSMS(appEntity, messageTemplateDto, apiSupplierDto, account, data, openErrorNotice);
        } else {
            throw FailCode.API_SUPPLIER_CODE_ERROR.getOperateException();
        }
        messageLogEntity.setStatus(Whether.YES.getValue());
        messageLogService.updateById(messageLogEntity);
    }

    /**
     * 阿里短信
     *
     * @param appEntity          应用信息
     * @param messageTemplateDto 模板信息
     * @param apiSupplierDto     供应商
     * @param account            账号
     * @param data               数据
     * @param openErrorNotice    发送失败是否通知
     */
    private void aliSMS(AppEntity appEntity, MessageTemplateDto messageTemplateDto, ApiSupplierDto apiSupplierDto, String account, TreeMap<String, String> data, boolean openErrorNotice) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-chengdu", apiSupplierDto.getAccessId(), apiSupplierDto.getAccessSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("PhoneNumbers", account);
        request.putQueryParameter("SignName", messageTemplateDto.getSignName());
        request.putQueryParameter("TemplateCode", messageTemplateDto.getTemplateCode());
        request.putQueryParameter("TemplateParam", JSONUtils.getStringByObject(data));
        try {
            CommonResponse response = client.getCommonResponse(request);
            if (response.getHttpStatus() != 200) {
                log.error(response.getData());
                throw FailCode.USER_PHONE_CAPTCHA_CODE_SEND_FAIL.getOperateException();
            }
            AliCaptchaModel aliCaptchaModel = JSONUtils.getObjectByString(response.getData(), AliCaptchaModel.class);
            if (!aliCaptchaModel.getCode().equals("OK")) {
                throw FailCode.MESSAGE_SEND_FAIL.getOperateException(aliCaptchaModel.getMessage());
            }
        } catch (ClientException e) {
            String message = "阿里云短信发送失败，请检查是否缴费，异常信息：" + e.getMessage();
            log.error(message, e.getMessage(), e);
            if (openErrorNotice) {
                sendSystemErrorNoticeMessage(appEntity, message);
            }
            throw FailCode.MESSAGE_SEND_FAIL.getOperateException(e.getMessage());
        }
    }

    /**
     * 腾讯短信
     *
     * @param appEntity          应用信息
     * @param messageTemplateDto 模板信息
     * @param apiSupplierDto     供应商
     * @param account            账号
     * @param data               数据
     * @param openErrorNotice    发送失败是否通知
     */
    private void qqSMS(AppEntity appEntity, MessageTemplateDto messageTemplateDto, ApiSupplierDto apiSupplierDto, String account, TreeMap<String, String> data, boolean openErrorNotice) {
        try {
            Credential cred = new Credential(apiSupplierDto.getAccessId(), apiSupplierDto.getAccessSecret());
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, "ap-chengdu", clientProfile);
            // 模板
            SendSmsRequest req = new SendSmsRequest();
            req.setTemplateID(messageTemplateDto.getTemplateCode());
            if (messageTemplateDto.getOtherConfig() == null) {
                throw FailCode.MESSAGE_TEMPLATE_OTHER_CONFIG_NOT_NULL.getOperateException();
            }
            JSONObject map = JSONUtils.getObjectByString(messageTemplateDto.getOtherConfig(), JSONObject.class);
            if (map.get("SmsSdkAppid") != null) {
                throw FailCode.MESSAGE_TEMPLATE_LACK_SMS_SDK_APP_ID_ERROR.getOperateException();
            }
            req.setSmsSdkAppid(String.valueOf(map.get("SmsSdkAppid")));
            //电话号码
            String[] phoneNumberSet1 = {"86" + account};
            req.setPhoneNumberSet(phoneNumberSet1);
            req.setSign(messageTemplateDto.getSignName());
            // 模板参数
            req.setTemplateParamSet(data.values().toArray(new String[]{}));
            SendSmsResponse resp = client.SendSms(req);
            log.info(SendSmsResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            String message = "腾讯云短信发送失败，请检查是否缴费，异常信息：" + e.getMessage();
            log.error(message, e.getMessage(), e);
            if (openErrorNotice) {
                sendSystemErrorNoticeMessage(appEntity, message);
            }
            throw FailCode.MESSAGE_SEND_FAIL.getOperateException(e.getMessage());
        }
    }

    /**
     * 上海助通科技
     *
     * @param appEntity          应用信息
     * @param messageTemplateDto 模板信息
     * @param apiSupplierDto     供应商
     * @param account            账号
     * @param data               数据
     * @param openErrorNotice    发送失败是否通知
     */
    private void ztSMS(AppEntity appEntity, MessageTemplateDto messageTemplateDto, ApiSupplierDto apiSupplierDto, String account, TreeMap<String, String> data, boolean openErrorNotice) {
        try {
            String accessId = apiSupplierDto.getAccessId();
            String accessSecret = apiSupplierDto.getAccessSecret();
            String templateCode = messageTemplateDto.getTemplateCode();
            String signName = messageTemplateDto.getSignName();
            String urls = "https://api-shss.zthysms.com/v2/sendSmsTp";
            JSONObject requestJson = new JSONObject();
            requestJson.set("username", accessId);
            long tKey = System.currentTimeMillis() / 1000;
            requestJson.set("tKey", tKey);
            requestJson.set("password", SecureUtil.md5(SecureUtil.md5(accessSecret) + tKey));
            requestJson.set("tpId", templateCode);
            requestJson.set("signature", "【" + signName + "】");
            requestJson.set("ext", "");
            requestJson.set("extend", "");
            JSONArray records = new JSONArray();
            JSONObject record = new JSONObject();
            record.set("mobile", account);
            record.set("tpContent", data);
            records.add(record);
            requestJson.set("records", records);
            String result = HttpRequest.post(urls).body(JSONUtil.toJsonStr(requestJson)).execute().body();
            log.info(result);
        } catch (Exception e) {
            String message = "上海助通科技短信发送失败，请检查是否缴费，异常信息：" + e.getMessage();
            log.error(message, e.getMessage(), e);
            if (openErrorNotice) {
                sendSystemErrorNoticeMessage(appEntity, message);
            }
            throw FailCode.MESSAGE_SEND_FAIL.getOperateException(e.getMessage());
        }
    }

    /**
     * 腾讯邮箱
     *
     * @param appEntity          应用信息
     * @param messageTemplateDto 模板信息
     * @param apiSupplierDto     供应商
     * @param account            账号
     * @param data               数据
     * @param openErrorNotice    发送失败是否通知
     */
    private void qqMail(AppEntity appEntity, MessageTemplateDto messageTemplateDto, ApiSupplierDto apiSupplierDto, String account, TreeMap<String, String> data, boolean openErrorNotice) {
        //是否使用ssl加密,qq需要使用
        Properties properties = System.getProperties();
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", true);
            properties.put("mail.smtp.ssl.socketFactory", sf);

            //是否启用验证,true:启用,false:不启用,默认启用
            properties.put("mail.smtp.auth", true);
            //邮件服务器地址
            properties.setProperty("mail.smtp.host", "smtp.qq.com");

            //添加验证
            Session session = Session.getInstance(properties, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(apiSupplierDto.getAccessId(), apiSupplierDto.getAccessSecret()); //发件人邮件用户名、密码
                }
            });

            //创建消息
            MimeMessage message = new MimeMessage(session);
            //发件人地址
            message.setFrom(new InternetAddress(apiSupplierDto.getAccessId(), messageTemplateDto.getSignName()));
            //收件人地址
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(account));
            //标题Title
            message.setSubject(messageTemplateDto.getSignName());
            // 解析模板
            String content = messageTemplateDto.getTemplate();
            if (data != null) {
                content = messageAnalysis(messageTemplateDto.getTemplate(), data);
            }
            message.setContent(content, "text/html;charset=utf-8");
            // 发送消息
            Transport.send(message);
        } catch (GeneralSecurityException | MessagingException | UnsupportedEncodingException e) {
            String message = "腾讯邮箱发送失败，请排查故障，异常信息：" + e.getMessage();
            log.error(message, e.getMessage(), e);
            if (openErrorNotice) {
                sendSystemErrorNoticeMessage(appEntity, message);
            }
            throw FailCode.MESSAGE_SEND_FAIL.getOperateException(e.getMessage());
        }
    }
}
