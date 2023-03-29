package com.xinder.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinder.api.bean.Notification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xinder
 * @since 2023-03-26
 */
public interface NotificationMapper extends BaseMapper<Notification> {

    /**
     * 通过uid
     * 根据接收者用户id查询通知
     *
     * @param uid  uid
     * @param type 类型
     * @return {@link List}<{@link Notification}>
     */
    List<Notification> getByToUid(@Param("uid") Long uid, @Param("type") Integer type);


    /**
     * 获取该通知类型的总数
     *
     * @param uid   用户id
     * @param type 类型
     * @return {@link Long}
     */
    Long getCount(@Param("uid") Long uid, @Param("type") Integer type);
}
