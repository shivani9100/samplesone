package com.Inventory.Project.AssectService.AssectEmployee;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.Inventory.Project.AssectService.Assect.AssectModel;
import com.Inventory.Project.AssectService.Assect.AssectModelRequest;
import com.Inventory.Project.AssectService.Employee.Employee;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "AssectEmployee")
//@Table(name = "AssectEmployee", uniqueConstraints = {
		//@UniqueConstraint(columnNames = { "deskNumber" }, name = "uniqueDeskNumber") })
public class AssectEmployeeModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer assectemployeeid;

	@ManyToOne(cascade = { CascadeType.ALL, CascadeType.REMOVE })
	@JoinColumn(name = "assectid")
	private AssectModel assectModel;

	@ManyToOne(cascade = { CascadeType.ALL, CascadeType.REMOVE })
	@JoinColumn(name = "employeeid")
	private Employee employee;

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Employee getEmployee() {
		return employee;
	}

	@Temporal(TemporalType.DATE)
	private Date dateOfAssignment;
	private String deskNumber;
	private String location;
	private String floor;
	private Boolean status;
	private String updatedBy;
	private String createdBy;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date createdon;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date updateon;

	public Date getCreatedon() {
		return createdon;
	}

	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}

	public Date getUpdateon() {
		return updateon;
	}

	public void setUpdateon(Date updateon) {
		this.updateon = updateon;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public AssectEmployeeModel(Integer assectemployeeid, AssectModel assectModel, Employee employee,
			Date dateOfAssignment, String deskNumber, String location, String floor, Boolean status) {
		super();
		this.assectemployeeid = assectemployeeid;
		this.assectModel = assectModel;
		this.employee = employee;
		this.dateOfAssignment = dateOfAssignment;
		this.deskNumber = deskNumber;
		this.location = location;
		this.floor = floor;
		this.status = status;
	}

	public Integer getAssectemployeeid() {
		return assectemployeeid;
	}

	public void setAssectemployeeid(Integer assectemployeeid) {
		this.assectemployeeid = assectemployeeid;
	}

	public AssectModel getAssectModel() {
		return assectModel;
	}

	public void setAssectModel(AssectModel assectModel) {
		this.assectModel = assectModel;
	}

	public Date getDateOfAssignment() {
		return dateOfAssignment;
	}

	public void setDateOfAssignment(Date dateOfAssignment) {
		this.dateOfAssignment = dateOfAssignment;
	}

	public String getDeskNumber() {
		return deskNumber;
	}

	public void setDeskNumber(String deskNumber) {
		this.deskNumber = deskNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public AssectEmployeeModel() {
		super();
	}

}
