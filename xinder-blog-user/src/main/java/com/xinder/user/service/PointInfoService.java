package com.xinder.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinder.api.bean.PointInfo;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.dto.PointInfoListDtoResult;
import com.xinder.api.response.result.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xinder
 * @since 2023-03-30
 */
public interface PointInfoService extends IService<PointInfo> {

    /**
     * 添加(签到用的)
     *
     * @param pointInfo 积分信息
     * @return {@link Result}
     */
    Result add(PointInfo pointInfo);

    /**
     * 根据uid获取列表
     *
     * @param uid uid
     * @return {@link PointInfoListDtoResult}
     */
    PointInfoListDtoResult getListByUid(Long uid, PageDtoReq pageDtoReq);

    /**
     * 减少
     *
     * @param pointInfo 点信息
     * @return {@link Result}
     */
    Result reduce(PointInfo pointInfo);
}