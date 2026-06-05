package com.xc.basic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xc.basic.entity.UserEntity;
import org.springframework.stereotype.Repository;
/**
 * <p>用户Mapper接口</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Repository
public interface UserMapper extends BaseMapper<UserEntity> {
}
