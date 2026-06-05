package com.xc.basic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * <p>应用授权实体</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
@TableName("xc_authorize")
public class AuthorizeEntity {
    /**
     * 关联主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 应用id
     */
    private String appId;
    /**
     * 权限id
     */
    private String authorityId;

    /**
     * 审核状态，对应字典：auditStatus，0：未申请，1：未审核，2：已审核，3：已拒绝
     */
    private String auditStatus;

    /**
     * 申请，拒绝，取消原因
     */
    private String reason;

    /**
     * 发布时间
     */
    private Date applyTime;
}
