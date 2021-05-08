package com.Inventory.Project.AssectService.RequestAsset;

import java.util.Date;

import com.Inventory.Project.AssectService.Employee.Employee;

public class RequestAssetRequestBody {
	
	private Integer ticketId;

	private String query;

	private byte[] attachments;

	private String ccmail;

	private Employee employee;

	private Date dateOfRequest;

	private Boolean requestStatus;

	private RequestAssetLocationDetailsModelRequest fromLocation;

	private RequestAssetLocationDetailsModelRequest toLocation;

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

	public RequestAssetLocationDetailsModelRequest getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(RequestAssetLocationDetailsModelRequest fromLocation) {
		this.fromLocation = fromLocation;
	}

	public RequestAssetLocationDetailsModelRequest getToLocation() {
		return toLocation;
	}

	public void setToLocation(RequestAssetLocationDetailsModelRequest toLocation) {
		this.toLocation = toLocation;
	}

}
