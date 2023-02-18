package com.xinder.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinder.api.bean.ArticleTags;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xinder
 * @since 2023-02-17
 */
public interface ArticleTagsMapper extends BaseMapper<ArticleTags> {

    void deleteByAidAndTid(@Param("aid") Long aid, @Param("tid") Long tid);

    void deleteByTid(@Param("tIds") List<Long> tIds);
}
