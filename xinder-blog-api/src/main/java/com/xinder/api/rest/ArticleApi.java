package com.xinder.api.rest;

import com.xinder.api.request.ArticleDtoReq;
import com.xinder.api.response.dto.ArticleListDtoResult;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Xinder
 * @date 2023-01-06 21:50
 */
@Api(tags = "ArticleController")
public interface ArticleApi {


    /**
     * 文章列表（有分页）
     * @param articleDtoReq ...
     * @return ...
     */
    @ApiOperation(value = "分页查询博客文章", notes = "分页查询博客文章", tags = {"ArticleController"})
    @RequestMapping(
            value = "/list",
            method = RequestMethod.POST
    )
    BaseResponse<ArticleListDtoResult> articleList(ArticleDtoReq articleDtoReq, String keywords);

    /**
     * 根据博客id查询
     * @param aid
     * @return
     */
    @ApiOperation(value = "根据文章aid查询", notes = "根据文章aid查询", tags = {"ArticleController"})
    @RequestMapping(value = "/{aid}", method = RequestMethod.GET)
    BaseResponse<DtoResult> getArticleById(@PathVariable("aid") Long aid);
}
