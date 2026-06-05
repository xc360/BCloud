package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.basic.dto.DictDto;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.basic.bean.DictBean;
import com.xc.basic.entity.DictEntity;

import java.util.List;

/**
 * <p>应用字典Service接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface DictService extends IService<DictEntity> {

    /**
     * 获取字典分页数据
     *
     * @param current    当前页
     * @param pagingBean 分页信息
     * @param dictEntity 字典参数
     * @return 分页信息及字典数据
     */
    public PagingDto<DictDto> getDictPage(Integer current, PagingBean pagingBean, DictEntity dictEntity);

    /**
     * 字典批量添加
     *
     * @param appId     应用id
     * @param dictBeans 字典集合
     * @return 字典集合
     */
    public List<DictDto> createAppDictList(String appId, List<DictBean> dictBeans);

    /**
     * 获取字典集合
     *
     * @param queryBean  查询信息
     * @param dictEntity 查询参数
     * @return 字典集合
     */
    public List<DictDto> getDictList(QueryBean queryBean, DictEntity dictEntity);

    /**
     * 上下移动
     *
     * @param entity 字典实体
     * @param isUp   是否向上
     */
    public boolean move(DictEntity entity, boolean isUp);
}
