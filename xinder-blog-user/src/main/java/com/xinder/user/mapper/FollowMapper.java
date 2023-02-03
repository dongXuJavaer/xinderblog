package com.xinder.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinder.api.bean.Follow;
import com.xinder.api.bean.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Xinder
 * @since 2023-02-03
 */
public interface FollowMapper extends BaseMapper<Follow> {

    int deleteByUidAndFoll(@Param("currUid") Long currUid, @Param("followedUid") Long uid);

    List<Follow> selectFollowByUid(@Param("uid") Long uid);
}
