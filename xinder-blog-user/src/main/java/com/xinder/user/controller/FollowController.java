package com.xinder.user.controller;


import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.UserDtoSimpleResult;
import com.xinder.api.response.dto.UserListDtoSimpleResult;
import com.xinder.api.response.result.Result;
import com.xinder.api.rest.FollowApi;
import com.xinder.common.abstcontroller.AbstractController;
import com.xinder.user.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Xinder
 * @since 2023-02-03
 */
@RestController
public class FollowController extends AbstractController implements FollowApi {

    @Autowired
    FollowService followService;

    @Override
    public BaseResponse<Result> follow(Long uid) {
        Result result = followService.follow(uid);
        return buildJson(result);
    }

    @Override
    public BaseResponse<Result> cancel(Long uid) {
        Result result = followService.cancel(uid);
        return buildJson(result);
    }

    @Override
    public BaseResponse<UserListDtoSimpleResult> followList(Long uid) {
        UserListDtoSimpleResult listDtoSimpleResult = followService.followList(uid);
        return buildJson(listDtoSimpleResult);
    }

    @Override
    public BaseResponse<UserListDtoSimpleResult> fansList(Long uid) {
        UserListDtoSimpleResult listDtoSimpleResult = followService.fansList(uid);
        return buildJson(listDtoSimpleResult);
    }
}

