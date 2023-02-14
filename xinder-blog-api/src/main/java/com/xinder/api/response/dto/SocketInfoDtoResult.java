package com.xinder.api.response.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Xinder
 * @date 2023-02-13 16:18
 */
@Data
public class SocketInfoDtoResult extends DtoResult {


    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "建立连接用户id")
    private Integer fromUid;

    @ApiModelProperty(value = "接收方id。 如果是群聊，则为群聊id")
    private Integer toId;

    @ApiModelProperty(value = "接收方名称。 如果是群聊，则为群聊名称")
    private String toName;

    @ApiModelProperty(value = "接收方头像")
    private String pic;

    @ApiModelProperty(value = "0:关闭聊天框     1:私聊;    2: 群聊")
    private Integer type;

    @ApiModelProperty(value = "连接时间")
    private LocalDateTime createTime;
}
