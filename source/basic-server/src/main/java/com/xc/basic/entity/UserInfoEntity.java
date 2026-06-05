package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * <p>用户信息实体类</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_user_info")
public class UserInfoEntity {
    /**
     * 用户主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 个人说明
     */
    @TableField("`explain`")
    private String explain;
    /**
     * 性别：对应字典：sex，0：女，1：男
     */
    private String sex;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 区域
     */
    private String region;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 乐观锁
     */
    @Version
    private Integer version;
}
