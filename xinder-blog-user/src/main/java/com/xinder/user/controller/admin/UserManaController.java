package com.xinder.user.controller.admin;

import com.xinder.api.bean.RespBean;
import com.xinder.api.bean.Role;
import com.xinder.api.bean.User;
import com.xinder.api.enums.PermissionsEnums;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.dto.UserListDtoResult;
import com.xinder.api.rest.admin.UserManaApi;
import com.xinder.common.abstcontroller.AbstractController;
import com.xinder.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionCacheOptimizer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by sang on 2017/12/24.
 */
@RestController
public class UserManaController extends AbstractController implements UserManaApi {

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    //    @PreAuthorize("hasAuthority('管理员')")
    @PreAuthorize("@checkAuth.checkUserAuth()")
    @Override
    public BaseResponse<UserListDtoResult> getUserByNickname(@RequestParam(value = "nickname", required = false) String nickname) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Object context = request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        UserListDtoResult userDtoResults = userServiceImpl.getUserByNickname(nickname);

        return buildJson(userDtoResults) ;
    }

    @Override
    public User getUserById(@PathVariable Long id) {
        return userServiceImpl.getUserById(id);
    }

    @Override
    public List<Role> getAllRole() {
        return userServiceImpl.getAllRole();
    }

    @Override
    public RespBean updateUserEnabled(Boolean enabled, Long uid) {
        if (userServiceImpl.updateUserEnabled(enabled, uid) == 1) {
            return new RespBean("success", "更新成功!");
        } else {
            return new RespBean("error", "更新失败!");
        }
    }

    @Override
    public RespBean deleteUserById(@PathVariable Long uid) {
        if (userServiceImpl.deleteUserById(uid) == 1) {
            return new RespBean("success", "删除成功!");
        } else {
            return new RespBean("error", "删除失败!");
        }
    }

    @Override
    public RespBean updateUserRoles(Long[] rids, Long id) {
        if (userServiceImpl.updateUserRoles(rids, id) == rids.length) {
            return new RespBean("success", "更新成功!");
        } else {
            return new RespBean("error", "更新失败!");
        }
    }
}
