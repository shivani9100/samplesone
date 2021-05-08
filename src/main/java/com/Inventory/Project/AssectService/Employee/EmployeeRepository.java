package com.Inventory.Project.AssectService.Employee;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

	Employee findByEmail(String email);

	List<Employee> findByRolesName(String name);

	List<Employee> findByEmailContaining(String email);

	List<Employee> findByEmployeeidContaining(String employeeid);

	List<Employee> findByFirstNameContaining(String firstName);

	List<Employee> findByLastNameContaining(String lastName);

	Page<Employee> findByFirstNameContaining(String firstName, Pageable pageable);

	List<Employee> findByReportingManager(String reportingManager);

	// @Query("select e from Employee e where concat(e.firstName,e.lastName) LIKE
	// %?1%)")
//    @Query("SELECT e FROM Employee e WHERE CONCAT(e.firstName,e.lastName) LIKE %?1%")
//    Employee findByKeyword(String keyword);

	@Query(value = "select u from Employee u where u.employeeid like %:text% or u.firstName like %:text% or u.lastName like %:text% or u.email like %:text%")
	Page fullTextSearch(String text, Pageable pageable);

	Employee findByFirstNameAndLastName(String firstName, String lastName);

	Employee findByFirstName(String firstName);

	Employee findByLastName(String lastName);

}
