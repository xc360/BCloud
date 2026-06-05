package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.basic.dto.DataTypeDto;
import com.xc.basic.bean.DataTypeBean;
import com.xc.basic.entity.DataTypeEntity;
import com.xc.basic.entity.DictEntity;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;

import java.util.List;

/**
 * <p>数据类型</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface DataTypeService extends IService<DataTypeEntity> {
    /**
     * 获取数据类型分页数据
     *
     * @param current          当前页
     * @param pagingBean       分页信息
     * @param dataTypeEntity 数据类型参数
     * @return 分页信息及数据类型数据
     */
    public PagingDto<DataTypeDto> getDataTypePage(Integer current, PagingBean pagingBean, DataTypeEntity dataTypeEntity);

    /**
     * 获取数据类型集合
     *
     * @param queryBean        基础信息
     * @param dataTypeEntity 数据类型参数
     * @return 分页信息及数据类型数据
     */
    public List<DataTypeDto> getDataTypeList(QueryBean queryBean, DataTypeEntity dataTypeEntity);

    /**
     * 数据类型批量添加
     *
     * @param appId           应用id
     * @param dataTypeBeans 数据类型集合
     * @return 数据类型集合
     */
    public List<DataTypeDto> createDataTypeList(String appId, List<DataTypeBean> dataTypeBeans);

    /**
     * 上下移动
     *
     * @param entity 数据类型实体
     * @param isUp   是否向上
     */
    public boolean move(DataTypeEntity entity, boolean isUp);
}
