package com.Inventory.Project.AssectService.Assect;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.Inventory.Project.AssectService.Model.AssetTypeMaster;
import com.Inventory.Project.AssectService.Model.Brand;
import com.Inventory.Project.AssectService.Model.HardDiskCapacity;
import com.Inventory.Project.AssectService.Model.HardDiskTypeMaster;
import com.Inventory.Project.AssectService.Model.Model;
import com.Inventory.Project.AssectService.Model.RamCapacityMaster;
import com.Inventory.Project.AssectService.Model.RamTypeMaster;
import com.Inventory.Project.AssectService.Model.Vendor;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Assect_10", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "serial_no" }, name = "uniqueSerialNo") })

public class AssectModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long assectid;

	@ManyToOne
	@JoinColumn(name = "brandid")
	private Brand brand; 

	@Column(name = "serial_no")
	private String serialNumber;
	private String purchaseDCNo; 
	@Temporal(TemporalType.DATE)
	private Date warrentStartDate; 
	@ManyToOne
	@JoinColumn(name = "vendorId")
	private Vendor vendor; 
	@ManyToOne
	@JoinColumn(name = "assetId")
	private AssetTypeMaster assectType; 
	@ManyToOne
	@JoinColumn(name = "modelid")
	private Model models; 
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
	@ManyToOne
	@JoinColumn(name = "harddiskCapacityId")
	private HardDiskCapacity hardDiskCapacity; 
	@ManyToOne
	@JoinColumn(name = "hardDiskTypeId")
	private HardDiskTypeMaster hardDiskType;
	private Integer noOfRams;
	@ManyToOne
	@JoinColumn(name = "ramtypeId")
	private RamTypeMaster typeOfRam; 
	@ManyToOne
	@JoinColumn(name = "ramcapacityId")
	private RamCapacityMaster ramCapacity; 
	private String location;
	private String floor;
	private String createdBy;
	private String updateBy;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date createdOn;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date updatedOn;
	
	private Date lastmodifiedDate;

	public long getAssectid() {
		return assectid;
	}

	public void setAssectid(long assectid) {
		this.assectid = assectid;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
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

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public AssetTypeMaster getAssectType() {
		return assectType;
	}

	public void setAssectType(AssetTypeMaster assectType) {
		this.assectType = assectType;
	}

	public Model getModels() {
		return models;
	}

	public void setModels(Model models) {
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

	public HardDiskCapacity getHardDiskCapacity() {
		return hardDiskCapacity;
	}

	public void setHardDiskCapacity(HardDiskCapacity hardDiskCapacity) {
		this.hardDiskCapacity = hardDiskCapacity;
	}

	public HardDiskTypeMaster getHardDiskType() {
		return hardDiskType;
	}

	public void setHardDiskType(HardDiskTypeMaster hardDiskType) {
		this.hardDiskType = hardDiskType;
	}

	public Integer getNoOfRams() {
		return noOfRams;
	}

	public void setNoOfRams(Integer noOfRams) {
		this.noOfRams = noOfRams;
	}

	public RamTypeMaster getTypeOfRam() {
		return typeOfRam;
	}

	public void setTypeOfRam(RamTypeMaster typeOfRam) {
		this.typeOfRam = typeOfRam;
	}

	public RamCapacityMaster getRamCapacity() {
		return ramCapacity;
	}

	public void setRamCapacity(RamCapacityMaster ramCapacity) {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getLastmodifiedDate() {
		return lastmodifiedDate;
	}

	public void setLastmodifiedDate(Date lastmodifiedDate) {
		this.lastmodifiedDate = lastmodifiedDate;
	}

	

	

	
	

}