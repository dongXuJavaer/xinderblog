package com.xinder.api.response.dto;

import com.xinder.api.bean.Article;
import com.xinder.api.bean.Tags;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.PageDtoResult;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author Xinder
 * @date 2023-01-10 23:41
 */
public class TagsDtoListResult extends PageDtoResult{

    private static final long serialVersionUID = 5742179663559L;

    @ApiModelProperty(name = "list", notes = "标签列表")
    private List<Tags> list;

    public List<Tags> getList() {
        return list;
    }

    public void setList(List<Tags> list) {
        this.list = list;
    }

}
