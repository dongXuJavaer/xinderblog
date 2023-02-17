package com.xinder.api.request;

import com.xinder.api.request.comm.PageDtoReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Xinder
 * @date 2023-02-17 14:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class TagsDtoReq extends PageDtoReq {

    private static final long serialVersionUID = 965132976922378L;

    private Long id;

    @ApiModelProperty(value = "标签名称")
    private String tagName;

    @ApiModelProperty(value = "创建者id")
    private Integer uid;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
