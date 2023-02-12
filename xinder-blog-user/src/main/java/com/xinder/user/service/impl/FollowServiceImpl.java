package com.xinder.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.Follow;
import com.xinder.api.bean.User;
import com.xinder.api.enums.ResultCode;
import com.xinder.api.response.dto.UserDtoSimpleResult;
import com.xinder.api.response.dto.UserListDtoSimpleResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.common.util.TokenDecode;
import com.xinder.common.util.Util;
import com.xinder.user.mapper.FollowMapper;
import com.xinder.user.mapper.UserMapper;
import com.xinder.user.service.FollowService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xinder
 * @since 2023-02-03
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {

    @Autowired
    private TokenDecode tokenDecode;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public Result follow(Long uid) {
        User user = userMapper.getUserById(uid);
        if (user == null) {
            return Result.fail(ResultCode.FOLLOW_FAIL.getDesc());
        }

        User currentUser = Util.getCurrentUser(tokenDecode, redisTemplate);
        if (StringUtils.isEmpty(currentUser.getUsername())){
            return Result.fail("关注失败，未登录");
        }
        Follow follow = new Follow();
        follow.setUid(currentUser.getId());
        follow.setFollowedUid(user.getId());
        followMapper.insert(follow);
        return Result.success(ResultCode.FOLLOW_SUCCESS.getDesc());
    }

    @Override
    @Transactional
    public Result cancel(Long uid) {
        User currentUser = Util.getCurrentUser(tokenDecode, redisTemplate);
        int i = followMapper.deleteByUidAndFoll(currentUser.getId(), uid);
        Result result = i > 0 ? Result.success(ResultCode.FOLLOW_CANCEL_SUCCESS.getDesc()) :
                Result.fail(ResultCode.FOLLOW_CANCEL_FAILS.getDesc());
        return result;
    }

    @Override
    public UserListDtoSimpleResult followList(Long uid) {
        List<User> userList = userMapper.selectBatchFollow(uid);

        List<UserDtoSimpleResult> list = new ArrayList<>(userList.size());
        userList.forEach(user -> {
            UserDtoSimpleResult userDtoSimpleResult = DtoResult.dataDtoSuccess(UserDtoSimpleResult.class);
            BeanUtils.copyProperties(user, userDtoSimpleResult);
            list.add(userDtoSimpleResult);
        });
        UserListDtoSimpleResult listDtoSimpleResult = DtoResult.dataDtoSuccess(UserListDtoSimpleResult.class);
        listDtoSimpleResult.setList(list);
        return listDtoSimpleResult;
    }

    @Override
    public UserListDtoSimpleResult fansList(Long uid) {
        List<User> userList = userMapper.selectBatchFans(uid);

        List<UserDtoSimpleResult> list = new ArrayList<>(userList.size());
        userList.forEach(user -> {
            UserDtoSimpleResult userDtoSimpleResult = DtoResult.dataDtoSuccess(UserDtoSimpleResult.class);
            BeanUtils.copyProperties(user, userDtoSimpleResult);
            list.add(userDtoSimpleResult);
        });
        UserListDtoSimpleResult listDtoSimpleResult = DtoResult.dataDtoSuccess(UserListDtoSimpleResult.class);
        listDtoSimpleResult.setList(list);
        return listDtoSimpleResult;
    }
}
