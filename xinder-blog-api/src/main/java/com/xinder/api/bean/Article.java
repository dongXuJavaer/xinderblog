package com.xinder.api.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by sang on 2017/12/20.
 */

@Data
@NoArgsConstructor
@TableName("article")
public class Article implements Serializable {

    @ApiModelProperty(value = "文章主键", notes = "仅修改时传入")
    @TableId
    private Long id;

    @TableField(value = "title")
    @ApiModelProperty(value = "文章标题")
    private String title;

    @TableField(value = "md_content")
    @ApiModelProperty(value = "文章内容（md格式）")
    private String mdContent;

    @TableField(value = "html_content")
    @ApiModelProperty(value = "文章内容（html格式）")
    private String htmlContent;

    @TableField(value = "summary")
    @ApiModelProperty(value = "简介")
    private String summary;

    @TableField(value = "cid")
    @ApiModelProperty(value = "分类id")
    private Long cid;

    @TableField(value = "state")
    @ApiModelProperty(value = "博客状态", notes = "0表示草稿箱，1表示已发表，2表示已删除")
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "edit_time")
    @ApiModelProperty(value = "上次编辑时间")
    private Date editTime;

    @TableField(value = "uid")
    @ApiModelProperty(value = "用户id")
    private Long uid;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @TableField(exist = false)
    @ApiModelProperty(value = "分类名称")
    private String cateName;

    @TableField(value = "page_view")
    private Integer pageView;

    @TableField(exist = false)
    @ApiModelProperty(value = "包含的标签")
    private List<Tags> tags;

    @TableField("read_count")
    @ApiModelProperty(value = "浏览量")
    private Integer readCount;

    private String[] dynamicTags;

    private String stateStr;
}
