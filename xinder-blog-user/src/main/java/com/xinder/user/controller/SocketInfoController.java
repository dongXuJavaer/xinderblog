package com.xinder.user.controller;


import com.xinder.api.bean.SocketInfo;
import com.xinder.api.request.SocketInfoReq;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.SocketInfoListDtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.api.rest.SocketInfoApi;
import com.xinder.common.abstcontroller.AbstractController;
import com.xinder.user.service.SocketInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.Resources;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Xinder
 * @since 2023-02-13
 */
@RestController
@RequestMapping("/socket")
public class SocketInfoController extends AbstractController implements SocketInfoApi {

    @Autowired
    private SocketInfoService socketInfoService;


    @Override
    public BaseResponse<SocketInfoListDtoResult> getSocketInfoList(SocketInfoReq socketInfoReq) {
        SocketInfoListDtoResult dtoResult = socketInfoService.getSocketInfoList(socketInfoReq);
        return buildJson(dtoResult);
    }

    @Override
    public BaseResponse<Result> addSocket(Integer fromUid, Integer toId, Integer type) {
        Result result = socketInfoService.addSocket(fromUid, toId, type);
        return buildJson(result);
    }
}

