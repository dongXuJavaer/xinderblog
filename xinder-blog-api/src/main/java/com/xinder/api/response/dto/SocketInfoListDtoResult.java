package com.xinder.api.response.dto;

import com.xinder.api.bean.SocketInfo;
import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author Xinder
 * @date 2023-02-13 15:41
 */
public class SocketInfoListDtoResult extends DtoResult {

    @ApiModelProperty(name = "list", notes = "聊天框列表")
    private List<SocketInfoDtoResult> list;

    public List<SocketInfoDtoResult> getList() {
        return list;
    }

    public void setList(List<SocketInfoDtoResult> list) {
        this.list = list;
    }
}
