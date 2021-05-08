package com.Inventory.Project.AssectService.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "RamTypeMaster", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "ramtype_name" }, name = "uniqueRamtypeName") })
public class RamTypeMaster {

	@Id
	@Valid
	@Size(min = 2)
	@GeneratedValue(strategy = GenerationType.IDENTITY)

//     @GeneratedValue(generator = "UUID2")
//    @GenericGenerator(
//        name = "UUID2",
//    strategy = "org.hibernate.id.UUIDGenerator"
//    )

	@Column(name = "ramtype_id")
	private int ramtypeId;

	@NotNull
//    @Size(min = 3, message = " ramtypeName must having minimum 3 charactor")
//    @Size(max = 15, message = " ramtypeName must having maximum 15 charactor")
	@Valid
	@Min(value = 3)
	@Column(name = "ramtype_name")
	private String ramtypeName;

	@NotNull
	@Column(name = "ramtype_status")
	private boolean ramtypeStatus;
	// @JsonManagedReference
	@ManyToMany(mappedBy = "ramTypeMasters")
	private List<RamCapacityMaster> ramMaster = new ArrayList<>();

	private String createdBy;

	private String updatedBy;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date createdOn;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date updatedOn;
	
	private Date lastmodefiedDate;

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

	public List<RamCapacityMaster> getRamMaster() {
		return ramMaster;
	}

	public void setRamMaster(List<RamCapacityMaster> ramMaster) {
		this.ramMaster = ramMaster;
	}

	public Date getLastmodefiedDate() {
		return lastmodefiedDate;
	}

	public void setLastmodefiedDate(Date lastmodefiedDate) {
		this.lastmodefiedDate = lastmodefiedDate;
	}
	

}