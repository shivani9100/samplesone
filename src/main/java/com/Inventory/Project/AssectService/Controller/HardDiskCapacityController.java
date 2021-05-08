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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Exceldata.HardDiskCapacityExcelData;
import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.HardDiskCapacityNotFoundException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Model.HardDiskCapacity;
import com.Inventory.Project.AssectService.Model.HardDiskTypeMaster;
import com.Inventory.Project.AssectService.Pagination.HardDiskCapacityDaoImpl;
import com.Inventory.Project.AssectService.Pagination.PagenationResponse;
import com.Inventory.Project.AssectService.Response.Response;
import com.Inventory.Project.AssectService.Response.ResponseList;
import com.Inventory.Project.AssectService.Service.HardDiskCapacityImp;
import com.Inventory.Project.AssectService.Service.HardDiskTypeMasterService;
import com.Inventory.Project.AssectService.Validations.Validation;

@CrossOrigin
@RestController
@RequestMapping("/hardDiskCapacity")
public class HardDiskCapacityController {

	@Autowired
	private HardDiskCapacityImp hardDiskCapacityService;

	@Autowired
	private HardDiskTypeMasterService diskTypeMasterService;
	@Autowired
	private HardDiskCapacityExcelData hardDiskCapacityExcel;

	@Autowired
	private HardDiskCapacityDaoImpl hardDiskCapacityDaoImpl;

	private static final Logger logger = LogManager.getLogger(HardDiskCapacityController.class);

