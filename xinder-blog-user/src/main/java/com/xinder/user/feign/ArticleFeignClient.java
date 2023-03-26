package com.xinder.user.feign;

/**
 * @author Xinder
 * @date 2023-03-26 16:38
 */

import com.xinder.api.rest.ArticleApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("${appname.article}")
public interface ArticleFeignClient extends ArticleApi {
}
