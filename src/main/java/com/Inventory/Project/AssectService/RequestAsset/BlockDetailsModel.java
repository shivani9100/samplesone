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
public class BlockDetailsModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer blockid;

	private String blockName;

//	
	@JsonBackReference
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "blockDetailsModel")
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
	 * @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	 * 
	 * @JoinTable(name = "floor_block", joinColumns = { @JoinColumn(name =
	 * "blockid") }, inverseJoinColumns = {
	 * 
	 * @JoinColumn(name = "floorid") }) private List<Floor> floor = new
	 * ArrayList<>();
	 */
	public Integer getBlockid() {
		return blockid;
	}

	public void setBlockid(Integer blockid) {
		this.blockid = blockid;
	}

	public String getBlockName() {
		return blockName;
	}

	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

}