	@PostMapping("/{hardDiskTypeId}/{employeeId}")
	public ResponseEntity<?> saveHardDiskCapacity(@RequestBody HardDiskCapacity hardDiskCapacity,
			@PathVariable("employeeId") String employeeId, @PathVariable("hardDiskTypeId") Integer hardDiskTypeId) {

		try {
			if (hardDiskCapacity == null || hardDiskTypeId == null) {
				logger.error("HardDiskCapacity object is null  in post");
				Response response = new Response();

				response.setMessage("request object shouldn't null ");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
//			if (!Validation.isTypeValid(hardDiskCapacity.getHarddiskCapacityType())
//					|| hardDiskCapacity.getHarddiskCapacityType().isEmpty()) {
//				logger.info("Null values are not allowed");
//				Response response = new Response();
//
//				response.setMessage("Give a valid values ");
//				response.setStatus("422");
//				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
//			}
			HardDiskTypeMaster hardDiskTypeById = diskTypeMasterService.getHardDiskTypeById(hardDiskTypeId);
			List<HardDiskCapacity> capac = hardDiskTypeById.getCapac();
//			System.out.println(capac.size());
//			for (HardDiskCapacity hardDiskCapacity2 : capac) {
//				System.out.println("ppr");
//				System.out.println(hardDiskCapacity2.getHarddiskCapacityId());
//				System.out.println(hardDiskCapacity2.getHarddiskCapacityType());
//				System.out.println(hardDiskCapacity2.isHarddiskCapacityStatus());
//				
//			}

			capac.forEach(a -> {
				System.out.println(a.getHarddiskCapacityType());
				if (a.getHarddiskCapacityType().equalsIgnoreCase(hardDiskCapacity.getHarddiskCapacityType())) {
					throw new DataIntegrityViolationException("hardDiskCapacity already exist");
				}
			});

			System.out.println("createdby" + employeeId);
			hardDiskCapacity.setCreatedBy(employeeId);
			// System.out.println("createdby"+employeeId);
			Boolean boolean1 = hardDiskCapacityService.saveHardDiskCapacity(hardDiskCapacity, hardDiskTypeId);
			logger.info("HardDisk capacity is added or created");
			if (boolean1) {
				Response response = new Response();

				response.setMessage("HardDiskCapacity Registered successfully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {

				Response response = new Response();

				response.setMessage("HardDiskCapacity not Registered");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);

			}

		} catch (DataIntegrityViolationException exception) {
			logger.error(exception);
			Response response = new Response();
			response.setMessage("HardDiskCapacity" + " alreay exists");
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

	@GetMapping("/{hardDiskTypeId}")
	public ResponseEntity<?> getHardDiskCapacityList(@PathVariable("hardDiskTypeId") Integer hardDiskTypeId) {

		try {
			List<HardDiskCapacity> listOfHardDiskCapacity = hardDiskCapacityService
					.getListOfHardDiskCapacity(hardDiskTypeId);

			logger.info("getting list of records" + listOfHardDiskCapacity);

			return new ResponseEntity<>(listOfHardDiskCapacity, HttpStatus.OK);

		} catch (Exception e) {

			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping(path = { "/{hardDiskTypeId}/{pageNo}/{pageSize}/{harddiskCapacityType}" })
	// + "/{searchByhardharddiskCapacityType}")
	public ResponseEntity<?> getAllHardDiskCapacity(@PathVariable("hardDiskTypeId") Integer hardDiskTypeId,
			@PathVariable(required = false, name = "harddiskCapacityType") String harddiskCapacityType,
			@PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer pageSize) {
		if (harddiskCapacityType.equalsIgnoreCase("null") || harddiskCapacityType.equalsIgnoreCase("undefined")) {
			harddiskCapacityType = null;
		}

		if (pageNo == null || pageSize == null) {
			logger.error("HardDiskCacity records are null");
			Response response = new Response();

			response.setMessage("Page number And pageSize Connot be null");
			response.setStatus("400");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {

			// List<HardDiskCapacity> hardDiskCapacityList = null;
			ResponseList hardDiskCapacityList = null;

			if (harddiskCapacityType == null) {
				hardDiskCapacityList = hardDiskCapacityService.getAllHardDiskCapacityList(hardDiskTypeId, pageNo,
						pageSize);

			} else {
				hardDiskCapacityList = hardDiskCapacityService.getAllHardDiskCapacityList(hardDiskTypeId,
						harddiskCapacityType, pageNo, pageSize);

			}

			logger.info("getting list of Objects" + hardDiskCapacityList);

			return new ResponseEntity<>(hardDiskCapacityList, HttpStatus.OK);

		} catch (Exception e) {

			logger.debug("Inside Catch" + e.getMessage());

			Response response = new Response();
			response.setMessage("Exception catch");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}

	}

	@PutMapping("/{hardDiskTypeId}/{employeeId}")
	public ResponseEntity<?> updateByHardDiskCapacityId(@RequestBody HardDiskCapacity hardDiskCapacity,
			@PathVariable("hardDiskTypeId") Integer hardDiskTypeId, @PathVariable("employeeId") String employeeId) {
		try {
			if (hardDiskCapacity == null || hardDiskTypeId == null) {
				logger.error("harddiskcapacity object is null");
				Response response = new Response();
				response.setMessage("HardDiskCapacity is Not Found");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (!Validation.isTypeValid(hardDiskCapacity.getHarddiskCapacityType())
					|| hardDiskCapacity.getHarddiskCapacityType().isEmpty()) {
				logger.error("hardDiskcapacity fields are null");
				Response response = new Response();
				response.setMessage("HardDiskCapacity  Fields are null");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			HardDiskTypeMaster hardDiskTypeById = diskTypeMasterService.getHardDiskTypeById(hardDiskTypeId);
			List<HardDiskCapacity> capac = hardDiskTypeById.getCapac();
			capac.forEach(a -> {
				if (a.getHarddiskCapacityType().equalsIgnoreCase(hardDiskCapacity.getHarddiskCapacityType())) {
					throw new DataIntegrityViolationException("hardDiskCapacity already exist");
				}
			});

			Boolean diskCapacity = hardDiskCapacityService.updateByHardDiskCapacityId(hardDiskCapacity, hardDiskTypeId);
			if (diskCapacity == null) {
				throw new RecordNotFoundException("record is not present");
			}
			hardDiskCapacity.setUpdatedBy(employeeId);
			Boolean diskCapacity1 = hardDiskCapacityService.updateByHardDiskCapacityId(hardDiskCapacity,
					hardDiskTypeId);
			if (diskCapacity1) {
				Response response = new Response();
				logger.info("harddiskcapacity is updated");
				response.setMessage("HardDiskCapacity updated successfully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				Response response = new Response();
				response.setMessage("HardDiskCapacity not updated");
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
			logger.error(exception);
			Response response = new Response();
			response.setMessage("HardDiskCapacity" + " alreay exists");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			logger.debug("inside catch block" + e.getMessage());

			Response response = new Response();
			response.setMessage(" HardDiskapacity Record not found By Id");
			response.setStatus("404");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("/{hardDiskTypeId}/{hardDiskCapacityId}")
	public ResponseEntity<?> getHardDiskCapacitybyTypeIdAndCapacityId(
			@PathVariable("hardDiskTypeId") Integer hardDiskTypeId,
			@PathVariable("hardDiskCapacityId") Integer hardDiskCapacityId) {

		try {
			HardDiskCapacity hardDiskCapacity = hardDiskCapacityService
					.findByHarddisktypeIdAndHardiskcapacityId(hardDiskTypeId, hardDiskCapacityId);

			logger.info("getting list of records" + hardDiskCapacity);

			return new ResponseEntity<>(hardDiskCapacity, HttpStatus.OK);

		} catch (Exception e) {

			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/download/HardDiksCapacityDummyExcelFile")
	public ResponseEntity<?> dummyfile(HttpServletResponse httpServletResponse) {
		Response response = new Response();
		try {
			httpServletResponse.setContentType("application/octet-stream");
			httpServletResponse.setHeader("Content-Disposition",
					"attachment; filename = HardDiskCapacityFormatExcel.xlsx");
			ByteArrayInputStream listToExcelFile = hardDiskCapacityExcel.dummyHardDiskCapcityExcelFile();
			long copy = IOUtils.copy(listToExcelFile, httpServletResponse.getOutputStream());

			logger.info("Successfully HardDiskCapacity Dummy Excel file is Downloading");
			response.setMessage("Successfully HardDiskCapacity Dummy Excel file is Downloading");
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

	@PostMapping("/upload/HardDiskcapacity/{employeeid}")
	public ResponseEntity<?> uploadHarddiskCapacity(@RequestParam("file") MultipartFile file,
			@PathVariable("employeeid") String employeeid) {
		Response response = new Response();
		if (hardDiskCapacityExcel.hasExcelFormat(file)) {
			try {
				hardDiskCapacityService.save(file, employeeid);
				response.setMessage("File Uploaded SuccessFully : " + file.getOriginalFilename());
				response.setStatus("200");
				logger.info("Successfully Uploaded the HardDiskCapacity Excel file");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} catch (DataIntegrityViolationException e) {
				logger.error(e);
				response.setMessage("Duplicate Entry of HardDiskCapacity");
				response.setStatus("409");
				e.printStackTrace();
				logger.debug("Duplicate Entry of HardDiskCapacity");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} 
			catch (HardDiskCapacityNotFoundException e) {
				logger.debug("IOException Caught : " + e.getMessage());
				e.printStackTrace();
				response.setMessage(e.getMessage() + "!");
				response.setStatus("409");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
				// TODO: handle exception
			} 
			catch (FeildsShouldNotBeEmptyException e) {
				logger.debug("IOException Caught : " + e.getMessage());
				e.printStackTrace();
				response.setMessage(e.getMessage() + "!");
				response.setStatus("409");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
				// TODO: handle exception
			} catch (RecordNotFoundException e) {
				logger.debug("IOException Caught : " + e.getMessage());
				e.printStackTrace();
				response.setMessage("ReocrdNotFound Exception Caught : " + e.getMessage() + "!");
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

	}

	@GetMapping(path = { "/{hardDiskTypeId}/{pageNo}/{pageSize}/{sortByDate}/{harddiskCapacityType}" })
	// + "/{searchByhardharddiskCapacityType}")
	public ResponseEntity<?> getAllHardDiskCapacity(@PathVariable("hardDiskTypeId") Integer hardDiskTypeId,
			@PathVariable(required = false, name = "harddiskCapacityType") String harddiskCapacityType,
			@PathVariable(required = true, name = "sortByDate") String sortByDate,
			@PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer pageSize) {
		if (harddiskCapacityType.equalsIgnoreCase("null") || harddiskCapacityType.equalsIgnoreCase("undefined")) {
			harddiskCapacityType = null;
		}

		if (pageNo == null || pageSize == null) {
			logger.error("HardDiskCacity records are null");
			Response response = new Response();

			response.setMessage("Page number And pageSize Connot be null");
			response.setStatus("400");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {

			// List<HardDiskCapacity> hardDiskCapacityList = null;
			ResponseList hardDiskCapacityList = null;

			if (harddiskCapacityType == null && sortByDate.equals("lastmodefiedDate")) {
				hardDiskCapacityList = hardDiskCapacityService.getAllHardDiskCapacityList(hardDiskTypeId, pageNo,
						pageSize, sortByDate);

			} else {
				hardDiskCapacityList = hardDiskCapacityService.getAllHardDiskCapacityList(hardDiskTypeId,
						harddiskCapacityType, pageNo, pageSize);

			}

			logger.info("getting list of Objects" + hardDiskCapacityList);

			return new ResponseEntity<>(hardDiskCapacityList, HttpStatus.OK);

		} catch (Exception e) {

			logger.debug("Inside Catch" + e.getMessage());

			Response response = new Response();
			response.setMessage("Exception catch");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}

	}

	@GetMapping(path = { "/sort/{pageNo}/{pageSize}/{searchBy}" })

	public ResponseEntity<?> getAllHardDiskCapacity(@PathVariable(required = false, name = "searchBy") String searchBy,
			@PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer pageSize) {

		if (pageNo == null || pageSize == null) {
			logger.error("HardDiskCacity records are null");
			Response response = new Response();

			response.setMessage("Page number And pageSize Connot be null");
			response.setStatus("400");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {

			ResponseList hardDiskCapacityList = null;

			PagenationResponse allHardDiskCapacityList1 = hardDiskCapacityDaoImpl.getAllHardDiskCapacities(pageNo,
					pageSize, searchBy);

			logger.info("getting list of Objects" + allHardDiskCapacityList1);

			return new ResponseEntity<>(allHardDiskCapacityList1, HttpStatus.OK);

		} catch (RecordNotFoundException e) {

			logger.debug("Inside Catch" + e.getMessage());

			Response response = new Response();
			response.setMessage("RecordNotFoundException : " + e.getMessage());
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		} catch (Exception e) {

			logger.debug("Inside Catch" + e.getMessage());

			Response response = new Response();
			response.setMessage("Exception catch");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}

	}

}