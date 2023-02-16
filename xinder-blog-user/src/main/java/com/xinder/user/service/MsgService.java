package com.xinder.user.service;

import com.xinder.api.request.SocketMsgReq;
import com.xinder.api.response.dto.MsgDtoResult;
import com.xinder.api.response.dto.MsgDtoResultList;

/**
 * @author Xinder
 * @date 2023-02-16 15:54
 */
public interface MsgService {


    /**
     * 查询某人的消息列表
     * @param socketMsgReq
     * @return
     */
    MsgDtoResultList getMsgList(SocketMsgReq socketMsgReq);

}
