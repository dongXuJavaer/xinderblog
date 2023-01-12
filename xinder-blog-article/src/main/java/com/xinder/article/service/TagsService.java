package com.xinder.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinder.api.bean.Tags;
import com.xinder.api.response.dto.TagsDtoListResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.article.mapper.TagsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
