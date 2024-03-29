package com.xinder.api.rest;

import com.xinder.api.bean.PointInfo;
import com.xinder.api.request.UserDtoReq;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.RespBean;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.PointInfoListDtoResult;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.dto.UserDtoSimpleResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Xinder
 * @date 2023-01-08 10:54
 */
@Api(tags = "PointApi")
@RequestMapping("/point")
public interface PointInfoApi {


    @ApiOperation(value = "用户积分明细", notes = "积分明细", tags = {"PointApi"})
    @RequestMapping(value = "/list/{uid}", method = RequestMethod.POST)
    BaseResponse<PointInfoListDtoResult> getListByUid(@PathVariable("uid") Long uid, @RequestBody PageDtoReq pageDtoReq);

    @ApiOperation(value = "获取某人总积分", notes = "积分明细", tags = {"PointApi"})
    @RequestMapping(value = "/count/{uid}", method = RequestMethod.GET)
    BaseResponse<DtoResult> getPointCount(@PathVariable("uid") Long uid);

    @ApiOperation(value = "签到", notes = "签到", tags = {"PointApi"})
    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    BaseResponse<Result> signIn(@RequestBody PointInfo pointInfo);

    @ApiOperation(value = "积分扣减", notes = "积分扣减", tags = {"PointApi"})
    @RequestMapping(value = "/reduce", method = RequestMethod.POST)
    BaseResponse<Result> reduce(@RequestBody PointInfo pointInfo);

    @ApiOperation(value = "查询用户对某资源是否下载过", notes = "查询用户对某资源是否下载过", tags = {"PointApi"})
    @RequestMapping(value = "/judge", method = RequestMethod.POST)
    BaseResponse<DtoResult> getByUidAndRid(@RequestParam("uid") Long uid,@RequestParam("rid") Long rid);

    @ApiOperation(value = "增加积分", notes = "查询用户对某资源是否下载过", tags = {"PointApi"})
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    BaseResponse<Result> add(@RequestBody PointInfo pointInfo);

}
