package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.basic.dto.PageDto;
import com.xc.basic.bean.PageBean;
import com.xc.basic.bean.PageColumnBean;
import com.xc.basic.dto.PageColumnDto;
import com.xc.basic.entity.PageEntity;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;

import java.util.List;

/**
 * <p>页面服务接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface PageService extends IService<PageEntity> {
    /**
     * 页面分页查询
     *
     * @param current    当前第几页
     * @param pagingBean 分页基础参数信息
     * @param pageEntity 查询条件
     * @return 分页数据
     */
    public PagingDto<PageDto> getPagePage(Integer current, PagingBean pagingBean, PageEntity pageEntity);

    /**
     * 获取页面集合
     *
     * @param queryBean  基础信息
     * @param pageEntity 查询条件
     * @return 页面集合
     */
    public List<PageDto> getPageList(QueryBean queryBean, PageEntity pageEntity);

    /**
     * 页面批量添加
     *
     * @param appId       应用id
     * @param pageBeans 页面集合
     * @return 页面集合
     */
    public List<PageDto> createPageList(String appId, List<PageBean> pageBeans);

    /**
     * 创建页面
     *
     * @param appId    应用主键
     * @param pageBean 参数
     * @return 页面信息
     */
    public PageDto createPage(String appId, PageBean pageBean);

    /**
     * 修改页面
     *
     * @param appId    应用主键
     * @param pageId   页面主键
     * @param pageBean 参数
     * @return 页面信息
     */
    public PageDto updatePage(String appId, String pageId, PageBean pageBean);

    /**
     * 删除页面
     *
     * @param appId  应用主键
     * @param pageId 页面主键
     */
    public void deletePage(String appId, String pageId);

    /**
     * 创建页面栏目关联集合
     * @param appId               应用主键
     * @param pageColumnBeans 页面栏目关联集合
     * @return 页面栏目关联集合
     */
    public List<PageColumnDto> createAppPageColumnRelationList(String appId, List<PageColumnBean> pageColumnBeans);

    /**
     * 获取的页面栏目关联集合
     *
     * @param appId 应用主键
     * @return 页面栏目关联集合
     */
    public List<PageColumnDto> getAppPageColumnRelationList(String appId);
}
