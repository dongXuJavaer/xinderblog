package com.xinder.api.rest;

import com.xinder.api.bean.RespBean;
import com.xinder.api.bean.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Xinder
 * @date 2023-01-12 19:43
 */
public interface LoginReqApi {

    @RequestMapping("/login_error")
    public RespBean loginError();

    @RequestMapping("/login_success")
    public RespBean loginSuccess();

    /**
     * 如果自动跳转到这个页面，说明用户未登录，返回相应的提示即可
     * <p>
     * 如果要支持表单登录，可以在这个方法中判断请求的类型，进而决定返回JSON还是HTML页面
     *
     * @return
     */
    @RequestMapping("/login_page")
    public RespBean loginPage();

    @PostMapping("/reg")
    public RespBean reg(User user);

}
