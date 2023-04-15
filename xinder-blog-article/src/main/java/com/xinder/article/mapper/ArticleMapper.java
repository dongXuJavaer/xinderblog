package com.xinder.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinder.api.bean.Article;
import com.xinder.api.request.ArticleDtoReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by sang on 2017/12/20.
 */
//@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    int addNewArticle(Article article);

    int updateArticle(Article article);

    List<Article> getArticleByState(@Param("state") Integer state, @Param("offset") Integer offset, @Param("count") Integer count,
                                    @Param("keywords") String keywords);

//    List<Article> getArticleByStateByAdmin(@Param("start") int start, @Param("count") Integer count, @Param("keywords") String keywords);

    int getArticleCountByState(@Param("state") Integer state, @Param("keywords") String keywords);

    int updateArticleState(@Param("aids") Long aids[], @Param("state") Integer state);

    int updateArticleStateById(@Param("articleId") Integer articleId, @Param("state") Integer state);

    int deleteArticleById(@Param("aids") Long[] aids);

    Article getArticleById(Long aid);

    void pvIncrement(Long aid);

    //INSERT INTO pv(countDate,pv,uid) SELECT NOW(),SUM(pageView),uid FROM article GROUP BY uid
    void pvStatisticsPerDay();

    List<String> getCategories(Long uid);

    List<Integer> getDataStatistics(Long uid);

    Long getCount(@Param("req") ArticleDtoReq req, @Param("keyword") String keyword);

    List<Article> getArticleList(@Param("req") ArticleDtoReq req, @Param("offset") Long offset, @Param("keywords") String keywords);


    Integer batchUpdateStateById(@Param("articleList") List<Article> articleList , @Param("state") Integer state);

    /**
     * 批量查询
     *
     * @param aids 多头
     * @return {@link List}<{@link Article}>
     */
    List<Article> selectBatchByIds(@Param("aids") List<Long> aids);
}
