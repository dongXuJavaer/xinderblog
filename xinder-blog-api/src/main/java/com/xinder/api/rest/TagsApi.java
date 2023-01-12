package com.xinder.api.rest;

import com.xinder.api.bean.Tags;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.TagsDtoListResult;
import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
     * 添加
     */
    @GetMapping("/mytags/{uid}")
    @ApiOperation(value = "查询个人标签", notes = "", tags = {"TagsApi"})
    BaseResponse<TagsDtoListResult> myTags(@PathVariable("uid") Integer uid);
}
