package com.xinder.article.controller;

import com.xinder.api.bean.Tags;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.TagsDtoListResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.rest.TagsApi;
import com.xinder.article.service.TagsService;
import com.xinder.common.abstcontroller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xinder
 * @date 2023-01-10 23:40
 */
@RestController
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
    public BaseResponse<TagsDtoListResult> myTags(@PathVariable("uid") Integer uid) {
        TagsDtoListResult dtoListResult = tagsService.myTags(uid);
        return buildJson(dtoListResult);
    }
}
