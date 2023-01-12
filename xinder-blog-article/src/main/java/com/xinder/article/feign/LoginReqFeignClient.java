package com.xinder.article.feign;

import com.xinder.api.rest.LoginReqApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Xinder
 * @date 2023-01-12 19:45
 */
@FeignClient("${appname.user}")
public interface LoginReqFeignClient extends LoginReqApi {
}
