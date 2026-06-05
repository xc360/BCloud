package com.xc.basic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xc.basic.entity.AuthorityEntity;
import com.xc.basic.entity.MenuEntity;
import org.springframework.stereotype.Repository;

/**
 * <p>菜单Mapper</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Repository
public interface MenuMapper extends BaseMapper<MenuEntity> {
}
