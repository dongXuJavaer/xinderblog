package com.xinder.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinder.api.bean.SocketInfo;
import com.xinder.api.request.SocketInfoReq;
import com.xinder.api.response.dto.SocketInfoListDtoResult;
import com.xinder.api.response.result.Result;

/**
 * <p>
 * 聊天框服务类
 * </p>
 *
 * @author Xinder
 * @since 2023-02-13
 */
public interface SocketInfoService extends IService<SocketInfo> {

    /**
     * 获取聊天框列表
     * @return
     */
    SocketInfoListDtoResult getSocketInfoList(SocketInfoReq socketInfoReq);

    /**
     * 发起私信，添加聊天socket
     * @return
     */
    Result addSocket(Integer fromUid, Integer toId, Integer type);

    /**
     * 关闭某人的私信socket
     * @param id
     * @return
     */
    Result removeSocket(Long id);
}
