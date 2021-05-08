package com.Inventory.Project.AssectService.Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Exceldata.RamCapacityExcelData;
import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.RamTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Exception.ResourceNotFoundException;
import com.Inventory.Project.AssectService.Model.RamCapacityMaster;
import com.Inventory.Project.AssectService.Model.RamTypeMaster;
import com.Inventory.Project.AssectService.Pagination.PagenationResponse;
import com.Inventory.Project.AssectService.Pagination.RamCapcityDaoImpl;
import com.Inventory.Project.AssectService.Response.Response;
import com.Inventory.Project.AssectService.Response.ResponseList;
import com.Inventory.Project.AssectService.Service.RamCapacityService;
import com.Inventory.Project.AssectService.Service.RamTypeService;
import com.Inventory.Project.AssectService.Validations.Validation;

@RestController
@RequestMapping("/rammaster")
public class RamCapacityMasterController {

	private static final Logger logger = LogManager.getLogger(RamCapacityMasterController.class);

	@Autowired
	private RamCapacityService capacityService;

	@Autowired
	private RamCapacityExcelData ramCapacityExcel;

	@Autowired
	private RamTypeService ramTypeService;

	@Autowired
	private RamCapcityDaoImpl ramcapcityimpl;

	// Store the RamCapacity Data into DataBase //
	@PostMapping(value = "/{ramTypeId}/{employeeId}", consumes = { "application/json" })
	public ResponseEntity<?> saveRamData(@RequestBody RamCapacityMaster master,
			@PathVariable("employeeId") String employeeId, @PathVariable(value = "ramTypeId") Integer ramTypeId) {

		try {
			if (master == null || ramTypeId == null) {
				logger.error("ramMaster object is null  in post");
				Response response = new Response();
				response.setMessage("Request Object Shouldn't Null ");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (master.getRamCapacity().isEmpty() || master.getRamCapacity() == null) {
				logger.info("Null values are not allowed");
				Response response = new Response();
				response.setMessage("request object feilds shouldn't null ");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (ramTypeService.getRamDetailsById(ramTypeId) == null) {
				throw new ResourceNotFoundException("RamTypeMaster Id not found");
			}
			RamTypeMaster ramTypeById = ramTypeService.getRamDetailsById(ramTypeId);
			List<RamCapacityMaster> capacity = ramTypeById.getRamMaster();
			capacity.forEach(a -> {
				if (a.getRamCapacity().equalsIgnoreCase(master.getRamCapacity())) {
					throw new DataIntegrityViolationException("RamCapacity already exist");
				}
			});

			master.setCreatedBy(employeeId);

			Boolean boolean1 = capacityService.saveRamDeatils(master, ramTypeId);
			logger.info("RamMaster is added or created");
			if (boolean1) {
				Response response = new Response();
				response.setMessage("RamMaster Registered successfully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {

				Response response = new Response();
				response.setMessage("RamMaster not Registered");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);

			}

		} catch (DataIntegrityViolationException exception) {

			Response response = new Response();
			logger.error(exception);
			response.setMessage("Duplicate values not allowed");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		catch (Exception e) {
			Response response = new Response();
			logger.debug("inside catch block " + e.getMessage());

			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}

	}

	@GetMapping("/getallramcapacity")
	public ResponseEntity<?> getRamMasterList() {

		try {
			List<RamCapacityMaster> list = capacityService.getListOfRamMaster();

			logger.info("getting list of records" + list);

			return new ResponseEntity<>(list, HttpStatus.OK);

		} catch (Exception e) {

			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("No Data Found");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	// Get All RamCapacity Details //
	@GetMapping(path = { "/{pageNo}/{sizePerPage}/{searchByRamCapacity}" })
	public ResponseEntity<?> getAllRaminfo(
			@PathVariable(required = false, name = "searchByRamCapacity") String searchByRamCapacity,
			@PathVariable("pageNo") Integer pageNo, @PathVariable("sizePerPage") Integer sizePerPage) {
		if (searchByRamCapacity.equalsIgnoreCase("null") || searchByRamCapacity.equalsIgnoreCase("undefined")) {
			searchByRamCapacity = null;
		}
		if (pageNo == null || sizePerPage == null) {
			logger.error("Ram object is null  in post");
			Response response = new Response();

			response.setMessage("page number and size per page shouldnot null");
			response.setStatus("400");

			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			ResponseList listOfRams = null;

			if (searchByRamCapacity == null) {
				listOfRams = capacityService.getAllRamDetails(pageNo, sizePerPage);
			}

			else {
				listOfRams = capacityService.getAllRamDetails(searchByRamCapacity, pageNo, sizePerPage);
			}

			logger.info("getting list of records" + listOfRams);

			return new ResponseEntity<>(listOfRams, HttpStatus.OK);
		} catch (Exception e) {

			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	// Get RamCapacity details By Id //
	@GetMapping("/ramById/{ramTypeId}/{ramId}")
	public ResponseEntity<?> getRamByRamIdRamTypeId(@PathVariable("ramTypeId") Integer ramTypeId,
			@PathVariable("ramId") Integer ramId) throws RecordNotFoundException {
		try {
			if (ramId == null || ramTypeId == null) {
				logger.error("RamData Id is null");
				Response response = new Response();
				response.setMessage("RamData Id is null");
				response.setStatus("422");

				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			if (ramTypeService.getRamDetailsById(ramTypeId) == null) {
				throw new RecordNotFoundException("RamType not found");
			}
			RamCapacityMaster ram = capacityService.findByRamIdRamTypeId(ramId, ramTypeId);

			if (ram == null) {
				throw new RecordNotFoundException("No record exist for given id");
			}
			return new ResponseEntity<>(ram, HttpStatus.OK);
		} catch (RecordNotFoundException e) {
			logger.debug("RamType with RamCapacity Not Found" + e.getMessage());
			Response response = new Response();
			response.setMessage("RamType with RamCapacity Not Found");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

		catch (Exception e) {
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}
	}

	@GetMapping("/ramById/{ramTypeId}/{pageNumber}/{pageSize}/{searchby}")
	public ResponseEntity<?> getRamCapacityByRamType(@PathVariable(value = "ramTypeId") Integer ramTypeId,
			@PathVariable(required = false, name = "searchby") String searchby,

			@PathVariable("pageNumber") Integer pageno, @PathVariable("pageSize") Integer pagesize) {

		try {

			if (searchby.equalsIgnoreCase("null") || searchby.equalsIgnoreCase("undefined")) {
				searchby = null;
			}

			if (pageno == null || pagesize == null) {
				Response response = new Response();
				response.setMessage("Bad request");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			ResponseList data = null;
			if (searchby == null) {
				data = capacityService.getAllOfRamDetails(ramTypeId, pageno, pagesize);
			} else {
				data = capacityService.getAllRamMaster(ramTypeId, pageno, pagesize, searchby);
			}
			return new ResponseEntity<>(data, HttpStatus.OK);
		} catch (Exception exception) {
			logger.debug("Inside Catch Block : " + exception.getMessage());
			Response response = new Response();
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

	@GetMapping("/ramById/{ramTypeId}")
	public ResponseEntity<?> getModelByBrand(@PathVariable(value = "ramTypeId") Integer ramTypeId) {
		try {
			if (ramTypeId == null) {
				Response response = new Response();
				response.setMessage("Bad request");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			if (ramTypeService.getRamDetailsById(ramTypeId) == null) {
				throw new RecordNotFoundException("RamTypeMaster Not Found");
			}

			List<RamCapacityMaster> list = capacityService.getRamByRamTypeId(ramTypeId);

			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (RecordNotFoundException exception) {
			logger.debug("RamType Id not found " + exception.getMessage());
			Response response = new Response();
			response.setMessage("RamType Id not found");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

		catch (Exception e) {
			logger.debug("Inside Catch Block : " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/updateRam/{ramTypeId}/{employeeId}")
	public ResponseEntity<?> updateByHardDiskCapacityId(@RequestBody RamCapacityMaster master,
			@PathVariable("ramTypeId") Integer ramTypeId, @PathVariable("employeeId") String employeeId) {
		try {
			if (master == null || ramTypeId == null) {
				logger.error("RamMaster object is null");
				Response response = new Response();
				response.setMessage("RamCapacity is Not Found");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (!Validation.isTypeValid(master.getRamCapacity()) || master.getRamCapacity().isEmpty()) {
				logger.error("RamCapacity fields are null");
				Response response = new Response();
				response.setMessage("HardDiskCapacity  Fields are null");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			RamTypeMaster ramType = ramTypeService.getRamDetailsById(ramTypeId);
			List<RamCapacityMaster> capacity = ramType.getRamMaster();
			capacity.forEach(a -> {
				if (a.getRamCapacity().equalsIgnoreCase(master.getRamCapacity())) {
					throw new DataIntegrityViolationException("RamCapacity already exist");
				}
			});

			Boolean boolean1 = capacityService.updateRamDetailsById(master, ramTypeId, master.getRamId());
			if (boolean1 == null) {
				throw new RecordNotFoundException("record is not present");
			}
			master.setUpdatedBy(employeeId);
			Boolean b = capacityService.updateRamDetailsById(master, ramTypeId, master.getRamId());

			if (b) {
				Response response = new Response();
				logger.info("Ramcapacity is updated");
				response.setMessage("RamCapacity updated successfully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				Response response = new Response();
				response.setMessage("RamCapacity not updated");
				response.setStatus("404");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}
		} catch (RecordNotFoundException e) {
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Record not Found");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		} catch (DataIntegrityViolationException exception) {
			Response response = new Response();
			logger.error(exception);
			response.setMessage("RamCapacity" + " alreay exists");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			logger.debug("inside catch block" + e.getMessage());

			Response response = new Response();
			response.setMessage(" RamCapacity Record not found By Id");
			response.setStatus("404");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

	// Get RamCapacity details By Status //
	@GetMapping("bystatus/{status}")
	public ResponseEntity<?> getRamByStatus(@PathVariable("status") Boolean status) {

		try {

			if (status == null) {

				logger.error("Ram status is null");

				Response response = new Response();

				response.setMessage("Ram name is null");
				response.setStatus("422");

				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			List<RamCapacityMaster> list = capacityService.getRamDetailsByStatus(status);

			return new ResponseEntity<>(list, HttpStatus.OK);
		}

		catch (Exception e) {
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}

	}

	// Deleting the RamCapacity By Id //
	@DeleteMapping("/delete/{ramTypeId}/{ramId}")
	@ResponseBody
	public ResponseEntity<?> deleteRamDetailsById(@PathVariable("ramTypeId") Integer ramTypeId,
			@PathVariable("ramId") Integer ramId) throws RecordNotFoundException {
		try {
			if (ramTypeId == null || ramId == null) {
				logger.error("Id is null in DeleteMapping");
				Response response = new Response();
				response.setMessage("Id shouldn't null for while Deleting ");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

			}
			capacityService.deleteByRamId(ramId, ramTypeId);

			logger.info("RamMaster is deleted");

			Response response = new Response();
			response.setMessage("RamMaster deleted successfully");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (RecordNotFoundException e) {
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("No records found");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		} catch (Exception e) {
			Response response = new Response();
			logger.debug("inside catch block " + e.getMessage());

			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}

	}

	@GetMapping("/download/RamCapacityDummyExcelFile")
	public ResponseEntity<?> dummyfile(HttpServletResponse httpServletResponse) {
		Response response = new Response();
		try {
			httpServletResponse.setContentType("application/octet-stream");
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename=RamCapacityFormatExcel.xlsx");
			/* List<AssetTypeMasterEx> list = assetRepo.findAll(); */
			ByteArrayInputStream listToExcelFile = ramCapacityExcel.exportDummyRamTypeListToExcelFile();
			long copy = IOUtils.copy(listToExcelFile, httpServletResponse.getOutputStream());
			logger.info("Successfully RamCapacity Dummy Excel file is Downloading");
			response.setMessage("Successfully RamCapacity Dummy Excel file is Downloading");
			response.setStatus("200");
			return new ResponseEntity<>(copy, HttpStatus.OK);

		} catch (IOException e) {
			logger.debug("IOException Caught : " + e.getMessage());
			e.printStackTrace();
			response.setMessage("Input and OutPut stream Exception : " + e.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception e) {
			logger.debug("Exception Caught : " + e.getMessage());
			e.printStackTrace();
			response.setMessage("Exception caught : " + e.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/updload/Ramcapacity/{employeeid}")
	public ResponseEntity<?> uploadAssetTypeFile(@RequestParam("file") MultipartFile file,
			@PathVariable("employeeid") String employeeid) {
		Response response = new Response();
		if (ramCapacityExcel.hasExcelFormat(file)) {
			try {
				capacityService.save(file, employeeid);
				response.setMessage("File Uploaded SuccessFully : " + file.getOriginalFilename());
				response.setStatus("200");
				logger.info("Successfully Uploaded the RamCapacity Excel file");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} catch (RamTypeNotFoundException e) {
				logger.debug("RecordNotFoundException Caught : " + e.getMessage());
				e.printStackTrace();
				response.setMessage(e.getMessage() + "!");
				response.setStatus("409");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);

			}

			catch (DataIntegrityViolationException e) {
				logger.error(e);
				response.setMessage("Duplicate Entry of RamCapacity");
				response.setStatus("409");
				e.printStackTrace();
				logger.debug("Duplicate Entry of RamCapacity");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (RecordNotFoundException e) {
				logger.debug("RecordNotFoundException Caught : " + e.getMessage());
				e.printStackTrace();
				response.setMessage(" RecordNotFoundException : " + e.getMessage() + "!");
				response.setStatus("409");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);

			} catch (FeildsShouldNotBeEmptyException e) {
				logger.debug("FeildsShouldNotBeEmptyException Caught : " + e.getMessage());
				e.printStackTrace();
				response.setMessage(e.getMessage() + "!");
				response.setStatus("409");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (Exception e) {
				logger.debug("IOException Caught : " + e.getMessage());
				e.printStackTrace();
				response.setMessage("Could not upload the file exception Caught : " + file.getOriginalFilename() + "!");
				response.setStatus("409");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}
		}
		logger.debug("Please Upload Only Excel file");
		response.setMessage("Please upload ExcelFile");
		response.setStatus("400");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}// Get All RamCapacity Details //

	@GetMapping(path = { "/{ramtypeid}/{pageNo}/{pageSize}/{sortByDate}/{searchByRamCapacity}" })
	public ResponseEntity<?> getAllRaminfo(
			@PathVariable(required = false, name = "searchByRamCapacity") String searchByRamCapacity,
			@PathVariable(required = true, name = "sortByDate") String sortByDate,
			@PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer sizePerPage,
			@PathVariable("ramtypeid") Integer ramtypeid) {
		if (searchByRamCapacity.equalsIgnoreCase("null") || searchByRamCapacity.equalsIgnoreCase("undefined")) {
			searchByRamCapacity = null;
		}
		if (pageNo == null || sizePerPage == null) {
			logger.error("Ram object is null  in post");
			Response response = new Response();

			response.setMessage("page number and size per page shouldnot null");
			response.setStatus("400");

			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			ResponseList listOfRams = null;

			if (searchByRamCapacity == null && sortByDate.equals("lastmodefiedDate")) {
				listOfRams = capacityService.getAllRamDetails(ramtypeid, pageNo, sizePerPage, sortByDate);
			}

			else {
				listOfRams = capacityService.getAllRamMaster(ramtypeid, pageNo, sizePerPage, searchByRamCapacity);
			}

			logger.info("getting list of records" + listOfRams);

			return new ResponseEntity<>(listOfRams, HttpStatus.OK);
		} catch (Exception e) {

			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping(path = { "/sort/{pageNo}/{pageSize}/{searchby}" })
	public ResponseEntity<?> getAllRamCapacity(@PathVariable(required = false, name = "searchby") String searchby,
			@PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer pageSize) {
		if (searchby.equalsIgnoreCase("null") || searchby.equalsIgnoreCase("undefined")) {
			searchby = null;
		}

		if (pageNo == null || pageSize == null) {
			logger.error("RamCacity records are null");
			Response response = new Response();

			response.setMessage("Page number And pageSize Connot be null");
			response.setStatus("400");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {

			ResponseList ramCapacityList = null;
			PagenationResponse allRamcapacity = ramcapcityimpl.getAllRamCapacitys(pageNo, pageSize, searchby);
			logger.info("getting list of Objects" + allRamcapacity);

			return new ResponseEntity<>(allRamcapacity, HttpStatus.OK);

		} catch (RecordNotFoundException e) {

			logger.debug("Inside Catch" + e.getMessage());
			e.printStackTrace();
			Response response = new Response();
			response.setMessage("RecordNotFoundException : " + e.getMessage());
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		} catch (Exception e) {

			logger.debug("Inside Catch" + e.getMessage());
			e.printStackTrace();
			Response response = new Response();
			response.setMessage("Exception catch");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

}