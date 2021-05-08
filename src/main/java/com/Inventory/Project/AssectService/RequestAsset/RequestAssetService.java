package com.Inventory.Project.AssectService.RequestAsset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Inventory.Project.AssectService.Employee.Employee;
import com.Inventory.Project.AssectService.Employee.EmployeeRepository;
import com.Inventory.Project.AssectService.Employee.EmployeeService;
import com.Inventory.Project.AssectService.Exception.ResourceNotFoundException;
import com.Inventory.Project.AssectService.MaillingService.Mail;
import com.Inventory.Project.AssectService.MaillingService.MailService;
import com.Inventory.Project.AssectService.Response.ResponseList;

@Service
@Transactional
public class RequestAssetService {

	private static final Logger logger = LogManager.getLogger(RequestAssetService.class);

	@Autowired
	private RequestAssetRepository requestAssetRepository;
	@Autowired
	private Environment environment;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private LocationDetailsDao locationDao;
	@Autowired
	private CompanyDetailsDao companyDao;
	@Autowired
	private FloorDetailsDao floorDao;
	@Autowired
	private BlockDetailsDao blockDao;
	@Autowired
	private BuildingDetailsDao buildingDao;

	@Autowired
	private MailService mailService;

	@javax.transaction.Transactional
	public Boolean addAssetRequest(RequestAssetModel requestAssetModel, String id) {

		requestAssetModel.setDateOfRequest(new Date());

		requestAssetModel.setRequestStatus(true);

		Optional<Employee> employee = employeeRepository.findById(id);

		if (employee.isPresent()) {
			requestAssetModel.setEmployee(employee.get());
		}
		System.out.println("saving");
		RequestAssetModel requestAssetModel1 = requestAssetRepository.save(requestAssetModel);
		System.out.println("saving");

		Mail mail = new Mail();

		mail.setMailFrom(environment.getProperty("spring.mail.username"));
		if (requestAssetModel.getCcmail() != null) {
			mail.setMailCc(requestAssetModel1.getCcmail());
		}

		mail.setMailTo(requestAssetModel1.getEmployee().getEmail());
		mail.setMailSubject(environment.getProperty("trouble.ticket.mail.subject"));

		mail.setMailContent("Hi," + "A Trouble ticket with No. " + requestAssetModel1.getTicketId()
				+ " has been created and sent to the concerned person. Please await a response from them.");

		Boolean sendMailToEmployee = mailService.sendMail(mail);

		List<Employee> employeeByRole = employeeService.getEmployeeByRole(environment.getProperty("employee.role"));

		List<String> arrayList = new ArrayList<>();

		Mail mail1 = new Mail();

		if (!employeeByRole.isEmpty()) {
			employeeByRole.forEach(emp -> arrayList.add(emp.getEmail()));
		}
		String[] str = arrayList.toArray(new String[0]);

		mail1.setMailFrom(environment.getProperty("spring.mail.username"));

		mail1.setMailToArray(str);
		mail1.setMailSubject(environment.getProperty("trouble.ticket.mail.subject"));

		mail1.setMailContent("Hi," + "A Trouble ticket with No. " + requestAssetModel.getTicketId()
				+ " has been created and please review that.");

		Boolean sendMailToAdmin = mailService.sendMail(mail1);

		if (requestAssetModel1 != null && sendMailToAdmin && sendMailToEmployee) {
			return true;

		} else {
			requestAssetModel1.setEmployee(null);
			requestAssetRepository.save(requestAssetModel1);
			requestAssetRepository.delete(requestAssetModel1);
			return false;
		}
	}

	public ResponseList getListOfRequests(Integer pageNo, Integer sizePerPage, String sortBydate) {

		Pageable page = PageRequest.of(pageNo, sizePerPage,
				org.springframework.data.domain.Sort.by(sortBydate).descending());

		Page<RequestAssetModel> list = requestAssetRepository.findAll(page);
		ResponseList responseList = new ResponseList();
		int totalPages = list.getTotalPages();

		long totalElements = list.getTotalElements();
		responseList.setNoOfrecords(totalElements);
		responseList.setTotalNumberOfPages(totalPages);

		responseList.setList(list.getContent());
		return responseList;

	}

//	public Optional<RequestAssetModel> getRequestAssetbyId(Integer id) {
//		Optional<RequestAssetModel> optional = requestAssetRepository.findById(id);
//
//		return optional;
//	}

	public void deleteRequestAssetbyId(Integer id) {
		requestAssetRepository.deleteById(id);

	}

