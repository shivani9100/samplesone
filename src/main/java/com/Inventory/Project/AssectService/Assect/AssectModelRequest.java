package com.Inventory.Project.AssectService.Assect;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AssectModelRequest implements Serializable {

	private long assectid;
	private Integer brand;
	private String serialNumber;
	private String purchaseDCNo;
	@Temporal(TemporalType.DATE)
	private Date warrentStartDate;
	private Integer vendor;
	private Integer assetType;
	private Integer models;
	@Temporal(TemporalType.DATE)
	private Date purchaseDate;
	private String purchaseInvoiceNo;
	@Temporal(TemporalType.DATE)
	private Date warrentEndDate;
	private Double cost;
	@Lob
	private Byte[] invoiceDoc;
	@Lob
	private Byte[] warantyDoc;
	private Integer noOfHardDisks;
	private Integer hardDiskCapacity;
	private Integer hardDiskType;
	private Integer noOfRams;
	private Integer typeOfRam;
	private Integer ramCapacity;
	private String location;
	private String floor;
	private String createdBy;
	private String updateBy;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date createdOn;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date updatedOn;

	public long getAssectid() {
		return assectid;
	}

	public void setAssectid(long assectid) {
		this.assectid = assectid;
	}

	public Integer getBrand() {
		return brand;
	}

	public void setBrand(Integer brand) {
		this.brand = brand;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getPurchaseDCNo() {
		return purchaseDCNo;
	}

	public void setPurchaseDCNo(String purchaseDCNo) {
		this.purchaseDCNo = purchaseDCNo;
	}

	public Date getWarrentStartDate() {
		return warrentStartDate;
	}

	public void setWarrentStartDate(Date warrentStartDate) {
		this.warrentStartDate = warrentStartDate;
	}

	public Integer getVendor() {
		return vendor;
	}

	public void setVendor(Integer vendor) {
		this.vendor = vendor;
	}

	public Integer getAssetType() {
		return assetType;
	}

	public void setAssetType(Integer assetType) {
		this.assetType = assetType;
	}

	public Integer getModels() {
		return models;
	}

	public void setModels(Integer models) {
		this.models = models;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getPurchaseInvoiceNo() {
		return purchaseInvoiceNo;
	}

	public void setPurchaseInvoiceNo(String purchaseInvoiceNo) {
		this.purchaseInvoiceNo = purchaseInvoiceNo;
	}

	public Date getWarrentEndDate() {
		return warrentEndDate;
	}

	public void setWarrentEndDate(Date warrentEndDate) {
		this.warrentEndDate = warrentEndDate;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Byte[] getInvoiceDoc() {
		return invoiceDoc;
	}

	public void setInvoiceDoc(Byte[] invoiceDoc) {
		this.invoiceDoc = invoiceDoc;
	}

	public Byte[] getWarantyDoc() {
		return warantyDoc;
	}

	public void setWarantyDoc(Byte[] warantyDoc) {
		this.warantyDoc = warantyDoc;
	}

	public Integer getNoOfHardDisks() {
		return noOfHardDisks;
	}

	public void setNoOfHardDisks(Integer noOfHardDisks) {
		this.noOfHardDisks = noOfHardDisks;
	}

	public Integer getHardDiskCapacity() {
		return hardDiskCapacity;
	}

	public void setHardDiskCapacity(Integer hardDiskCapacity) {
		this.hardDiskCapacity = hardDiskCapacity;
	}

	public Integer getHardDiskType() {
		return hardDiskType;
	}

	public void setHardDiskType(Integer hardDiskType) {
		this.hardDiskType = hardDiskType;
	}

	public Integer getNoOfRams() {
		return noOfRams;
	}

	public void setNoOfRams(Integer noOfRams) {
		this.noOfRams = noOfRams;
	}

	public Integer getTypeOfRam() {
		return typeOfRam;
	}

	public void setTypeOfRam(Integer typeOfRam) {
		this.typeOfRam = typeOfRam;
	}

	public Integer getRamCapacity() {
		return ramCapacity;
	}

	public void setRamCapacity(Integer ramCapacity) {
		this.ramCapacity = ramCapacity;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
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
