package com.xinder.api.response.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Xinder
 * @date 2023-02-18 15:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class TagDtoResult extends DtoResult {

    private Long id;

    @ApiModelProperty(value = "标签名称")
    private String tagName;

    @ApiModelProperty(value = "创建者id")
    private Integer uid;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
