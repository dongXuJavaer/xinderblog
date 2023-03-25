package com.xinder.api.rest;

import com.xinder.api.response.dto.CommentListDtoResult;
import com.xinder.api.response.dto.CommentsDtoResult;
import com.xinder.api.response.base.BaseResponse;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Xinder
 * @date 2023-02-19 21:14
 */
@Api(tags = "CommentsApi")
@RequestMapping("/comments")
public interface CommentsApi {

    /**
     * 根据帖子id查询阶梯评论
     * @param aid
     * @return
     */
    @RequestMapping("/list/{aid}")
    BaseResponse<CommentListDtoResult> getStepListByAid(@PathVariable("aid") Long aid);
}
