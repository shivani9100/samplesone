package com.Inventory.Project.AssectService.Pagination;

public class HardDiskCapacityPagination {
    private Integer harddiskTypeId;
	private String harddiskType;
	private String createdBy;
	private String updatedBy;
	private String createdOn;
	private String updatedOn;
	private Integer harddiskCapacityId;
	private String harddiskCapacityType;
	private String lastmodefieddate;
	private boolean harddiskCapacityStatus;

	public Integer getHarddiskTypeId() {
		return harddiskTypeId;
	}

	public void setHarddiskTypeId(Integer harddiskTypeId) {
		this.harddiskTypeId = harddiskTypeId;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public HardDiskCapacityPagination() {
		super();
	}

	public String getHarddiskType() {
		return harddiskType;
	}

	public void setHarddiskType(String harddiskType) {
		this.harddiskType = harddiskType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getHarddiskCapacityId() {
		return harddiskCapacityId;
	}

	public void setHarddiskCapacityId(Integer harddiskCapacityId) {
		this.harddiskCapacityId = harddiskCapacityId;
	}

	public String getHarddiskCapacityType() {
		return harddiskCapacityType;
	}

	public void setHarddiskCapacityType(String harddiskCapacityType) {
		this.harddiskCapacityType = harddiskCapacityType;
	}

	public boolean isHarddiskCapacityStatus() {
		return harddiskCapacityStatus;
	}

	public void setHarddiskCapacityStatus(boolean harddiskCapacityStatus) {
		this.harddiskCapacityStatus = harddiskCapacityStatus;
	}

	public String getLastmodefieddate() {
		return lastmodefieddate;
	}

	public void setLastmodefieddate(String lastmodefieddate) {
		this.lastmodefieddate = lastmodefieddate;
	}

}
