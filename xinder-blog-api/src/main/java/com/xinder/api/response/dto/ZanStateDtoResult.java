package com.xinder.api.response.dto;

import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文章点赞状态Dto
 *
 * @author Xinder
 * @date 2023-03-28 14:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ZanStateDtoResult extends DtoResult {

    @ApiModelProperty(value = "总点赞数")
    private Long count;

    @ApiModelProperty(value = "查看的用户是否点赞")
    private Integer type;
}
