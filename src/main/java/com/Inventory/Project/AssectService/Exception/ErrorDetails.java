package com.Inventory.Project.AssectService.Exception;

import java.util.Date;

public class ErrorDetails {
	private Date timestamp;
	private String messaage;
	private String details;

	public ErrorDetails(Date timestamp, String messaage, String details) {
		super();
		this.timestamp = timestamp;
		this.messaage = messaage;
		this.details = details;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessaage() {
		return messaage;
	}

	public void setMessaage(String messaage) {
		this.messaage = messaage;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
