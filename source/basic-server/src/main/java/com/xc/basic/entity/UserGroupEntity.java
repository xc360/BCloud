package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>用户组实体</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_user__group")
public class UserGroupEntity {
    /***
     *关联主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 组id
     */
    private String groupId;
}
