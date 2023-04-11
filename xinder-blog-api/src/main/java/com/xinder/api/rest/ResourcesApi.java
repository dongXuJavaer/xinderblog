package com.xinder.api.rest;

import com.xinder.api.bean.Resources;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.ResourcesListDtoResult;
import com.xinder.api.response.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Xinder
 * @date 2023-04-08 22:55
 */
@Api(tags = "ResourcesApi")
@RequestMapping("/resources")
public interface ResourcesApi {

    @ApiOperation(value = "上传资源", notes = "积分明细", tags = {"PointApi"})
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    BaseResponse<String> uploadResources(MultipartFile file);

    @ApiOperation(value = "查看资源列表", notes = "查看资源列表", tags = {"PointApi"})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    BaseResponse<ResourcesListDtoResult> list(@RequestBody PageDtoReq pageDtoReq);

    @ApiOperation(value = "上传资源", notes = "积分明细", tags = {"PointApi"})
    @RequestMapping(value = "/download/{rid}", method = RequestMethod.GET)
    void downloadResources(@PathVariable("rid") Long rid, @RequestParam("uid") Long uid);

    @ApiOperation(value = "提交资源信息", notes = "积分明细", tags = {"PointApi"})
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    BaseResponse<Result> add(@RequestBody Resources resources);

    @ApiOperation(value = "删除资源信息", notes = "积分明细", tags = {"PointApi"})
    @RequestMapping(value = "/delete/{rid}", method = RequestMethod.POST)
    BaseResponse<Result> delete(@PathVariable("rid") Long rid);

    @ApiOperation(value = "根据id查询资源信息", notes = "根据id查询资源信息", tags = {"PointApi"})
    @RequestMapping(value = "/detail/{rid}", method = RequestMethod.POST)
    Resources getById(@PathVariable("rid") Long rid);

}
