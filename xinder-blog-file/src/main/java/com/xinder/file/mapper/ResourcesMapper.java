package com.xinder.file.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinder.api.bean.Resources;
import com.xinder.api.request.comm.PageDtoReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Xinder
 * @since 2023-03-26
 */
public interface ResourcesMapper extends BaseMapper<Resources> {


    /**
     * 获取总数
     *
     * @param uid uid
     * @return {@link Long}
     */
    Long getCount(@Param("uid") Long uid);

    /**
     * 分页查询资源列表
     *
     * @return {@link List}<{@link Resources}>
     */
    List<Resources> pageList(PageDtoReq req, Long offset, Long uid);
}
