package com.Inventory.Project.AssectService.Model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
//import javax.validation.constraints.Size;
import com.sun.istack.NotNull;

@Entity
//@Table(name = "HardDiskCapacity")

//@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "harddisk_capacity_type" }) })

public class HardDiskCapacity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@Column(name = "harddisk_capacity_id")
	private Integer harddiskCapacityId;

	@Size(min = 3, message = " name must having minimum 3 charactor")
	@Size(max = 15, message = " name must having maximum 15 charactor")
	@NotNull
	@Column(name = "harddisk_capacity_type", columnDefinition = "VARCHAR(20)")
	private String harddiskCapacityType;

	@Column(name = "harddisk_capacity_status")
	private boolean harddiskCapacityStatus;

	private String createdBy;

	private String updatedBy;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date createdOn;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date updatedOn;
	
	private Date lastmodefiedDate;

	@JsonBackReference
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "hardDiskType_capacities", joinColumns = {
			@JoinColumn(name = "harddiskCapacityId") }, inverseJoinColumns = {
					@JoinColumn(name = "hardDiskTypeId") })
	private Set<HardDiskTypeMaster> capacities = new HashSet<>();

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

	public Set<HardDiskTypeMaster> getCapacities() {
		return capacities;
	}

	public void setCapacities(Set<HardDiskTypeMaster> capacities) {
		this.capacities = capacities;
	}

	public Date getLastmodefiedDate() {
		return lastmodefiedDate;
	}

	public void setLastmodefiedDate(Date lastmodefiedDate) {
		this.lastmodefiedDate = lastmodefiedDate;
	}



}