	public RequestAssetModel getByAssetRequestTicketId(Integer ticketId) throws ResourceNotFoundException {

		Optional<RequestAssetModel> requestAssetModel = requestAssetRepository.findById(ticketId);

		RequestAssetModel requestAssetModel2 = requestAssetModel.get();

		return requestAssetModel2;
	}

	public List<RequestAssetModel> getByAssetRequestEmployeeId(String employeeid) {

		List<RequestAssetModel> requestAssetModel = requestAssetRepository.findByEmployeeEmployeeid(employeeid);

		return requestAssetModel;

	}

	public Boolean updateRequestAsset(String employeeId, RequestAssetModel requestAssetModel)
			throws ResourceNotFoundException {

		Optional<RequestAssetModel> requestAssetObject1 = requestAssetRepository
				.findById(requestAssetModel.getTicketId()).map(requestAssetObject -> {
					requestAssetObject.setRequestStatus(requestAssetModel.getRequestStatus());
					requestAssetObject.setAttachments(requestAssetModel.getAttachments());

					requestAssetObject.setCcmail(requestAssetModel.getCcmail());
					requestAssetObject.setDateOfRequest(requestAssetModel.getDateOfRequest());

					requestAssetObject.setEmployee(requestAssetModel.getEmployee());

					requestAssetObject.setQuery(requestAssetModel.getQuery());
					return requestAssetRepository.save(requestAssetObject);
				});
		if (requestAssetObject1.isPresent()) {
			return true;
		} else {
			return false;
		}

	}

	public List<RequestAssetModel> getAssetRequestByStatus(Boolean status) {

		List<RequestAssetModel> findByRequestStatus = requestAssetRepository.findByRequestStatus(status);

		return findByRequestStatus;

	}

	public ResponseList getListOfRequests(Integer pageNo, Integer sizePerPage) {

		Pageable page = PageRequest.of(pageNo, sizePerPage);

		Page<RequestAssetModel> list = requestAssetRepository.findAll(page);

		List<RequestAssetModel> content = list.getContent();

		ResponseList responseList = new ResponseList();

		int totalPages = list.getTotalPages();

		long totalElements = list.getTotalElements();
		responseList.setNoOfrecords(totalElements);
		responseList.setTotalNumberOfPages(totalPages);

		responseList.setList(content);
		return responseList;

	}

	public ResponseList getListOfRequestsySearch(Integer pageNo, Integer sizePerPage, String searchBy) {

		Pageable page = PageRequest.of(pageNo, sizePerPage);

		Page<RequestAssetModel> list = requestAssetRepository.findByEmployeeEmployeeidContaining(searchBy, page);

		List<RequestAssetModel> content = list.getContent();

		ResponseList responseList = new ResponseList();

		int totalPages = list.getTotalPages();

		long totalElements = list.getTotalElements();
		responseList.setNoOfrecords(totalElements);
		responseList.setTotalNumberOfPages(totalPages);

		responseList.setList(content);

		return responseList;

	}

