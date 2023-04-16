package com.xinder.user.controller;


import com.xinder.api.bean.Collect;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.ArticleListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.api.rest.CollectApi;
import com.xinder.common.abstcontroller.AbstractController;
import com.xinder.user.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Xinder
 * @since 2023-04-16
 */
@RestController
@RequestMapping("/collect")
public class CollectController extends AbstractController implements CollectApi {

    @Autowired
    private CollectService collectService;

    @Override
    public BaseResponse<ArticleListDtoResult> getCollectList(Long uid, PageDtoReq pageDtoReq) {
        ArticleListDtoResult result = collectService.getCollectList(uid, pageDtoReq);
        return buildJson(result);
    }

    @Override
    public BaseResponse<DtoResult> collectFlag(Long uid, Long aid) {
        DtoResult dtoResult = collectService.collectFlag(uid, aid);
        return buildJson(dtoResult);
    }

    @Override
    public BaseResponse<Result> addCollect(Collect collect) {
        Result result = collectService.addCollect(collect);
        return buildJson(result);
    }

    @Override
    public BaseResponse<Result> cancelCollect(Collect collect) {
        Result result = collectService.cancelCollect(collect);
        return buildJson(result);
    }
}

