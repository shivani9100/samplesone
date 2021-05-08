package com.Inventory.Project.AssectService.Model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Model implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer modelid;
	@Column(unique = true)
	private String modelname;

	private Boolean modelstatus;
	private String createdBy;

	private String updatedBy;

	private Date createdOn;

	private Date updatedOn;
	
	private Date lastmodefiedDate;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@JsonBackReference

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Brand.class)
	@JoinTable(

			name = "Brand_Model",

			joinColumns = @JoinColumn(name = "Model_modelid", referencedColumnName = "modelid"),

			inverseJoinColumns = @JoinColumn(name = "Brand_bid", referencedColumnName = "brandid")

	)

	private Brand brand;

	public Model() {
		super();
	}

	public Model(Integer modelid, String modelname, Boolean modelstatus, Brand brand) {
		super();
		this.modelid = modelid;
		this.modelname = modelname;
		this.modelstatus = modelstatus;
		this.brand = brand;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Integer getModelid() {
		return modelid;
	}

	public void setModelid(Integer modelid) {
		this.modelid = modelid;
	}

	public String getModelname() {
		return modelname;
	}

	public void setModelname(String modelname) {
		this.modelname = modelname;
	}

	public Boolean getModelstatus() {
		return modelstatus;
	}

	public void setModelstatus(Boolean modelstatus) {
		this.modelstatus = modelstatus;
	}

	public Date getLastmodefiedDate() {
		return lastmodefiedDate;
	}

	public void setLastmodefiedDate(Date lastmodefiedDate) {
		this.lastmodefiedDate = lastmodefiedDate;
	}

}
