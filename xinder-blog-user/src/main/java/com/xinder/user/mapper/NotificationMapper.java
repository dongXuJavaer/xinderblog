package com.xinder.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinder.api.bean.Notification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Xinder
 * @since 2023-03-26
 */
public interface NotificationMapper extends BaseMapper<Notification> {

    /**
     * 让由uid
     * 通过uid
     * 根据接收者用户id查询通知
     *
     * @param uid         用户id
     * @param type        类型
     * @param offset      数据偏移量
     * @param pageSize    页面大小
     * @return {@link List}<{@link Notification}>
     */
    List<Notification> getByToUid(@Param("uid") Long uid,
                                  @Param("type") Integer type,
                                  @Param("offset") Long offset,
                                  @Param("pageSize") Integer pageSize);


    /**
     * 获取该通知类型的总数
     *
     * @param uid  用户id
     * @param type 类型
     * @return {@link Long}
     */
    Long getCount(@Param("uid") Long uid, @Param("type") Integer type);
}
