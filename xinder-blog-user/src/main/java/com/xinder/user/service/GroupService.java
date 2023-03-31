package com.xinder.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinder.api.bean.Group;
import com.xinder.api.bean.GroupUser;
import com.xinder.api.response.dto.GroupDtoListResult;
import com.xinder.api.response.dto.UserDtoSimpleResult;
import com.xinder.api.response.dto.UserListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Xinder
 * @since 2023-02-11
 */
public interface GroupService extends IService<Group> {

    /**
     * 用户创建的群聊列表
     *
     * @param group
     * @return
     */
    GroupDtoListResult getList(Group group);

    /**
     * 用户加入的群聊列表
     *
     * @param uid
     * @return
     */
    GroupDtoListResult userAddList(@Param("uid") Long uid);

    /**
     * 用户加入群聊
     *
     * @param groupUser
     * @return
     */
    Result userAdd(GroupUser groupUser);

    /**
     * 用户退出群聊
     *
     * @param groupUser
     * @return
     */
    Result userQuit(GroupUser groupUser);

    /**
     * 创建群聊
     *
     * @param group
     * @return
     */
    Result createGroup(Group group);

    /**
     * 根据id查询群聊信息
     *
     * @param id
     * @return
     */
    DtoResult getGroupById(Integer id);

    /**
     * 修改群聊
     *
     * @param group
     * @return
     */
    Result updateGroup(Group group);

    /**
     * 解散群聊
     *
     * @param id
     * @return
     */
    Result disband(Integer id);


    /**
     * 获取群聊成员列表
     * @param id
     * @return
     */
    UserListDtoResult userList(Integer id);
}
