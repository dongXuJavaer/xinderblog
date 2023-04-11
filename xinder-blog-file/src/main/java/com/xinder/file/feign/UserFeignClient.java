package com.xinder.file.feign;

import com.xinder.api.rest.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

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
