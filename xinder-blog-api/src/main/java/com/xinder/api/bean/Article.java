package com.xinder.api.bean;

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

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "文章内容（md格式）")
    private String mdContent;

    @ApiModelProperty(value = "文章内容（html格式）")
    private String htmlContent;

    @ApiModelProperty(value = "简介")
    private String summary;

    @ApiModelProperty(value = "分类id")
    private Long cid;

    @ApiModelProperty(value = "博客状态", notes = "0表示草稿箱，1表示已发表，2表示已删除")
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "上次编辑时间")
    private Date editTime;

    @ApiModelProperty(value = "用户id")
    private Long uid;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "分类名称")
    private String cateName;

    private Integer pageView;

    @ApiModelProperty(value = "包含的标签")
    private List<Tags> tags;

    private String[] dynamicTags;

    private String stateStr;
}
