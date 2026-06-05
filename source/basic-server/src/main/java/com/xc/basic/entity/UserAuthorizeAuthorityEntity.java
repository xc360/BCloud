package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>用户授权关联权限实体</p>
 *
 * @author xc
 * @since 2023-08-01
 */
@Data
@TableName("xc_user_authorize__authority")
public class UserAuthorizeAuthorityEntity {
    /***
     * 关联主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 用户授权主键
     */
    private String userAuthorizeId;
    /**
     * 权限主键
     */
    private String authorityId;
}
