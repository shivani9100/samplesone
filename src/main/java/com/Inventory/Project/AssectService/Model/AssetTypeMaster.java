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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "AssetType1", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "assettype" }, name = "uniqueAssettype") })
public class AssetTypeMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "assetid")
	private Integer assetId;

	@Column(name = "assettype")
	private String assetType;

	@Column(name = "assetstatus")
	private boolean assetStatus;
	
    private Date lastmodefiedDate;


	private String createdBy;

	private String updatedBy;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date createdOn;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date updatedOn;

	@JsonManagedReference

	@OneToMany(mappedBy = "assetTypeMasterEx", fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Brand.class)
	private List<Brand> brandList = new ArrayList<>();

	public List<Brand> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<Brand> brandList) {
		this.brandList = brandList;
	}

	public AssetTypeMaster() {
		super();
	}

	public AssetTypeMaster(Integer assetId, String assetType, boolean assetStatus, String createdBy, String updatedBy,
			Date createdOn, Date updatedOn) {
		super();
		this.assetId = assetId;
		this.assetType = assetType;
		this.assetStatus = assetStatus;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
	}

	public Date getLastmodefiedDate() {
		return lastmodefiedDate;
	}

	public void setLastmodefiedDate(Date lastmodefiedDate) {
		this.lastmodefiedDate = lastmodefiedDate;
	}

	public Integer getAssetId() {
		return assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public boolean isAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(boolean assetStatus) {
		this.assetStatus = assetStatus;
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
}
