package com.xc.basic.web.rest.open;

import com.xc.api.basic.enums.BasicRestCode;
import com.xc.core.bean.SignBean;
import com.xc.basic.entity.AppEntity;
import com.xc.basic.service.BasicAuthorizeService;
import com.xc.core.model.ClientIpModel;
import com.xc.tool.utils.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>开放接口，其他接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Api(tags = {"开放接口，其他接口"})
@Slf4j
@RestController
public class OpenOuterIpRest {

    @Autowired
    private BasicAuthorizeService basicAuthorizeService;

    /**
     * 返回外网ip地址
     *
     * @param clientIpModel ip信息
     * @return 客户端的外网地址
     */
    @ApiOperation(value = "返回外网ip地址", notes = "返回外网ip地址")
    @GetMapping("/open/outer/ip")
    public String getOpenOuterIp(ClientIpModel clientIpModel, HttpServletRequest request, @ModelAttribute SignBean signBean) {
        signBean.setAuthorityCode(BasicRestCode.getOpenOuterIp.getCode());
        AppEntity appEntity = basicAuthorizeService.verifySign(signBean);
        log.info("客户端ip地址：{},应用名称：{},应用appId：{}", clientIpModel.getClientIp(), appEntity.getAppName(), appEntity.getAppId());
        return clientIpModel.getClientIp();
    }
}
