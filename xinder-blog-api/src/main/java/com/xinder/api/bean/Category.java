package com.xinder.api.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by sang on 2017/12/19.
 */
@Data
@TableName("category")
public class Category {

    @TableId
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "分类名")
    @TableField(value = "cate_name")
    private String cateName;

    @ApiModelProperty(value = "创建时间")
    private Date date;

}
