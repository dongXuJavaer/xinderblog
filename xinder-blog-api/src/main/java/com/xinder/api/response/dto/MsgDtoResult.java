package com.xinder.api.response.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Xinder
 * @date 2023-02-15 21:04
 */
@Data
@Accessors(chain = true)
public class MsgDtoResult extends DtoResult {

    private static final long serialVersionUID = 574217966803557L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "建立连接用户id")
    private Integer fromUid;

    @ApiModelProperty(value = "发送者名称")
    private String fromNickname;

    @ApiModelProperty(value = "接收方id。 如果是群聊，则为群聊id")
    private Integer toId;

//    @ApiModelProperty(value = "接收方名称。 如果是群聊，则为群聊名称")
//    private String toName;

//    @ApiModelProperty(value = "接收方头像")
//    private String pic;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "0:关闭聊天框     1:私聊;    2: 群聊")
    private Integer type;

    @ApiModelProperty(value = "发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "发送时间（字符串类型）")
    private String createTimeStr;

}
