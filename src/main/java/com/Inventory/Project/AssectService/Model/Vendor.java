package com.Inventory.Project.AssectService.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
//@Table(name = "Vendor",uniqueConstraints={@UniqueConstraint(columnNames = {"email" , "contact_number","gst_number"})})
@Table(name = "Vendor", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "gst_number" }, name = "uniqueGstNumber"),
		@UniqueConstraint(columnNames = { "email" }, name = "uniqueEmail"),
		@UniqueConstraint(columnNames = { "contact_number" }, name = "uniquePhoneNumber") })
public class Vendor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vendor_id")
	private Integer vendorId;
	@Column(name = "vendor_name")
	private String vendorName;

	private String email;
	@Column(name = "contact_number")
	private String contactNumber;
	@Column(name = "gst_number")
	private String gstNumber;
	@Column(name = " street_line1")
	private String streetLine1;
	@Column(name = "street_line2")
	private String streetLine2;

	private String pincode;

	private String cityname;

	private String state;
	
	private Date lastmodefiedDate;
	@Column(name = "vendor_status")
	private Boolean vendorStatus;
	@Lob
	@Column(name = "vendor_agreement")
	private Byte[] vendorAgreement;

	private String createdBy;

	private String updatedBy;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date createdOn;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date updatedOn;

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

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	public String getStreetLine1() {
		return streetLine1;
	}

	public void setStreetLine1(String streetLine1) {
		this.streetLine1 = streetLine1;
	}

	public String getStreetLine2() {
		return streetLine2;
	}

	public void setStreetLine2(String streetLine2) {
		this.streetLine2 = streetLine2;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Boolean getVendorStatus() {
		return vendorStatus;
	}

	public void setVendorStatus(Boolean vendorStatus) {
		this.vendorStatus = vendorStatus;
	}

	public Byte[] getVendorAgreement() {
		return vendorAgreement;
	}

	public void setVendorAgreement(Byte[] vendorAgreement) {
		this.vendorAgreement = vendorAgreement;
	}

	public Date getLastmodefiedDate() {
		return lastmodefiedDate;
	}

	public void setLastmodefiedDate(Date lastmodefiedDate) {
		this.lastmodefiedDate = lastmodefiedDate;
	}

}