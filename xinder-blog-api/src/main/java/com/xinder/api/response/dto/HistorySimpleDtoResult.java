package com.xinder.api.response.dto;

import com.xinder.api.bean.History;
import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.*;

/**
 * @author Xinder
 * @date 2023-03-31 16:48
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class HistorySimpleDtoResult extends DtoResult {

    @ApiModelProperty(name = "date", notes = "浏览日期")
    private Date date;

    @ApiModelProperty(name = "dateStr", notes = "浏览日期（字符串）")
    private String dateStr;


    @ApiModelProperty(name = "list", notes = "当前日期的浏览列表")
    private List<History> historyList = new ArrayList<>();
}
