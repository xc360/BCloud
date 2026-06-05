package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * <p>用户授权实体</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_user_authorize")
public class UserAuthorizeEntity {
    /***
     *关联主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 授权时间
     */
    private Date authorizeTime;
    /**
     * 备注
     */
    private String remark;
}
