package com.xinder.api.rest;

/**
 * @author Xinder
 * @date 2023-03-31 11:54
 */

import com.xinder.api.bean.History;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.HistoryListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "HistoryApi")
@RequestMapping("/history")
public interface HistoryApi {

    @ApiOperation(value = "用户登录后，cookie中的历史记录入库", notes = "用户登录后，cookie中的历史记录入库", tags = {"HistoryApi"})
    @RequestMapping(value = "/batch-save", method = RequestMethod.POST)
    BaseResponse<Result> batchSave(@RequestBody List<History> histories, @RequestParam("uid") Long uid);

    @ApiOperation(value = "根据用户id获取浏览记录", notes = "根据用户id获取浏览记录", tags = {"HistoryApi"})
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    BaseResponse<HistoryListDtoResult> getCurrentUserHistoryList();

}
