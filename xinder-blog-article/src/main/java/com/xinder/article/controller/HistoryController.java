package com.xinder.article.controller;


import com.xinder.api.bean.History;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.HistoryListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.api.rest.HistoryApi;
import com.xinder.article.service.HistoryService;
import com.xinder.common.abstcontroller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Xinder
 * @since 2023-03-30
 */
@RestController
@RequestMapping("/history")
public class HistoryController extends AbstractController implements HistoryApi {

    @Autowired
    private HistoryService historyService;

    @Override
    public BaseResponse<Result> batchSave(List<History> historyList, Long uid) {
        Result result = historyService.batchSave(historyList, uid);
        return buildJson(result);
    }

    @Override
    public BaseResponse<HistoryListDtoResult> getCurrentUserHistoryList() {
        HistoryListDtoResult listDtoResult = historyService.getCurrentUserHistoryList();
        return buildJson(listDtoResult);
    }
}

