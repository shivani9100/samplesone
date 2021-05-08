package com.Inventory.Project.AssectService.AssectEmployee;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Inventory.Project.AssectService.Assect.AssectModel;
import com.Inventory.Project.AssectService.Employee.Employee;

@Repository
public interface AssectEmployeeDao extends JpaRepository<AssectEmployeeModel, Integer> {

/* List<AssectEmployeeModel> findByAssectemployeeid(String id); */
    
    List<AssectEmployeeModel> findByEmployeeEmployeeid(String id);
    List<AssectEmployeeModel> findByEmployeeFirstName(String firstName);
    List<AssectEmployeeModel> findByEmployeeLastNameContaining(String lastName);
    
    AssectEmployeeModel findByAssectModelAssectid(long id);
    
    Page<AssectEmployeeModel> findBystatus(Boolean status,Pageable pageable);
    //List<AssectEmployeeModel> findByEmployeeFirstNameOrLastName(String firstName, String lastName);
    

 

    List<AssectEmployeeModel> findByEmployee(Employee employee);
    
    List<AssectEmployeeModel> findByAssectModel(AssectModel assetModel);
    
    @Query(value = "select u from AssectEmployeeModel u where u.employee.employeeid like %:text% or u.employee.firstName like %:text% or u.employee.lastName like %:text% or u.assectModel.serialNumber like %:text% or u.assectModel.models like %:text%")
    Page fullTextSearch(String text, Pageable pageable);
    
    //@Query("SELECT a from AssectEmployeeModel a CONCAT(a.assectModel,a.employee) Like %?1%")
    //List<AssectEmployeeModel> findByEmployeeOrAssectModel(String keyword);


}
