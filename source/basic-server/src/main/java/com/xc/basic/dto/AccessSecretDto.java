package com.xc.basic.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>访问秘钥</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class AccessSecretDto {

    @ApiModelProperty(value = "访问秘钥")
    private String accessSecret;
}
