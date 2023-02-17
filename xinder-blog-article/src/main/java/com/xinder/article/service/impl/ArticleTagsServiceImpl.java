package com.xinder.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinder.api.bean.ArticleTags;
import com.xinder.article.mapper.ArticleTagsMapper;
import com.xinder.article.service.ArticleTagsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xinder
 * @since 2023-02-17
 */
@Service
public class ArticleTagsServiceImpl extends ServiceImpl<ArticleTagsMapper, ArticleTags> implements ArticleTagsService {

}
