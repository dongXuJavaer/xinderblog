package com.xinder.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinder.api.bean.PointInfo;
import com.xinder.api.request.comm.PageDtoReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xinder
 * @since 2023-03-30
 */
public interface PointInfoMapper extends BaseMapper<PointInfo> {

    Long getCount(@Param("uid") Long uid);

    List<PointInfo> getPointInfoList(@Param("req") PageDtoReq req, @Param("offset") Long offset, @Param("uid") Long uid);
}
