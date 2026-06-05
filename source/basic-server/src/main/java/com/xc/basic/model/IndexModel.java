package com.xc.basic.model;

import com.xc.api.basic.dto.AuthorityDto;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>首页dto</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class IndexModel {
    /**
     * 权限集合
     */
    private List<AuthorityDto> authorityList;

    /**
     * 信息map
     */
    private Map<String, String> infoMap;
}
