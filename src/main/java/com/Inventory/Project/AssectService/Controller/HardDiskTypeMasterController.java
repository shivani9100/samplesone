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

import com.Inventory.Project.AssectService.Exceldata.HardDiskTypeExcelData;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Model.HardDiskTypeMaster;
import com.Inventory.Project.AssectService.Response.Response;
import com.Inventory.Project.AssectService.Response.ResponseList;
import com.Inventory.Project.AssectService.Service.HardDiskTypeMasterServiceImpl;
import com.Inventory.Project.AssectService.Validations.HardDiskTypeValidation;

@CrossOrigin
@RestController
@RequestMapping("hardDiskOfType")
public class HardDiskTypeMasterController {

	private static final Logger logger = LogManager.getLogger(HardDiskTypeMasterController.class);

	@Autowired
	private HardDiskTypeMasterServiceImpl hardDiskTypeMasterServiceImpl;

	@Autowired
	private HardDiskTypeExcelData hardDiskExcel;

	// 1.inserting HardDiskTypeMaster into DataBase
	@PostMapping("/{employeeid}")
	public ResponseEntity<?> saveHardDiskType(@RequestBody HardDiskTypeMaster hardDiskTypeMaster,
			@PathVariable("employeeid") String employeeid) {

		try {
			if (hardDiskTypeMaster == null) {

				logger.error("HarddiskType Object is null in post");

				Response response = new Response();

				response.setMessage("request object shouldn't null ");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

			}

			if (hardDiskTypeMaster.getHardDiskType().isEmpty() || hardDiskTypeMaster.getHardDiskStatus() == null) {

				logger.error("fields of HardDisktype Object are null in post");

				Response response = new Response();

				response.setMessage("request object feilds shouldn't null");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			if (!HardDiskTypeValidation.isHardDiskTypeValid(hardDiskTypeMaster.getHardDiskType())) {

				logger.error("Enter Valid HardDiskType");

				Response response = new Response();

				response.setMessage("Enter Valid HardDiskType");
				response.setStatus("422");
			}

			hardDiskTypeMaster.setCreatedBy(employeeid);
			Boolean b = hardDiskTypeMasterServiceImpl.insertHardDisktTpe(hardDiskTypeMaster);

			logger.info("HardDiskType is added or created");

			if (b) {
				Response response = new Response();
				response.setMessage("HardDiskType registered successfully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {

				Response response = new Response();
				response.setMessage("HardDiskType not registered successfully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);

			}

		} catch (DataIntegrityViolationException exception) {
			logger.error(exception);
			Response response = new Response();
			response.setMessage("HardDiskType" + " alreay exists");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			Response response = new Response();
			logger.debug("inside catch block " + e.getMessage());

			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}
	}

	// 2.Getting all HardDiskTypeMaster records
	@GetMapping("/getallharddisktype")
	public ResponseEntity<?> getHardDiskTypeList() {

		try {
			List<HardDiskTypeMaster> listOfHardDiskTypes = hardDiskTypeMasterServiceImpl.getListOfHardDiskTypes();

			logger.info("getting list of records" + listOfHardDiskTypes);

			return new ResponseEntity<>(listOfHardDiskTypes, HttpStatus.OK);

		} catch (Exception e) {

			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	// 3.Getting all HardDiskTypeMaster records By Pagination
	@GetMapping(path = { "/{pageNo}/{sizePerPage}/{searchByHardDiskType}" })
	public ResponseEntity<?> getHardDiskTypeList(
			@PathVariable(name = "searchByHardDiskType", required = false) String searchByHardDiskType,
			@PathVariable("pageNo") Integer pageNo, @PathVariable("sizePerPage") Integer sizePerPage) {

		if (searchByHardDiskType.equalsIgnoreCase("null") || searchByHardDiskType.equalsIgnoreCase("undefined")
				|| searchByHardDiskType.equalsIgnoreCase("NaN") || searchByHardDiskType.equalsIgnoreCase(" ")) {
			searchByHardDiskType = null;
		}

		if (pageNo == null || sizePerPage == null) {
			logger.error("Page num and page size are null  in post");
			Response response = new Response();

			response.setMessage("page number and size per page shouldnot null");
			response.setStatus("400");

			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			ResponseList listOfHardDiskTypes = null;

			if (searchByHardDiskType == null) {
				listOfHardDiskTypes = hardDiskTypeMasterServiceImpl.getListOfHardDiskTypes(pageNo, sizePerPage);
			}

			else {
				listOfHardDiskTypes = hardDiskTypeMasterServiceImpl.getListOfHardDiskTypes(searchByHardDiskType, pageNo,
						sizePerPage);
			}

			logger.info("getting list of records" + listOfHardDiskTypes);

			return new ResponseEntity<>(listOfHardDiskTypes, HttpStatus.OK);
		} catch (Exception e) {

			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	// 4.Getting HardDiskTypeMaster record by its Id
	@GetMapping("getHardDiskTypeById/{hardDiskId}")
	public ResponseEntity<?> getHardDiskTypeById(@PathVariable Integer hardDiskId) {

		try {

			if (hardDiskId == null) {
				logger.error("HradDiskTypeId is null in getMapping");

				Response response = new Response();

				response.setMessage("HardDsikTypeId shouldn't null ");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

			}

			HardDiskTypeMaster hardDiskTypeMaster = hardDiskTypeMasterServiceImpl.getHardDiskTypeById(hardDiskId);

			if (hardDiskTypeMaster == null) {
				throw new RecordNotFoundException("record is not present");
			}

			return new ResponseEntity<>(hardDiskTypeMaster, HttpStatus.OK);

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

	// 5.Getting HardDiskTypeMaster record by its Type
	@GetMapping("getHardDiskTypeByType/{hardDiskType}")
	public ResponseEntity<?> getHardDiskTypeByType(@PathVariable String hardDiskType) {

		try {

			if (hardDiskType.isEmpty()) {
				logger.error("HradDiskType is null in getMapping");

				Response response = new Response();

				response.setMessage("HardDsikType shouldn't null ");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

			}

			HardDiskTypeMaster hardDiskTypeMasterObj = hardDiskTypeMasterServiceImpl
					.getHardDiskTypeByType(hardDiskType);

			if (hardDiskTypeMasterObj == null) {
				throw new RecordNotFoundException("record is not present");
			}

			return new ResponseEntity<>(hardDiskTypeMasterObj, HttpStatus.OK);

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

	// 6.Getting all HardDiskTypeMaster records by its Status
	@GetMapping("getHardDiskTypeByStatus/{hardDiskStatus}")
	public ResponseEntity<?> getVendorByName(@PathVariable("hardDiskStatus") Boolean hardDiskStatus) {

		try {

			if (hardDiskStatus == null) {

				logger.error("HardDiskType status is null  in getHardDiskType By Status");

				Response response = new Response();

				response.setMessage("HardDsikStatus shouldn't null ");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			List<HardDiskTypeMaster> list = hardDiskTypeMasterServiceImpl.getHardDiskTypeByStatus(hardDiskStatus);

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

	// 7.Deleting HardDiskTypeMaster record by its Id
	@DeleteMapping("deleteharddisktype/{hardDiskId}")
	@ResponseBody
	public ResponseEntity<?> deleteharddisktypeById(@PathVariable Integer hardDiskId) {

		try {
			if (hardDiskId == null) {
				logger.error("HardDsikTypeId is null in DeleteMapping");

				Response response = new Response();

				response.setMessage("HardDsikId shouldn't null for while Deleting ");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

			}

			HardDiskTypeMaster hardDiskTypeMasterObj = hardDiskTypeMasterServiceImpl.getHardDiskTypeById(hardDiskId);

			if (hardDiskTypeMasterObj == null) {
				throw new RecordNotFoundException("record is not present");
			}

			hardDiskTypeMasterServiceImpl.deleteHardDiskTypeById(hardDiskId);

			logger.info("HardDiskType is deleted" + hardDiskId);

			Response response = new Response();
			response.setMessage("HardDiskType deleted successfully");
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

	// 8.Updating HardDiskTypeMaster record by its Id
	@PutMapping("/{employeeid}")
	public ResponseEntity<?> updateHardDiskType(@RequestBody HardDiskTypeMaster hardDiskTypeMaster,
			@PathVariable("employeeid") String employeeid) {

		try {
			if (hardDiskTypeMaster == null) {

				logger.error("HarddiskType Object is null in post");

				Response response = new Response();

				response.setMessage("request object shouldn't null ");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

			}

			if (hardDiskTypeMaster.getHardDiskTypeId() == null || hardDiskTypeMaster.getHardDiskType().isEmpty()
					|| hardDiskTypeMaster.getHardDiskStatus() == null) {

				logger.error("fields of HardDisktype Object are null in post");

				Response response = new Response();

				response.setMessage("request object feilds shouldn't null");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			if (!HardDiskTypeValidation.isHardDiskTypeValid(hardDiskTypeMaster.getHardDiskType())) {

				logger.error("Enter Valid HardDiskType");

				Response response = new Response();

				response.setMessage("Enter Valid HardDiskType");
				response.setStatus("422");
			}

			HardDiskTypeMaster hardDiskTypeMasterObj = hardDiskTypeMasterServiceImpl
					.getHardDiskTypeById(hardDiskTypeMaster.getHardDiskTypeId());

			if (hardDiskTypeMasterObj == null) {
				throw new RecordNotFoundException("record is not present");
			}

			hardDiskTypeMaster.setUpdatedBy(employeeid);
			Boolean b = hardDiskTypeMasterServiceImpl.updateHardDiskType(hardDiskTypeMaster);

			logger.info("HardDiskType is added or created");

			if (b) {
				Response response = new Response();
				response.setMessage("HardDiskType Updated successfully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {

				Response response = new Response();
				response.setMessage("HardDiskType not Updated successfully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);

			}

		} catch (DataIntegrityViolationException exception) {
			Response response = new Response();
			logger.error(exception);
			response.setMessage("HardDiskType" + " alreay exists");
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

	@GetMapping("/harddiskTypeformat/download")
	public ResponseEntity<?> hardDiskcapacityFormatDownload(HttpServletResponse httpServletResponse) {

		try {
			httpServletResponse.setContentType("application/octet-stream");
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename=HardDiskType.xlsx");

			ByteArrayInputStream listToExcelFile = hardDiskExcel.dummyHardDiskTypeExcelFile();
			long copy = IOUtils.copy(listToExcelFile, httpServletResponse.getOutputStream());
			Response response = new Response();
			response.setMessage("Successfully Excel file is Downloading");
			response.setStatus("200");
			return new ResponseEntity<>(copy, HttpStatus.OK);

		} catch (IOException e) {
			logger.error(e);
			Response response = new Response();
			response.setMessage("Input and OutPut stream Exception : " + e.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception e) {
			logger.error(e);
			Response response = new Response();
			response.setMessage("Exception caught : " + e.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/updload/hardDiskType/{employeeid}")
	public ResponseEntity<?> uploadAssetTypeFile(@RequestParam("file") MultipartFile file,
			@PathVariable("employeeid") String employeeid) {
		Response response = new Response();
		if (hardDiskExcel.hasExcelFormat(file)) {
			try {
				hardDiskTypeMasterServiceImpl.save(file, employeeid);
				response.setMessage("File Uploaded SuccessFully : " + file.getOriginalFilename());
				response.setStatus("200");
				logger.info("Successfully Uploaded the HardDiskType Excel file");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} catch (DataIntegrityViolationException e) {
				logger.error(e);
				response.setMessage("Duplicate Entry of HardDiskType");
				response.setStatus("409");
				e.printStackTrace();
				logger.debug("Duplicate Entry of HardDiskType");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (RecordNotFoundException e) {
				logger.debug("IOException Caught : " + e.getMessage());
				e.printStackTrace();
				response.setMessage("RecordNotFound Exception  : " + e.getMessage() + "!");
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

	@GetMapping("/download/HardDiskTypeDummyExcelFile")
	public ResponseEntity<?> dummyfile(HttpServletResponse httpServletResponse) {
		Response response = new Response();
		try {
			httpServletResponse.setContentType("application/octet-stream");
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename=HardDiskTypeFormatExcel.xlsx");

			ByteArrayInputStream listToExcelFile = hardDiskExcel.dummyHardDiskTypeExcelFile();
			long copy = IOUtils.copy(listToExcelFile, httpServletResponse.getOutputStream());

			logger.info("Successfully HardDiskType Dummy Excel file is Downloading");
			response.setMessage("Successfully HardDiskType Dummy Excel file is Downloading");
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

	// 3.Getting all HardDiskTypeMaster records By Pagination
	@GetMapping(path = { "/{pageNo}/{pageSize}/{sortByDate}/{searchByHardDiskType}" })
	public ResponseEntity<?> getHardDiskTypeList(
			@PathVariable(name = "searchByHardDiskType", required = false) String searchByHardDiskType,
			@PathVariable(required = true, name = "sortByDate") String sortByDate,
			@PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer sizePerPage) {

		if (searchByHardDiskType.equalsIgnoreCase("null") || searchByHardDiskType.equalsIgnoreCase("undefined")
				|| searchByHardDiskType.equalsIgnoreCase("NaN") || searchByHardDiskType.equalsIgnoreCase(" ")) {
			searchByHardDiskType = null;
		}

		if (pageNo == null || sizePerPage == null) {
			logger.error("Page num and page size are null  in post");
			Response response = new Response();

			response.setMessage("page number and size per page shouldnot null");
			response.setStatus("400");

			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			ResponseList listOfHardDiskTypes = null;

			if (searchByHardDiskType == null && sortByDate.equals("lastmodefiedDate")) {
				listOfHardDiskTypes = hardDiskTypeMasterServiceImpl.getListOfHardDiskTypes(pageNo, sizePerPage,
						sortByDate);
			}

			else {
				listOfHardDiskTypes = hardDiskTypeMasterServiceImpl.getListOfHardDiskTypes(searchByHardDiskType, pageNo,
						sizePerPage);
			}

			logger.info("getting list of records" + listOfHardDiskTypes);

			return new ResponseEntity<>(listOfHardDiskTypes, HttpStatus.OK);
		} catch (Exception e) {

			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}
}