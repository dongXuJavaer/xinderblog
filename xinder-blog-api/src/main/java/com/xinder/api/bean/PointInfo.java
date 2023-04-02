package com.xinder.api.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Xinder
 * @since 2023-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PointInfo对象", description="")
public class PointInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "流水信息")
    private String content;

    @ApiModelProperty(value = "用户id")
    private Long uid;

    @ApiModelProperty(value = "0: 积分扣减,  1:积分增加")
    private Integer type;

    @ApiModelProperty(value = "积分数量")
    private Integer point;

    @ApiModelProperty(value = "下载资源时的资源id")
    private Long rid;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
