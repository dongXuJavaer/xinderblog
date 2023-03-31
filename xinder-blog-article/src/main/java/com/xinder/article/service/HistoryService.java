package com.xinder.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinder.api.bean.Article;
import com.xinder.api.bean.History;
import com.xinder.api.response.dto.HistoryListDtoResult;
import com.xinder.api.response.result.Result;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Xinder
 * @since 2023-03-30
 */
public interface HistoryService extends IService<History> {

    /**
     * 批量添加用户的历史浏览信息
     *
     * @param historyList cookie中的历史浏览记录
     * @return {@link Result}
     */
    Result batchSave(List<History> historyList,Long uid);

    /**
     * 未登录时，向客户端浏览器添加浏览记录
     * 1. 检查是否已经浏览了该帖子，如果浏览了，则只更新时间;  如果为第一次浏览，则添加
     *
     * @param article      文章
     * @param cookieDomain cookie域
     * @return boolean
     */
    boolean addHistoryToFront(Article article, String cookieDomain);

    /**
     * 获得当前用户的浏览历史列表；包含登录与未登录
     *
     * @return {@link HistoryListDtoResult}
     */
    HistoryListDtoResult getCurrentUserHistoryList();
}
