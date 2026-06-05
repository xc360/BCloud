package com.xc.basic.bean;

import com.xc.api.basic.bean.LoginBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>登录参数类</p>
 *
 * @author xc
 * @version v1.0.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class MyAppLoginBean extends LoginBean {

    @ApiModelProperty(value = "应用主键")
    private String appId;
}
