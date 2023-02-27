package com.xinder.api.rest;

import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.UserDtoSimpleResult;
import com.xinder.api.response.dto.UserListDtoSimpleResult;
import com.xinder.api.response.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Xinder
 * @date 2023-02-03 18:20
 */
@Api(tags = "FollowApi")
@RequestMapping("/follow")
public interface FollowApi {

    /**
     * 关注用户
     *
     * @param uid 目标用户id
     * @return
     */
    @RequestMapping(value = "/{uid}")
    @ApiOperation(value = "关注用户", notes = "关注用户", tags = {"FollowApi"})
    BaseResponse<Result> follow(@PathVariable("uid") Long uid);

    /**
     * 取消关注
     *
     * @param uid 目标用户id
     * @return
     */
    @RequestMapping(value = "cancel/{uid}")
    @ApiOperation(value = "取消关注", notes = "取消关注", tags = {"FollowApi"})
    BaseResponse<Result> cancel(@PathVariable("uid") Long uid);

    /**
     * 某用户的关注列表
     *
     * @return
     */
    @RequestMapping(value = "list/{uid}")
    @ApiOperation(value = "关注列表", notes = "关注列表", tags = {"FollowApi"})
    BaseResponse<UserListDtoSimpleResult> followList(@PathVariable("uid") Long uid);

    /**
     * 某用户的粉丝列表
     *
     * @return
     */
    @RequestMapping(value = "fans/list/{uid}")
    @ApiOperation(value = "粉丝列表", notes = "粉丝列表", tags = {"FollowApi"})
    BaseResponse<UserListDtoSimpleResult> fansList(@PathVariable("uid") Long uid);

}
