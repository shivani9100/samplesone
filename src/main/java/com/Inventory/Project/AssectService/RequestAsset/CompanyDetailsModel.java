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
public class CompanyDetailsModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer companyid;

	private String company;
	@JsonBackReference
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "companyDetailsModel")
	private List<RequestAssetLocationDetailsModel> requestAssetModel;

	public Integer getCompanyid() {
		return companyid;
	}

	public void setCompanyid(Integer companyid) {
		this.companyid = companyid;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public List<RequestAssetLocationDetailsModel> getRequestAssetModel() {
		return requestAssetModel;
	}

	public void setRequestAssetModel(List<RequestAssetLocationDetailsModel> requestAssetModel) {
		this.requestAssetModel = requestAssetModel;
	}

	

	/*
	 * @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy
	 * = "company") private List<LocationDetailsModel> location = new ArrayList<>();
	 */

}
