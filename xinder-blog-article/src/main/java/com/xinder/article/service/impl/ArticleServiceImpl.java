package com.xinder.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.Article;
import com.xinder.api.bean.ArticleTags;
import com.xinder.api.bean.Tags;
import com.xinder.api.request.ArticleDtoReq;
import com.xinder.api.response.dto.ArticleListDtoResult;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.Result;
import com.xinder.article.mapper.ArticleESMapper;
import com.xinder.article.mapper.ArticleMapper;
import com.xinder.article.mapper.ArticleTagsMapper;
import com.xinder.article.mapper.TagsMapper;
import com.xinder.article.service.ArticleService;
import com.xinder.common.util.SFunction;
import com.xinder.common.util.SerializedLambdaUtil;
import com.xinder.common.util.TokenDecode;
import com.xinder.common.util.Util;
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

    public int addNewArticle(Article article) {
        //??????????????????
        if (article.getSummary() == null || "".equals(article.getSummary())) {
            //????????????
            String stripHtml = stripHtml(article.getHtmlContent());
            article.setSummary(stripHtml.substring(0, stripHtml.length() > 50 ? 50 : stripHtml.length()));
        }
        if (article.getId() == -1) {
            //????????????
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (article.getState() == 1) {
                //??????????????????
                article.setCreateTime(timestamp);
            }
            article.setEditTime(timestamp);
            //??????????????????
            article.setUid(Util.getCurrentUser(tokenDecode, redisTemplate).getId());
            int i = articleMapper.addNewArticle(article);
            //?????????
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
                //??????????????????
                article.setCreateTime(timestamp);
            }
            //??????
            article.setEditTime(new Timestamp(System.currentTimeMillis()));
            int i = articleMapper.updateArticle(article);
            //????????????
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
        //1.????????????????????????????????????????????????
        tagsMapper.deleteTagsByAid(aid);
        //2.?????????????????????????????????????????????
        tagsMapper.saveTags(dynamicTags);
        //3.?????????????????????id
        List<Long> tIds = tagsMapper.getTagsIdByTagName(dynamicTags);
        //4.???????????????????????????
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
            return articleMapper.updateArticleState(aids, 2);//?????????????????????
        }
    }

    public int restoreArticle(Integer articleId) {
        return articleMapper.updateArticleStateById(articleId, 1); // ???????????????????????????
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
     * ???????????????????????????
     *
     * @return
     */
    public List<String> getCategories() {
        return articleMapper.getCategories(Util.getCurrentUser(tokenDecode, redisTemplate).getId());
    }

    /**
     * ???????????????????????????
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

        // ES??????
        NativeSearchQuery query = buildNativeSearchQuery(currentPage, pageSize, keywords);
        SearchHits<Article> searchHits = getSearchHits(query);
        List<Article> articleList = queryByES(query, searchHits);
        Long rows = searchHits.getTotalHits();

        articleList.forEach(item -> {
            if (StringUtils.isEmpty(item.getHeadPic())) {
                item.setHeadPic("");
            }
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
     * ?????????+1
     *
     * @param article
     * @return
     */
    private boolean addReadCount(Article article) {
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
        // ????????????????????????????????????
        if (article.getId() == null || article.getId() == -1) {
            //??????????????????
            if (StringUtils.isEmpty(article.getSummary())) {
                //????????????
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
        return Result.success("??????");
    }

    /*
        ??????????????????????????????????????????
        1 ???????????????????????????
        2.1 ????????????????????????????????????????????????????????????
        2.2 ????????????????????????????????????????????????????????????
     */
    private void refreshTag(Article article) {
        List<Tags> pushTags = article.getTags(); // ???????????????
        Map<Long, Tags> pushTagsHashMap = pushTags.stream().collect(Collectors.toMap(Tags::getId, Function.identity()));

        List<Tags> tagsList = tagsMapper.selectByAid(article.getId());
        Map<Long, Tags> tagsMap = tagsList.stream().collect(Collectors.toMap(Tags::getId, Function.identity()));
        // ????????????
        tagsList.forEach(tags -> {
            Tags t = pushTagsHashMap.get(tags.getId());
            if (t == null) {
                articleTagsMapper.deleteByAidAndTid(article.getId(), tags.getId());
            }
        });

        // ????????????
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

    private NativeSearchQuery buildNativeSearchQuery(Long currentPage, Integer pageSize, String keywords) {
        // 1. ?????? ??????????????????????????? NativeSearchQueryBuilder
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        // 2. ?????????????????????
        //?????????QueryBuilders.matchQuery("title", keywords) ??????????????? ---> ???    ??? ???????????????????????????
        //?????????QueryBuilders.matchPhraseQuery("title", keywords) ???????????????????????????
//        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("title", keywords));
        if (StringUtils.isEmpty(keywords)) {
            builder.withQuery(QueryBuilders.matchAllQuery());
        } else {
            builder.withQuery(QueryBuilders.multiMatchQuery(keywords, "title", "summary"));
        }

        builder.withPageable(PageRequest.of(currentPage.intValue() - 1, pageSize))//   ??????
                .withSort(SortBuilders.fieldSort("readCount").order(SortOrder.DESC)) // ??????
                .withHighlightFields(
                        new HighlightBuilder.Field(SerializedLambdaUtil.getFieldName(Article::getTitle)),
                        new HighlightBuilder.Field(SerializedLambdaUtil.getFieldName(Article::getSummary)))  // ??????????????????
                .withHighlightBuilder(new HighlightBuilder().preTags("<span style=\"color:red\">").postTags("</span>")) // ????????????
        ;

        //4.??????????????????
        NativeSearchQuery query = builder.build();
        return query;
    }

    private SearchHits<Article> getSearchHits(NativeSearchQuery query) {
        //5.????????????
        SearchHits<Article> searchHits = elasticsearchRestTemplate.search(query, Article.class);
        return searchHits;
    }

    // ES??????
    private List<Article> queryByES(NativeSearchQuery query, SearchHits<Article> searchHits) {

        //?????????searchHits????????????????????????
        SearchPage<Article> searchPage = SearchHitSupport.searchPageFor(searchHits, query.getPageable());

        // ??????????????????
        List<SearchHit<Article>> hitList = searchPage.getContent();
        List<Article> articleList = new ArrayList<>(hitList.size());
        // ???????????????????????????
        hitList.forEach(searchHit -> {
            Article content = searchHit.getContent();
            Article article = new Article();
            // ??????????????????????????????????????????
            BeanUtils.copyProperties(content, article);

            // ?????????????????????????????????????????????
            Map<String, List<String>> highlightFieldsMap = searchHit.getHighlightFields();
            highlightFieldsMap.forEach((key, fragments) -> setHighlight(article, Article::getTitle, key, fragments));
            highlightFieldsMap.forEach((key, fragments) -> setHighlight(article, Article::getSummary, key, fragments));
            articleList.add(article);

        });
        return articleList;
    }

    /**
     * @param article
     * @param sFunction ????????????????????????geeter????????????
     * @param key       ?????????????????? ?????????????????????????????????
     * @param fragments ??????????????????????????? ?????????????????????
     */
    private <T, R> void setHighlight(Article article, SFunction<T, R> sFunction, String key, List<String> fragments) {

        String fieldName = SerializedLambdaUtil.getFieldName(sFunction);
        // ????????????????????????????????????
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
            return Result.fail("????????????");
        }
        return Result.success("????????????");
    }
}
