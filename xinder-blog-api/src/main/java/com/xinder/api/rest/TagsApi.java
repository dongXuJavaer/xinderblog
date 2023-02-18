package com.xinder.api.rest;

import com.xinder.api.bean.Tags;
import com.xinder.api.request.TagsDtoReq;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.TagDtoResult;
import com.xinder.api.response.dto.TagsDtoListResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Xinder
 * @date 2023-01-10 23:40
 */
@Api(tags = "TagsApi")
@RequestMapping("/tags")
public interface TagsApi {

    /**
     * 获取所有标签
     */
    @GetMapping("/all")
    @ApiOperation(value = "获取所有标签", notes = "", tags = {"TagsApi"})
    BaseResponse<TagsDtoListResult> getAllTags();

    /**
     * 添加
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加标签", notes = "", tags = {"TagsApi"})
    BaseResponse<DtoResult> addTags(Tags tags);

    /**
     * 分页查询个人标签
     */
    @PostMapping("/mytags/page")
    @ApiOperation(value = "查询个人标签", notes = "", tags = {"TagsApi"})
    BaseResponse<TagsDtoListResult> myTagsPage(@RequestBody TagsDtoReq tagsDtoReq);

    /**
     * 修改标签
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改标签", notes = "", tags = {"TagsApi"})

    BaseResponse<Result> updateTag(@RequestBody TagsDtoReq tagsDtoReq);
    /**
     * 批量删除标签
     */
    @PostMapping("/delete")
    @ApiOperation(value = "修改标签", notes = "", tags = {"TagsApi"})
    BaseResponse<Result> deleteTag(@RequestBody List<Long> ids);
}
