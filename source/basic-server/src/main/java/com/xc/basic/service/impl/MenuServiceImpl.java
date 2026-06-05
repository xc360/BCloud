package com.xc.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xc.api.basic.dto.MenuDto;
import com.xc.basic.bean.MenuBean;
import com.xc.basic.bean.QueryMenuBean;
import com.xc.basic.config.Constants;
import com.xc.basic.entity.*;
import com.xc.basic.enums.FailCode;
import com.xc.basic.mapper.*;
import com.xc.basic.service.AppService;
import com.xc.basic.service.MenuService;
import com.xc.core.bean.PagingBean;
import com.xc.core.bean.QueryBean;
import com.xc.core.dto.PagingDto;
import com.xc.core.enums.AuditStatus;
import com.xc.core.enums.EffectStatus;
import com.xc.core.enums.Whether;
import com.xc.core.utils.ServiceUtils;
import com.xc.tool.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>菜单服务实现类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuEntity> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public PagingDto<MenuDto> getAppMenuPage(String appId, Integer current, PagingBean pagingBean, QueryMenuBean queryMenuBean) {
        MenuEntity menuEntity = ObjectUtils.convert(new MenuEntity(), queryMenuBean);
        menuEntity.setAppId(appId);
        QueryWrapper<MenuEntity> queryWrapper = ServiceUtils.queryLike(menuEntity, pagingBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, pagingBean);
        IPage<MenuEntity> iPage = this.page(new Page<>(current, pagingBean.getSize()), queryWrapper);
        return new PagingDto<>(iPage.getTotal(), ObjectUtils.convertList(iPage.getRecords(), MenuDto::new));
    }

    @Override
    @Transactional
    public void deleteMenu(MenuEntity menuEntity) {
        // 删除下级菜单
        List<MenuEntity> entities = getChildrenMenu(menuEntity.getAppId(), menuEntity.getNode());
        List<String> menuIds = entities.stream().map(MenuEntity::getId).collect(Collectors.toList());
        menuIds.add(menuEntity.getId());
        // 删除菜单及下级菜单
        if (!this.removeByIds(menuIds)) {
            throw FailCode.MENU_DELETE_FAIL.getOperateException();
        }
    }

    @Override
    public List<MenuEntity> getChildrenMenu(String appId, String node) {
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setParentNode(node);
        menuEntity.setAppId(appId);
        List<MenuEntity> entities = menuMapper.selectList(new QueryWrapper<>(menuEntity));
        int index = 0;
        while (index < entities.size()) {
            MenuEntity entity = entities.get(index);
            MenuEntity menu = new MenuEntity();
            menu.setParentNode(entity.getNode());
            menu.setAppId(appId);
            List<MenuEntity> menuList = menuMapper.selectList(new QueryWrapper<>(menu));
            entities.addAll(menuList);
            index++;
        }
        return entities;
    }

    @Override
    @Transactional
    public List<MenuDto> createAppMenuList(String appId, List<MenuBean> menuBeans) {
        // 删除应用的所有菜单
        MenuEntity menu = new MenuEntity();
        menu.setAppId(appId);
        menuMapper.delete(new QueryWrapper<>(menu));
        // 批量添加菜单
        List<MenuEntity> entities = new ArrayList<>();
        for (MenuBean menuBean : menuBeans) {
            MenuEntity menuEntity = ObjectUtils.convert(new MenuEntity(), menuBean);
            menuEntity.setAppId(appId);
            entities.add(menuEntity);
        }
        try {
            if (entities.size() > 0) {
                if (!this.saveBatch(entities)) {
                    throw FailCode.MENU_CREATE_FAIL.getOperateException();
                }
            }
        } catch (DuplicateKeyException e) {
            throw FailCode.MENU_CODE_REPEAT.getOperateException();
        }
        return ObjectUtils.convertList(entities, MenuDto::new);
    }

    @Override
    public List<MenuEntity> getAppMenuList(String appId, QueryBean queryBean, QueryMenuBean queryMenuBean) {
        MenuEntity menuEntity = ObjectUtils.convert(new MenuEntity(), queryMenuBean);
        menuEntity.setAppId(appId);
        QueryWrapper<MenuEntity> queryWrapper = ServiceUtils.queryLike(menuEntity, queryBean.getLikeFields());
        ServiceUtils.querySort(queryWrapper, queryBean);
        return list(queryWrapper);
    }

    @Override
    @Transactional
    public boolean move(MenuEntity entity, boolean isUp) {
        // 查询需要处理的菜单
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setAppId(entity.getAppId());
        menuEntity.setParentNode(entity.getParentNode());
        QueryWrapper<MenuEntity> queryWrapper = new QueryWrapper<>(menuEntity);
        queryWrapper.lambda().orderByAsc(MenuEntity::getSeq);
        List<MenuEntity> entities = this.list(queryWrapper);
        Set<Long> entitySet = entities.stream().map(MenuEntity::getSeq).collect(Collectors.toSet());
        if (entitySet.size() != entities.size() || entities.get(entities.size() - 1).getSeq() != entities.size()) {
            for (int i = 0; i < entities.size(); i++) {
                MenuEntity menu = entities.get(i);
                if (menu.getSeq() != i + 1) {
                    menu.setSeq(i + 1L);
                    this.updateById(menu);
                }
            }
        }
        for (int i = 0; i < entities.size(); i++) {
            MenuEntity menu = entities.get(i);
            if (entity.getId().equals(menu.getId())) {
                MenuEntity menu1;
                if (isUp) {
                    if (entities.size() <= i + 1) {
                        return false;
                    }
                    menu1 = entities.get(i + 1);
                } else {
                    if (i - 1 < 0) {
                        return false;
                    }
                    menu1 = entities.get(i - 1);
                }
                Long seq = menu1.getSeq();
                menu1.setSeq(menu.getSeq());
                this.updateById(menu1);
                // 修改原位置
                menu.setSeq(seq);
                this.updateById(menu);
            }
        }
        return true;
    }
}
