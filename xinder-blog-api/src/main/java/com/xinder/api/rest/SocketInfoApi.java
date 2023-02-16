package com.xinder.api.rest;

import com.xinder.api.bean.SocketInfo;
import com.xinder.api.request.SocketInfoReq;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.SocketInfoListDtoResult;
import com.xinder.api.response.result.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Xinder
 * @date 2023-02-13 15:35
 */
@Api(tags = "SocketInfoApi")
@RequestMapping("/socket")
public interface SocketInfoApi {


    /**
     * 用户获取聊天框列表
     *
     * @return
     */
    @RequestMapping("/list")
    BaseResponse<SocketInfoListDtoResult> getSocketInfoList(@RequestBody SocketInfoReq socketInfoReq);

    /**
     * 发起聊天
     *
     * @return
     */
    @RequestMapping("/add")
    BaseResponse<Result> addSocket(
            @RequestParam("fromUid") Integer fromUid,
            @RequestParam("toId") Integer toId,
            @RequestParam("type") Integer type
    );

    /**
     * 移除某人的聊天框
     */
    @RequestMapping("/remove/{id}")
    BaseResponse<Result> remove(@PathVariable("id") Long id);
}
