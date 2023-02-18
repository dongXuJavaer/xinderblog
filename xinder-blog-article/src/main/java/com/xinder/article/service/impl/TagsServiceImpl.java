package com.xinder.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.Tags;
import com.xinder.api.request.TagsDtoReq;
import com.xinder.api.response.dto.TagDtoResult;
import com.xinder.api.response.dto.TagsDtoListResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.article.mapper.ArticleTagsMapper;
import com.xinder.article.mapper.TagsMapper;
import com.xinder.article.service.TagsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Xinder
 * @date 2023-01-10 23:48
 */
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements TagsService {

    @Autowired
    private TagsMapper tagsMapper;

    @Autowired
    private ArticleTagsMapper articleTagsMapper;

    @Override
    public TagsDtoListResult getAll() {
        List<Tags> list = super.list();
        TagsDtoListResult dtoListResult = DtoResult.dataDtoSuccess(TagsDtoListResult.class);
        dtoListResult.setList(list);
        return dtoListResult;
    }

    @Override
    public DtoResult addTags(Tags tags) {
        int i = tagsMapper.insert(tags);
        DtoResult dtoResult;
        if (i > 0) {
            dtoResult = DtoResult.dataDtoSuccess(DtoResult.class);
            dtoResult.setMsg("添加成功");
            dtoResult.setData(tags);
        } else {
            dtoResult = DtoResult.dataDtoFail(DtoResult.class);
            dtoResult.setMsg("添加失败");
        }
        return dtoResult;
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

    @Override
    public TagsDtoListResult myTagsPage(TagsDtoReq req) {
        Integer pageSize = req.getPageSize();
        Long currentPage = req.getCurrentPage();
        Long offset = (currentPage - 1) * pageSize;
        Long rows = tagsMapper.getCount(req, offset);
        List<Tags> tagsList = tagsMapper.getTagsList(req, offset);
        TagsDtoListResult dtoListResult = DtoResult.dataDtoSuccess(TagsDtoListResult.class);

        long totalPage = Double.valueOf(Math.ceil((float) rows / (float) pageSize)).longValue();
        dtoListResult.setTotalCount(rows);
        dtoListResult.setTotalPage(totalPage);
        dtoListResult.setCurrentPage(currentPage);
        dtoListResult.setList(tagsList);

        return dtoListResult;
    }

    @Override
    public Result updateTag(TagsDtoReq tagsDtoReq) {
        Tags tags = new Tags();
        BeanUtils.copyProperties(tagsDtoReq, tags);
        int i = tagsMapper.updateById(tags);
        return i > 0 ? Result.success("修改成功") : Result.fail("修改失败");
    }

    @Override
    @Transactional
    public Result deleteTag(List<Long> ids) {
        int i = tagsMapper.deleteBatchIds(ids);
        articleTagsMapper.deleteByTid(ids);
        return i > 0 ? Result.success("删除成功") : Result.fail("删除失败");
    }
}
