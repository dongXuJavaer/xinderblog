package com.xinder.article.service;

import com.xinder.api.request.ArticleDtoReq;
import com.xinder.api.response.ArticleListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;


/**
 * @author Xinder
 * @date 2023-01-06 23:06
 */
public interface ArticleService {

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

}
