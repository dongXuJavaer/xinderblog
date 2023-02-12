package com.xinder.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinder.api.bean.Group;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 群
 *
 * @author Xinder
 * @since 2023-02-11
 */
public interface GroupMapper extends BaseMapper<Group> {

    /**
     * 查询用户加入的群聊
     * @param uid
     * @return
     */
    List<Group> selectUserAddList(@Param("uid") Long uid);


    /**
     * 根据用户与id查询群聊
     * @param uid
     * @return
     */
    Group selectByUserId(@Param("uid") Integer uid, @Param("groupId") Integer groupId);

}
