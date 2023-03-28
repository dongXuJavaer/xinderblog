package com.xinder.api.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xinder.api.response.dto.UserDtoResult;
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
 *  通知实体类
 * </p>
 *
 * @author Xinder
 * @since 2023-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Notification对象", description = "通知实体类")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "通知内容")
    private String content;

    @ApiModelProperty(value = "通知类型:  1:系统通知  2:关注消息  3:帖子评论通知   4: 帖子点赞通知 ")
    private Integer type;

    @ApiModelProperty(value = "帖子id")
    private Long Aid;

    @ApiModelProperty(value = "发送的用户id")
    private Long fromUid;

    @ApiModelProperty(value = "接收者用户id。 如果是系统通知，该字段为空")
    private Long toUid;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "0: 未读,    1:已读")
    private Integer readFlag = 0;


    @ApiModelProperty(value = "发送的用户信息")
    private UserDtoResult fromUser;

}
