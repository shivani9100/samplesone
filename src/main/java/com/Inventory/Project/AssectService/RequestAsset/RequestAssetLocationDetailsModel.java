package com.Inventory.Project.AssectService.RequestAsset;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class RequestAssetLocationDetailsModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToMany( cascade = { CascadeType.MERGE, CascadeType.REFRESH })

	@JoinTable(name = "RequestAsset_company", joinColumns = { @JoinColumn(name = "ticketId") }, inverseJoinColumns = {

			@JoinColumn(name = "companyid") })
	//@JsonManagedReference
	private List<CompanyDetailsModel> companyDetailsModel;
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH })

	@JoinTable(name = "RequestAsset_location", joinColumns = { @JoinColumn(name = "ticketId") }, inverseJoinColumns = {

			@JoinColumn(name = "locationid") })
	//@JsonManagedReference
	private List<LocationDetailsModel> locationDetailsModel;
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.REFRESH })

	@JoinTable(name = "RequestAsset_building", joinColumns = { @JoinColumn(name = "ticketId") }, inverseJoinColumns = {

			@JoinColumn(name = "buildingid") })
	//@JsonManagedReference
	private List<BuildingDetailsModel> buildingDetailsModel;
	@ManyToMany( cascade = { CascadeType.MERGE, CascadeType.REFRESH })

	@JoinTable(name = "RequestAsset_floor", joinColumns = { @JoinColumn(name = "ticketId") }, inverseJoinColumns = {

			@JoinColumn(name = "floorid") })
	//@JsonManagedReference
	private List<FloorDetailsModel> floorDetailsModel;
	@ManyToMany( cascade = { CascadeType.MERGE, CascadeType.REFRESH })

	@JoinTable(name = "RequestAsset_block", joinColumns = { @JoinColumn(name = "ticketId") }, inverseJoinColumns = {

			@JoinColumn(name = "blockid") })
	//@JsonManagedReference
	private List<BlockDetailsModel> blockDetailsModel;

	private Integer deskNumber;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<CompanyDetailsModel> getCompanyDetailsModel() {
		return companyDetailsModel;
	}

	public void setCompanyDetailsModel(List<CompanyDetailsModel> companyDetailsModel) {
		this.companyDetailsModel = companyDetailsModel;
	}

	public List<LocationDetailsModel> getLocationDetailsModel() {
		return locationDetailsModel;
	}

	public void setLocationDetailsModel(List<LocationDetailsModel> locationDetailsModel) {
		this.locationDetailsModel = locationDetailsModel;
	}

	public List<BuildingDetailsModel> getBuildingDetailsModel() {
		return buildingDetailsModel;
	}

	public void setBuildingDetailsModel(List<BuildingDetailsModel> buildingDetailsModel) {
		this.buildingDetailsModel = buildingDetailsModel;
	}

	public List<FloorDetailsModel> getFloorDetailsModel() {
		return floorDetailsModel;
	}

	public void setFloorDetailsModel(List<FloorDetailsModel> floorDetailsModel) {
		this.floorDetailsModel = floorDetailsModel;
	}

	public List<BlockDetailsModel> getBlockDetailsModel() {
		return blockDetailsModel;
	}

	public void setBlockDetailsModel(List<BlockDetailsModel> blockDetailsModel) {
		this.blockDetailsModel = blockDetailsModel;
	}

	public Integer getDeskNumber() {
		return deskNumber;
	}

	public void setDeskNumber(Integer deskNumber) {
		this.deskNumber = deskNumber;
	}
}
