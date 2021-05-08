package com.Inventory.Project.AssectService.Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
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

import com.Inventory.Project.AssectService.Dao.VendorRepository;
import com.Inventory.Project.AssectService.Exceldata.VenderExcelData;
import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.ResourceNotFoundException;
import com.Inventory.Project.AssectService.Model.Vendor;
import com.Inventory.Project.AssectService.Response.Response;
import com.Inventory.Project.AssectService.Response.ResponseList;
import com.Inventory.Project.AssectService.Service.VendorService;

@RestController
@RequestMapping("/vendorservice")
public class VendorController {

	private static final Logger logger = LogManager.getLogger(VendorController.class);

	@Autowired
	private VendorService vendorService;

	@Autowired
	private VenderExcelData vendorexcel;

	@Autowired
	private VendorRepository vendorRepo;

	@PostMapping("/{employeeId}")

	public ResponseEntity<Object> saveVendor(@RequestBody Vendor vendor,
			@PathVariable("employeeId") String employeeId) {

		try {
			if (vendor == null) {
				logger.error("vendor object is null  in post");
				Response response = new Response();

				response.setMessage("request object shouldn't null ");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			if (vendor.getVendorName() == null || vendor.getVendorName().isEmpty() || vendor.getEmail() == null
					|| vendor.getEmail().isEmpty() || vendor.getGstNumber() == null || vendor.getContactNumber() == null
					|| vendor.getContactNumber().isEmpty() || vendor.getCityname() == null
					|| vendor.getCityname().isEmpty()) {
				logger.error("feilds of vendor object is null in post ");
				Response response = new Response();

				response.setMessage("request object feilds shouldn't null ");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

			}

//		vendorService.insertVendor(vendor);

			vendor.setCreatedBy(employeeId);

			Boolean b = vendorService.insertVendor(vendor);

			logger.info("vendor is added or created");

			if (b) {
				Response response = new Response();
				response.setMessage("vendor registered successfully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {

				Response response = new Response();
				response.setMessage("vendor not registered successfully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);

			}

		}

		catch (DataIntegrityViolationException exception) {
			Response response = new Response();
			logger.error(exception);
			String constraintName = null;
			if ((exception.getCause() != null) && (exception.getCause() instanceof ConstraintViolationException)) {
				constraintName = ((ConstraintViolationException) exception.getCause()).getConstraintName();
			}
			logger.debug("inside catch block " + exception.getMessage());
			if (constraintName.equals("vendor.uniqueEmail")) {
				response.setMessage("Email" + " alreay exists");
			}

			if (constraintName.equals("vendor.uniqueGstNumber")) {
				response.setMessage("Gst Number" + " alreay exists");
			}
			if (constraintName.equals("vendor.uniquePhoneNumber")) {
				response.setMessage("phone Number" + " alreay exists");
			}

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

	@GetMapping
	public ResponseEntity<?> getAll() {
		try {
			logger.info("getting list of records");
			List<Vendor> list = vendorService.getAllVendor();

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

	@PutMapping("/{employeeId}")
	public ResponseEntity<?> upateVendor(@RequestBody Vendor vendor, @PathVariable("employeeId") String employeeId) {
		try {
			if (vendor == null) {
				logger.error("vendor object is null  in post");
				Response response = new Response();

				response.setMessage("request object shouldn't null ");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			if (vendor.getVendorName() == null || vendor.getVendorName().isEmpty() || vendor.getEmail() == null
					|| vendor.getEmail().isEmpty() || vendor.getGstNumber() == null || vendor.getContactNumber() == null
					|| vendor.getContactNumber().isEmpty() || vendor.getCityname() == null
					|| vendor.getCityname().isEmpty()) {
				logger.error("feilds of vendor object is null in post ");
				Response response = new Response();
				response.setMessage("request object feilds shouldn't null ");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

			}

			Vendor vendor2 = vendorService.getVendorById(vendor.getVendorId());

			if (vendor2 == null) {
				throw new ResourceNotFoundException("record is not present");
			}

			vendor.setUpdatedBy(employeeId);
			Boolean boolean1 = vendorService.upateVendor(vendor);
			if (boolean1) {
				Response response = new Response();
				response.setMessage("vendor updated successfully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {

				Response response = new Response();
				response.setMessage("vendor not updated successfully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);

			}

		} catch (ResourceNotFoundException e) {
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("No records found");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (DataIntegrityViolationException exception) {
			Response response = new Response();
			logger.error(exception);
			String constraintName = null;
			if ((exception.getCause() != null) && (exception.getCause() instanceof ConstraintViolationException)) {
				constraintName = ((ConstraintViolationException) exception.getCause()).getConstraintName();
			}
			logger.debug("inside catch block " + exception.getMessage());
			if (constraintName.equals("vendor.uniqueEmail")) {
				response.setMessage("Email already exists");
			}

			if (constraintName.equals("vendor.uniqueGstNumber")) {
				response.setMessage("Gst Number already exists");
			}
			if (constraintName.equals("vendor.uniquePhoneNumber")) {
				response.setMessage("phone Number already exists");
			}

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

	@GetMapping(path = { "/{pageNo}/{sizePerPage}/{searchByvendorName}" })
	public ResponseEntity<?> getVendorList(@PathVariable(name = "searchByvendorName") String searchByvendorName,
			@PathVariable("pageNo") Integer pageNo, @PathVariable("sizePerPage") Integer sizePerPage) {

		if (searchByvendorName.equalsIgnoreCase("null") || searchByvendorName.equalsIgnoreCase("undefined")) {
			searchByvendorName = null;
		}

		if (pageNo == null || sizePerPage == null) {
			logger.error("vendor object is null  in post");
			Response response = new Response();

			response.setMessage("page number and size per page shouldnot null");
			response.setStatus("400");

			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			ResponseList listOfVendors = null;

			if (searchByvendorName == null) {
				listOfVendors = vendorService.getListOfVendors(pageNo, sizePerPage);
			}

			else {
				listOfVendors = vendorService.getListOfVendors(searchByvendorName, pageNo, sizePerPage);
			}

			logger.info("getting list of records" + listOfVendors);

			return new ResponseEntity<>(listOfVendors, HttpStatus.OK);
		} catch (Exception e) {

			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getVendorById(@PathVariable("id") Integer id) {
		try {

			if (id == null) {

				logger.error("vendor id is null  in getMapping by id");

				Response response = new Response();

				response.setMessage("vendor id is null");
				response.setStatus("422");

				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			Vendor vendor = vendorService.getVendorById(id);
//
			if (vendor == null) {
				throw new ResourceNotFoundException("record is not present");
			}

			return new ResponseEntity<>(vendor, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();

			response.setMessage("No records found");
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

	@GetMapping("byname/{name}")
	public ResponseEntity<?> getVendorByName(@PathVariable("name") String name) {

		try {

			if (name == null) {

				logger.error("vendor status is null  in getVendor by name");

				Response response = new Response();

				response.setMessage("vendor name is null");
				response.setStatus("422");

				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			Vendor vendor = vendorService.getVendorByName(name);

			if (vendor == null) {
				throw new ResourceNotFoundException("record is not present");
			}

			return new ResponseEntity<>(vendor, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();

			response.setMessage("Exception caught");
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

	@GetMapping("bystatus/{status}")
	public ResponseEntity<?> getVendorByName(@PathVariable("status") Boolean status) {

		try {

			if (status == null) {

				logger.error("vendor status is null  in getvendor by status");

				Response response = new Response();

				response.setMessage("vendor name is null");
				response.setStatus("422");

				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			List<Vendor> list = vendorService.getVendorByStatus(status);

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

	@GetMapping("/download/vendorExcelfile")
	public ResponseEntity<?> getExcelData(HttpServletResponse httpServletResponse) {
		Response response = new Response();
		try {
			httpServletResponse.setContentType("application/octet-stream");
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename=Vendor.xlsx");
			List<Vendor> list = vendorRepo.findAll();
			ByteArrayInputStream listToExcelFile = vendorexcel.exportingVendorDataToExcelFile(list);
			long copy = IOUtils.copy(listToExcelFile, httpServletResponse.getOutputStream());

			logger.info("Successfully Vendor Excel file is Downloading");

			response.setMessage("Successfully Excel file is Downloading");
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

	@GetMapping("/download/dummyvendorexcelfile")
	public ResponseEntity<?> dummyfile(HttpServletResponse httpServletResponse) {
		Response response = new Response();
		try {
			httpServletResponse.setContentType("application/octet-stream");
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename = VendorFormatExcel.xlsx");
			/* List<AssetTypeMasterEx> list = assetRepo.findAll(); */
			ByteArrayInputStream listToExcelFile = vendorexcel.dummyVendorExcelFile();
			long copy = IOUtils.copy(listToExcelFile, httpServletResponse.getOutputStream());

			logger.info("Successfully Vendor Dummy Excel file is Downloading");
			response.setMessage("Successfully Vendor Dummy Excel file is Downloading");
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

	@PostMapping("/upload/VendorExcelFile/{employeeid}")
	public ResponseEntity<?> uploadAssetTypeFile(@RequestParam("file") MultipartFile file,
			@PathVariable("employeeid") String employeeid) {
		Response response = new Response();
		if (vendorexcel.hasExcelFormat(file)) {
			try {
				vendorService.save(file, employeeid);
				response.setMessage("File Uploaded SuccessFully : " + file.getOriginalFilename());
				response.setStatus("200");
				logger.info("Successfully Uploaded the Vendor Excel file");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} catch (DataIntegrityViolationException exception) {
				logger.error(exception);
				String constraintName = null;
				if ((exception.getCause() != null) && (exception.getCause() instanceof ConstraintViolationException)) {
					constraintName = ((ConstraintViolationException) exception.getCause()).getConstraintName();
				}
				logger.debug("inside catch block " + exception.getMessage());
				if (constraintName.equals("vendor.uniqueEmail")) {
					response.setMessage("Email" + " alreay exists");
				}

				if (constraintName.equals("vendor.uniqueGstNumber")) {
					response.setMessage("Gst Number" + " alreay exists");
				}
				if (constraintName.equals("vendor.uniquePhoneNumber")) {
					response.setMessage("phone Number" + " alreay exists");
				}

				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} catch (FeildsShouldNotBeEmptyException e) {
				logger.debug("FeildsShouldNotBeEmptyException  : " + e.getMessage());
				e.printStackTrace();
				response.setMessage("FeildsShouldNotBeEmptyException : " + e.getMessage() + "!");
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

	@DeleteMapping("/{id}")
	@ResponseBody
	public Map<String, Boolean> deleteVendorById(@PathVariable("id") Integer id) throws ResourceNotFoundException {

		Map<String, Boolean> response = vendorService.deleteVendorById(id);
//		logger.info("deleted vendor successfully" + id);

		return response;

	}

	@GetMapping(path = { "/{pageNo}/{pageSize}/{sortByDate}/{searchByvendorName}" })
	public ResponseEntity<?> getVendorList(@PathVariable(name = "searchByvendorName") String searchByvendorName,
			@PathVariable(required = true, name = "sortByDate") String sortByDate,
			@PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer sizePerPage) {

		if (searchByvendorName.equalsIgnoreCase("null") || searchByvendorName.equalsIgnoreCase("undefined")) {
			searchByvendorName = null;
		}

		if (pageNo == null || sizePerPage == null) {
			logger.error("vendor object is null  in post");
			Response response = new Response();

			response.setMessage("page number and size per page shouldnot null");
			response.setStatus("400");

			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			ResponseList listOfVendors = null;

			if (searchByvendorName == null && sortByDate.equals("lastmodefiedDate")) {
				listOfVendors = vendorService.getListOfVendors(pageNo, sizePerPage, sortByDate);
			}

			else {
				listOfVendors = vendorService.getListOfVendors(searchByvendorName, pageNo, sizePerPage);
			}

			logger.info("getting list of records" + listOfVendors);

			return new ResponseEntity<>(listOfVendors, HttpStatus.OK);
		} catch (Exception e) {

			e.printStackTrace();
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

}
