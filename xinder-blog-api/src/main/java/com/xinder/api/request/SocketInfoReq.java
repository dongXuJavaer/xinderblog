package com.xinder.api.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author Xinder
 * @date 2023-02-13 16:06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "SocketInfo请求对象", description = "")
public class SocketInfoReq {

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "建立连接用户id")
    private Integer fromUid;

    @ApiModelProperty(value = "接收方id。 如果是群聊，则为群聊id")
    private Integer toId;

    @ApiModelProperty(value = "0:关闭聊天框     1:私聊;    2: 群聊")
    private Integer type;

    @ApiModelProperty(value = "连接时间")
    private LocalDateTime createTime;

}
