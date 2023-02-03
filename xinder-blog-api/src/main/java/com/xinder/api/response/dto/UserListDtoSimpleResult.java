package com.xinder.api.response.dto;

import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author Xinder
 * @date 2023-02-03 20:05
 */
public class UserListDtoSimpleResult extends DtoResult {


    @ApiModelProperty(name = "list", notes = "用户端查询的用户列表")
    private List<UserDtoSimpleResult> list;

    public List<UserDtoSimpleResult> getList() {
        return list;
    }

    public void setList(List<UserDtoSimpleResult> list) {
        this.list = list;
    }
}
