package com.Inventory.Project.AssectService.Pagination;

public class RamCapacityPagination {
	private int ramtypeId;
	private String ramtypeName;
	private Integer ramId;
	private String ramCapacity;
	private boolean ramtypeStatus;
	private String createdBy;
	private String updatedBy;
	private String createdOn;
	private String updatedOn;

	public RamCapacityPagination() {

	}

	public int getRamtypeId() {
		return ramtypeId;
	}

	public void setRamtypeId(int ramtypeId) {
		this.ramtypeId = ramtypeId;
	}

	public String getRamtypeName() {
		return ramtypeName;
	}

	public void setRamtypeName(String ramtypeName) {
		this.ramtypeName = ramtypeName;
	}

	public Integer getRamId() {
		return ramId;
	}

	public void setRamId(Integer ramId) {
		this.ramId = ramId;
	}

	public String getRamCapacity() {
		return ramCapacity;
	}

	public void setRamCapacity(String ramCapacity) {
		this.ramCapacity = ramCapacity;
	}

	public boolean isRamtypeStatus() {
		return ramtypeStatus;
	}

	public void setRamtypeStatus(boolean ramtypeStatus) {
		this.ramtypeStatus = ramtypeStatus;
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
}
