package com.xinder.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinder.api.bean.PointInfo;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.dto.PointInfoListDtoResult;
import com.xinder.api.response.result.DtoResult;
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
    Result signIn(PointInfo pointInfo);

    /**
     * 根据uid获取列表
     *
     * @param uid uid
     * @return {@link PointInfoListDtoResult}
     */
    PointInfoListDtoResult getListByUid(Long uid, PageDtoReq pageDtoReq);

    /**
     * 积分扣减，  会判断用户积分是否充足
     *
     * @param pointInfo 点信息
     * @return {@link Result}
     */
    Result reduce(PointInfo pointInfo);

    /**
     * 查询用户对某资源是否下载
     *
     * @param uid uid
     * @param rid 掉
     * @return {@link DtoResult}
     */
    DtoResult getByUidAndRid(Long uid, Long rid);

    /**
     * 获取某人的积分总数
     *
     * @param uid uid
     * @return {@link DtoResult}
     */
    DtoResult getPointCount(Long uid);

    /**
     * 添加
     *
     * @param pointInfo 点信息
     * @return {@link DtoResult}
     */
    Result add(PointInfo pointInfo);
}
