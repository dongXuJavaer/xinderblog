package com.xinder.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinder.api.bean.MsgPrivate;
import com.xinder.api.request.SocketMsgReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xinder
 * @since 2023-02-15
 */
public interface MsgPrivateMapper extends BaseMapper<MsgPrivate> {


    /**
     * 查看与某人的聊天记录
     * @param socketMsgReq
     * @return
     */
    List<MsgPrivate> selectSocketMsgList(@Param("req") SocketMsgReq socketMsgReq);
}
