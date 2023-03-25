package com.xinder.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinder.api.bean.Comments;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xinder
 * @since 2023-02-19
 */
public interface CommentsMapper extends BaseMapper<Comments> {

    /**
     * 查询帖子的一级评论
     * @param aid
     * @return
     */
    List<Comments> getRootListByAid(@Param("aid") Long aid);

    /**
     * 根据父id查询
     * @param parentId
     * @return
     */
    List<Comments> getListByParentId(@Param("aid") Long aid, @Param("parentId") Long parentId);
}
