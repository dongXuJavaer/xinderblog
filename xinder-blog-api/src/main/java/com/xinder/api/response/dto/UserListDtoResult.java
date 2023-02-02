package com.xinder.api.response.dto;

import com.xinder.api.bean.Article;
import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author Xinder
 * @date 2023-02-02 18:54
 */
public class UserListDtoResult extends DtoResult {
    private static final long serialVersionUID = 5742179663559L;

    @ApiModelProperty(name = "list", notes = "文章列表")
    private List<UserDtoResult> list;

    public List<UserDtoResult> getList() {
        return list;
    }

    public void setList(List<UserDtoResult> list) {
        this.list = list;
    }
}
