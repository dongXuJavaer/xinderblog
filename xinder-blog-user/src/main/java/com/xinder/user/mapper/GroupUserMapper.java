package com.xinder.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinder.api.bean.Group;
import com.xinder.api.bean.GroupUser;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xinder
 * @since 2023-02-11
 */
public interface GroupUserMapper extends BaseMapper<GroupUser> {


    /**
     * 查询某用户加入的群聊
     * @param uid
     * @return
     */
    GroupUser selectByUid(@Param("uid") Long uid);

    /**
     * 根据uid和groupId查询
     * @param uid
     * @param groupId
     * @return
     */
    Group selectByUidAndGroupId(@Param("uid") Integer uid, @Param("groupId") Integer groupId);
}
