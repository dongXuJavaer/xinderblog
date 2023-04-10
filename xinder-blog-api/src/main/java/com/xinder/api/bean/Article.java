package com.xinder.api.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by xinder on 2017/12/20.
 */

@Setter
@Getter
@NoArgsConstructor
@TableName("article")
@Document(indexName = "articles")
public class Article implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "文章主键", notes = "仅修改时传入")
    @Id
    private Long id;

    @TableField(value = "title")
    @ApiModelProperty(value = "文章标题")
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String title;

    @TableField(value = "md_content")
    @ApiModelProperty(value = "文章内容（md格式）")
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String mdContent;

    @TableField(value = "html_content")
    @ApiModelProperty(value = "文章内容（html格式）")
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String htmlContent;

    @TableField(value = "summary")
    @ApiModelProperty(value = "简介")
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String summary;

    @TableField(value = "cid")
    @Field(type = FieldType.Long)
    @ApiModelProperty(value = "分类id")
    private Long cid;

    @TableField(value = "state")
    @ApiModelProperty(value = "博客状态", notes = "0表示草稿箱，1表示已发表，2表示已删除")
    @Field(type = FieldType.Integer)
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private Date createTime;

    @TableField(value = "edit_time")
    @ApiModelProperty(value = "上次编辑时间")
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private Date editTime;

    @TableField(value = "uid")
    @ApiModelProperty(value = "用户id")
    @Field(type = FieldType.Long)
    private Long uid;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户昵称")
    @Field(type = FieldType.Text)
    private String nickname;

    @TableField(exist = false)
    @ApiModelProperty(value = "分类名称")
    @Field(type = FieldType.Keyword)
    private String cateName;

//    @TableField(value = "page_view")
//    private Integer pageView;

    @TableField(exist = false)
    @ApiModelProperty(value = "包含的标签")
    @Field(type = FieldType.Object)
    private List<Tags> tags;

    @TableField("read_count")
    @ApiModelProperty(value = "浏览量")
    private Long readCount;

    @TableField("head_pic")
    @ApiModelProperty(value = "封面图片链接")
    @Field(type = FieldType.Text)
    private String headPic;

    @TableField("attachment")
    @ApiModelProperty(value = "附件链接")
    private String attachment;

    // TODO: 2023-02-19 考虑删除
    @TableField(exist = false)
    @ApiModelProperty(value = "用户新添加的标签")
    private String[] dynamicTags;

    @TableField("comment_flag")
    @ApiModelProperty(value = "是否能被评论，0: 不能   1: 能")
    private Integer commentFlag;


    @TableField(exist = false)
    @ApiModelProperty(value = "状态说明")
    private String stateStr;
}
