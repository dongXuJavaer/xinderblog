package com.xinder.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.Article;
import com.xinder.api.bean.ArticleTags;
import com.xinder.api.bean.Tags;
import com.xinder.api.bean.Zan;
import com.xinder.api.enums.ZanTypeEnums;
import com.xinder.api.request.ArticleDtoReq;
import com.xinder.api.response.base.BaseResponse;
import com.xinder.api.response.dto.ArticleListDtoResult;
import com.xinder.api.response.dto.UserDtoResult;
import com.xinder.api.response.dto.ZanStateDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.article.feign.UserFeignClient;
import com.xinder.article.mapper.*;
import com.xinder.article.service.ArticleService;
import com.xinder.common.util.SFunction;
import com.xinder.common.util.SerializedLambdaUtil;
import com.xinder.common.util.TokenDecode;
import com.xinder.common.util.Util;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Xinder on 2023-1-6 23:07:57.
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagsMapper tagsMapper;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleTagsMapper articleTagsMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private TokenDecode tokenDecode;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticleESMapper articleESMapper;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private ZanMapper zanMapper;

    public int addNewArticle(Article article) {
        //处理文章摘要
        if (article.getSummary() == null || "".equals(article.getSummary())) {
            //直接截取
            String stripHtml = stripHtml(article.getHtmlContent());
            article.setSummary(stripHtml.substring(0, stripHtml.length() > 50 ? 50 : stripHtml.length()));
        }
        if (article.getId() == -1) {
            //添加操作
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (article.getState() == 1) {
                //设置发表日期
                article.setCreateTime(timestamp);
            }
            article.setEditTime(timestamp);
            //设置当前用户
            article.setUid(Util.getCurrentUser(tokenDecode, redisTemplate).getId());
            int i = articleMapper.addNewArticle(article);
            //打标签
            String[] dynamicTags = article.getDynamicTags();
            if (dynamicTags != null && dynamicTags.length > 0) {
                int tags = addTagsToArticle(dynamicTags, article.getId());
                if (tags == -1) {
                    return tags;
                }
            }
            articleESMapper.save(article);
            return i;
        } else {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (article.getState() == 1) {
                //设置发表日期
                article.setCreateTime(timestamp);
            }
            //更新
            article.setEditTime(new Timestamp(System.currentTimeMillis()));
            int i = articleMapper.updateArticle(article);
            //修改标签
            String[] dynamicTags = article.getDynamicTags();
            if (dynamicTags != null && dynamicTags.length > 0) {
                int tags = addTagsToArticle(dynamicTags, article.getId());
                if (tags == -1) {
                    return tags;
                }
            }
            articleESMapper.save(article);
            return i;
        }
    }

    private int addTagsToArticle(String[] dynamicTags, Long aid) {
        //1.删除该文章目前所有的标签关联关系
        tagsMapper.deleteTagsByAid(aid);
        //2.将上传上来的标签全部存入数据库
        tagsMapper.saveTags(dynamicTags);
        //3.查询这些标签的id
        List<Long> tIds = tagsMapper.getTagsIdByTagName(dynamicTags);
        //4.重新给文章设置标签
        int i = tagsMapper.saveTags2ArticleTags(tIds, aid);
        return i == dynamicTags.length ? i : -1;
    }

    public String stripHtml(String content) {
        content = content.replaceAll("<p .*?>", "");
        content = content.replaceAll("<br\\s*/?>", "");
        content = content.replaceAll("\\<.*?>", "");
        return content;
    }

    public List<Article> getArticleByState(Integer state, Integer page, Integer count, String keywords) {
        int start = (page - 1) * count;
        return articleMapper.getArticleByState(state, start, count, keywords);
    }


