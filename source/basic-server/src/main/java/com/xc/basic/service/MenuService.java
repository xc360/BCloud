package com.xc.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xc.api.basic.dto.MenuDto;
import com.xc.basic.bean.MenuBean;
import com.xc.basic.bean.QueryMenuBean;
import com.xc.basic.entity.MenuEntity;
import com.xc.basic.entity.UserEntity;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;

import java.util.List;

/**
 * <p>菜单服务接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
public interface MenuService extends IService<MenuEntity> {

    /**
     * 获取菜单分页数据
     *
     * @param current       当前页
     * @param pagingBean    分页信息
     * @param queryMenuBean 菜单参数
     * @return 分页信息及角色数据
     */
    public PagingDto<MenuDto> getAppMenuPage(String appId, Integer current, PagingBean pagingBean, QueryMenuBean queryMenuBean);

    /**
     * 删除菜单
     *
     * @param menuEntity 条件
     */
    public void deleteMenu(MenuEntity menuEntity);

    /**
     * 创建应用菜单集合
     *
     * @param id        应用id
     * @param menuBeans 菜单集合
     * @return 返回菜单集合
     */
    public List<MenuDto> createAppMenuList(String id, List<MenuBean> menuBeans);

    /**
     * 获取菜单集合
     *
     * @param appId         应用主键
     * @param queryMenuBean 菜单查询条件
     * @return 菜单集合
     */
    public List<MenuEntity> getAppMenuList(String appId, QueryBean queryBean, QueryMenuBean queryMenuBean);

    /**
     * 获取子集的所有菜单
     *
     * @param appId 应用主键
     * @param node  父节点
     * @return 菜单集合
     */
    public List<MenuEntity> getChildrenMenu(String appId, String node);

    /**
     * 上下移动
     *
     * @param entity 菜单实体
     * @param isUp   是否向上
     */
    public boolean move(MenuEntity entity, boolean isUp);
}
