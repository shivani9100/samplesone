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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "RamMaster")
//, uniqueConstraints = {
// @UniqueConstraint(columnNames = { "ram_capacity" }, name =
// "uniqueRamCapacity") })
public class RamCapacityMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ramId;
	@Column(name = "ram_capacity")
	private String ramCapacity;
	private boolean ramStatus;
	@JsonBackReference
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "ramType_capacities", joinColumns = {
			@JoinColumn(name = "ramCapacity_Id") }, inverseJoinColumns = { @JoinColumn(name = "ramType_Id") })
	private List<RamTypeMaster> ramTypeMasters = new ArrayList<>();

	private String createdBy;
	
	private Date lastmodefiedDate;

	private String updatedBy;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date createdOn;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date updatedOn;
	
	

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

	public Boolean getRamStatus() {
		return ramStatus;
	}

	public void setRamStatus(boolean ramStatus) {
		this.ramStatus = ramStatus;
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

	public List<RamTypeMaster> getRamTypeMasters() {
		return ramTypeMasters;
	}

	public void setRamTypeMasters(List<RamTypeMaster> ramTypeMasters) {
		this.ramTypeMasters = ramTypeMasters;
	}

	public Date getLastmodefiedDate() {
		return lastmodefiedDate;
	}

	public void setLastmodefiedDate(Date lastmodefiedDate) {
		this.lastmodefiedDate = lastmodefiedDate;
	}
	

}