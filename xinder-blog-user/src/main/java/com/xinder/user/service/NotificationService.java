package com.xinder.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinder.api.bean.Notification;
import com.xinder.api.response.dto.NotificationDtoListResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.PageDtoResult;
import com.xinder.api.response.result.Result;

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
     * 获取当前通知类型的总数
     *
     * @return {@link DtoResult}
     */
    DtoResult getCount(Integer type);

    /**
     * 评论通知列表
     *
     * @param pageDtoResult 页面dto结果
     * @return {@link NotificationDtoListResult}
     */
    NotificationDtoListResult commentsList(PageDtoResult pageDtoResult);

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

    /**
     * 发送通知
     *
     * @param notification 通知
     * @return {@link Result}
     */
    Result add(Notification notification);

    /**
     * 删除通知
     *
     * @param notification 通知
     * @return {@link Result}
     */
    Result delete(Notification notification);


}
