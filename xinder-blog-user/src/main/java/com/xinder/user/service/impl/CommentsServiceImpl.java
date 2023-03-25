package com.xinder.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.Comments;
import com.xinder.api.response.dto.CommentListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.user.mapper.CommentsMapper;
import com.xinder.user.service.CommentsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 评论service类
 * </p>
 *
 * @author Xinder
 * @since 2023-02-19
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements CommentsService {

    @Resource
    private CommentsMapper commentsMapper;

    @Override
    public CommentListDtoResult getStepListByAid(Long aid) {
        // 获取根评论
        List<Comments> commentsList = commentsMapper.getRootListByAid(aid);
        // 获取根评论下的回复内容
        buildCommentsList(null, commentsList);
        CommentListDtoResult dtoResult = DtoResult.dataDtoSuccess(CommentListDtoResult.class);
        dtoResult.setList(commentsList);
        return dtoResult;
    }

    /**
     * 查询评论的子评论
     *
     * @param parentComment
     * @param subCommentsList
     */
    private void buildCommentsList(Comments parentComment, List<Comments> subCommentsList) {
        if (subCommentsList != null) {

            subCommentsList.forEach(comments -> {
                List<Comments> list = commentsMapper.getListByParentId(comments.getAid(), comments.getId());
                comments.setSubComments(list);
//                Optional.ofNullable(parentComment).ifPresent(comments::setParentComments);
                comments.setParentComments(parentComment);

                Comments subParent = new Comments();
                BeanUtils.copyProperties(comments, subParent);
                // 设置为null，解决循环死链
                subParent.setSubComments(null);
                buildCommentsList(subParent, list);
            });
        }
    }
}
