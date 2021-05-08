package com.Inventory.Project.AssectService.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "hard_disk_type_master", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "harddisk_type" }, name = "uniqueHardDidkType") })
public class HardDiskTypeMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// @Column(name = "hard_id")
	private Integer hardDiskTypeId;
	@Column(name = "harddisk_type", columnDefinition = "VARCHAR(50)")
	private String hardDiskType;
	@Column(name = "harddisk_status")
	private Boolean hardDiskStatus;

	private String createdBy;
	private String updatedBy;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date createdOn;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date updatedOn;
	
	private Date lastmodefiedDate;

	// @JsonManagedReference
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "capacities")

	private List<HardDiskCapacity> capac = new ArrayList<>();

	public Integer getHardDiskTypeId() {
		return hardDiskTypeId;
	}

	public void setHardDiskTypeId(Integer hardDiskTypeId) {
		this.hardDiskTypeId = hardDiskTypeId;
	}

	public String getHardDiskType() {
		return hardDiskType;
	}

	public void setHardDiskType(String hardDiskType) {
		this.hardDiskType = hardDiskType;
	}

	public Boolean getHardDiskStatus() {
		return hardDiskStatus;
	}

	public void setHardDiskStatus(Boolean hardDiskStatus) {
		this.hardDiskStatus = hardDiskStatus;
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

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public List<HardDiskCapacity> getCapac() {
		return capac;
	}

	public void setCapac(List<HardDiskCapacity> capac) {
		this.capac = capac;
	}

	public Date getLastmodefiedDate() {
		return lastmodefiedDate;
	}

	public void setLastmodefiedDate(Date lastmodefiedDate) {
		this.lastmodefiedDate = lastmodefiedDate;
	}



	
}
