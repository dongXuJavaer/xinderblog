package com.xinder.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinder.api.bean.Collect;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.dto.ArticleListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import org.springframework.data.domain.PageRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xinder
 * @since 2023-04-16
 */
public interface CollectService extends IService<Collect> {

    /**
     * 获取帖子收藏列表
     *
     * @param uid uid
     * @return {@link ArticleListDtoResult}
     */
    ArticleListDtoResult getCollectList(Long uid, PageDtoReq pageDtoReq);

    /**
     * 对某帖子是否已经收藏   true 已经收藏       false 未收藏
     *
     * @param uid uid
     * @param aid 援助
     * @return boolean
     */
    DtoResult collectFlag(Long uid, Long aid);

    /**
     * 添加收藏
     *
     * @param collect 收集
     * @return {@link Result}
     */
    Result addCollect(Collect collect);

    /**
     * 取消收藏
     *
     * @param collect 收集
     * @return {@link Result}
     */
    Result cancelCollect(Collect collect);
}
