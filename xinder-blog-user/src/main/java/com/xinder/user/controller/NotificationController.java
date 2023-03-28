package com.xinder.user.controller;


import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.NotificationDtoListResult;
import com.xinder.api.rest.NotificationApi;
import com.xinder.common.abstcontroller.AbstractController;
import com.xinder.user.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
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
    @RequestMapping(value = "/comments/list", method = RequestMethod.POST)
    public BaseResponse<NotificationDtoListResult> commentsList() {
        NotificationDtoListResult listResult = notificationService.commentsList();
        return buildJson(listResult);
    }

    @Override
    public BaseResponse<NotificationDtoListResult> zanList() {
        NotificationDtoListResult listResult = notificationService.zanList();
        return buildJson(listResult);
    }

    @Override
    public BaseResponse<NotificationDtoListResult> sysList() {
        NotificationDtoListResult listResult = notificationService.sysList();
        return buildJson(listResult);
    }

    @Override
    public BaseResponse<NotificationDtoListResult> followList() {
        NotificationDtoListResult listResult = notificationService.followList();
        return buildJson(listResult);
    }
}

