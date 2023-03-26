package com.xinder.api.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xinder.api.response.dto.UserDtoResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Xinder
 * @since 2023-02-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Comments对象", description = "评论实体类")
public class Comments implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "帖子id")
    private Long aid;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "父评论id，空表示根级评论，其他值表示该评论的 回复的评论id")
    private Long parentId;

    @ApiModelProperty(value = "用户id")
    private Long uid;

    @ApiModelProperty(value = "被赞次数")
    private Long up = 0L;

    @ApiModelProperty(value = "评论日期")
    private Date createTime;

    @ApiModelProperty(name = "user", notes = "当前评论的用户信息")
    @TableField(exist = false)
    private UserDtoResult user;

    @ApiModelProperty(name = "subComments", notes = "当前评论的直接子评论(某些人对我评论的回复)")
    @TableField(exist = false)
    private List<Comments> subComments;

    @ApiModelProperty(name = "parentComments", notes = "当前评论的父评论")
    @TableField(exist = false)
    private Comments parentComments;

    @ApiModelProperty(name = "allSubComments", notes = "当前评论的所有子评论；根评论是才用该属性")
    @TableField(exist = false)
    private List<Comments> allSubComments;

}
