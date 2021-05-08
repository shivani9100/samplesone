package com.Inventory.Project.AssectService.AssectEmployee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.Inventory.Project.AssectService.Assect.AssectDao;
import com.Inventory.Project.AssectService.Assect.AssectModel;
import com.Inventory.Project.AssectService.Dao.ModelRepo;
import com.Inventory.Project.AssectService.Employee.Employee;
import com.Inventory.Project.AssectService.Employee.EmployeeRepository;
import com.Inventory.Project.AssectService.Exception.ResourceNotFoundException;
import com.Inventory.Project.AssectService.Model.Model;
import com.Inventory.Project.AssectService.Response.ResponseList;

@Service
public class AssectEmployeeService {

	@Autowired
	private AssectEmployeeDao assectEmployeeDao;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired(required = true)
	private AssectDao assectdao;

	@Autowired
	private ModelRepo modelrepo;

	public Boolean addAssectToEmloyee(AssectEmployeeModel assectEmployeeModel, String employeeid, long assectid) {
		Optional<AssectModel> assectid1 = assectdao.findById(assectid);
		Optional<Employee> employeeid1 = employeeRepository.findById(employeeid);
		if (assectid1.isPresent()) {
			assectEmployeeModel.setAssectModel(assectid1.get());
		}
		if (employeeid1.isPresent()) {
			assectEmployeeModel.setEmployee(employeeid1.get());
		}
		AssectEmployeeModel save = assectEmployeeDao.save(assectEmployeeModel);
		if (save == null) {
			return false;
		}
		return true;

	}

	public AssectEmployeeModel getByAssectassectEmployeeid(Integer assectemployeeid) {
		Optional<AssectEmployeeModel> assectEmployee = assectEmployeeDao.findById(assectemployeeid);
		if (assectEmployee != null) {
			return assectEmployee.get();
		}
		return null;
	}

	public Boolean UpdateAssectEmployee(AssectEmployeeModel assectEmployeeModel, String employeeid, long assectid,
			Integer assectEmployeeid) {
		Optional<AssectModel> assectid1 = assectdao.findById(assectid);
		Optional<Employee> employeeid1 = employeeRepository.findById(employeeid);
		Optional<AssectEmployeeModel> assectemployeeid1 = assectEmployeeDao.findById(assectEmployeeid);
		if (assectid1.isPresent()) {
			assectEmployeeModel.setAssectModel(assectid1.get());
		}
		if (employeeid1.isPresent()) {
			assectEmployeeModel.setEmployee(employeeid1.get());
		}
		if (assectemployeeid1.isPresent()) {
			AssectEmployeeModel assectEmployeeModel2 = new AssectEmployeeModel();
			assectEmployeeModel2.setAssectemployeeid(assectEmployeeid);
			assectEmployeeModel2.setAssectModel(assectid1.get());
			assectEmployeeModel2.setEmployee(employeeid1.get());
			assectEmployeeModel2.setDateOfAssignment(assectEmployeeModel.getDateOfAssignment());
			assectEmployeeModel2.setDeskNumber(assectEmployeeModel.getDeskNumber());
			assectEmployeeModel2.setFloor(assectEmployeeModel.getFloor());
			assectEmployeeModel2.setLocation(assectEmployeeModel.getLocation());
			assectEmployeeModel2.setStatus(assectEmployeeModel2.getStatus());
			AssectEmployeeModel save = assectEmployeeDao.save(assectEmployeeModel2);
			if (save == null) {
				return false;
			} else {
				return true;
			}

		}
		return null;
	}

	public Boolean UpdateAssectEmployee(AssectEmployeeModel assectEmployeeModel, String employeeid, long assectid) {
		Optional<AssectModel> assectid1 = assectdao.findById(assectid);
		Optional<Employee> employeeid1 = employeeRepository.findById(employeeid);
		if (assectid1.isPresent()) {
			assectEmployeeModel.setAssectModel(assectid1.get());
		}
		if (employeeid1.isPresent()) {
			assectEmployeeModel.setEmployee(employeeid1.get());
		}
		AssectEmployeeModel save = assectEmployeeDao.save(assectEmployeeModel);
		if (save == null) {
			return false;
		}
		return true;
	}

