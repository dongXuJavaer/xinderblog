package com.xinder.file.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xinder.api.bean.Notification;
import com.xinder.api.bean.Resources;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.dto.ResourcesListDtoResult;
import com.xinder.api.response.result.Result;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Xinder
 * @since 2023-03-26
 */
public interface ResourcesService extends IService<Resources> {

    /**
     * 下载资源
     *
     * @param rid 资源id
     * @param uid uid
     */
    void downloadResources(Long rid, Long uid);

    /**
     * 添加资源信息
     *
     * @param resources 资源
     * @return {@link Result}
     */
    Result add(Resources resources);

    /**
     * 分页查询资源列表
     *
     * @param pageDtoReq 页面dto点播
     * @return {@link ResourcesListDtoResult}
     */
    ResourcesListDtoResult getPageList(PageDtoReq pageDtoReq, String uid);
}
