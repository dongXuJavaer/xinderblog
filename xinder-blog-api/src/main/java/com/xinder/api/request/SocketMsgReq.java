package com.xinder.api.request;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 消息请求接收实体类
 *
 * @author Xinder
 * @date 2023-02-14 23:07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "SocketMsg对象", description = "消息实体类")
public class SocketMsgReq {

    @ApiModelProperty(value = "消息id")
    private Long id;

    @ApiModelProperty(value = "发送消息用户id")
    private Integer fromUid;

    @ApiModelProperty(value = "接收消息id")
    private Integer toId;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "消息类型  1 私聊， 2 群聊")
    private Integer type;

    @ApiModelProperty(value = "发送时间")
    @JSONField(format = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime createTime;
}
