package com.xinder.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinder.api.bean.SocketInfo;
import com.xinder.api.request.SocketInfoReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xinder
 * @since 2023-02-13
 */
public interface SocketInfoMapper extends BaseMapper<SocketInfo> {

    /**
     * 查询列表
     * @param socketInfoReq
     * @return
     */
    List<SocketInfo> list(@Param("req") SocketInfoReq socketInfoReq);
}
