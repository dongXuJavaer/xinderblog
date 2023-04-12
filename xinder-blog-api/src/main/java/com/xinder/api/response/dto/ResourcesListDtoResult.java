package com.xinder.api.response.dto;

import com.xinder.api.bean.Article;
import com.xinder.api.bean.Resources;
import com.xinder.api.response.result.PageDtoResult;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 资源列表dto结果
 *
 * @author Xinder
 * @date 2023-01-06 21:41
 */
public class ResourcesListDtoResult extends PageDtoResult implements Serializable {

    private static final long serialVersionUID = 574217966803559L;

    @ApiModelProperty(name = "list", notes = "资源列表")
    private List<Resources> list;

    public List<Resources> getList() {
        return list;
    }

    public void setList(List<Resources> list) {
        this.list = list;
    }
}
