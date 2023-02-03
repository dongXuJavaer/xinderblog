package com.xinder.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinder.api.bean.Follow;
import com.xinder.api.response.dto.UserDtoSimpleResult;
import com.xinder.api.response.dto.UserListDtoSimpleResult;
import com.xinder.api.response.result.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xinder
 * @since 2023-02-03
 */
public interface FollowService extends IService<Follow> {

    /**
     * 关注某用户
     * @param uid
     * @return
     */
    Result follow(Long uid);

    /**
     * 取消关注某用户
     * @param uid
     * @return
     */
    Result cancel(Long uid);

    /**
     * 某用户的关注列表
     * @param uid
     * @return
     */
    UserListDtoSimpleResult followList(Long uid);

    /**
     * 某用户的粉丝列表
     * @param uid
     * @return
     */
    UserListDtoSimpleResult fansList(Long uid);
}
