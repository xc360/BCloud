package com.xc.basic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xc.basic.entity.DeletedUserEntity;
import org.springframework.stereotype.Repository;

/**
 * <p>已删除用户mapper</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Repository
public interface DeletedUserMapper extends BaseMapper<DeletedUserEntity> {
}
