package com.xinder.api.response.dto;

import com.xinder.api.bean.Notification;
import com.xinder.api.response.result.DtoResult;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author Xinder
 * @date 2023-03-28 16:03
 */

public class NotificationDtoListResult extends DtoResult {

    private static final long serialVersionUID = 5742179663559L;
    @ApiModelProperty(name = "list", notes = "通知列表")
    private List<Notification> list;

    public List<Notification> getList() {
        return list;
    }

    public void setList(List<Notification> list) {
        this.list = list;
    }
}