//    public List<Article> getArticleByStateByAdmin(Integer page, Integer count,String keywords) {
//        int start = (page - 1) * count;
//        return articleMapper.getArticleByStateByAdmin(start, count,keywords);
//    }

    public int getArticleCountByState(Integer state, String keywords) {
        return articleMapper.getArticleCountByState(state, keywords);
    }

    public int updateArticleState(Long[] aids, Integer state) {
        if (state == 2) {
            return articleMapper.deleteArticleById(aids);
        } else {
            return articleMapper.updateArticleState(aids, 2);//放入到回收站中
        }
    }

    public int restoreArticle(Integer articleId) {
        return articleMapper.updateArticleStateById(articleId, 1); // 从回收站还原在原处
    }

    public Article getArticleById(Long aid) {
        Article article = articleMapper.getArticleById(aid);
        articleMapper.pvIncrement(aid);
        return article;
    }

    public void pvStatisticsPerDay() {
        articleMapper.pvStatisticsPerDay();
    }

    /**
     * 获取最近七天的日期
     *
     * @return
     */
    public List<String> getCategories() {
        return articleMapper.getCategories(Util.getCurrentUser(tokenDecode, redisTemplate).getId());
    }

    /**
     * 获取最近七天的数据
     *
     * @return
     */
    public List<Integer> getDataStatistics() {
        return articleMapper.getDataStatistics(Util.getCurrentUser(tokenDecode, redisTemplate).getId());
    }

    @Override
    public ArticleListDtoResult getArticleByState(ArticleDtoReq req, String keywords) {
        Integer pageSize = req.getPageSize();
        Long currentPage = req.getCurrentPage();
        Long offset = (currentPage - 1) * pageSize;
        req.setState(1);

        // ES搜索
        NativeSearchQuery query = buildNativeSearchQuery(currentPage, pageSize, keywords, req.getCid());
        SearchHits<Article> searchHits = getSearchHits(query);
        List<Article> articleList = queryByES(query, searchHits);
        Long rows = searchHits.getTotalHits();

        articleList.forEach(item -> {
            if (StringUtils.isEmpty(item.getHeadPic())) {
                item.setHeadPic("");
            }
//            Long readCount = (Long) redisTemplate.opsForHash().get(CommonConstant.REDIS_KEY_ARTICLE, item.getId());
//            item.setReadCount(readCount);
        });

        ArticleListDtoResult dtoResult = DtoResult.dataDtoSuccess(ArticleListDtoResult.class);
        long totalPage = Double.valueOf(Math.ceil((float) rows / (float) pageSize)).longValue();
        dtoResult.setTotalCount(rows);
        dtoResult.setTotalPage(totalPage);
        dtoResult.setCurrentPage(currentPage);
        dtoResult.setList(articleList);
        return dtoResult;
    }

    @Override
    public DtoResult getById(Long id) {
        Article article = articleMapper.getArticleById(id);
        this.addReadCount(article);
        DtoResult result = DtoResult.success();
        result.setData(article);
        return result;
    }

    /**
     * 浏览数+1
     *
     * @param article
     * @return
     */
    private boolean addReadCount(Article article) {
//        HashOperations hashOperations = redisTemplate.opsForHash();
//        Long readCount = (Long) hashOperations.get(CommonConstant.REDIS_KEY_ARTICLE, article.getAttachment());
//        if (readCount != null) {
//            hashOperations.increment(CommonConstant.REDIS_KEY_ARTICLE, article.getAttachment(), 1L);
//        } else {
//            hashOperations.putIfAbsent(CommonConstant.REDIS_KEY_ARTICLE, article.getAttachment(), 1L);
//        }
        articleESMapper.delete(article);
        articleESMapper.save(article);
        article.setReadCount(article.getReadCount() + 1);
        return articleMapper.updateById(article) > 0;
    }

    @Override
    public ArticleListDtoResult getList(ArticleDtoReq articleDtoReq) {
        Article article = new Article();
        BeanUtils.copyProperties(articleDtoReq, article);
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>(article);
        List<Article> articleList = articleMapper.selectList(articleQueryWrapper);
        ArticleListDtoResult articleListDtoResult = DtoResult.dataDtoSuccess(ArticleListDtoResult.class);
        articleListDtoResult.setList(articleList);
        return articleListDtoResult;
    }

    @Override
    @Transactional
    public Result publish(Article article) {
        // 判断是新发表文章还是修改
        if (article.getId() == null || article.getId() == -1) {

            UserDtoResult userDtoResult = userFeignClient.currentUser().getData();
            System.out.println(userDtoResult);
            article.setUid(userDtoResult.getId());

            //处理文章摘要
            if (StringUtils.isEmpty(article.getSummary())) {
                //直接截取
                String stripHtml = stripHtml(article.getHtmlContent());
                article.setSummary(stripHtml.substring(0, Math.min(stripHtml.length(), 50)));
            }
            articleMapper.insert(article);
        } else {
            transactionTemplate.executeWithoutResult(status -> {
                refreshTag(article);
                article.setEditTime(new Date());
                articleMapper.updateArticle(article);
            });
        }

        articleESMapper.save(article);
        return Result.success("成功");
    }

    /*
        提交的标签与数据库的标签对比
        1 如果存在，则不添加
        2.1 如果提交的标签不存在、数据库存在，则删除
        2.2 如果提交的标签存在，数据库不存在，则添加
     */
    private void refreshTag(Article article) {
        List<Tags> pushTags = article.getTags(); // 提交的标签
        Map<Long, Tags> pushTagsHashMap = pushTags.stream().collect(Collectors.toMap(Tags::getId, Function.identity()));

        List<Tags> tagsList = tagsMapper.selectByAid(article.getId());
        Map<Long, Tags> tagsMap = tagsList.stream().collect(Collectors.toMap(Tags::getId, Function.identity()));
        // 删除标签
        tagsList.forEach(tags -> {
            Tags t = pushTagsHashMap.get(tags.getId());
            if (t == null) {
                articleTagsMapper.deleteByAidAndTid(article.getId(), tags.getId());
            }
        });

        // 添加标签
        pushTags.forEach(tags -> {
            Tags t = tagsMap.get(tags.getId());
            if (t == null) {
                ArticleTags articleTags = new ArticleTags()
                        .setAid(article.getId())
                        .setTid(tags.getId());
                articleTagsMapper.insert(articleTags);
            }
        });
    }

    private NativeSearchQuery buildNativeSearchQuery(Long currentPage, Integer pageSize, String keywords, Long cid) {
        // 1. 创建 查询对象的构建对象 NativeSearchQueryBuilder
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        // 2. 设置查询的条件
        //使用：QueryBuilders.matchQuery("title", keywords) ，搜索华为 ---> 华    为 二字可以拆分查询，
        //使用：QueryBuilders.matchPhraseQuery("title", keywords) 华为二字不拆分查询
//        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("title", keywords));
        if (StringUtils.isEmpty(keywords)) {
            builder.withQuery(QueryBuilders.matchAllQuery());
        } else {
            builder.withQuery(QueryBuilders.multiMatchQuery(keywords, "title", "summary"));
        }

        builder.withPageable(PageRequest.of(currentPage.intValue() - 1, pageSize))//   分页
                .withSort(SortBuilders.fieldSort("readCount").order(SortOrder.DESC)) // 排序
                .withHighlightFields(
                        new HighlightBuilder.Field(SerializedLambdaUtil.getFieldName(Article::getTitle)),
                        new HighlightBuilder.Field(SerializedLambdaUtil.getFieldName(Article::getSummary)))  // 设置高亮属性
                .withHighlightBuilder(new HighlightBuilder().preTags("<span style=\"color:red\">").postTags("</span>")) // 高亮标识
        ;

        // 3. 封装分类过滤(布尔检索)
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (cid != null && cid != -1L) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("cid", cid));
        }

        // 关联过滤
        builder.withFilter(boolQueryBuilder);

        //4.构建查询对象
        NativeSearchQuery query = builder.build();
        return query;
    }

    private SearchHits<Article> getSearchHits(NativeSearchQuery query) {
        //5.执行查询
        SearchHits<Article> searchHits = elasticsearchRestTemplate.search(query, Article.class);
        return searchHits;
    }

    // ES搜索
    private List<Article> queryByES(NativeSearchQuery query, SearchHits<Article> searchHits) {

        //对搜索searchHits集合进行分页封装
        SearchPage<Article> searchPage = SearchHitSupport.searchPageFor(searchHits, query.getPageable());

        // 获取命中的列
        List<SearchHit<Article>> hitList = searchPage.getContent();
        List<Article> articleList = new ArrayList<>(hitList.size());
        // 获取命中的每行数据
        hitList.forEach(searchHit -> {
            Article content = searchHit.getContent();
            Article article = new Article();
            // 进行复制，目的的为了释放空间
            BeanUtils.copyProperties(content, article);

            // 获取设置了高亮的属性与其对应值
            Map<String, List<String>> highlightFieldsMap = searchHit.getHighlightFields();
            highlightFieldsMap.forEach((key, fragments) -> setHighlight(article, Article::getTitle, key, fragments));
            highlightFieldsMap.forEach((key, fragments) -> setHighlight(article, Article::getSummary, key, fragments));
            articleList.add(article);

        });
        return articleList;
    }

    /**
     * @param article
     * @param sFunction 需要高亮的字段的geeter方法引用
     * @param key       已经获取了的 设置了高亮的属性字段名
     * @param fragments 获取的已经获取了的 设置了高亮的值
     */
    private <T, R> void setHighlight(Article article, SFunction<T, R> sFunction, String key, List<String> fragments) {

        String fieldName = SerializedLambdaUtil.getFieldName(sFunction);
        // 判断是否是需要设置的字段
        if (!key.equals(fieldName)) {
            return;
        }
        Class<? extends Article> cls = article.getClass();
        Field[] fields = cls.getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            if (field.getName().equals(fieldName)) {
                StringBuffer sb = new StringBuffer();
                for (String s : fragments) {
                    sb.append(s);
                }
                try {
                    Field instanceField = cls.getDeclaredField(fieldName);
                    instanceField.setAccessible(true);
                    instanceField.set(article, sb.toString());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public Result importArticle() {
        try {
            List<Article> articleList = articleMapper.getArticleList(null, null, null);
            articleESMapper.saveAll(articleList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("导入失败");
        }
        return Result.success("导入成功");
    }

    @Override
    public DtoResult getCount() {
        long count = articleESMapper.count();
        DtoResult success = DtoResult.success();
        success.setData(count);
        return success;
    }

    @Override
    public Result zan(Long aid) {
        BaseResponse<UserDtoResult> currentUser = userFeignClient.currentUser();
        if (currentUser.getData().getId() == null) {
            return Result.fail("未登录");
        }

        Zan zan = zanMapper.getByAidAndUid(aid, currentUser.getData().getId());
        if (zan != null) {
            if (ZanTypeEnums.ZAN.getCode().equals(zan.getType())) {
                zan.setType(ZanTypeEnums.CANCEL.getCode());
            } else {
                zan.setType(ZanTypeEnums.ZAN.getCode());
            }
            zanMapper.updateById(zan);
        } else {
            Zan entity = new Zan()
                    .setAid(aid)
                    .setUid(currentUser.getData().getId())
                    .setType(ZanTypeEnums.ZAN.getCode());
            int insert = zanMapper.insert(entity);
            if (insert < 0) {
                return Result.fail("失败");
            }
        }

        return Result.success("成功");
    }

    @Override
    public ZanStateDtoResult zanState(Long aid) {
        BaseResponse<UserDtoResult> currentUser = userFeignClient.currentUser();
        ZanStateDtoResult dtoResult = DtoResult.dataDtoSuccess(ZanStateDtoResult.class);
        // 未登录 点赞按钮是【未点赞状态】
        if (currentUser.getData().getId() == null) {
            dtoResult.setType(ZanTypeEnums.CANCEL.getCode());
        }else {
            Zan zan = zanMapper.getByAidAndUid(aid, currentUser.getData().getId());
            dtoResult.setType(zan.getType());
        }
        Long count = zanMapper.getCountByAid(aid);
        dtoResult.setCount(count);
        return dtoResult;
    }
}
