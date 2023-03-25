package com.xinder.api.response.dto;

import com.xinder.api.bean.Comments;
import com.xinder.api.bean.Group;
import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Xinder
 * @date 2023-02-19 16:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentListDtoResult extends DtoResult {

    @ApiModelProperty(name = "list", notes = "所有评论（根级评论）")
    private List<Comments> list;

}
