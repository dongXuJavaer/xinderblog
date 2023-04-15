package com.xinder.article.feign;

import com.xinder.api.response.RespBean;
import com.xinder.api.rest.admin.UserManaApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Xinder
 * @date 2023-01-12 19:21
 */
@FeignClient("${appname.user}")
public interface UserManaFeignClient extends UserManaApi {

    @Override
    @RequestMapping(value = "/user/role", method = RequestMethod.PUT)
    RespBean updateUserRoles(@RequestParam("rids") Long[] rids, @RequestParam("id") Long id);

//    @Override
//    @RequestMapping(value = "/user/enabled", method = RequestMethod.PUT)
//    RespBean updateUserEnabled(@RequestParam("enabled") Boolean enabled, @RequestParam("uid") Long uid);

}
