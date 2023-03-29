package com.xinder.article.feign;

import com.xinder.api.rest.NotificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Xinder
 * @date 2023-03-28 20:41
 */
@FeignClient("${appname.user}")
public interface NotificationFeignClient extends NotificationApi {
}