	public ResponseList getAllAssectEmployees(Integer pageno, Integer pagesize) {

		Pageable request = PageRequest.of(pageno, pagesize);
		Page<AssectEmployeeModel> page = assectEmployeeDao.findAll(request);
		List<AssectEmployeeModel> list = page.getContent();
		long totalElements = page.getTotalElements();
		int totalPages = page.getTotalPages();
		ResponseList responseList = new ResponseList();
		responseList.setTotalNumberOfPages(totalPages);
		responseList.setNoOfrecords(totalElements);
		responseList.setList(list);
		/*
		 * List<AssectEmployeeModel> list = assectEmployeeDao.findAll(); if (list !=
		 * null) { return new ResponseEntity<>(list, HttpStatus.OK); } else { return new
		 * ResponseEntity<String>("no data found", HttpStatus.NOT_FOUND); }
		 */
		return responseList;

	}

	public ResponseList getAssectEmployeeBystatus(Boolean status, Integer pageno, Integer pagesize) {
		PageRequest request = PageRequest.of(pageno, pagesize);
		Page<AssectEmployeeModel> page = assectEmployeeDao.findBystatus(status, request);
		List<AssectEmployeeModel> list = page.getContent();
		long totalElements = page.getTotalElements();
		int totalPages = page.getTotalPages();
		ResponseList responseList = new ResponseList();
		responseList.setTotalNumberOfPages(totalPages);
		responseList.setNoOfrecords(totalElements);
		responseList.setList(list);
		return responseList;

	}

	public List<AssectEmployeeModel> getByEmployeeEmployeeid(String employeeid) throws ResourceNotFoundException {
		List<AssectEmployeeModel> list = assectEmployeeDao.findByEmployeeEmployeeid(employeeid);
		if (list != null) {
			return list;
		} else {
			throw new ResourceNotFoundException("No Data Found with this Employee id");
		}

	}

	public AssectEmployeeModel getByAssectid(long assectid) {
		AssectEmployeeModel assectEmployeeModel = assectEmployeeDao.findByAssectModelAssectid(assectid);
		if (assectEmployeeModel != null) {
			return assectEmployeeModel;
		} else {
			return null;
		}

	}

	public void deAllocate(Integer assectEmployeeid) {

		Optional<AssectEmployeeModel> optional = assectEmployeeDao.findById(assectEmployeeid);
		if (optional.isPresent()) {
			AssectEmployeeModel assectEmployeeModel2 = new AssectEmployeeModel();
			assectEmployeeModel2.setAssectModel(null);
			assectEmployeeModel2.setEmployee(null);

			assectEmployeeModel2.setCreatedBy(null);
			assectEmployeeModel2.setUpdatedBy(null);
			assectEmployeeModel2.setDeskNumber(null);
			assectEmployeeModel2.setDateOfAssignment(null);
			assectEmployeeModel2.setAssectemployeeid(assectEmployeeid);
			assectEmployeeModel2.setFloor(null);
			assectEmployeeModel2.setLocation(null);
			assectEmployeeModel2.setStatus(null);

			assectEmployeeDao.save(assectEmployeeModel2);
			assectEmployeeDao.deleteById(assectEmployeeid);
		}

	}

	public List<AssectEmployeeModel> getAllList() {
		List<AssectEmployeeModel> list = assectEmployeeDao.findAll();
		return list;

	}

	/* get assects without allocated assects */
	public List<AssectModel> getAllAssetsWithoutAllocated() {
		List<AssectModel> assects = assectdao.findAll();

		List<AssectEmployeeModel> assectEmployee = assectEmployeeDao.findAll();
		CopyOnWriteArrayList<AssectEmployeeModel> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
		copyOnWriteArrayList.addAll(assectEmployee);
		CopyOnWriteArrayList<AssectModel> copyOnWriteArrayList1 = new CopyOnWriteArrayList<>();
		copyOnWriteArrayList1.addAll(assects);
		for (AssectModel assectModel : copyOnWriteArrayList1) {
			for (AssectEmployeeModel assectEmployeeModel : copyOnWriteArrayList) {

				if (assectModel.getAssectid() == assectEmployeeModel.getAssectModel().getAssectid()) {
					assects.remove(assectModel);
				}

			}
		}

		return assects;

	}

