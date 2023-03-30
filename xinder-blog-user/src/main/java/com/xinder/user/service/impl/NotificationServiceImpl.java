package com.xinder.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.Notification;
import com.xinder.api.enums.NotificationEnums;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.dto.NotificationDtoListResult;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
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
    public DtoResult getCount(Integer type) {
        UserDtoResult currentUser = userService.getCurrentUser();
        DtoResult dtoResult;
        if (currentUser.getId() == null) {
            dtoResult = DtoResult.dataDtoFail(DtoResult.class);
            dtoResult.setMsg("未登录");
            return dtoResult;
        }

        boolean containFlag = NotificationEnums.judgeType(type);
        if (!containFlag) {
            dtoResult = DtoResult.dataDtoFail(DtoResult.class);
            dtoResult.setMsg("类型合法！");
            return dtoResult;
        }

        Long count = notificationMapper.getCount(currentUser.getId(), type);
        dtoResult = DtoResult.dataDtoSuccess(DtoResult.class);
        dtoResult.setData(count);
        return dtoResult;
    }

    @Override
    public NotificationDtoListResult commentsList(PageDtoReq pageDtoReq) {
        UserDtoResult currentUser = userService.getCurrentUser();
        NotificationDtoListResult dtoListResult = null;
        if (currentUser.getId() == null) {
            dtoListResult = DtoResult.dataDtoFail(NotificationDtoListResult.class);
            dtoListResult.setMsg("未登录");
            return dtoListResult;
        }

        Long count = notificationMapper.getCount(currentUser.getId(), NotificationEnums.COMMENTS.getCode());
        dtoListResult = DtoResult.dataDtoSuccess(NotificationDtoListResult.class);
        dtoListResult.setTotalCount(count);

        Integer pageSize = pageDtoReq.getPageSize();
        Long currentPage = pageDtoReq.getCurrentPage();
        Long offset = (currentPage - 1) * pageSize;
        List<Notification> notificationList = notificationMapper.getByToUid(currentUser.getId(), NotificationEnums.COMMENTS.getCode(), offset, pageSize);
        dtoListResult.setList(notificationList);

        return dtoListResult;
    }

    @Override
    public NotificationDtoListResult zanList(PageDtoReq pageDtoReq) {
        UserDtoResult currentUser = userService.getCurrentUser();
        NotificationDtoListResult dtoListResult = DtoResult.dataDtoSuccess(NotificationDtoListResult.class);
        if (currentUser.getId() == null) {
            dtoListResult.setMsg("未登录");
            return dtoListResult;
        }

        Long count = notificationMapper.getCount(currentUser.getId(), NotificationEnums.ZAN.getCode());
        dtoListResult = DtoResult.dataDtoSuccess(NotificationDtoListResult.class);
        dtoListResult.setTotalCount(count);

        Integer pageSize = pageDtoReq.getPageSize();
        Long currentPage = pageDtoReq.getCurrentPage();
        Long offset = (currentPage - 1) * pageSize;
        List<Notification> notificationList = notificationMapper.getByToUid(currentUser.getId(), NotificationEnums.ZAN.getCode(), offset, pageSize);
        dtoListResult.setList(notificationList);

        return dtoListResult;
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

    @Override
    public Result add(Notification notification) {
        int i = notificationMapper.insert(notification);
        return i > 0 ? Result.success("发送成功") : Result.fail("发送失败");
    }

    @Override
    public Result delete(Notification notification) {
        int i;
        if (notification.getId() == null) {
            LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<Notification>()
                    .eq(Notification::getAid, notification.getAid())
                    .eq(Notification::getFromUid, notification.getFromUid())
                    .eq(Notification::getType, notification.getType());
            i = notificationMapper.delete(wrapper);
        } else {
            i = notificationMapper.deleteById(notification.getId());
        }
        return i > 0 ? Result.success("删除成功") : Result.fail("删除失败");
    }
}
