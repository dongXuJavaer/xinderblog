package com.xinder.api.response.dto;

import com.xinder.api.bean.PointInfo;
import com.xinder.api.response.result.DtoResult;
import com.xinder.api.response.result.PageDtoResult;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author Xinder
 * @date 2023-02-02 18:54
 */
public class PointInfoListDtoResult extends PageDtoResult {
    private static final long serialVersionUID = 5742179663559L;

    @ApiModelProperty(name = "list", notes = "积分列表")
    private List<PointInfo> list;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<PointInfo> getList() {
        return list;
    }

    public void setList(List<PointInfo> list) {
        this.list = list;
    }
}
