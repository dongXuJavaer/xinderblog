package com.xinder.api.rest.admin;

import com.xinder.api.response.RespBean;
import com.xinder.api.bean.Role;
import com.xinder.api.bean.User;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.UserListDtoResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Xinder
 * @date 2023-01-12 19:21
 */
public interface UserManaApi {


    @RequestMapping(value = "/admin/user", method = RequestMethod.GET)
    BaseResponse<UserListDtoResult> getUserByNickname(
            @RequestParam(value = "nickname", required = false) String nickname);

    @RequestMapping(value = "/admin/user/{id}", method = RequestMethod.GET)
    User getUserById(@PathVariable Long id);

    @RequestMapping(value = "/admin/roles", method = RequestMethod.GET)
    List<Role> getAllRole();

    @RequestMapping(value = "/admin/user/enabled", method = RequestMethod.PUT)
    RespBean updateUserEnabled(Boolean enabled, Long uid);

    @RequestMapping(value = "/admin/user/{uid}", method = RequestMethod.DELETE)
    RespBean deleteUserById(@PathVariable Long uid);

    @RequestMapping(value = "/admin/user/role", method = RequestMethod.PUT)
    RespBean updateUserRoles(Long[] rids, Long id);
}
