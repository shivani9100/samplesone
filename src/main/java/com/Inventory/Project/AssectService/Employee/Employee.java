package com.Inventory.Project.AssectService.Employee;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sun.istack.NotNull;

@Entity
@Table(name = "employee", uniqueConstraints = {

		@UniqueConstraint(columnNames = { "user_mail" }, name = "uniqueEmail"),
		@UniqueConstraint(columnNames = { "user_mobile" }, name = "uniqueUserMobile") })
public class Employee {

	@Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@NotNull
	private String employeeid;
	private String firstName;
	private String lastName;
	@Column(name = "user_mail")
	private String email;
	@Column(name = "user_mobile")
	private String phoneNumber;
	private String password;
	// @Transient
	private String passwordConfirm;
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "employee_role", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<com.Inventory.Project.AssectService.Employee.Role> roles;

	private String reportingManager;

	public String getReportingManager() {
		return reportingManager;
	}

	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}

	public Set<com.Inventory.Project.AssectService.Employee.Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<com.Inventory.Project.AssectService.Employee.Role> roles) {
		this.roles = roles;
	}

	public String getEmployeeid() {
		return employeeid;
	}

	public void setEmployeeid(String id) {
		this.employeeid = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

}
