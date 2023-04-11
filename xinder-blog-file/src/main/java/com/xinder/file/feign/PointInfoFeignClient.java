package com.xinder.file.feign;

import com.xinder.api.rest.PointInfoApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Xinder
 * @date 2023-04-11 17:36
 */
@FeignClient("${appname.user}")
public interface PointInfoFeignClient extends PointInfoApi {
}
