package com.xinder.user.controller;


import com.xinder.api.bean.PointInfo;
import com.xinder.api.request.UserDtoReq;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.PointInfoListDtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.api.rest.PointInfoApi;
import com.xinder.common.abstcontroller.AbstractController;
import com.xinder.user.mapper.PointInfoMapper;
import com.xinder.user.service.PointInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Xinder
 * @since 2023-03-30
 */
@RestController
@RequestMapping("/point")
public class PointInfoController extends AbstractController implements PointInfoApi {

    @Autowired
    private PointInfoService pointInfoService;

    @Override
    public BaseResponse<PointInfoListDtoResult> getListByUid(Long uid, PageDtoReq pageDtoReq) {
        PointInfoListDtoResult dtoResult = pointInfoService.getListByUid(uid, pageDtoReq);
        return buildJson(dtoResult);
    }

    @Override
    public BaseResponse<Result> add(PointInfo pointInfo) {
        Result result =pointInfoService.add(pointInfo);
        return buildJson(result);
    }

    @Override
    public BaseResponse<Result> reduce(PointInfo pointInfo) {
        Result result =pointInfoService.reduce(pointInfo);
        return buildJson(result);
    }


}

