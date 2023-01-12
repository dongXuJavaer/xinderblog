package com.xinder.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.Tags;
import com.xinder.api.response.dto.TagsDtoListResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.article.mapper.TagsMapper;
import com.xinder.article.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xinder
 * @date 2023-01-10 23:48
 */
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements TagsService {

    @Autowired
    private TagsMapper tagsMapper;

    @Override
    public TagsDtoListResult getAll() {
        List<Tags> list = super.list();
        TagsDtoListResult dtoListResult = DtoResult.dataDtoSuccess(TagsDtoListResult.class);
        dtoListResult.setList(list);
        return dtoListResult;
    }

    @Override
    public DtoResult addTags(Tags tags) {
        tagsMapper.insert(tags);
        DtoResult success = DtoResult.success();
        return success;
    }

    @Override
    public TagsDtoListResult myTags(Integer uid) {
        LambdaQueryWrapper<Tags> queryWrapper = new LambdaQueryWrapper<Tags>();
        queryWrapper.eq(Tags::getUid, uid);
        List<Tags> tags = tagsMapper.selectList(queryWrapper);
        TagsDtoListResult dtoListResult = DtoResult.dataDtoSuccess(TagsDtoListResult.class);
        dtoListResult.setList(tags);
        return dtoListResult;
    }
}
