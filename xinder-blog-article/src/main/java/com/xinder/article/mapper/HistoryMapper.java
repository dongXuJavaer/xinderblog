package com.xinder.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinder.api.bean.History;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xinder
 * @since 2023-03-30
 */
public interface HistoryMapper extends BaseMapper<History> {

    /**
     * 批量保存
     *
     * @param historyList 历史列表
     * @param uid         uid
     */
    int batchSave(@Param("historyList") List<History> historyList, @Param("uid") Long uid);

    /**
     * 批量更新
     *
     * @param updateList 更新列表
     */
    void batchUpdate(@Param("updateList") List<History> updateList);
}
