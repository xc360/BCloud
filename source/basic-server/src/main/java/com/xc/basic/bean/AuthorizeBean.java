package com.xc.basic.bean;

import com.xc.core.bean.AuditBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>创建应用权限</p>
 *
 * @author xc
 * @version v1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthorizeBean extends AuditBean {

    @ApiModelProperty(value = "权限主键")
    private String authorityId;

    @ApiModelProperty(value = "权限标识集合")
    private List<String> authorityCodes;

    @ApiModelProperty(value = "申请的是那个应用的")
    private String authorityAppId;
}
