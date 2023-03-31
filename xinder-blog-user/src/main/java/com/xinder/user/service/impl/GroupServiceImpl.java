package com.xinder.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.Group;
import com.xinder.api.bean.GroupUser;
import com.xinder.api.bean.User;
import com.xinder.api.response.dto.GroupDtoListResult;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.dto.UserDtoSimpleResult;
import com.xinder.api.response.dto.UserListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.user.mapper.GroupMapper;
import com.xinder.user.mapper.GroupUserMapper;
import com.xinder.user.mapper.UserMapper;
import com.xinder.user.service.GroupService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xinder
 * @since 2023-02-11
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    @Resource
    private GroupMapper groupMapper;

    @Resource
    private GroupUserMapper groupUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public GroupDtoListResult getList(Group group) {
        QueryWrapper<Group> groupQueryWrapper = new QueryWrapper<>(group);
        List<Group> groups = groupMapper.selectList(groupQueryWrapper);
        GroupDtoListResult groupDtoListResult = DtoResult.dataDtoSuccess(GroupDtoListResult.class);
        groupDtoListResult.setList(groups);
        return groupDtoListResult;
    }

    @Override
    public GroupDtoListResult userAddList(Long uid) {
        GroupUser groupUser = new GroupUser();
        List<Group> groupList = this.groupMapper.selectUserAddList(uid);
        GroupDtoListResult groupDtoListResult = DtoResult.dataDtoSuccess(GroupDtoListResult.class);
        groupDtoListResult.setList(groupList);
        return groupDtoListResult;
    }

    @Override
    public Result userAdd(GroupUser groupUser) {
        Group group = groupMapper.selectById(groupUser.getGroupId());
        groupUser.setGroupName(group.getName());
        boolean b = judgeUserInGroup(groupUser.getUid(), groupUser.getGroupId());
        if (b) {
            return Result.fail("已经加入该群");
        }
        int i = groupUserMapper.insert(groupUser);
        return i > 0 ? Result.success("加入成功") : Result.success("加入失败");
    }

    @Override
    public Result userQuit(GroupUser groupUser) {
        LambdaUpdateWrapper<GroupUser> lambdaQueryWrapper = new LambdaUpdateWrapper<>();
        lambdaQueryWrapper.eq(GroupUser::getGroupId, groupUser.getGroupId());
        lambdaQueryWrapper.eq(GroupUser::getUid, groupUser.getUid());
        int delete = groupUserMapper.delete(lambdaQueryWrapper);
        if (delete > 0) {
            return Result.success();
        } else {
            return Result.fail("退出失败");
        }
    }

    // 判断用户是否已经在某群中    true: 在某群中    false: 不在
    private boolean judgeUserInGroup(Integer uid, Integer groupId) {
        Group g1 = groupUserMapper.selectByUidAndGroupId(uid, groupId);
        Group g2 = groupMapper.selectById(groupId);
        if (!Objects.equals(g2.getCreateUser(), uid)) {
            g2 = null;
        }
        return g1 != null || g2 != null;
    }

    @Override
    public Result createGroup(Group group) {
        int i = groupMapper.insert(group);
        return i > 0 ? Result.success("创建成功") : Result.fail("创建失败");
    }

    @Override
    public DtoResult getGroupById(Integer id) {
        Group group = groupMapper.selectById(id);
        DtoResult dtoResult = DtoResult.success();
        dtoResult.setData(group);
        return dtoResult;
    }

    @Override
    public Result updateGroup(Group group) {
        int i = groupMapper.updateById(group);
        return i > 0 ? Result.success("修改成功") : Result.fail("修改失败");
    }

    @Override
    @Transactional
    public Result disband(Integer id) {
        LambdaUpdateWrapper<GroupUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(GroupUser::getGroupId, id);
        groupUserMapper.delete(wrapper);
        int i = groupMapper.deleteById(id);
        // TODO: 2023-02-12  解散的同时给所有用户发送通知
        return i > 0 ? Result.success("解散成功") : Result.fail("解散失败");
    }

    @Override
    public UserListDtoResult userList(Integer id) {
        Group group = groupMapper.selectById(id);
        UserListDtoResult dtoResult = null;
        if (group == null) {
            dtoResult = DtoResult.dataDtoFail(UserListDtoResult.class);
            dtoResult.setMsg("群聊已解散");
            return dtoResult;
        }
        User creator = userMapper.selectById(group.getCreateUser());
        UserDtoResult createDto = new UserDtoResult();
        BeanUtils.copyProperties(creator, createDto);
        createDto.setCreateTime(group.getCreateTime()); // 设置时间为用户加入群聊的时间
        List<User> userList = groupUserMapper.getUserByGroupId(id);

        List<UserDtoResult> dtoList = new ArrayList<>(userList.size() + 1);
        dtoList.add(createDto);
        userList.forEach(user -> {
            UserDtoResult userDtoResult = new UserDtoResult();
            BeanUtils.copyProperties(user, userDtoResult);
            dtoList.add(userDtoResult);
        });

        dtoResult = DtoResult.dataDtoSuccess(UserListDtoResult.class);
        dtoResult.setList(dtoList);
        return dtoResult;
    }
}
