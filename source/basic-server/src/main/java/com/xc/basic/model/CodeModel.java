package com.xc.basic.model;

import com.xc.basic.entity.UserEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>授权码模型</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class CodeModel {
    /**
     * 授权码
     */
    private String code;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 用户信息
     */
    private UserEntity userEntity;
}
