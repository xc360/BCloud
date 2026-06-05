package com.xc.basic.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>app秘钥</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class AppSecretDto {

    @ApiModelProperty(value = "应用秘钥")
    private String appSecret;
}
