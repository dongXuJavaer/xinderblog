package com.xinder.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.Article;
import com.xinder.api.bean.Collect;
import com.xinder.api.request.comm.PageDtoReq;
import com.xinder.api.response.dto.ArticleListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.user.feign.ArticleFeignClient;
import com.xinder.user.mapper.CollectMapper;
import com.xinder.user.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xinder
 * @since 2023-04-16
 */
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private ArticleFeignClient articleFeignClient;

    @Override
    public ArticleListDtoResult getCollectList(Long uid, PageDtoReq req) {
        Integer pageSize = req.getPageSize();
        Long currentPage = req.getCurrentPage();
        Long offset = (currentPage - 1) * pageSize;
        List<Collect> collectList = collectMapper.selectPageList(req, offset, uid);

        Long[] aids = collectList.stream().map(Collect::getAid).collect(Collectors.toList())
                .toArray(new Long[collectList.size()]);
        ArticleListDtoResult articleListDtoResult = articleFeignClient.getBatchById(aids);
        return articleListDtoResult;
    }

    @Override
    public DtoResult collectFlag(Long uid, Long aid) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<Collect>()
                .eq(Collect::getUid, uid)
                .eq(Collect::getAid, aid);
        Collect collect = collectMapper.selectOne(wrapper);
        DtoResult dtoResult = DtoResult.dataDtoSuccess(DtoResult.class);
        boolean flag = collect != null;
        dtoResult.setData(flag);
        return dtoResult;
    }

    @Override
    public Result addCollect(Collect collect) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<Collect>()
                .eq(Collect::getAid, collect.getAid())
                .eq(Collect::getUid, collect.getUid());
        Collect one = collectMapper.selectOne(wrapper);
        if (one != null) {
            return Result.fail("收藏失败，该帖子已经收藏！");
        }

        int i = collectMapper.insert(collect);
        return i > 0 ? Result.success("收藏成功") : Result.fail("收藏失败");
    }

    @Override
    public Result cancelCollect(Collect collect) {
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<Collect>()
                .eq(Collect::getAid, collect.getAid())
                .eq(Collect::getUid, collect.getUid());
        Collect one = collectMapper.selectOne(wrapper);
        if (one == null) {
            return Result.fail("取消收藏失败，未收藏该帖子！");
        }

        int i = collectMapper.deleteById(one.getId());
        return i > 0 ? Result.success("取消收藏成功") : Result.fail("取消收藏失败");
    }
}
