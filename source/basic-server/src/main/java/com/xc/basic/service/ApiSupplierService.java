package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.basic.dto.ApiSupplierDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.ApiSupplierBean;
import com.xc.basic.entity.ApiSupplierEntity;

import java.util.List;

/**
 * <p>接口供应商服务</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface ApiSupplierService extends IService<ApiSupplierEntity> {
    /**
     * 获取接口供应商分页数据
     *
     * @param current           当前页
     * @param pagingBean        分页信息
     * @param apiSupplierEntity 接口供应商参数
     * @return 分页信息及接口供应商数据
     */
    public PagingDto<ApiSupplierDto> getApiSupplierPage(Integer current, PagingBean pagingBean, ApiSupplierEntity apiSupplierEntity);

    /**
     * 接口供应商批量添加
     *
     * @param appId            应用主键
     * @param apiSupplierBeans 接口供应商集合
     * @return 接口供应商集合
     */
    public List<ApiSupplierDto> createAppApiSupplierList(String appId, List<ApiSupplierBean> apiSupplierBeans);

    /**
     * 获取接口供应商集合
     *
     * @param queryBean         查询信息
     * @param apiSupplierEntity 查询参数
     * @return 接口供应商集合
     */
    public List<ApiSupplierDto> getApiSupplierList(QueryBean queryBean, ApiSupplierEntity apiSupplierEntity);


    /**
     * 获取应用接口供应商
     *
     * @param code 标识
     * @return 接口供应商
     */
    public ApiSupplierDto getAppApiSupplierByCode(String appId, String code);
}
