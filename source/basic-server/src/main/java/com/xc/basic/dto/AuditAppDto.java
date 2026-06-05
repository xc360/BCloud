package com.xc.basic.dto;

import com.xc.api.basic.dto.AppDto;
import com.xc.api.basic.dto.AuthorityDto;
import com.xc.api.basic.dto.VersionDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>审核应用dto</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class AuditAppDto {

    @ApiModelProperty(value = "应用的权限")
    private List<AuthorityDto> authorityList;

    @ApiModelProperty(value = "版本集合")
    private List<VersionDto> versionList;

    @ApiModelProperty(value = "应用信息")
    private AppDto appDto;
}
