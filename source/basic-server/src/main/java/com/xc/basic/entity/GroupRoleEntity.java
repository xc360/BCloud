package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>用户组角色关联实体</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_group__role")
public class GroupRoleEntity {
    /***
     *关联主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 组id
     */
    private String groupId;
}
