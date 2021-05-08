package com.Inventory.Project.AssectService.RequestAsset;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.Inventory.Project.AssectService.Employee.Employee;

@Entity
public class RequestAssetModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ticketId;

	private String query;

	private byte[] attachments;

	private String ccmail;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "employeeid")
	private Employee employee;

	private Date dateOfRequest;

	private Boolean requestStatus;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn
	private RequestAssetLocationDetailsModel fromLocation;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn
	private RequestAssetLocationDetailsModel toLocation;

	public Integer getTicketId() {
		return ticketId;
	}

	public void setTicketId(Integer ticketId) {
		this.ticketId = ticketId;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public byte[] getAttachments() {
		return attachments;
	}

	public void setAttachments(byte[] attachments) {
		this.attachments = attachments;
	}

	public String getCcmail() {
		return ccmail;
	}

	public void setCcmail(String ccmail) {
		this.ccmail = ccmail;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getDateOfRequest() {
		return dateOfRequest;
	}

	public void setDateOfRequest(Date dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}

	public Boolean getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(Boolean requestStatus) {
		this.requestStatus = requestStatus;
	}

	public RequestAssetLocationDetailsModel getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(RequestAssetLocationDetailsModel fromLocation) {
		this.fromLocation = fromLocation;
	}

	public RequestAssetLocationDetailsModel getToLocation() {
		return toLocation;
	}

	public void setToLocation(RequestAssetLocationDetailsModel toLocation) {
		this.toLocation = toLocation;
	}

}
