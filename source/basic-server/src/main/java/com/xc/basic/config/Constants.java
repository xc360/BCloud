package com.xc.basic.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>项目配置类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "xc.basic")
public class Constants {
    /**
     * 服务唯一机器码，防止uuid重复
     */
    private String machineId = "b001";
    /**
     * 生成appId的前缀
     */
    private String appIdPrefix = "xc";
    /**
     * 账号前缀
     */
    private String accountPrefix = "1";
    /**
     * 访问ID前缀
     */
    private String accessIdPrefix = "U";
    /**
     * 登录失败几次启用验证码
     */
    private int openVerifyCodeNum = 5;

    /**
     * 密码错误次数，达到该次数锁定
     */
    private int passwordErrorsNum = 10;
    /**
     * 根，树形结构的根节点
     */
    private String root = "root";
    /**
     * 开启超级管理员功能，默认关闭。
     * 超级管理员不需要关联权限就拥有所有权限。
     * 该账户创建的应用拥有所有的应用权限，无需申请，
     * 建议线上环境关闭该功能
     */
    private Boolean openInitialAdmin = false;
    /**
     * 文章文件个数,如需调大，请检测网盘url地址是否支持传递
     */
    private Long articleFileMaxNum = 500L;
    /**
     * 文章文件前缀,".jpg", ".jpeg", ".gif", ".png", ".bmp", ".webp", ".zip"
     */
    private String[] articleFileSuffixArray = {".jpg", ".jpeg", ".gif", ".png", ".bmp", ".webp", ".zip", ".mp4"};
    /**
     * 打开手机验证码
     */
    private Boolean openPhoneCaptcha = true;
    /**
     * 打开邮箱验证码
     */
    private Boolean openEmailCaptcha = true;
    /**
     * 注销保留时间，默认180天
     */
    private Long logOffGuardTime = 15552000000L;
    /**
     * 刷新权限
     */
    private Boolean refreshAuthority = false;
    /**
     * 签名有效期，默认30分钟
     */
    private Long signValidTime = 1800000L;
    /**
     * 开启日志同步
     */
    private Boolean openLogSync = false;
    /**
     * 基础路径
     */
    private String basicPath = "";
    /**
     * 日志服务名称
     */
    private String logServeName = "basic-server";
}
