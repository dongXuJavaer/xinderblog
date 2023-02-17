package com.xinder.api.rest;

import com.xinder.api.request.SocketInfoReq;
import com.xinder.api.request.SocketMsgReq;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.MsgDtoResult;
import com.xinder.api.response.dto.MsgDtoResultList;
import com.xinder.api.response.dto.SocketInfoListDtoResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Xinder
 * @date 2023-02-16 15:48
 */
@Api(tags = "SocketInfoApi")
@RequestMapping("/msg")
public interface MsgApi {

    /**
     * 查询与某人的消息信息
     * @param socketMsgReq
     * @return
     */
    @RequestMapping(
            value = "/list",
            method = RequestMethod.POST
    )
    BaseResponse<MsgDtoResultList> getMsgList(@RequestBody SocketMsgReq socketMsgReq);
}
