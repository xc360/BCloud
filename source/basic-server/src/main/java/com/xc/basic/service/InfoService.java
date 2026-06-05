package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.basic.bean.InfoBean;
import com.xc.api.basic.dto.InfoDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.entity.InfoEntity;

import java.util.List;
import java.util.Map;

/**
 * <p>应用信息Service接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface InfoService extends IService<InfoEntity> {

    /**
     * 获取应用信息分页数据
     *
     * @param current    当前页
     * @param pagingBean 分页信息
     * @param infoEntity 应用信息参数
     * @return 分页信息及应用信息数据
     */
    public PagingDto<InfoDto> getInfoPage(Integer current, PagingBean pagingBean, InfoEntity infoEntity);

    /**
     * 应用信息批量添加
     *
     * @param appId        应用id
     * @param infoBeans 应用信息集合
     * @return 应用信息集合
     */
    public List<InfoDto> createAppInfoList(String appId, List<InfoBean> infoBeans);

    /**
     * 获取的信息集合
     *
     * @param queryBean  查询信息
     * @param infoEntity 查询参数
     * @return 信息集合
     */
    public List<InfoDto> getInfoList(QueryBean queryBean, InfoEntity infoEntity);

    /**
     * 获取应用信息Map集合
     *
     * @param id   应用id
     * @param type 信息类型
     * @return map集合
     */
    public Map<String, String> getAppValidInfoMap(String id, String type);
}
