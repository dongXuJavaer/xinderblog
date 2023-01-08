package com.xinder.api.request.comm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PageDtoReq implements Serializable {

	private static final long serialVersionUID = 965132976922378L;

	@ApiModelProperty(name = "currentPage", value = "页数(第几页),默认第一页", notes = "页数(第几页),默认第一页")
	private Long currentPage = 1L;
	@ApiModelProperty(name = "pageSize", value = "每页展示条数,默认10条", notes = "每页展示条数,默认10条")
	private Integer pageSize = 10;


}
