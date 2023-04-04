package com.xinder.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinder.api.bean.Role;
import com.xinder.api.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by sang on 2017/12/17.
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User loadUserByUsername(@Param("username") String username);

    long reg(User user);

    int updateUserEmail(@Param("email") String email, @Param("id") Long id);

    List<User> getUserByNickname(@Param("nickname") String nickname);

    List<Role> getAllRole();

    int updateUserEnabled(@Param("enabled") Boolean enabled, @Param("uid") Long uid);

    int deleteUserById(Long uid);

    int deleteUserRolesByUid(Long id);

    int setUserRoles(@Param("rids") Long[] rids, @Param("id") Long id);

    User getUserById(@Param("id") Long id);

    /**
     * 查询openId查询用户
     * @param openId
     * @return
     */
    User selectByOpenid(String openId);

    /**
     * 查询关注列表
     * @param uid
     * @return
     */
    List<User> selectBatchFollow(@Param("uid") Long uid);

    /**
     * 查询粉丝列表
     * @param uid
     * @return
     */
    List<User> selectBatchFans(@Param("uid") Long uid);

    /**
     * 取消绑定qq
     *
     * @param user 用户
     * @return int
     */
    int cancelBindQQ(User user);
}
