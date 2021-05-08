package com.Inventory.Project.AssectService.RequestAsset;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class LocationDetailsModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer locationid;

	private String location;
	@JsonBackReference
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "locationDetailsModel")
	private List<RequestAssetLocationDetailsModel> requestAssetModel;

	/*
	 * @JsonBackReference
	 * 
	 * @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	 * 
	 * @JoinTable(name = "company_location", joinColumns = { @JoinColumn(name =
	 * "locationid") }, inverseJoinColumns = {
	 * 
	 * @JoinColumn(name = "companyid") }) private List<CompanyDetailsModel> company
	 * = new ArrayList<>();
	 * 
	 * @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy
	 * = "location") private List<BuildingDetailsModel> building = new
	 * ArrayList<>();
	 */

	public Integer getLocationid() {
		return locationid;
	}

	public List<RequestAssetLocationDetailsModel> getRequestAssetModel() {
		return requestAssetModel;
	}

	public void setRequestAssetModel(List<RequestAssetLocationDetailsModel> requestAssetModel) {
		this.requestAssetModel = requestAssetModel;
	}

	public void setLocationid(Integer locationid) {
		this.locationid = locationid;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
