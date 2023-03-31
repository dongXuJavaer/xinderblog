package com.xinder.user.feign;

import com.xinder.api.rest.HistoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Xinder
 * @date 2023-03-31 12:55
 */
@FeignClient("${appname.article}")
public interface HistoryFeignClient extends HistoryApi {
}