	public List<AssectEmployeeModel> search(String keyword) {
		List<AssectEmployeeModel> list = new ArrayList<>();
		// System.out.println(list + "......");
		AssectModel assetModel;
		list = assectEmployeeDao.findByEmployeeEmployeeid(keyword);

		// System.out.println(list + "\n\n" + list.isEmpty() + "//////");
		if (!list.isEmpty()) {
			// System.out.println("anil");
			return list;
		} else {

			Employee employee;
			// System.out.println("anil");

			if (keyword.contains(" ")) {
				String arr[] = keyword.split(" ");
				String firstName = arr[0];
				String lastName = arr[1];
				// System.out.println(firstName+" "+lastName);
				employee = employeeRepository.findByFirstNameAndLastName(firstName, lastName);
				list = assectEmployeeDao.findByEmployee(employee);
			} /*
				 * else { employee = employeeRepository.findByFirstName(keyword); if (employee
				 * == null) { employee = employeeRepository.findByLastName(keyword); }
				 * 
				 * System.out.println(employee + "!!!!"); list =
				 * assectEmployeeDao.findByEmployee(employee); }
				 */
			if (!list.isEmpty()) {
				return list;
			} else {
				assetModel = assectdao.findBySerialNumberContaining(keyword);
				// System.out.println(assetModel + "&&&");
				if (assetModel != null) {
					list = assectEmployeeDao.findByAssectModel(assetModel);
					return list;
				} else {

					// System.out.println("modelrepo");
					Model model = modelrepo.findByModelnameContaining(keyword);
					// System.out.println(model);
					if (model != null) {
						assetModel = assectdao.findByModels(model);
						// System.out.println(assetModel);
						if (assetModel != null) {
							list = assectEmployeeDao.findByAssectModel(assetModel);
							// System.out.println(list);
						}
					}
				}

				return list;

			}
		}

	}

	public ResponseList searchAll(String text, Integer pageno, Integer pagesize) {
		PageRequest request = PageRequest.of(pageno, pagesize);
		Page page1 = assectEmployeeDao.fullTextSearch(text, request);
		List<AssectEmployeeModel> list = page1.getContent();
		long totalElements = page1.getTotalElements();
		int totalPages = page1.getTotalPages();

		ResponseList responseList = new ResponseList();
		responseList.setTotalNumberOfPages(totalPages);
		responseList.setNoOfrecords(totalElements);
		responseList.setList(list);
		return responseList;

	}

	/*
	 * public ResponseEntity<?> delete(AssectEmployeeModel assectEmployeeModel) {
	 * assectEmployeeDao.deleteById(assectEmployeeModel.getAssectemployeeid());
	 * return null; }
	 */

}

/*
 * public ResponseEntity<?> editByEmployeeid(String employeeid,long assectid,
 * AssectEmployeeModel assectEmployeeModel) { List<AssectEmployeeModel> list =
 * assectEmployeeDao.findByEmployeeEmployeeid(employeeid); if (list.isEmpty()) {
 * return new ResponseEntity<String>("No data found with this id",
 * HttpStatus.NOT_FOUND); } else { AssectEmployeeModel model = new
 * AssectEmployeeModel();
 * model.setAssectemployeeid(assectEmployeeModel.getAssectemployeeid());
 * model.setDateOfAssignment(assectEmployeeModel.getDateOfAssignment());
 * model.setDeskNumber(assectEmployeeModel.getDeskNumber());
 * model.setAssectemployeeid(assectEmployeeModel.getAssectemployeeid());
 * model.setFloor(assectEmployeeModel.getFloor());
 * model.setAssectModel(assectEmployeeModel.getAssectModel());
 * model.setEmployee(assectEmployeeDao.findByEmployeeEmployeeid(employeeid));
 * model.setAssectModel(assectEmployeeDao.findByAssectModelAssectid(assectid));
 * 
 * model.setLocation(assectEmployeeModel.getLocation());
 * model.setStatus(assectEmployeeModel.getStatus());
 * 
 * AssectEmployeeModel save = assectEmployeeDao.save(model); return new
 * ResponseEntity<>(save, HttpStatus.OK);
 * 
 * }
 * 
 * }
 */

/*
 * for (AssectEmployeeModel assectEmployeeModel : list) { Boolean status =
 * assectEmployeeModel.getStatus(); if(status==false) {
 * assectEmployeeDao.deleteById(assectEmployeeModel.getAssectemployeeid()); }
 * 
 * }
 */