	public Boolean addAssetRequest(RequestAssetRequestBody requestAssetRequestBody, String id) {

		RequestAssetModel requestAssetModel = new RequestAssetModel();

		RequestAssetLocationDetailsModel model = new RequestAssetLocationDetailsModel();

		ArrayList<CompanyDetailsModel> companyList = new ArrayList<>();
		ArrayList<LocationDetailsModel> locationList = new ArrayList<>();
		ArrayList<FloorDetailsModel> floorList = new ArrayList<>();
		ArrayList<BlockDetailsModel> blockList = new ArrayList<>();
		ArrayList<BuildingDetailsModel> buildingList = new ArrayList<>();

		// RequestAssetLocationDetailsModel requestAssetLocationDetailsModel = new
		// RequestAssetLocationDetailsModel();

		Optional<CompanyDetailsModel> company = companyDao
				.findById(requestAssetRequestBody.getFromLocation().getCompanyDetailsModel());

		Optional<BuildingDetailsModel> building = buildingDao
				.findById(requestAssetRequestBody.getFromLocation().getBuildingDetailsModel());

		Optional<FloorDetailsModel> floor = floorDao
				.findById(requestAssetRequestBody.getFromLocation().getFloorDetailsModel());

		Optional<LocationDetailsModel> location = locationDao
				.findById(requestAssetRequestBody.getFromLocation().getLocationDetailsModel());
		Optional<BlockDetailsModel> block = blockDao
				.findById(requestAssetRequestBody.getFromLocation().getBlockDetailsModel());

		companyList.add(company.get());
		locationList.add(location.get());
		floorList.add(floor.get());
		blockList.add(block.get());
		buildingList.add(building.get());

		model.setBlockDetailsModel(blockList);
		model.setBuildingDetailsModel(buildingList);
		model.setCompanyDetailsModel(companyList);
		model.setFloorDetailsModel(floorList);
		model.setLocationDetailsModel(locationList);
		model.setDeskNumber(requestAssetRequestBody.getFromLocation().getDeskNumber());
		RequestAssetLocationDetailsModel model1 = new RequestAssetLocationDetailsModel();

		if (requestAssetRequestBody.getToLocation() != null) {

			ArrayList<CompanyDetailsModel> companyList1 = new ArrayList<>();
			ArrayList<LocationDetailsModel> locationList1 = new ArrayList<>();
			ArrayList<FloorDetailsModel> floorList1 = new ArrayList<>();
			ArrayList<BlockDetailsModel> blockList1 = new ArrayList<>();
			ArrayList<BuildingDetailsModel> buildingList1 = new ArrayList<>();

			// RequestAssetLocationDetailsModel requestAssetLocationDetailsModel = new
			// RequestAssetLocationDetailsModel();

			Optional<CompanyDetailsModel> company1 = companyDao
					.findById(requestAssetRequestBody.getToLocation().getCompanyDetailsModel());

			Optional<BuildingDetailsModel> building1 = buildingDao
					.findById(requestAssetRequestBody.getToLocation().getBuildingDetailsModel());

			Optional<FloorDetailsModel> floor1 = floorDao
					.findById(requestAssetRequestBody.getToLocation().getFloorDetailsModel());

			Optional<LocationDetailsModel> location1 = locationDao
					.findById(requestAssetRequestBody.getToLocation().getLocationDetailsModel());
			Optional<BlockDetailsModel> block1 = blockDao
					.findById(requestAssetRequestBody.getToLocation().getBlockDetailsModel());

			companyList1.add(company1.get());
			locationList1.add(location1.get());
			floorList1.add(floor1.get());
			blockList1.add(block1.get());
			buildingList1.add(building1.get());

			model1.setBlockDetailsModel(blockList1);
			model1.setBuildingDetailsModel(buildingList1);
			model1.setCompanyDetailsModel(companyList1);
			model1.setFloorDetailsModel(floorList1);
			model1.setLocationDetailsModel(locationList1);
			model1.setDeskNumber(requestAssetRequestBody.getToLocation().getDeskNumber());
			requestAssetModel.setToLocation(model1);
		}

		if (requestAssetRequestBody.getAttachments() != null) {
			requestAssetModel.setAttachments(requestAssetRequestBody.getAttachments());
		}
		Optional<Employee> employee = employeeRepository.findById(id);

		if (employee.isPresent()) {
			requestAssetModel.setEmployee(employee.get());
		}

		requestAssetModel.setCcmail(requestAssetRequestBody.getCcmail());

		requestAssetModel.setQuery(requestAssetRequestBody.getQuery());

		requestAssetModel.setDateOfRequest(new Date());

		requestAssetModel.setRequestStatus(true);

		requestAssetModel.setFromLocation(model);

		RequestAssetModel requestAssetModel1 = requestAssetRepository.save(requestAssetModel);

		Mail mail = new Mail();

		mail.setMailFrom(environment.getProperty("spring.mail.username"));
		if (requestAssetModel.getCcmail() != null) {
			mail.setMailCc(requestAssetModel1.getCcmail());
		}

		mail.setMailTo(requestAssetModel1.getEmployee().getEmail());
		mail.setMailSubject(environment.getProperty("trouble.ticket.mail.subject"));

		mail.setMailContent("Hi," + "A Trouble ticket with No. " + requestAssetModel1.getTicketId()
				+ " has been created and sent to the concerned person. Please await a response from them.");

		Boolean sendMailToEmployee = mailService.sendMail(mail);

		List<Employee> employeeByRole = employeeService.getEmployeeByRole(environment.getProperty("employee.role"));

		List<String> arrayList = new ArrayList<>();

		Mail mail1 = new Mail();

		if (!employeeByRole.isEmpty()) {
			employeeByRole.forEach(emp -> arrayList.add(emp.getEmail()));
		}
		String[] str = arrayList.toArray(new String[0]);

		mail1.setMailFrom(environment.getProperty("spring.mail.username"));

		mail1.setMailToArray(str);
		mail1.setMailSubject(environment.getProperty("trouble.ticket.mail.subject"));

		mail1.setMailContent("Hi," + "A Trouble ticket with No. " + requestAssetModel.getTicketId()
				+ " has been created and please review that.");

		Boolean sendMailToAdmin = mailService.sendMail(mail1);

		if (requestAssetModel1 != null && sendMailToAdmin && sendMailToEmployee) {
			return true;

		} else {
			requestAssetModel1.setEmployee(null);
			requestAssetRepository.save(requestAssetModel1);
			requestAssetRepository.delete(requestAssetModel1);
			return false;
		}

	}

