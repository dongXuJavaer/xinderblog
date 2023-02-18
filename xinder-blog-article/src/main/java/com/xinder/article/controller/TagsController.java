package com.xinder.article.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.xinder.api.bean.Tags;
import com.xinder.api.request.TagsDtoReq;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.TagDtoResult;
import com.xinder.api.response.dto.TagsDtoListResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.api.rest.TagsApi;
import com.xinder.article.service.TagsService;
import com.xinder.common.abstcontroller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Xinder
 * @date 2023-01-10 23:40
 */
@RestController
@RequestMapping("/tags")
public class TagsController extends AbstractController implements TagsApi {

    @Autowired
    private TagsService tagsService;

    @Override
    public BaseResponse<TagsDtoListResult> getAllTags() {
        TagsDtoListResult dtoListResult = tagsService.getAll();
        return buildJson(dtoListResult);
    }

    @Override
    public BaseResponse<DtoResult> addTags(@RequestBody Tags tags) {
        DtoResult dtoResult = tagsService.addTags(tags);
        return buildJson(dtoResult);
    }

    @Override
    public BaseResponse<TagsDtoListResult> myTagsPage(TagsDtoReq tagsDtoReq) {
        TagsDtoListResult dtoListResult = tagsService.myTagsPage(tagsDtoReq);
        return buildJson(dtoListResult);
    }

    @Override
    public BaseResponse<Result> updateTag(TagsDtoReq tagsDtoReq) {
        return buildJson(tagsService.updateTag(tagsDtoReq));
    }

    @Override
    public BaseResponse<Result> deleteTag(List<Long> ids) {
        Result result = tagsService.deleteTag(ids);
        return buildJson(result);
    }
}
