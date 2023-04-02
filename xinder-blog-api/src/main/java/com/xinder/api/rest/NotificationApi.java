package com.xinder.api.rest;

import com.xinder.api.bean.Notification;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.NotificationDtoListResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.PageDtoResult;
import com.xinder.api.response.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

/**
 * @author Xinder
 * @date 2023-03-28 16:01
 */
@Api(tags = "NotificationApi")
@RequestMapping("/notification")
public interface NotificationApi {

    @ApiOperation(value = "获取 评论/点赞 通知的总数", notes = "获取 评论/点赞 通知的总数", tags = {"NotificationApi"})
    @RequestMapping(value = "/count/{type}", method = RequestMethod.POST)
    BaseResponse<DtoResult> getCount(@PathVariable("type") Integer type);

    @ApiOperation(value = "查看评论通知列表", notes = "查看评论通知列表", tags = {"NotificationApi"})
    @RequestMapping(value = "/comments/list", method = RequestMethod.POST)
    BaseResponse<NotificationDtoListResult> commentsList(@RequestBody PageDtoReq pageDtoReq);

    @ApiOperation(value = "查看点赞通知列表", notes = "查看评论通知列表", tags = {"NotificationApi"})
    @RequestMapping(value = "/zan/list", method = RequestMethod.POST)
    BaseResponse<NotificationDtoListResult> zanList(@RequestBody PageDtoReq pageDtoReq);

    @ApiOperation(value = "查看系统通知列表", notes = "查看系统通知列表", tags = {"NotificationApi"})
    @RequestMapping(value = "/sys/list", method = RequestMethod.POST)
    BaseResponse<NotificationDtoListResult> sysList(@RequestBody PageDtoReq pageDtoReq);

    @ApiOperation(value = "查看关注通知列表", notes = "查看关注通知列表", tags = {"NotificationApi"})
    @RequestMapping(value = "/follow/list", method = RequestMethod.POST)
    BaseResponse<NotificationDtoListResult> followList();

    @ApiOperation(value = "添加通知", notes = "添加通知", tags = {"NotificationApi"})
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    BaseResponse<Result> add(@RequestBody Notification notification);

    @ApiOperation(value = "删除通知", notes = "删除通知", tags = {"NotificationApi"})
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    BaseResponse<Result> remove(@RequestBody Notification notification);


}
