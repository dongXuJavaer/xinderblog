package com.xinder.api.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by sang on 2017/12/21.
 */
@Data
@NoArgsConstructor
@TableName("tags")
public class Tags implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("tag_name")
    @ApiModelProperty(value = "标签名称")
    private String tagName;

    @TableField("uid")
    @ApiModelProperty(value = "创建者id")
    private Integer uid;

    @TableField("create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
