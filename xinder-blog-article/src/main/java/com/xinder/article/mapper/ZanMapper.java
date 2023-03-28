package com.xinder.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinder.api.bean.Zan;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xinder
 * @since 2023-03-28
 */
public interface ZanMapper extends BaseMapper<Zan> {

    Zan getByAidAndUid(@Param("aid") Long aid, @Param("uid") Long uid);

    Long getCountByAid(@Param("aid") Long aid);
}
