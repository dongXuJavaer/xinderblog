package com.xinder.article.mapper;

import com.xinder.api.bean.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Xinder
 * @date 2023-02-20 22:54
 */
@Repository
public interface ArticleESMapper extends ElasticsearchRepository<Article, Long> {
}
