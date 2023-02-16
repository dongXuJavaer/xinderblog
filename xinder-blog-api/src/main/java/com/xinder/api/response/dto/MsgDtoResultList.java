package com.xinder.api.response.dto;

import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author Xinder
 * @date 2023-02-16 16:38
 */
public class MsgDtoResultList extends DtoResult {

    private static final long serialVersionUID = 5742179663559L;
    @ApiModelProperty(name = "list", notes = "消息列表")
    private List<MsgDtoResult> list;

    public List<MsgDtoResult> getList() {
        return list;
    }

    public void setList(List<MsgDtoResult> list) {
        this.list = list;
    }
}
