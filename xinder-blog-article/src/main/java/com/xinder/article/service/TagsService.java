package com.xinder.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinder.api.bean.Tags;
import com.xinder.api.request.TagsDtoReq;
import com.xinder.api.response.dto.TagDtoResult;
import com.xinder.api.response.dto.TagsDtoListResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.article.mapper.TagsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xinder
 * @date 2023-01-10 23:45
 */
public interface TagsService extends IService<Tags> {

    /**
     * 获取所有标签
     * @return
     */
    TagsDtoListResult getAll();

    /**
     * 添加标签
     * @param tags
     * @return
     */
    DtoResult addTags(Tags tags);

    /**
     * 获取个人博客标签
     * @param uid
     * @return
     */
    TagsDtoListResult myTags(Integer uid);

    /**
     * 分页获取个人博客标签
     * @param tagsDtoReq
     * @return
     */
    TagsDtoListResult myTagsPage(TagsDtoReq tagsDtoReq);

    /**
     * 修改标签
     * @param tagsDtoReq
     * @return
     */
    Result updateTag(TagsDtoReq tagsDtoReq);

    /**
     * 批量删除标签
     * @param ids
     * @return
     */
    Result deleteTag(List<Long> ids);
}
