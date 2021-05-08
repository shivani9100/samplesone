package com.Inventory.Project.AssectService.RequestAsset;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Inventory.Project.AssectService.Employee.Employee;
import com.Inventory.Project.AssectService.Employee.EmployeeService;
import com.Inventory.Project.AssectService.Exception.ResourceNotFoundException;
import com.Inventory.Project.AssectService.Response.Response;
import com.Inventory.Project.AssectService.Response.ResponseList;

@RestController
@RequestMapping("/requestforasset")
public class RequestAssetController {

	private static final Logger logger = LogManager.getLogger(RequestAssetController.class);

	@Autowired
	private RequestAssetService requestAssetService;

	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/{employeeId}")
	public ResponseEntity<?> requestingAsset(@PathVariable("employeeId") String id,
			@RequestBody RequestAssetRequestBody requestAssetModel) throws ResourceNotFoundException {

		try {

			if (requestAssetModel.getQuery() == null || requestAssetModel.getQuery().isEmpty()
					|| requestAssetModel.getCcmail().isEmpty() || requestAssetModel.getFromLocation() == null

					|| requestAssetModel.getFromLocation().getBlockDetailsModel() == null
					|| requestAssetModel.getFromLocation().getCompanyDetailsModel() == null

					|| requestAssetModel.getFromLocation().getBuildingDetailsModel() == null
					//|| requestAssetModel.getFromLocation().getDeskNumber() == null
					|| requestAssetModel.getFromLocation().getFloorDetailsModel() == null

			) {
				logger.error("UNPROCESSABLE_ENTITY in requestingAsset");

				Response responseData = new Response();
				responseData.setStatus("422");
				responseData.setMessage("Feilds shold not null");

				return new ResponseEntity<>(responseData, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			Employee employee = employeeService.getEmployeeById(id);

			if (employee == null) {
				throw new ResourceNotFoundException("employee id is not found" + id);
			}

			logger.info("storing requestforasset");

			Boolean boolean1 = requestAssetService.addAssetRequest(requestAssetModel, id);

			if (boolean1) {
				Response responseData = new Response();
				responseData.setStatus("200");
				responseData.setMessage("Your request is  success");
				return new ResponseEntity<>(responseData, HttpStatus.OK);
			} else {
				Response responseData = new Response();
				responseData.setStatus("200");
				responseData.setMessage("Your request is not success");
				return new ResponseEntity<>(responseData, HttpStatus.OK);
			}

		}

		catch (ResourceNotFoundException e) {

			logger.info("Resource not found exception occured" + e.toString());

			Response responseData = new Response();
			responseData.setStatus("409");
			responseData.setMessage("no records found with employee id" + id);
			return new ResponseEntity<>(responseData, HttpStatus.CONFLICT);
		} catch (Exception e) {

			logger.info("exception occured" + e.toString());
			Response responseData = new Response();
			responseData.setStatus("409");
			responseData.setMessage("Exception caught");
			return new ResponseEntity<>(responseData, HttpStatus.CONFLICT);
		}

	}

	@GetMapping(path = { "/sortBy/{pageNo}/{sizePerPage}/{sortBy}" })

	public ResponseEntity<?> getListOfRequests(@PathVariable(name = "sortBy") String sortBy,

			@PathVariable("pageNo") Integer pageNo, @PathVariable("sizePerPage") Integer sizePerPage) {
		try {

			if (sortBy.equalsIgnoreCase("null")) {
				sortBy = null;
			}

			logger.info("getting list of requests");

			ResponseList responseList = null;

			if (sortBy == null) {
				responseList = requestAssetService.getListOfRequests(pageNo, sizePerPage);
			}

			responseList = requestAssetService.getListOfRequests(pageNo, sizePerPage, sortBy);

			return new ResponseEntity<>(responseList, HttpStatus.OK);
		} catch (Exception e) {

			logger.info("exception occured" + e.toString());

			Response responseData = new Response();
			responseData.setStatus("409");
			responseData.setMessage("Exception caught");
			return new ResponseEntity<>(responseData, HttpStatus.CONFLICT);
		}

	}

	@GetMapping(path = { "searchBy/{pageNo}/{sizePerPage}/{searchBy}", })

	public ResponseEntity<?> getListOfRequestsBysearch(@PathVariable(name = "searchBy") String searchBy,

			@PathVariable("pageNo") Integer pageNo, @PathVariable("sizePerPage") Integer sizePerPage) {
		try {

			if (searchBy.equalsIgnoreCase("null")) {
				searchBy = null;
			}
			logger.info("getting list of requests");

			ResponseList responseList = null;

			if (searchBy == null) {
				responseList = requestAssetService.getListOfRequests(pageNo, sizePerPage);
			}

			responseList = requestAssetService.getListOfRequestsySearch(pageNo, sizePerPage, searchBy);

			return new ResponseEntity<>(responseList, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info("exception occured" + e.toString());

			Response responseData = new Response();
			responseData.setStatus("409");
			responseData.setMessage("Exception caught");
			return new ResponseEntity<>(responseData, HttpStatus.CONFLICT);
		}
	}

	/*
	 * @GetMapping(path = { "/{pageNo}/{sizePerPage}" })
	 * 
	 * public ResponseEntity<?> getListOfRequests(
	 * 
	 * @PathVariable("pageNo") Integer pageNo, @PathVariable("sizePerPage") Integer
	 * sizePerPage) {
	 * 
	 * logger.info("getting list of requests");
	 * 
	 * List<RequestAssetModel> addAssetRequest = null;
	 * 
	 * 
	 * addAssetRequest = requestAssetService.getListOfRequests(pageNo, sizePerPage);
	 * 
	 * 
	 * 
	 * 
	 * return new ResponseEntity<>(addAssetRequest, HttpStatus.CREATED);
	 * 
	 * }
	 */

	@GetMapping("byticketid/{ticketId}")
	public ResponseEntity<?> getRequestingAssetByTicketid(@PathVariable("ticketId") Integer ticketId)
			throws ResourceNotFoundException {

		try {

			if (ticketId == null) {
				logger.info("bad request for getRequestingAssetByTicketId");
				Response responseData = new Response();
				responseData.setMessage("ticket id should not be null");
				responseData.setStatus("400");
				return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
			}

			RequestAssetModel assetRequestTicketId = requestAssetService.getByAssetRequestTicketId(ticketId);

			if (assetRequestTicketId == null) {
				throw new ResourceNotFoundException("requesting asset not found with ticket id" + ticketId);
			}
			Response responseData = new Response();
			responseData.setMessage("success");
			responseData.setStatus("200");
			return new ResponseEntity<>(assetRequestTicketId, HttpStatus.OK);

		} catch (ResourceNotFoundException e) {

			logger.info("Resource not found exception occured" + e.toString());

			Response responseData = new Response();
			responseData.setStatus("409");
			responseData.setMessage("no records found with ticket id" + ticketId);
			return new ResponseEntity<>(responseData, HttpStatus.CONFLICT);
		} catch (Exception e) {

			logger.info("exception occured" + e.toString());

			Response responseData = new Response();
			responseData.setStatus("409");
			responseData.setMessage("Exception caught");
			return new ResponseEntity<>(responseData, HttpStatus.CONFLICT);
		}

	}

	@GetMapping("byemployeeid/{employeeid}")
	public ResponseEntity<?> getRequestingAssetByEmployeeid(@PathVariable("employeeid") String employeeid)
			throws ResourceNotFoundException {
		try {

			if (employeeid == null) {
				logger.info("bad request for getRequestingAssetByemployeeId");
				Response responseData = new Response();
				responseData.setMessage("ticket id should not be null");
				responseData.setStatus("400");
				return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
			}

			Employee employeeById = employeeService.getEmployeeById(employeeid);
			if (employeeById == null) {
				throw new ResourceNotFoundException("no employees found with id" + employeeid);
			}

			List<RequestAssetModel> assetRequestemployeeId = requestAssetService
					.getByAssetRequestEmployeeId(employeeid);

			return new ResponseEntity<>(assetRequestemployeeId, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {

			logger.info("Resource not found exception occured" + e.toString());

			Response responseData = new Response();
			responseData.setStatus("409");
			responseData.setMessage("no records found with employee id" + employeeid);
			return new ResponseEntity<>(responseData, HttpStatus.CONFLICT);
		} catch (Exception e) {

			logger.info("exception occured" + e.toString());

			Response responseData = new Response();
			responseData.setStatus("409");
			responseData.setMessage("Exception caught");
			return new ResponseEntity<>(responseData, HttpStatus.CONFLICT);
		}

	}

	@PutMapping("updaterequeststatus/{employeeId}")
	public ResponseEntity<?> updateRequestingAssetByTicketid(@PathVariable("employeeId") String employeeId,
			@RequestBody RequestAssetRequestBody requestAssetModel) {

		try {

			if (employeeId == null) {
				logger.info("bad request for updateRequestingAssetByTicketid");
				Response responseData = new Response();
				responseData.setMessage("ticket id should not be null");
				responseData.setStatus("400");
				return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
			}
			Boolean updateRequestAsset = requestAssetService.updateRequestAsset(employeeId, requestAssetModel);
			if (updateRequestAsset) {
				Response responseData = new Response();
				responseData.setMessage("updated successfully");
				responseData.setStatus("200");

				return new ResponseEntity<>(responseData, HttpStatus.OK);

			} else {
				Response responseData = new Response();
				responseData.setMessage("Not updated successfully");
				responseData.setStatus("200");
				return new ResponseEntity<>(responseData, HttpStatus.OK);

			}
		} catch (Exception e) {

			logger.info("exception occured" + e.toString());

			Response responseData = new Response();
			responseData.setStatus("409");
			responseData.setMessage("Exception caught");
			return new ResponseEntity<>(responseData, HttpStatus.CONFLICT);

		}

	}

	@GetMapping("getByStatus/{status}")
	public ResponseEntity<?> getRequestingAssetByEmployeeid(@PathVariable("status") Boolean status)
			throws ResourceNotFoundException {

		List<RequestAssetModel> assetRequestByStatus = requestAssetService.getAssetRequestByStatus(status);

		return new ResponseEntity<>(assetRequestByStatus, HttpStatus.OK);

	}

}
