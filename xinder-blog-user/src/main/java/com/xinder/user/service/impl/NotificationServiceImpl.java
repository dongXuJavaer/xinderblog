package com.xinder.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.Notification;
import com.xinder.api.enums.NotificationEnums;
import com.xinder.api.response.dto.NotificationDtoListResult;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.user.mapper.NotificationMapper;
import com.xinder.user.service.NotificationService;
import com.xinder.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xinder
 * @since 2023-03-26
 */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserService userService;

    @Override
    public NotificationDtoListResult commentsList() {
        UserDtoResult currentUser = userService.getCurrentUser();
        NotificationDtoListResult dtoListResult = null;
        if (currentUser.getId() == null) {
            dtoListResult = DtoResult.dataDtoFail(NotificationDtoListResult.class);
            dtoListResult.setMsg("未登录");
            return dtoListResult;
        }

        List<Notification> notificationList = notificationMapper.getByToUid(currentUser.getId(), NotificationEnums.COMMENTS.getCode());
        dtoListResult = DtoResult.dataDtoSuccess(NotificationDtoListResult.class);
        dtoListResult.setList(notificationList);

        return dtoListResult;
    }

    @Override
    public NotificationDtoListResult zanList() {
        UserDtoResult currentUser = userService.getCurrentUser();
        NotificationDtoListResult dtoListResult = DtoResult.dataDtoSuccess(NotificationDtoListResult.class);
        if (currentUser.getId() == null) {
            dtoListResult.setMsg("未登录");
            return dtoListResult;
        }
        return null;
    }

    @Override
    public NotificationDtoListResult sysList() {
        UserDtoResult currentUser = userService.getCurrentUser();
        NotificationDtoListResult dtoListResult = DtoResult.dataDtoSuccess(NotificationDtoListResult.class);
        if (currentUser.getId() == null) {
            dtoListResult.setMsg("未登录");
            return dtoListResult;
        }


        return null;
    }

    @Override
    public NotificationDtoListResult followList() {
        UserDtoResult currentUser = userService.getCurrentUser();
        NotificationDtoListResult dtoListResult = DtoResult.dataDtoSuccess(NotificationDtoListResult.class);
        if (currentUser.getId() == null) {
            dtoListResult.setMsg("未登录");
            return dtoListResult;
        }
        return null;
    }
}
