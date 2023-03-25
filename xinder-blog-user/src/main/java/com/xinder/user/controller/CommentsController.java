package com.xinder.user.controller;


import com.xinder.api.bean.Comments;
import com.xinder.api.response.dto.CommentListDtoResult;
import com.xinder.api.response.dto.CommentsDtoResult;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.rest.CommentsApi;
import com.xinder.common.abstcontroller.AbstractController;
import com.xinder.user.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Xinder
 * @since 2023-02-19
 */
@RestController
@RequestMapping("/comments")
public class CommentsController extends AbstractController implements CommentsApi {

    @Autowired
    private CommentsService commentsService;

    @Override
    public BaseResponse<CommentListDtoResult> getStepListByAid(Long aid) {
        CommentListDtoResult dtoResult = commentsService.getStepListByAid(aid);
        return buildJson(dtoResult);
    }
}

