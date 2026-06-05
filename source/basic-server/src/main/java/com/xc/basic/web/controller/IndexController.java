package com.xc.basic.web.controller;

import com.xc.basic.config.Constants;
import com.xc.basic.service.IndexService;
import com.xc.core.aspect.BasicConstants;
import com.xc.core.aspect.CaptchaHandle;
import com.xc.core.dto.RateCaptchaDto;
import com.xc.core.model.ClientIpModel;
import com.xc.tool.utils.Base64Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * <p>无需权限接口，首页Controller</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"无需权限接口，首页Controller"})
@Controller
public class IndexController {

    @Autowired
    private BasicConstants basicConstants;
    @Autowired
    private IndexService indexService;
    @Autowired
    private Constants constants;
    @Value("${spring.profiles.active}")
    private String active;

    /**
     * 首页
     *
     * @return 首页
     */
    @ApiOperation(value = "首页")
    @GetMapping("/")
    public String index() {
        return "forward:/nav/app";
    }

    /**
     * 管理页面
     *
     * @return 管理页面
     */
    @ApiOperation(value = "管理页面")
    @GetMapping("/admin")
    public String admin() {
        return "forward:/admin/index.html";
    }


    @ApiOperation(value = "验证码页面")
    @GetMapping("/captcha")
    public String captcha(Model model, ClientIpModel clientIpModel, HttpServletRequest request, @RequestParam String redirectUrl,
                          @RequestParam(required = false) String captchaCode, @RequestParam(required = false) String captcha) throws IOException {
        // 应用信息
        Map<String, Object> map = indexService.getAppInfo();
        model.addAttribute("appName", map.get("appName"));
        model.addAttribute("app", map);
        // 验证码信息
        model.addAttribute("redirectUrl", redirectUrl);
        String clientIp = clientIpModel.getClientIp();
        RateCaptchaDto rateCaptchaDto = CaptchaHandle.imgCaptchaHandle(basicConstants, clientIp, redirectUrl, captchaCode, captcha);
        if (rateCaptchaDto.getRedirectUrl() != null) {
            String protocol = request.getScheme(); // 获取协议
            String domain = request.getServerName(); // 获取域名
            int port = request.getServerPort(); // 获取端口
            String url = protocol + "://" + domain + ":" + port;
            if ("pro".equals(active)) {
                url = url + constants.getBasicPath();
            }
            return "redirect:" + url + rateCaptchaDto.getRedirectUrl();
        } else {
            String captchaUrl = "data:image/png;base64," + Base64Utils.encode(rateCaptchaDto.getImgBytes());
            model.addAttribute("captchaUrl", captchaUrl);
            model.addAttribute("openUrl", rateCaptchaDto.getOpenUrl());
            model.addAttribute("captchaCode", rateCaptchaDto.getCode());
            model.addAttribute("message", rateCaptchaDto.getMessage());
            return "/basic/captcha/captcha";
        }
    }


}
