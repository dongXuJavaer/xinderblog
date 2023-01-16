package com.xinder.article.feign;

import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.rest.UserApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Xinder
 * @date 2023-01-12 19:04
 */
@FeignClient("${appname.user}")
public interface UserFeignClient extends UserApi {

    // TODO: 2023-01-14 远程调用时需要配置参数
//    @RequestMapping("/user/login")
//    BaseResponse<UserDtoResult> login(
//            @RequestParam("username") String username,
//            String password,
//            HttpServletResponse response);

}
