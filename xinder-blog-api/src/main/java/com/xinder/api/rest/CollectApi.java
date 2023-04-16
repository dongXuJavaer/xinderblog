package com.xinder.api.rest;

import com.xinder.api.bean.Collect;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.ArticleListDtoResult;
import com.xinder.api.response.dto.CategoryListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @author Xinder
 * @date 2023-01-09 21:30
 */

@Api(tags = "CollectApi")
@RequestMapping("/collect")
public interface CollectApi {


    @ApiOperation(value = "某人获取自己的收藏帖子列表", notes = "某人获取自己的收藏帖子列表", tags = {"CollectApi"})
    @RequestMapping(value = "/list/{uid}", method = RequestMethod.POST)
    BaseResponse<ArticleListDtoResult> getCollectList(@PathVariable("uid") Long uid, @RequestBody PageDtoReq pageDtoReq);

    @ApiOperation(value = "某人是否已经收藏某帖子", notes = "某人是否已经收藏某帖子", tags = {"CollectApi"})
    @RequestMapping(value = "/flag", method = RequestMethod.GET)
    BaseResponse<DtoResult> collectFlag(@RequestParam("uid") Long uid, @RequestParam("aid") Long aid);

    @ApiOperation(value = "收藏", notes = "收藏", tags = {"CollectApi"})
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    BaseResponse<Result> addCollect(@RequestBody Collect collect);

    @ApiOperation(value = "取消收藏", notes = "取消收藏", tags = {"CollectApi"})
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    BaseResponse<Result> cancelCollect(@RequestBody Collect collect);

}
