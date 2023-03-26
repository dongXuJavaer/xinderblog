package com.xinder.api.rest;

import com.xinder.api.bean.Comments;
import com.xinder.api.response.dto.CommentListDtoResult;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * 获取总评论数
     * @return
     */
    @RequestMapping("/num")
    BaseResponse<DtoResult> CommentsCount();

    /**
     * 发表、回复
     * @return
     */
    @RequestMapping("/publish")
    BaseResponse<Result> publishComments(@RequestBody Comments comments);
}
