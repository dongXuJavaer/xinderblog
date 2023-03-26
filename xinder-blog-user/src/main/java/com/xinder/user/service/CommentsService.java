package com.xinder.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinder.api.bean.Comments;
import com.xinder.api.response.dto.CommentListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xinder
 * @since 2023-02-19
 */
public interface CommentsService extends IService<Comments> {

    /**
     * 根据帖子id查询阶梯评论
     * @param aid
     * @return
     */
    CommentListDtoResult getStepListByAid(Long aid);

    /**
     * 统计评论总数
     * @return
     */
    DtoResult getAllCommentsCount();

    /**
     * 发表评论
     * @param comments
     * @return
     */
    Result publishComments(Comments comments);
}
