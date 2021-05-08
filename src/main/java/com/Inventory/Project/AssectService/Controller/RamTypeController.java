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

import com.Inventory.Project.AssectService.Exceldata.RamTypeExcelData;
import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Model.RamTypeMaster;
import com.Inventory.Project.AssectService.Pagination.PagenationResponse;
import com.Inventory.Project.AssectService.Pagination.RamTypeDaoImpl;
import com.Inventory.Project.AssectService.Response.Response;
import com.Inventory.Project.AssectService.Response.ResponseList;
import com.Inventory.Project.AssectService.Service.RamTypeService;

@RestController
@RequestMapping("/ramtypemaster")
public class RamTypeController {

	@Autowired
	private RamTypeService ramTypeService;

	@Autowired
	private RamTypeExcelData ramTypeExcel;

	@Autowired
	private RamTypeDaoImpl ramTypeDaoImpl;
	private static final Logger logger = LogManager.getLogger(RamTypeController.class);

	// Store the RamCapacity Data into DataBase
	@PostMapping("/{employeeId}")
	public ResponseEntity<?> saveRamData(@RequestBody RamTypeMaster master,
			@PathVariable("employeeId") String employeeId) {

		try {
			if (master == null) {
//                    logger.error("ramMaster object is null  in post");
				Response response = new Response();

				response.setMessage("request object shouldn't null ");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (master.getRamtypeName().isEmpty() || master.getRamtypeName() == null) {
//                    logger.info("Null values are not allowed");
				Response response = new Response();
				response.setMessage("request object feilds shouldn't null ");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			master.setCreatedBy(employeeId);
			Boolean boolean1 = ramTypeService.saveRamDeatils(master);
//                logger.info("RamMaster is added or created");
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
			logger.error(exception);
			Response response = new Response();

			response.setMessage("Duplicate values not allowed");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		catch (Exception e) {
			Response response = new Response();
//                logger.debug("inside catch block " + e.getMessage());
			logger.error(e);
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}

	}

	@GetMapping("/getallramtype")
	public ResponseEntity<?> getHardDiskTypeList() {

		try {
			List<RamTypeMaster> listOfRamType = ramTypeService.getListOfRamType();

			return new ResponseEntity<>(listOfRamType, HttpStatus.OK);

		} catch (Exception e) {
			logger.error(e);
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping(path = { "/{pageNo}/{sizePerPage}/{searchByRamTypeName}" })
	public ResponseEntity<?> getAllRaminfo(
			@PathVariable(required = false, name = "searchByRamTypeName") String searchByRamTypeName,
			@PathVariable("pageNo") Integer pageNo, @PathVariable("sizePerPage") Integer sizePerPage) {
		if (searchByRamTypeName.equalsIgnoreCase("null") || searchByRamTypeName.equalsIgnoreCase("undefined")) {
			searchByRamTypeName = null;
		}
		if (pageNo == null || sizePerPage == null) {
//                logger.error("Ram object is null  in post");
			Response response = new Response();

			response.setMessage("page number and size per page shouldnot null");
			response.setStatus("400");

			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			ResponseList listOfRams = null;

			if (searchByRamTypeName == null) {
				listOfRams = ramTypeService.getAllRamDetails(pageNo, sizePerPage);
			}

			else {
				listOfRams = ramTypeService.getAllRamDetails(searchByRamTypeName, pageNo, sizePerPage);
			}

//                logger.info("getting list of records" + listOfRams);

			return new ResponseEntity<>(listOfRams, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e);
//                logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	// Get RamCapacity details By Id //
	@GetMapping("/{id}")
	public ResponseEntity<?> getRamById(@PathVariable("id") Integer id) throws RecordNotFoundException {
		try {
			if (id == null) {
//                    logger.error("Ram id is null");
				Response response = new Response();
				response.setMessage("ram id is null");
				response.setStatus("422");

				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			RamTypeMaster ram = ramTypeService.getRamDetailsById(id);
			if (ram == null) {
				throw new RecordNotFoundException("No record exist for given id");
			}
			return new ResponseEntity<>(ram, HttpStatus.OK);
		} catch (RecordNotFoundException e) {
//                logger.debug("inside catch block " + e.getMessage());
			logger.error(e);
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

		catch (Exception e) {
//                logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}
	}

	// Updating the RamCapacity ById //
	@PutMapping("/{employeeId}")
	public ResponseEntity<?> createOrUpdateRamDetailsById(@PathVariable("employeeId") String employeeId,
			@RequestBody RamTypeMaster master) throws RecordNotFoundException {
		try {
			if (master == null) {
//                    logger.error("ramMaster object is null  in post");
				Response response = new Response();

				response.setMessage("request object shouldn't null ");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (master.getRamtypeName().isEmpty() || master.getRamtypeName() == null) {
//                    logger.info("Null values are not allowed");
				Response response = new Response();
				response.setMessage("request object feilds shouldn't null ");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			RamTypeMaster ram = ramTypeService.getRamDetailsById(master.getRamtypeId());

			if (ram == null) {
				throw new RecordNotFoundException("record is not present");
			}

			master.setUpdatedBy(employeeId);
			Boolean boolean1 = ramTypeService.updateRamDetailsById(master);
			if (boolean1) {
				Response response = new Response();
				response.setMessage("RamMaster updated successfully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {

				Response response = new Response();
				response.setMessage("RamMaster not updated");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);

			}

		} catch (RecordNotFoundException e) {
//                logger.debug("inside catch block " + e.getMessage());
			logger.error(e);
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (DataIntegrityViolationException exception) {
			logger.error(exception);
			Response response = new Response();
			response.setMessage("Duplicate values not allowed");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		catch (Exception e) {
			Response response = new Response();
			logger.error(e);
//                logger.debug("inside catch block " + e.getMessage());

			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}

	}

	// Get RamCapacity details By Status //
	@GetMapping("bystatus/{status}")
	public ResponseEntity<?> getRamByStatus(@PathVariable("status") Boolean status) {

		try {

			if (status == null) {

//                    logger.error("Ram status is null");

				Response response = new Response();
				response.setMessage("Ram name is null");
				response.setStatus("422");

				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			List<RamTypeMaster> list = ramTypeService.getRamDetailsByStatus(status);

			return new ResponseEntity<>(list, HttpStatus.OK);
		}

		catch (Exception e) {
//                logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			logger.error(e);
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}

	}

	// Deleting the RamCapacity By Id //
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> deleteRamDetailsById(@PathVariable("id") Integer id) throws RecordNotFoundException {
		try {
			if (id == null) {
//                    logger.error("Id is null in DeleteMapping");

				Response response = new Response();

				response.setMessage("Id shouldn't null for while Deleting ");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

			}

			RamTypeMaster master = ramTypeService.getRamDetailsById(id);

			if (master == null) {
				throw new RecordNotFoundException("record is not present");
			}

			ramTypeService.deleteByRamId(id);

//                logger.info("RamMaster is deleted");

			Response response = new Response();
			response.setMessage("RamMaster deleted successfully");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (RecordNotFoundException e) {
//                logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			logger.error(e);
			response.setMessage("No records found");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		} catch (Exception e) {
			logger.error(e);
			Response response = new Response();
//                logger.debug("inside catch block " + e.getMessage());

			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}

	}

	@GetMapping("/download/ramtype/Dummyfile")
	public ResponseEntity<?> dummyfile(HttpServletResponse httpServletResponse) {
		Response response = new Response();
		try {
			httpServletResponse.setContentType("application/octet-stream");
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename = RamTypeFormatExcel.xlsx");
			/* List<AssetTypeMasterEx> list = assetRepo.findAll(); */
			ByteArrayInputStream listToExcelFile = ramTypeExcel.dummyRamTypeMasteExcelFile();
			long copy = IOUtils.copy(listToExcelFile, httpServletResponse.getOutputStream());

			response.setMessage("Successfully RamType Dummy Excel file is Downloading");
			response.setStatus("200");
			return new ResponseEntity<>(copy, HttpStatus.OK);

		}

		catch (IOException e) {

			logger.error(e);
			e.printStackTrace();
			response.setMessage("Input and OutPut stream Exception : " + e.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			response.setMessage("Exception caught : " + e.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/Upload/RamType/{employeeid}")
	public ResponseEntity<?> uploadAssetTypeFile(@RequestParam("file") MultipartFile file,
			@PathVariable("employeeid") String employeeid) {
		Response response = new Response();
		if (ramTypeExcel.hasExcelFormat(file)) {
			try {
				ramTypeService.save(file, employeeid);
				response.setMessage("File Uploaded SuccessFully : " + file.getOriginalFilename());
				response.setStatus("200");

				return new ResponseEntity<>(response, HttpStatus.OK);
			} catch (DataIntegrityViolationException e) {
				logger.error(e);
				response.setMessage("Duplicate Entry of RamType");
				response.setStatus("409");
				e.printStackTrace();

				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (FeildsShouldNotBeEmptyException e) {
				e.printStackTrace();
				response.setMessage("FeildsShouldNotBeEmptyException : " + e.getMessage() + "!");
				response.setStatus("409");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (RecordNotFoundException e) {
				e.printStackTrace();
				response.setMessage("RecordNotFoundException : " + e.getMessage() + "!");
				response.setStatus("409");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);

			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
				response.setMessage("Could not upload the file exception Caught : " + file.getOriginalFilename() + "!");
				response.setStatus("409");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}
		}
		response.setMessage("Please upload ExcelFile");
		response.setStatus("400");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

	@GetMapping(path = { "/{pageNo}/{pageSize}/{sortByDate}/{searchByRamTypeName}" })
	public ResponseEntity<?> getAllRaminfo(
			@PathVariable(required = false, name = "searchByRamTypeName") String searchByRamTypeName,
			@PathVariable(required = true, name = "sortByDate") String sortByDate,
			@PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer sizePerPage) {
		if (searchByRamTypeName.equalsIgnoreCase("null") || searchByRamTypeName.equalsIgnoreCase("undefined")) {
			searchByRamTypeName = null;
		}
		if (pageNo == null || sizePerPage == null) {
//                logger.error("Ram object is null  in post");
			Response response = new Response();

			response.setMessage("page number and size per page shouldnot null");
			response.setStatus("400");

			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			ResponseList listOfRams = null;

			if (searchByRamTypeName == null && sortByDate.equals("lastmodefiedDate")) {
				listOfRams = ramTypeService.getAllRamDetails(pageNo, sizePerPage, sortByDate);
			}

			else {
				listOfRams = ramTypeService.getAllRamDetails(searchByRamTypeName, pageNo, sizePerPage);
			}

//            logger.info("getting list of records" + listOfRams);

			return new ResponseEntity<>(listOfRams, HttpStatus.OK);
		} catch (Exception e) {

//            logger.debug("inside catch block " + e.getMessage());
			logger.error(e);
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/sort/{pagenumber}/{pagesize}/{searchBy}")
	public ResponseEntity<?> getAllRamTypes(@PathVariable("pagenumber") Integer pagenumber,
			@PathVariable("pagesize") Integer pagesize, @PathVariable("searchBy") String searchBy) {

		if (searchBy.equalsIgnoreCase("null") || searchBy.equalsIgnoreCase("undefined")) {
			searchBy = null;
		}
		if (pagenumber == null || pagesize == null) {

			logger.error("please Enter pageNumber and pageSize");
			Response response = new Response();

			response.setMessage("Pagenumber And pageSize Connot be null");
			response.setStatus("400");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		try {

			ResponseList responseList = null;
			PagenationResponse allRamTypes = ramTypeDaoImpl.getAllRamTypes(pagenumber, pagesize, searchBy);

			logger.info("RamType List Data : " + allRamTypes);

			return new ResponseEntity<>(allRamTypes, HttpStatus.OK);

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
