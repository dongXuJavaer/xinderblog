package com.xinder.api.response.result;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class PageDtoResult extends DtoResult implements Serializable {

	private static final long serialVersionUID = -2557457192133618747L;

	@ApiModelProperty(name = "page", notes = "当前页")
	private Long currentPage;
	@ApiModelProperty(name = "totalPage", notes = "总页数")
	private Long totalPage;
	@ApiModelProperty(name = "totalCount", notes = "总条数")
	private Long totalCount;

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Long getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Long currentPage) {
		this.currentPage = currentPage;
	}

	public Long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Long totalPage) {
		this.totalPage = totalPage;
	}

}
