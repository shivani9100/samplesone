package com.Inventory.Project.AssectService.Response;

import java.util.List;

public class ResponseList {
	private List<?> list;
	private Long noOfrecords;
	private Integer totalNumberOfPages;
	

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	public Long getNoOfrecords() {
		return noOfrecords;
	}

	public void setNoOfrecords(Long noOfrecords) {
		this.noOfrecords = noOfrecords;
	}

	public Integer getTotalNumberOfPages() {
		return totalNumberOfPages;
	}

	public void setTotalNumberOfPages(Integer totalNumberOfPages) {
		this.totalNumberOfPages = totalNumberOfPages;
	}
}