	public Boolean updateRequestAsset(String employeeId, RequestAssetRequestBody requestAssetModel) {

		Employee employeeById = employeeService.getEmployeeById(employeeId);
		
		ArrayList<CompanyDetailsModel> companyList1 = new ArrayList<>();
		ArrayList<LocationDetailsModel> locationList1 = new ArrayList<>();
		ArrayList<FloorDetailsModel> floorList1 = new ArrayList<>();
		ArrayList<BlockDetailsModel> blockList1 = new ArrayList<>();
		ArrayList<BuildingDetailsModel> buildingList1 = new ArrayList<>();

		RequestAssetLocationDetailsModel model = new RequestAssetLocationDetailsModel();

		ArrayList<CompanyDetailsModel> companyList = new ArrayList<>();
		ArrayList<LocationDetailsModel> locationList = new ArrayList<>();
		ArrayList<FloorDetailsModel> floorList = new ArrayList<>();
		ArrayList<BlockDetailsModel> blockList = new ArrayList<>();
		ArrayList<BuildingDetailsModel> buildingList = new ArrayList<>();

		Optional<RequestAssetModel> requestAssetObject1 = requestAssetRepository
				.findById(requestAssetModel.getTicketId()).map(requestAssetObject -> {

					if (requestAssetModel.getToLocation() != null) {

						RequestAssetLocationDetailsModel model1 = new RequestAssetLocationDetailsModel();

						Optional<CompanyDetailsModel> company1 = companyDao
								.findById(requestAssetModel.getToLocation().getCompanyDetailsModel());

						Optional<BuildingDetailsModel> building1 = buildingDao
								.findById(requestAssetModel.getToLocation().getBuildingDetailsModel());

						Optional<FloorDetailsModel> floor1 = floorDao
								.findById(requestAssetModel.getToLocation().getFloorDetailsModel());

						Optional<LocationDetailsModel> location1 = locationDao
								.findById(requestAssetModel.getToLocation().getLocationDetailsModel());
						Optional<BlockDetailsModel> block1 = blockDao
								.findById(requestAssetModel.getToLocation().getBlockDetailsModel());
						companyList1.add(company1.get());
						locationList1.add(location1.get());
						floorList1.add(floor1.get());
						blockList1.add(block1.get());
						buildingList1.add(building1.get());
						model1.setBlockDetailsModel(blockList1);
						model1.setBuildingDetailsModel(buildingList1);
						model1.setCompanyDetailsModel(companyList1);
						model1.setFloorDetailsModel(floorList1);
						model1.setLocationDetailsModel(locationList1);
						model1.setDeskNumber(requestAssetModel.getToLocation().getDeskNumber());
						requestAssetObject.setToLocation(model1);

					}

					Optional<CompanyDetailsModel> company = companyDao
							.findById(requestAssetModel.getFromLocation().getCompanyDetailsModel());

					Optional<BuildingDetailsModel> building = buildingDao
							.findById(requestAssetModel.getFromLocation().getBuildingDetailsModel());

					Optional<FloorDetailsModel> floor = floorDao
							.findById(requestAssetModel.getFromLocation().getFloorDetailsModel());

					Optional<LocationDetailsModel> location = locationDao
							.findById(requestAssetModel.getFromLocation().getLocationDetailsModel());
					Optional<BlockDetailsModel> block = blockDao
							.findById(requestAssetModel.getFromLocation().getBlockDetailsModel());

					companyList.add(company.get());
					locationList.add(location.get());
					floorList.add(floor.get());
					blockList.add(block.get());
					buildingList.add(building.get());

					model.setBlockDetailsModel(blockList);
					model.setBuildingDetailsModel(buildingList);
					model.setCompanyDetailsModel(companyList);
					model.setFloorDetailsModel(floorList);
					model.setLocationDetailsModel(locationList);
					model.setDeskNumber(requestAssetModel.getFromLocation().getDeskNumber());

					requestAssetObject.setFromLocation(model);

					requestAssetObject.setRequestStatus(requestAssetModel.getRequestStatus());
					requestAssetObject.setAttachments(requestAssetModel.getAttachments());

					requestAssetObject.setCcmail(requestAssetModel.getCcmail());
					requestAssetObject.setDateOfRequest(requestAssetModel.getDateOfRequest());

					requestAssetObject.setEmployee(employeeById);

					requestAssetObject.setQuery(requestAssetModel.getQuery());
					return requestAssetRepository.save(requestAssetObject);
				});
		if (requestAssetObject1.isPresent()) {
			return true;
		} else {
			return false;
		}

	}

}
