package com.xc.basic.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>应用字典bean</p>
 *
 * @author xc
 * @version v1.0.0
 */
@Data
public class DictBean {

    @ApiModelProperty(value = "字典名称")
    private String name;

    @ApiModelProperty(value = "字典值")
    private String value;

    @ApiModelProperty(value = "字典类型")
    private String type;

    @ApiModelProperty(value = "排序，导入使用")
    private Long seq;

    @ApiModelProperty(value = "状态，对应字典表的status")
    private String status;
}
