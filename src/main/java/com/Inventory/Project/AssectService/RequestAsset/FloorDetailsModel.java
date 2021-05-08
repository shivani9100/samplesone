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
public class FloorDetailsModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer floorid;

	private String floor;
	@JsonBackReference
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "floorDetailsModel")
	private List<RequestAssetLocationDetailsModel> requestAssetModel;



	public List<RequestAssetLocationDetailsModel> getRequestAssetModel() {
		return requestAssetModel;
	}

	public void setRequestAssetModel(List<RequestAssetLocationDetailsModel> requestAssetModel) {
		this.requestAssetModel = requestAssetModel;
	}

	/*
	 * @JsonBackReference
	 * 
	 * @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	 * 
	 * @JoinTable(name = "building_floor", joinColumns = { @JoinColumn(name =
	 * "floorid") }, inverseJoinColumns = {
	 * 
	 * @JoinColumn(name = "buildingid") }) private List<BuildingDetailsModel>
	 * building = new ArrayList<>();
	 * 
	 * @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy
	 * = "floor") private List<Block> block = new ArrayList<>();
	 */
	public Integer getFloorid() {
		return floorid;
	}

	public void setFloorid(Integer floorid) {
		this.floorid = floorid;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

}
