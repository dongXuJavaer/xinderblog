package com.xinder.api.response.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.xinder.api.bean.Article;
import com.xinder.api.bean.Role;
import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 前端用户查看的用户信息
 * @author Xinder
 * @date 2023-02-03 17:05
 */
@Data
public class UserDtoSimpleResult extends DtoResult {

    @TableId
    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像链接")
    private String userface;

    @ApiModelProperty(value = "性别" , notes = "1:女    2:男")
    private Integer gender;

    @ApiModelProperty(value = "帖子列表")
    private List<Article> articleList;

    // TODO: 2023-02-03 群组信息
}
