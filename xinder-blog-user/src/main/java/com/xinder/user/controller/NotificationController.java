package com.xinder.user.controller;


import com.xinder.api.bean.Notification;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.NotificationDtoListResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.PageDtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.api.rest.NotificationApi;
import com.xinder.common.abstcontroller.AbstractController;
import com.xinder.user.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Xinder
 * @since 2023-03-26
 */
@RestController
@RequestMapping("/notification")
public class NotificationController extends AbstractController implements NotificationApi {

    @Autowired
    private NotificationService notificationService;

    @Override
    public BaseResponse<DtoResult> getCount(Integer type) {
        DtoResult dtoResult = notificationService.getCount(type);
        return buildJson(dtoResult);
    }

    @Override
    public BaseResponse<NotificationDtoListResult> commentsList(PageDtoReq pageDtoReq) {
        NotificationDtoListResult listResult = notificationService.commentsList(pageDtoReq);
        return buildJson(listResult);
    }

    @Override
    public BaseResponse<NotificationDtoListResult> zanList(PageDtoReq pageDtoReq) {
        NotificationDtoListResult listResult = notificationService.zanList(pageDtoReq);
        return buildJson(listResult);
    }

    @Override
    public BaseResponse<NotificationDtoListResult> sysList(PageDtoReq pageDtoReq) {
        NotificationDtoListResult listResult = notificationService.sysList(pageDtoReq);
        return buildJson(listResult);
    }

    @Override
    public BaseResponse<NotificationDtoListResult> followList() {
        NotificationDtoListResult listResult = notificationService.followList();
        return buildJson(listResult);
    }

    @Override
    public BaseResponse<Result> add(Notification notification) {
        Result result = notificationService.add(notification);
        return buildJson(result);
    }

    @Override
    public BaseResponse<Result> remove(Notification notification) {
        Result result = notificationService.delete(notification);
        return buildJson(result);
    }
}

