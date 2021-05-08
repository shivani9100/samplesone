package com.Inventory.Project.AssectService.Model;

import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Brand implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer brandid;
	// @Column(unique = true)
	private String brandname;

	private Boolean brandstatus;

	private String createdBy;

	private String updatedBy;

	private Date createdOn;
	private Date updatedOn;

	private Date lastmodefiedDate;

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

	@JsonManagedReference
	@OneToMany(mappedBy = "brand", fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Model.class)
	private List<Model> modelList = new ArrayList<>();

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = AssetTypeMaster.class)
	@JoinTable(name = "AssetType_brand", inverseJoinColumns = @JoinColumn(name = "assetType_id", referencedColumnName = "assetId"),

			joinColumns = @JoinColumn(name = "Brand_bid", referencedColumnName = "brandid")

	)

	private AssetTypeMaster assetTypeMasterEx;

	public AssetTypeMaster getAssetTypeMasterEx() {
		return assetTypeMasterEx;
	}

	public void setAssetTypeMasterEx(AssetTypeMaster assetTypeMasterEx) {
		this.assetTypeMasterEx = assetTypeMasterEx;
	}

	public Brand() {
		super();
	}

	public Brand(Integer brandid, String brandname, Boolean brandstatus, List<Model> modelList, Date lastmodefiedDate) {
		super();
		this.brandid = brandid;
		this.brandname = brandname;
		this.brandstatus = brandstatus;
		this.modelList = modelList;
		this.lastmodefiedDate = lastmodefiedDate;
	}

	public List<Model> getModelList() {
		return modelList;
	}

	public void setModelList(List<Model> modelList) {
		this.modelList = modelList;
	}

	public Integer getBrandid() {
		return brandid;
	}

	public void setBrandid(Integer brandid) {
		this.brandid = brandid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getBrandname() {
		return brandname;
	}

	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}

	public Boolean getBrandstatus() {
		return brandstatus;
	}

	public void setBrandstatus(Boolean brandstatus) {
		this.brandstatus = brandstatus;
	}

	public Date getLastmodefiedDate() {
		return lastmodefiedDate;
	}

	public void setLastmodefiedDate(Date lastmodefiedDate) {
		this.lastmodefiedDate = lastmodefiedDate;
	}

}
