package com.xinder.api.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 私信消息
 * </p>
 *
 * @author Xinder
 * @since 2023-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MsgPrivate对象", description = "")
public class MsgPrivate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息id，主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "发送消息用户id")
    private Integer fromUid;

    @ApiModelProperty(value = "接收消息用户id")
    private Integer toUid;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "发送时间")
    @TableField("create_time")
    private LocalDateTime createTime;


}
