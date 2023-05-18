package com.xinder.api.rest;

import com.xinder.api.bean.Article;
import com.xinder.api.request.ArticleDtoReq;
import com.xinder.api.response.dto.ArticleListDtoResult;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.ZanStateDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
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
    @ApiOperation(value = "分页查询博客文章", notes = "分页查询博客文章", tags = {"ArticleApi"})
    @RequestMapping(
            value = "/list",
            method = RequestMethod.POST
    )
    BaseResponse<ArticleListDtoResult> articleList(@RequestBody ArticleDtoReq articleDtoReq, @RequestParam("keywords") String keywords);

    /**
     * 根据博客id查询
     * @param aid
     * @return
     */
    @ApiOperation(value = "根据文章aid查询", notes = "根据文章aid查询", tags = {"ArticleApi"})
    @RequestMapping(value = "/{aid}", method = RequestMethod.GET)
    BaseResponse<DtoResult> getArticleById(@PathVariable("aid") Long aid);

    /**
     * 根据用户id查询
     * @return
     */
    @ApiOperation(value = "根据用户uid查询", notes = "根据文章aid查询", tags = {"ArticleApi"})
    @RequestMapping(value = "/u", method = RequestMethod.POST)
    BaseResponse<ArticleListDtoResult> getArticleByUid(@RequestBody ArticleDtoReq articleDtoReq);


    /**
     * 上传封面图片
     * @param file
     * @return
     */
    @ApiOperation(value = "上传帖子封面", notes = "", tags = {"ArticleApi"})
    @RequestMapping(value = "/upload/headpic", method = RequestMethod.POST)
    BaseResponse<String> uploadHeadPic(MultipartFile file);

    /**
     * 上传附件
     * @param file
     * @return
     */
    @ApiOperation(value = "上传资源文件", notes = "", tags = {"ArticleApi"})
    @RequestMapping(value = "/upload/attachment", method = RequestMethod.POST)
    BaseResponse<String> uploadAttachment(MultipartFile file);

    /**
     * 发表文章
     * @param article ..
     * @return
     */
    @ApiOperation(value = "发表/修改 帖子", notes = "发表/修改 帖子", tags = {"ArticleApi"})
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    BaseResponse<Result> publish(@RequestBody Article article);

    @ApiOperation(value = "删除帖子（放入回收站）", notes = "发表/修改 帖子", tags = {"ArticleApi"})
    @RequestMapping(value = "/remove/{aid}", method = RequestMethod.GET)
    BaseResponse<Result> remove(@PathVariable("aid") Long aid);

    /**
     * 导入数据
     * @return
     */
    @ApiOperation(value = "导入数据", notes = "导入数据", tags = {"ArticleApi"})
    @RequestMapping(value = "/import", method = RequestMethod.GET)
    BaseResponse<Result> importArticle();

    /**
     * 统计帖子数量
     * @return
     */
    @ApiOperation(value = "统计帖子数量", notes = "统计帖子数量", tags = {"ArticleApi"})
    @RequestMapping(value = "/num", method = RequestMethod.POST)
    BaseResponse<DtoResult> initNum();

    @ApiOperation(value = "点赞", notes = "点赞", tags = {"ArticleApi"})
    @RequestMapping(value = "/zan/{aid}", method = RequestMethod.GET)
    BaseResponse<Result> zan(@PathVariable("aid") Long aid);

    @ApiOperation(value = "当前文章的点赞状态", notes = "当前文章的点赞状态", tags = {"ArticleApi"})
    @RequestMapping(value = "/zan/state/{aid}", method = RequestMethod.GET)
    BaseResponse<ZanStateDtoResult> zanState(@PathVariable("aid") Long aid);

    @ApiOperation(value = "审核帖子", notes = "审核帖子", tags = {"ArticleApi"})
    @RequestMapping(value = "/audit", method = RequestMethod.PUT)
    BaseResponse<Result> audit(@RequestParam("aids") Long[] aids, @RequestParam("type") Integer type);

    @RequestMapping(value = "/dustbin", method = RequestMethod.PUT)
    @ApiOperation(value = "删除帖子", notes = "删除帖子", tags = {"ArticleApi"})
    BaseResponse<Result> updateArticleState(@RequestParam("aids") Long[] aids,@RequestParam("state")  Integer state);

    @RequestMapping(value = "/batch/id", method = RequestMethod.GET)
    @ApiOperation(value = "根据id批量查询", notes = "根据id批量查询", tags = {"ArticleApi"})
    ArticleListDtoResult getBatchById(@RequestParam("aid") Long[] aids);
}
