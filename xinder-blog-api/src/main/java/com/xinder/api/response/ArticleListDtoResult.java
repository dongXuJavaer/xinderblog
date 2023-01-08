package com.xinder.api.response;

import com.xinder.api.bean.Article;
import com.xinder.api.response.result.PageDtoResult;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author Xinder
 * @date 2023-01-06 21:41
 */
public class ArticleListDtoResult extends PageDtoResult implements Serializable {

    private static final long serialVersionUID = 574217966803559L;

    @ApiModelProperty(name = "list", notes = "文章列表")
    private List<Article> list;

    public List<Article> getList() {
        return list;
    }

    public void setList(List<Article> list) {
        this.list = list;
    }
}
