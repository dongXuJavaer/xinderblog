package com.xinder.user.controller;

import com.xinder.api.bean.MsgPrivate;
import com.xinder.api.request.SocketMsgReq;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.MsgDtoResult;
import com.xinder.api.response.dto.MsgDtoResultList;
import com.xinder.api.response.dto.SocketInfoListDtoResult;
import com.xinder.api.rest.MsgApi;
import com.xinder.common.abstcontroller.AbstractController;
import com.xinder.user.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Xinder
 * @date 2023-02-16 15:47
 */
@RestController
@RequestMapping("/msg")
public class MsgController extends AbstractController implements MsgApi {


    @Autowired
    private MsgService msgService;

    @Override
    public BaseResponse<MsgDtoResultList> getMsgList(SocketMsgReq socketMsgReq) {
        MsgDtoResultList msgList = msgService.getMsgList(socketMsgReq);
        return buildJson(msgList);
    }
}
