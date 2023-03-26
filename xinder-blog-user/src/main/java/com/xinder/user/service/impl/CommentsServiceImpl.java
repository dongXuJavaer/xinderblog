package com.xinder.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.Article;
import com.xinder.api.bean.Comments;
import com.xinder.api.bean.Notification;
import com.xinder.api.enums.NotificationEnums;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.CommentListDtoResult;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.user.feign.ArticleFeignClient;
import com.xinder.user.mapper.CommentsMapper;
import com.xinder.user.mapper.NotificationMapper;
import com.xinder.user.service.CommentsService;
import com.xinder.user.service.NotificationService;
import com.xinder.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private ArticleFeignClient articleFeignClient;

    @Override
    public CommentListDtoResult getStepListByAid(Long aid) {
        // 获取根评论
        List<Comments> commentsList = commentsMapper.getRootListByAid(aid);
        // 获取根评论下的回复内容
        buildCommentsList(commentsList);
        CommentListDtoResult dtoResult = DtoResult.dataDtoSuccess(CommentListDtoResult.class);
        dtoResult.setList(commentsList);
        return dtoResult;
    }

    /**
     * 给根评论的所有子评论
     *
     * @param rootCommentsList 根评论集合
     */
    private void buildCommentsList(List<Comments> rootCommentsList) {
        rootCommentsList.forEach(root -> {
            List<Comments> list = commentsMapper.getListByParentId(root.getAid(), root.getId());
            if (list != null) {
                root.setSubComments(list);
                ArrayList<Comments> all = new ArrayList<>(list);
                root.setAllSubComments(all);
                buildAllSubComments(root, list, root);
            }
        });

        rootCommentsList.forEach(root -> root.getAllSubComments().sort((o1, o2) -> o2.getId().compareTo(o1.getId())));

    }

    /**
     * 给每个根评论构建所有子评论
     *
     * @param parentComment   当前子评论集合的直接父评论
     * @param subCommentsList 子评论集合
     * @param rootComments    当前子评论集合对应的根评论
     */
    private void buildAllSubComments(Comments parentComment, List<Comments> subCommentsList, Comments rootComments) {

        for (Comments comments : subCommentsList) {
            List<Comments> list = commentsMapper.getListByParentId(comments.getAid(), comments.getId());
            comments.setSubComments(list);

            // 设置父节点
            Comments parent = new Comments();
            BeanUtils.copyProperties(parentComment, parent);
            // 设置为null，解决循环死链
            parent.setSubComments(null);
            parent.setAllSubComments(null);
            comments.setParentComments(parent);

            rootComments.getAllSubComments().addAll(list);
            buildAllSubComments(comments, list, rootComments);
        }
    }

    @Override
    public DtoResult getAllCommentsCount() {
        int count = count();
        DtoResult result = DtoResult.success();
        result.setData(count);
        return result;
    }

    @Override
    public Result publishComments(Comments comments) {
        if (StringUtils.isEmpty(comments.getContent())) {
            return Result.fail("评论信息不能为空");
        }

        UserDtoResult currentUser = userService.getCurrentUser();
        if (currentUser.getId() == null) {
            return Result.fail("未登录");
        }

        comments.setUid(currentUser.getId());
        BaseResponse<DtoResult> resp = articleFeignClient.getArticleById(comments.getAid());
        Article article = JSON.parseObject(JSON.toJSONString(resp.getData().getData()), Article.class);

        transactionTemplate.executeWithoutResult((status) -> {
            commentsMapper.insert(comments);

            // 给帖子的作者发通知
            Notification notification = new Notification()
                    .setType(NotificationEnums.COMMENTS.getCode())
                    .setContent(comments.getContent())
                    .setFromUid(currentUser.getId())
                    .setToUid(article.getUid());
            notificationMapper.insert(notification);

            // 如果不是根评论，那么被回复的那个人也要被通知到
            if (comments.getParentId() != null) {
                Comments com = commentsMapper.selectById(comments.getParentId());
                notification.setToUid(com.getUid());
            }
        });

        return Result.success("评论成功");
    }
}
