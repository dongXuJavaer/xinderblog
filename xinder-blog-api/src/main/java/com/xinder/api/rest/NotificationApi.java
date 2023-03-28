package com.xinder.api.rest;

import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.NotificationDtoListResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Xinder
 * @date 2023-03-28 16:01
 */
@Api(tags = "NotificationApi")
@RequestMapping("/notification")
public interface NotificationApi {

    @ApiOperation(value = "查看评论通知列表", notes = "查看评论通知列表", tags = {"NotificationApi"})
    @RequestMapping(value = "/comments/list", method = RequestMethod.POST)
    BaseResponse<NotificationDtoListResult> commentsList();

    @ApiOperation(value = "查看点赞通知列表", notes = "查看评论通知列表", tags = {"NotificationApi"})
    @RequestMapping(value = "/zan/list", method = RequestMethod.POST)
    BaseResponse<NotificationDtoListResult> zanList();

    @ApiOperation(value = "查看系统通知列表", notes = "查看系统通知列表", tags = {"NotificationApi"})
    @RequestMapping(value = "/sys/list", method = RequestMethod.POST)
    BaseResponse<NotificationDtoListResult> sysList();

    @ApiOperation(value = "查看关注通知列表", notes = "查看关注通知列表", tags = {"NotificationApi"})
    @RequestMapping(value = "/follow/list", method = RequestMethod.POST)
    BaseResponse<NotificationDtoListResult> followList();
}
