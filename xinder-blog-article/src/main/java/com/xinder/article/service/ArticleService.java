package com.xinder.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinder.api.bean.Article;
import com.xinder.api.request.ArticleDtoReq;
import com.xinder.api.response.dto.ArticleListDtoResult;
import com.xinder.api.response.dto.ZanStateDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;


/**
 * @author Xinder
 * @date 2023-01-06 23:06
 */
public interface ArticleService extends IService<Article> {

    /**
     * 获取博客列表（带分页）
     * @param articleDtoReq
     * @return
     */
    ArticleListDtoResult getArticleByState(ArticleDtoReq articleDtoReq, String keywords);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    DtoResult getById(Long id);

    /**
     * 通用查询文章
     * @param articleDtoReq
     * @return
     */
    ArticleListDtoResult getList(ArticleDtoReq articleDtoReq);

    /**
     * 发布帖子
     * @param article
     * @return
     */
    Result publish(Article article);

    /**
     * 数据导入es
     * @return
     */
    Result importArticle();

    /**
     * 统计所有帖子总数
     * @return
     */
    DtoResult getCount();

    /**
     * 帖子点赞
     * @return
     */
    Result zan(Long aid);

    /**
     * 当前文章的点赞状态: 包括 总点赞数、当前用户是否点赞
     * @param aid
     * @return
     */
    ZanStateDtoResult zanState(Long aid);


    /**
     * 审核文章
     * @param aid
     * @param type  1 通过审核，   0 未通过审核，将帖子状态设置为0
     * @return
     */
    Result audit(Long[] aid, Integer type);

    /**
     * 通过id获查询
     *
     * @param aids 帖子id列表
     * @return {@link ArticleListDtoResult}
     */
    ArticleListDtoResult getBatchById(Long[] aids);

    /**
     * 用户删除帖子（放入回收站）
     * @param aid
     * @return
     */
    Result removeArticle(Long aid);
}
