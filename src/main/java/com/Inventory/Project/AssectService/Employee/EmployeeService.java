package com.Inventory.Project.AssectService.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Response.ResponseList;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public Boolean saveEmployeeData(Employee profile) {
		Employee editProfile = employeeRepository.save(profile);
		if (editProfile == null) {
			return false;
		} else {
			return true;
		}
	}

	public Employee findByEmail(String email) {
		Employee profile = employeeRepository.findByEmail(email);
		return profile;
	}

	public List<Employee> getAllEmployee() {

		List<Employee> findAll = employeeRepository.findAll();

		return findAll;
	}

	public ResponseList getAllEmployeeDetails(int pageNo, int sizePerPage) {
		Pageable paging = PageRequest.of(pageNo, sizePerPage);

		Page<Employee> page = employeeRepository.findAll(paging);

		List<Employee> list = page.getContent();

		int totalPages = page.getTotalPages();

		long totalElements = page.getTotalElements();

		ResponseList response = new ResponseList();
		response.setNoOfrecords(totalElements);
		response.setTotalNumberOfPages(totalPages);
		response.setList(list);
		return response;
	}

	public ResponseList getAllEmployeeDetails(String search, int pageNo, int sizePerPage) {

		Pageable paging = PageRequest.of(pageNo, sizePerPage);

		Page<Employee> page = employeeRepository.findByFirstNameContaining(search, paging);

		int totalPages = page.getTotalPages();
		System.out.println(totalPages);
		long totalElements = page.getTotalElements();
		System.out.println(totalElements);
		List<Employee> list = page.getContent();

		ResponseList response = new ResponseList();
		response.setNoOfrecords(totalElements);
		response.setTotalNumberOfPages(totalPages);
		response.setList(list);
		return response;

	}

	public Employee getEmployeeById(String id) {

		Optional<Employee> user = employeeRepository.findById(id);

		if (user.isPresent()) {
			return user.get();
		} else {
			return null;
		}
	}

	public Boolean updateOrEditEmployeeProfile(Employee profile) {
		Employee editProfile = employeeRepository.save(profile);
		if (editProfile == null) {
			return false;
		}

		return true;

	}

	public Map<String, Boolean> deleteEmployeeProfileById(String id) {

		employeeRepository.findById(id);

		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	public List<Employee> getEmployeeByRole(String role) {

		List<Employee> findByRoleRoleName = employeeRepository.findByRolesName(role);
		return findByRoleRoleName;

	}

	public List<String> getAllEmployeeByContainsEmail(String email) {
		List<Employee> findByEmailContaining = employeeRepository.findByEmailContaining(email);

		List<String> arrayList = new ArrayList<>();

		findByEmailContaining.forEach(employee -> arrayList.add(employee.getEmail()));

		return arrayList;
	}

	public ResponseList searchAll(String text, Integer pageno, Integer pagesize) {
		PageRequest request = PageRequest.of(pageno, pagesize);
		Page page1 = employeeRepository.fullTextSearch(text, request);
		List<Employee> list = page1.getContent();
		long totalElements = page1.getTotalElements();
		int totalPages = page1.getTotalPages();

		ResponseList responseList = new ResponseList();
		responseList.setTotalNumberOfPages(totalPages);
		responseList.setNoOfrecords(totalElements);
		responseList.setList(list);
		return responseList;

	}
	
	public List<Employee> getDetailsByReportingManager(String reportingManager) throws RecordNotFoundException {

		 

        List<Employee> getDetailsByReprotingManager = employeeRepository.findByReportingManager(reportingManager);
        if (getDetailsByReprotingManager == null) {
            throw new RecordNotFoundException("There is no Data about Reporting Manager");
        }
        return getDetailsByReprotingManager;

 

    }

}
