package com.Inventory.Project.AssectService.Pagination;

import java.util.List;

public class PagenationResponse {

	private List<?> list;

	private Long totalNumberOfPages;

	private Long totalNumberOfRecords;

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public Long getTotalNumberOfPages() {
		return totalNumberOfPages;
	}

	public void setTotalNumberOfPages(Long totalNumberOfPages) {
		this.totalNumberOfPages = totalNumberOfPages;
	}

	public Long getTotalNumberOfRecords() {
		return totalNumberOfRecords;
	}

	public void setTotalNumberOfRecords(Long totalNumberOfRecords) {
		this.totalNumberOfRecords = totalNumberOfRecords;
	}

}
