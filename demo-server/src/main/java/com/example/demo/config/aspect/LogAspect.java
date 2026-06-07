package com.example.demo.config.aspect;

import cn.hutool.json.JSONUtil;
import com.xc.core.aspect.AspectHandle;
import com.xc.core.aspect.BasicConstants;
import com.xc.core.enums.CoreFailCode;
import com.xc.core.model.ServletModel;
import com.xc.core.model.TokenModel;
import com.xc.tool.utils.AesUtils;
import com.xc.tool.utils.IpUtils;
import com.xc.tool.utils.JSONUtils;
import com.xc.tool.utils.Md5Utils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

/**
 * <p>业务日志记录类</p>
 *
 * @author xc
 * @version v1.0
 */
@Aspect
@Component
@Slf4j
@Order(4)
public class LogAspect extends AbstractPoint {


    /**
     * <p>请求处理</p>
     *
     * @param joinPoint 切面数据信息
     */
    @Before("writeLog()")
    public void logRequest(JoinPoint joinPoint) {
        BasicConstants basicConstants = AspectHandle.getBasicConstants();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            HttpServletResponse response = attributes.getResponse();
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            ServletModel servletModel = new ServletModel();
            servletModel.setClientIp(IpUtils.getClientIp(request));
            // 获取cookie中的token信息
            String refreshToken = null;
            String accessToken = null;
            String key = Md5Utils.getMD5(basicConstants.getCookieTokenKey() + basicConstants.getAppId());
            String tokenSecret = Md5Utils.getMD5(key + basicConstants.getAppId());
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(key)) {
                        if (cookie.getValue() != null) {
                            String tokenJson = AesUtils.decodeToBase64(cookie.getValue(), tokenSecret);
                            TokenModel oldTokenModel = JSONUtil.toBean(tokenJson, TokenModel.class);
                            accessToken = oldTokenModel.getAccessToken();
                            refreshToken = oldTokenModel.getRefreshToken();
                        }
                    }
                }
            }
            // 获取token
            if (accessToken == null || "".equals(accessToken)) {
                accessToken = getAccessToken(request);
            }
            try {
                request.setAttribute("requestTime", new Date().getTime());
                String redirectUri = AspectHandle.requestHandle(servletModel, method, joinPoint.getArgs(), accessToken, refreshToken, (tokenModel) -> {
                    if (response != null) {
                        try {
                            tokenModel.setData(null);
                            tokenModel.setAuthorityCodes(new ArrayList<>());
                            tokenModel.setGroups(new ArrayList<>());
                            String tokenJson = JSONUtils.getStringByObject(tokenModel);
                            String cipherText = AesUtils.encodeToBase64(tokenJson, tokenSecret);
                            Cookie tokenCookie = new Cookie(key, cipherText);
                            tokenCookie.setPath("/");
                            response.addCookie(tokenCookie);
                        } catch (Exception e) {
                            Cookie tokenCookie = new Cookie(key, null);
                            tokenCookie.setPath("/");
                            tokenCookie.setMaxAge(0);
                            response.addCookie(tokenCookie);
                        }
                    }
                });
                if (redirectUri != null && response != null) {
                    response.sendRedirect(redirectUri);
                }
            } catch (IOException ex) {
                throw CoreFailCode.REDIRECT_FAIL.getOperateException();
            }
        }
    }

    public String getAccessToken(HttpServletRequest request) {
        BasicConstants basicConstants = AspectHandle.getBasicConstants();
        String accessToken = request.getHeader(basicConstants.getTokenKey());
        if (accessToken == null || "".equals(accessToken)) {
            accessToken = request.getParameter(basicConstants.getTokenKey());
            if (accessToken == null || "".equals(accessToken)) {
                if (request.getCookies() != null) {
                    for (Cookie cookie : request.getCookies()) {
                        if (cookie.getName().equals(basicConstants.getTokenKey())) {
                            accessToken = cookie.getValue();
                        }
                    }
                }
            }
        }
        return accessToken;
    }


    /**
     * 异常处理
     *
     * @param pjp 切面对象
     * @return 数据
     * @throws Throwable 异常
     */
    @Around("writeLog()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Object obj;
        try {
            obj = pjp.proceed();
        } catch (Exception e) {
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                Method method = ((MethodSignature) pjp.getSignature()).getMethod();
                Long requestTime = (Long) request.getAttribute("requestTime");

                String accessToken = getAccessToken(request);

                ServletModel servletModel = new ServletModel();
                servletModel.setClientIp(IpUtils.getClientIp(request));
                servletModel.setRequestTime(requestTime);
                servletModel.setHttpMethod(request.getMethod());
                servletModel.setHttpUrl(request.getRequestURI());
                servletModel.setHttpParamMap(request.getParameterMap());
                servletModel.setHttpPathObj(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));

                AspectHandle.exceptionHandle(servletModel, method, pjp.getArgs(), e, accessToken);
            }
            throw e;
        }
        return obj;

    }

    /**
     * <p>响应处理</p>
     *
     * @param joinPoint 切面数据信息
     * @param result    返回数据对象
     */
    @AfterReturning(returning = "result", pointcut = "writeLog()")
    public void logResponse(JoinPoint joinPoint, Object result) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            Long requestTime = (Long) request.getAttribute("requestTime");

            String accessToken = getAccessToken(request);

            ServletModel servletModel = new ServletModel();
            servletModel.setClientIp(IpUtils.getClientIp(request));
            servletModel.setRequestTime(requestTime);
            servletModel.setHttpMethod(request.getMethod());
            servletModel.setHttpUrl(request.getRequestURI());
            servletModel.setHttpParamMap(request.getParameterMap());
            servletModel.setHttpPathObj(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));

            AspectHandle.responseHandle(servletModel, method, joinPoint.getArgs(), result, accessToken);
        }
    }

}
