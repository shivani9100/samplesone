package com.Inventory.Project.AssectService.Employee;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Role {

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	// @Column(unique=true)
	private String name;
//	@ManyToMany(mappedBy = "roles")
//	private Set<UserEditProfile> users;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

// 	public Set<UserEditProfile> getUsers() {
// 		return users;
// 	}
// 
// 	public void setUsers(Set<UserEditProfile> users) {
// 		this.users = users;
// 	}

}
