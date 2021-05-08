package com.Inventory.Project.AssectService.AssectEmployee;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Inventory.Project.AssectService.Assect.AssectModel;
import com.Inventory.Project.AssectService.Exception.ResourceNotFoundException;
import com.Inventory.Project.AssectService.Response.Response;
import com.Inventory.Project.AssectService.Response.ResponseList;

@RestController
@RequestMapping(value = "/assectEmployee")
public class AssectEmployeeController {
	@Autowired
	AssectEmployeeService assectemployeeservice;

	@Autowired
	Response assectEmployeeResponse;

	@Autowired
	CsvFileExporterAssetEmployee csvFileExporterAssetEmployee;

	@Autowired
	AssectEmployeeDao assectEmployeeDao;

	@Autowired
	AssetEmployeeExcelFileExport assetEmployeeExcelFileExport;

	private static final Logger log = LogManager.getLogger(AssectEmployeeController.class);

	@PostMapping("/addingAssectToEmployee/{employeeid}/{assectid}")
	public ResponseEntity<?> addAssectToEmloyee(@RequestBody AssectEmployeeModel assectEmployeeModel,
			@PathVariable("employeeid") String employeeid, @PathVariable("assectid") Long assectid) {
		System.out.println("Hello");
		try {
			if (assectEmployeeModel == null || employeeid == null || assectid == null) {
				log.error("Fields are Null");
				assectEmployeeResponse
						.setMessage("Employeeid and Assectid and AssectEmployee fields should Not Be Null");
				assectEmployeeResponse.setStatus("422");
				return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (assectEmployeeModel.getStatus() == null || assectEmployeeModel.getDateOfAssignment() == null
					|| assectEmployeeModel.getDeskNumber() == null || assectEmployeeModel.getDeskNumber().isEmpty()
					|| assectEmployeeModel.getFloor() == null || assectEmployeeModel.getFloor().isEmpty()
					|| assectEmployeeModel.getLocation() == null || assectEmployeeModel.getLocation().isEmpty()) {
				log.error("Entered Fields Should Not be Empty");
				assectEmployeeResponse.setMessage("Fields should not be empty");
				assectEmployeeResponse.setStatus("422");
				return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			assectEmployeeModel.setCreatedBy(employeeid);

			assectEmployeeModel.setCreatedon(new Date());
			Boolean addAssectToEmloyee = assectemployeeservice.addAssectToEmloyee(assectEmployeeModel, employeeid,
					assectid);

			if (addAssectToEmloyee) {
				log.info("Added Assect To Employee Successfully");
				assectEmployeeResponse.setMessage("Added Assect To Employee Successfully");
				assectEmployeeResponse.setStatus("200");
				return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.OK);
			} else {
				log.info("Failed to add assect to Employee");
				assectEmployeeResponse.setMessage("Failed to add assect to Employee");
				assectEmployeeResponse.setStatus("417");
				return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.EXPECTATION_FAILED);

			}
		} catch (DataIntegrityViolationException exception) {
			log.debug("Duplicate Entry of Desk Number");
			assectEmployeeResponse.setMessage("Duplicate Entry of DeskNumber already present in DB");
			assectEmployeeResponse.setStatus("409");
			return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.CONFLICT);
		} catch (Exception exception) {
			log.debug("Inside the catch Block : " + exception.getMessage());
			assectEmployeeResponse.setMessage("Exception caught");
			assectEmployeeResponse.setStatus("409");
			return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.CONFLICT);

		}

	}

