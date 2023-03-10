package com.xinder.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinder.api.bean.Article;
import com.xinder.api.request.ArticleDtoReq;
import com.xinder.api.response.dto.ArticleListDtoResult;
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
}
