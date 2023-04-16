package com.xinder.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinder.api.bean.Collect;
import com.xinder.api.request.comm.PageDtoReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Xinder
 * @since 2023-04-16
 */
public interface CollectMapper extends BaseMapper<Collect> {

    /**
     * 分页查询
     *
     * @param req    要求事情
     * @param offset 抵消
     * @param uid    uid
     * @return {@link List}<{@link Collect}>
     */
    List<Collect> selectPageList(@Param("req") PageDtoReq req, @Param("offset") Long offset, @Param("uid") Long uid);
}
