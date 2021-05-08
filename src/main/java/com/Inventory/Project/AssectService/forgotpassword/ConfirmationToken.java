package com.Inventory.Project.AssectService.forgotpassword;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.Inventory.Project.AssectService.Employee.Employee;

@Entity
public class ConfirmationToken {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "token_id")
	private long tokenid;

	@Column(name = "confirmation_token")
	private String confirmationToken;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "employee_id")
	private Employee employee;

	public ConfirmationToken(Employee employee) {
		super();
		this.employee = employee;
	}

	public long getTokenid() {
		return tokenid;
	}

	public void setTokenid(long tokenid) {
		this.tokenid = tokenid;
	}

	public String getConfirmationToken() {
		return confirmationToken;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public ConfirmationToken(long tokenid, String confirmationToken, Date createdDate, Employee employee) {
		super();
		this.tokenid = tokenid;
		this.confirmationToken = confirmationToken;
		this.createdDate = createdDate;
		this.employee = employee;
	}

	public ConfirmationToken() {
		super();
		// TODO Auto-generated constructor stub
	}

}
