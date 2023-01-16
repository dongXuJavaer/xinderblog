package com.xinder.api.rest;

import com.xinder.api.bean.Category;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.CategoryListDtoResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Xinder
 * @date 2023-01-09 21:30
 */

@Api(tags = "CategoryApi")
public interface CategoryApi {


    @ApiOperation(value = "获取所有博客分类", notes = "", tags = {"CategoryApi"})
    @RequestMapping(value = "/category/all", method = RequestMethod.GET)
    BaseResponse<CategoryListDtoResult> getAllCategories();

}
