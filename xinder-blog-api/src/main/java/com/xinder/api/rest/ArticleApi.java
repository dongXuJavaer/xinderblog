package com.xinder.api.rest;

import com.xinder.api.bean.Article;
import com.xinder.api.request.ArticleDtoReq;
import com.xinder.api.response.dto.ArticleListDtoResult;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Xinder
 * @date 2023-01-06 21:50
 */
@Api(tags = "ArticleApi")
@RequestMapping("/article")
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

    /**
     * 根据用户id查询
     * @return
     */
    @ApiOperation(value = "根据用户uid查询", notes = "根据文章aid查询", tags = {"ArticleController"})
    @RequestMapping(value = "/u", method = RequestMethod.POST)
    BaseResponse<ArticleListDtoResult> getArticleByUid(@RequestBody ArticleDtoReq articleDtoReq);


    /**
     * 上传封面图片
     * @param file
     * @return
     */
    @ApiOperation(value = "上传帖子封面", notes = "", tags = {"ArticleController"})
    @RequestMapping(value = "/upload/headpic", method = RequestMethod.POST)
    BaseResponse<String> uploadHeadPic(MultipartFile file);

    /**
     * 上传附件
     * @param file
     * @return
     */
    @ApiOperation(value = "上传资源文件", notes = "", tags = {"ArticleController"})
    @RequestMapping(value = "/upload/attachment", method = RequestMethod.POST)
    BaseResponse<String> uploadAttachment(MultipartFile file);

    /**
     * 发表文章
     * @param article ..
     * @return
     */
    @ApiOperation(value = "发表/修改 帖子", notes = "发表/修改 帖子----", tags = {"ArticleController"})
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    BaseResponse<String> publish(@RequestBody Article article);
}
