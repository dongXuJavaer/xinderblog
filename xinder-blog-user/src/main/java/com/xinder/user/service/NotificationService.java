package com.xinder.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinder.api.bean.Notification;
import com.xinder.api.response.dto.NotificationDtoListResult;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Xinder
 * @since 2023-03-26
 */
public interface NotificationService extends IService<Notification> {

    /**
     * 评论通知列表
     *
     * @return {@link NotificationDtoListResult}
     */
    NotificationDtoListResult commentsList();

    /**
     * 点赞通知列表
     *
     * @return
     */
    NotificationDtoListResult zanList();

    /**
     * sys列表
     * 系统通知列表
     *
     * @return {@link NotificationDtoListResult}
     */
    NotificationDtoListResult sysList();

    /**
     * 关注通知列表
     *
     * @return {@link NotificationDtoListResult}
     */
    NotificationDtoListResult followList();
}
