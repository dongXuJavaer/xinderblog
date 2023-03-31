package com.xinder.api.response.dto;

import com.xinder.api.bean.Comments;
import com.xinder.api.bean.History;
import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Xinder
 * @date 2023-03-31 13:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HistoryListDtoResult extends DtoResult {


    @ApiModelProperty(name = "list", notes = "按日期分类的浏览记录列表")
    private List<HistorySimpleDtoResult> list;

}
