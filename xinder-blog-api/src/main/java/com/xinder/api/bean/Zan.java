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

/**
 * <p>
 *
 * </p>
 *
 * @author Xinder
 * @since 2023-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Zan对象", description="")
public class Zan implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "帖子id")
    private Long aid;

    @ApiModelProperty(value = "用户id")
    private Long uid;

    @ApiModelProperty(value = "点赞时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "0:取消点赞     1:已点赞  ")
    private Integer type;


}