//	@GetMapping(value = { "/getAllAssectEmployee/{pageno}/{pagesize}/{status}", "/getAllAssectEmployee/{pageno}/{pagesize}"})
//	public ResponseEntity<?> getAllAssectEmployees(@PathVariable("pageno") Integer pageno,
//			@PathVariable("pagesize") Integer pagesize, @PathVariable(required = false,name = "status") Boolean status) {
//		if (status == null) {
//			status = null;
//		}
//		try {
//
//			List<AssectEmployeeModel> list = null;
//			if (status == null) {
//				assectemployeeservice.getAllAssectEmployees(pageno, pagesize);
//			}
//			if (status != null) {
//				assectemployeeservice.getAssectEmployeeBystatus(status, pageno, pagesize);
//			}
//			return new ResponseEntity<>(list, HttpStatus.OK);
//		} catch (Exception exception) {
//			log.debug("Inside Catch Block : " + exception.getMessage());
//			assectEmployeeResponse.setMessage("Exception caught");
//			assectEmployeeResponse.setStatus("409");
//			return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.CONFLICT);
//		}
//	}
	@GetMapping(value = { "/getAllAssectEmployee/{pageno}/{pagesize}" })
	public ResponseEntity<?> getAllAssectEmployees(@PathVariable("pageno") Integer pageno,
			@PathVariable("pagesize") Integer pagesize) {
		try {

			ResponseList list = null;

			list = assectemployeeservice.getAllAssectEmployees(pageno, pagesize);

			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception exception) {
			log.debug("Inside Catch Block : " + exception.getMessage());
			assectEmployeeResponse.setMessage("Exception caught");
			assectEmployeeResponse.setStatus("409");
			return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/getByEmployeeid/{employeeid}")
	public ResponseEntity<?> getByEmployeeEmployeeid(@PathVariable("employeeid") String employeeid) {
		try {
			if (employeeid == null) {
				log.error("Entered Employee id is Null");
				assectEmployeeResponse.setMessage("Employee id should not be Null");
				assectEmployeeResponse.setStatus("422");
				return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			List<AssectEmployeeModel> list = assectemployeeservice.getByEmployeeEmployeeid(employeeid);
			log.info("Employee object found : " + list);
			/* return new ResponseEntity<>(employeeModel,HttpStatus.OK); */
			if (list == null) {
				throw new ResourceNotFoundException("NO data found with this employee id");
			}
			return new ResponseEntity<>(list, HttpStatus.OK);

		} catch (ResourceNotFoundException exception) {
			log.debug("No data found with this employeeid");
			assectEmployeeResponse.setMessage("No Data Found with this employee id : " + employeeid);
			assectEmployeeResponse.setStatus("409");
			return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.CONFLICT);
		} catch (Exception exception) {
			log.debug("Inside the catch Block : " + exception.getMessage());
			assectEmployeeResponse.setMessage("Exception caught");
			assectEmployeeResponse.setStatus("409");
			return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.CONFLICT);
		}

	}

	@GetMapping("/getByAssectid/{assectid}")
	public ResponseEntity<?> getByAssectid(@PathVariable("assectid") Long assectid) {
		try {
			if (assectid == null) {
				log.error("Assect Id is Null");
				assectEmployeeResponse.setMessage("Assect id should not be Null");
				assectEmployeeResponse.setStatus("422");
				return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			AssectEmployeeModel assectEmployeeModel = assectemployeeservice.getByAssectid(assectid);
			if (assectEmployeeModel == null) {
				throw new ResourceNotFoundException("No Data Found with this assectid : " + assectid);
			}
			return new ResponseEntity<>(assectEmployeeModel, HttpStatus.OK);
		} catch (ResourceNotFoundException exception) {
			log.debug("No data Found");
			assectEmployeeResponse.setMessage("No Data Found with this Id");
			assectEmployeeResponse.setStatus("409");
			return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.debug("Inside the catch Block : " + e.getMessage());
			assectEmployeeResponse.setMessage("Exception caught");
			assectEmployeeResponse.setStatus("409");
			return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.CONFLICT);
		}

	}

	@GetMapping("/getByAssectemployeeid/{assectemployeeid}")
	public ResponseEntity<?> getByAssectemployeid(@PathVariable("assectemployeeid") Integer assectemployeeid) {
		try {
			if (assectemployeeid == null) {
				log.error("Assect Id is Null");
				assectEmployeeResponse.setMessage("Assect id should not be Null");
				assectEmployeeResponse.setStatus("422");
				return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			AssectEmployeeModel assectEmployeeModel = assectemployeeservice
					.getByAssectassectEmployeeid(assectemployeeid);
			if (assectEmployeeModel == null) {
				throw new ResourceNotFoundException("No Data Found with this assectid : " + assectemployeeid);
			}
			return new ResponseEntity<>(assectEmployeeModel, HttpStatus.OK);
		} catch (ResourceNotFoundException exception) {
			log.debug("No data Found");
			assectEmployeeResponse.setMessage("No Data Found with this Id");
			assectEmployeeResponse.setStatus("409");
			return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.debug("Inside the catch Block : " + e.getMessage());
			assectEmployeeResponse.setMessage("Exception caught");
			assectEmployeeResponse.setStatus("409");
			return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/update/{employeeid}/{assectid}/{assectemployeid}")
	public ResponseEntity<?> UpdateAssectToEmloyee(@RequestBody AssectEmployeeModel assectEmployeeModel,
			@PathVariable("employeeid") String employeeid, @PathVariable("assectid") Long assectid,
			@PathVariable("assectemployeid") Integer assectEmployeeid) {
		try {
			if (assectEmployeeModel == null || employeeid == null || assectid == null || assectEmployeeid == null) {
				log.error("Fields are Null");

				assectEmployeeResponse
						.setMessage("Employeeid and Assectid and AssectEmployee fields should Not Be Null");
				assectEmployeeResponse.setStatus("422");
				return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (assectEmployeeModel.getStatus() == null || assectEmployeeModel.getDateOfAssignment() == null
					|| assectEmployeeModel.getDeskNumber() == null || assectEmployeeModel.getDeskNumber().isEmpty()
					|| assectEmployeeModel.getFloor() == null || assectEmployeeModel.getFloor().isEmpty()
					|| assectEmployeeModel.getLocation() == null || assectEmployeeModel.getLocation().isEmpty()) {
				log.error("Entered Fields Should Not be Empty");
				assectEmployeeResponse.setMessage("Fields should not be empty");
				assectEmployeeResponse.setStatus("422");
				return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			assectEmployeeModel.setUpdatedBy(employeeid);
			assectEmployeeModel.setUpdateon(new Date());

			Boolean addAssectToEmloyee = assectemployeeservice.UpdateAssectEmployee(assectEmployeeModel, employeeid,
					assectid, assectEmployeeid);

			if (addAssectToEmloyee) {
				System.out.println("hello");
				log.info("Updated Assect To Employee Successfully");
				assectEmployeeResponse.setMessage("Updated Assect To Employee Successfully");
				assectEmployeeResponse.setStatus("200");
				return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.OK);
			} else {
				log.info("Failed to add assect to Employee");
				assectEmployeeResponse.setMessage("Failed to add assect to Employee");
				assectEmployeeResponse.setStatus("417");
				return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.EXPECTATION_FAILED);

			}
		} catch (DataIntegrityViolationException exception) {
			log.debug("Duplicate Entry of Desk Number");
			assectEmployeeResponse.setMessage("Duplicate Entry of DeskNumber already present in DB");
			assectEmployeeResponse.setStatus("409");
			return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.CONFLICT);
		} catch (Exception exception) {
			log.debug("Inside the catch Block : " + exception.getMessage());
			assectEmployeeResponse.setMessage("Exception caught");
			assectEmployeeResponse.setStatus("409");
			return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.CONFLICT);

		}

	}

	@DeleteMapping("/deAllocate/{assectEmployeeid}")
	public HashMap<String, String> deAllocate(@PathVariable("assectEmployeeid") Integer assectEmployeeid) {
		HashMap<String, String> hashMap = new HashMap<>();
		try {

			if (assectEmployeeid == null) {
				log.error("Assect Employee Id is Null");
				assectEmployeeResponse.setMessage("Assect Employee id should not be Null");
				assectEmployeeResponse.setStatus("422");

				hashMap.put("message", "feilds should ");
				return hashMap;
			} else {
				assectemployeeservice.deAllocate(assectEmployeeid);
				hashMap.put("message", "success");
				return hashMap;
			}

		} catch (Exception e) {
			log.debug("Inside the catch Block : " + e.getMessage());
			assectEmployeeResponse.setMessage("Exception caught");
			assectEmployeeResponse.setStatus("409");
			hashMap.put("message", "exception");
			return hashMap;
		}
	}

//	@PutMapping("/deAllocate/{assectEmployeeid}")
//	public ResponseEntity<?> deAllocate(@PathVariable("assectEmployeeid") Integer assectEmployeeid) {
//		try {
//			if (assectEmployeeid == null) {
//				log.error("Assect Employee Id is Null");
//				assectEmployeeResponse.setMessage("Assect Employee id should not be Null");
//				assectEmployeeResponse.setStatus("422");
//				return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.UNPROCESSABLE_ENTITY);
//			} else {
//				assectemployeeservice.deAllocate(assectEmployeeid);
//				return new ResponseEntity<>("Deleted", HttpStatus.OK);
//			}
//
//		} catch (Exception e) {
//			log.debug("Inside the catch Block : " + e.getMessage());
//			assectEmployeeResponse.setMessage("Exception caught");
//			assectEmployeeResponse.setStatus("409");
//			return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.CONFLICT);
//		}
//	}

	@GetMapping("/getAllList")
	public ResponseEntity<?> getAllList() {
		try {
			log.info("Getting List of Records");
			List<AssectEmployeeModel> list = assectemployeeservice.getAllList();
			return new ResponseEntity<>(list, HttpStatus.OK);

		} catch (Exception e) {
			log.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}
	}

	@GetMapping("/getAllAsectsWithoutAllocated")
	public ResponseEntity<?> getAllAssectsWithoutallocated() {
		try {
			log.info("getting asscts without allocated");
			List<AssectModel> list = assectemployeeservice.getAllAssetsWithoutAllocated();
			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			log.debug("inside catch block" + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}
	}

	@GetMapping("/search/{search}")
	public ResponseEntity<?> search(@PathVariable("search") String search) {
		try {
			if (search == null) {
				log.error("Entered search is Null");
				assectEmployeeResponse.setMessage("search should not be Null");
				assectEmployeeResponse.setStatus("422");
				return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			List<AssectEmployeeModel> list = assectemployeeservice.search(search);
			log.info("Employee object found : " + list);
			/* return new ResponseEntity<>(employeeModel,HttpStatus.OK); */

			if (list == null) {
				throw new ResourceNotFoundException("NO data found with this employee id");
			}
			return new ResponseEntity<>(list, HttpStatus.OK);

		} catch (ResourceNotFoundException exception) {
			log.debug("No data found with this employeeid");
			assectEmployeeResponse.setMessage("No Data Found with this employee id : " + search);
			assectEmployeeResponse.setStatus("409");
			return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.CONFLICT);
		} catch (Exception exception) {
			log.debug("Inside the catch Block : " + exception.getMessage());
			assectEmployeeResponse.setMessage("Exception caught");
			assectEmployeeResponse.setStatus("409");
			return new ResponseEntity<>(assectEmployeeResponse, HttpStatus.CONFLICT);
		}

	}

	/*
	 * @PutMapping("/getByEmployeeId/{employeeid}") public ResponseEntity<?>
	 * editByEmployeeid(@RequestParam("status") Boolean status){ return
	 * assectemployeeservice.editByEmployeeid(status); }
	 */

	@GetMapping("/download/AssetEmployeeExcelFile")
	public ResponseEntity<?> getExcelData(HttpServletResponse response1) {
		try {

			Response response = new Response();

			response1.setContentType("appliction/octet-stream ;charset=UTF-8-BOM");
			String headerkey = "Content-Disposition";
			String headervalue = "attachment; filename=AssetEmployee.xlsx";

			response1.setHeader(headerkey, headervalue);
			List<AssectEmployeeModel> list = assectemployeeservice.getAllList();
			ByteArrayInputStream listToExcelFile = assetEmployeeExcelFileExport
					.exportAssetEmployeeListToExcelFile(list);
			// int copy = IOUtils.copy(listToExcelFile, response1.getOutputStream());
			long copy = org.apache.commons.compress.utils.IOUtils.copy(listToExcelFile, response1.getOutputStream());
			response.setMessage("SuccessFully Excel File is Generated");
			response.setStatus("200");
			return new ResponseEntity<>(copy, HttpStatus.OK);

		} catch (IOException exception) {
			Response response = new Response();
			response.setMessage("Input and Output Exception");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception e) {
			Response response = new Response();
			response.setMessage("Exception Caught :" + e.getMessage());
			response.setStatus("409");

			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}

	}

	@GetMapping(value = "/download/AssetEmployeeCSVFile")
	public ResponseEntity<?> getCsvSData(HttpServletResponse httpServletResponse) {
		Response response = new Response();
		try {
			httpServletResponse.setContentType("application/csv; charset = utf-8");
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename=AssetEmployee.csv");

			List<AssectEmployeeModel> list = assectEmployeeDao.findAll();

			csvFileExporterAssetEmployee.getCsvData(httpServletResponse.getWriter(), list);
			response.setMessage("SuccessFully Downloading Csv File");
			response.setStatus("200");

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (IOException e) {
			response.setMessage("Input and OutPut stream Exception : " + e.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception e) {
			response.setMessage("Exception caught : " + e.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/search/{pageNumber}/{pageSize}/{text}")
	public ResponseEntity getSearchAll(@PathVariable(required = false, name = "text") String text,

			@PathVariable("pageNumber") Integer pageno, @PathVariable("pageSize") Integer pagesize) {

		try {

			if (text.equalsIgnoreCase("null") || text.equalsIgnoreCase("undefined")) {
				text = null;
			}

			if (pageno == null || pagesize == null) {
				Response response = new Response();
				response.setMessage("bad request");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			ResponseList data = null;
			if (text == null) {
				data = assectemployeeservice.getAllAssectEmployees(pageno, pagesize);
			} else {
				data = assectemployeeservice.searchAll(text, pageno, pagesize);
			}
			return new ResponseEntity<>(data, HttpStatus.OK);
		} catch (Exception exception) {
			log.debug("Inside Catch Block : " + exception.getMessage());
			Response response = new Response();
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}
	}
}
