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
public class BuildingDetailsModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer buildingid;

	private String building;
	@JsonBackReference
	@ManyToMany(cascade = CascadeType.ALL,  mappedBy = "buildingDetailsModel")
	private List<RequestAssetLocationDetailsModel> requestAsset;

	public Integer getBuildingid() {
		return buildingid;
	}

	public void setBuildingid(Integer buildingid) {
		this.buildingid = buildingid;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public List<RequestAssetLocationDetailsModel> getRequestAsset() {
		return requestAsset;
	}

	public void setRequestAsset(List<RequestAssetLocationDetailsModel> requestAsset) {
		this.requestAsset = requestAsset;
	}



	/*
	 * @JsonBackReference
	 * 
	 * @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	 * 
	 * @JoinTable(name = "location_building", joinColumns = { @JoinColumn(name =
	 * "buildingid") }, inverseJoinColumns = {
	 * 
	 * @JoinColumn(name = "locationid") }) private List<LocationDetailsModel>
	 * location = new ArrayList<>();
	 * 
	 * @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy
	 * = "building") private List<Floor> floor = new ArrayList<>();
	 */

}
