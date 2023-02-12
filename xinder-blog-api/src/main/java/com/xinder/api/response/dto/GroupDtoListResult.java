package com.xinder.api.response.dto;

import com.xinder.api.bean.Group;
import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Xinder
 * @date 2023-02-11 13:51
 */
@Data
public class GroupDtoListResult extends DtoResult {

    private static final long serialVersionUID = 574217966803557L;

    @ApiModelProperty(name = "list", notes = "群聊列表")
    private List<Group> list;
}
