package com.Inventory.Project.AssectService.Assect;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Exception.AssetTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.BrandTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.HardDiskCapacityNotFoundException;
import com.Inventory.Project.AssectService.Exception.HardDiskNotFoundException;
import com.Inventory.Project.AssectService.Exception.ModelNotFoundException;
import com.Inventory.Project.AssectService.Exception.RamCapacityNotFoundException;
import com.Inventory.Project.AssectService.Exception.RamTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Exception.ResourceNotFoundException;
import com.Inventory.Project.AssectService.Model.Brand;
import com.Inventory.Project.AssectService.Model.HardDiskCapacity;
import com.Inventory.Project.AssectService.Model.HardDiskTypeMaster;
import com.Inventory.Project.AssectService.Model.Model;
import com.Inventory.Project.AssectService.Model.RamCapacityMaster;
import com.Inventory.Project.AssectService.Model.RamTypeMaster;
import com.Inventory.Project.AssectService.Model.Vendor;
import com.Inventory.Project.AssectService.Pagination.PagenationResponse;
import com.Inventory.Project.AssectService.Response.Response;
import com.Inventory.Project.AssectService.Response.ResponseList;

@RestController
@RequestMapping(value = "/Assectservice")
public class AssectController {

	// private static final Log log = LogManager.getLogger(AssectController.class);
	private static final Logger log = LogManager.getLogger(AssectController.class);
	@Autowired
	private AssectService assectservice;
	@Autowired
	private Response response;

	@Autowired
	AssectDao assetdao;

	@Autowired
	CsvFileExporter csvfileexport;

	@Autowired
	ExcelFileExport excelfileexport;

	@PostMapping(value = "/addAssect/{employeeid}")
	public ResponseEntity<?> addAssect(@RequestBody AssectModelRequest assectModel,
			@PathVariable("employeeid") String employeeid) {
		try {
			if (assectModel == null) {
				log.error("AssectModel is Null");
				response.setMessage("Assect Model should not Be Empty");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (assectModel.getAssetType() == null || assectModel.getAssetType() == null
					|| assectModel.getBrand() == null || assectModel.getBrand() == null || assectModel.getCost() == null
					|| assectModel.getFloor() == null || assectModel.getFloor().isEmpty()
					// || assectModel.getHardDiskCapacity() == null ||
					// assectModel.getHardDiskCapacity() == null
					// || assectModel.getHardDiskType() == null || assectModel.getHardDiskType() ==
					// null
					|| assectModel.getInvoiceDoc() == null || assectModel.getLocation() == null
					|| assectModel.getLocation().isEmpty() || assectModel.getModels() == null
					|| assectModel.getModels() == null || assectModel.getPurchaseDate() == null
					|| assectModel.getPurchaseDCNo() == null || assectModel.getPurchaseDCNo().isEmpty()
					|| assectModel.getPurchaseInvoiceNo() == null || assectModel.getPurchaseInvoiceNo().isEmpty()
					// || assectModel.getRamCapacity() == null || assectModel.getRamCapacity() ==
					// null
					|| assectModel.getSerialNumber() == null || assectModel.getSerialNumber().isEmpty()
					// || assectModel.getTypeOfRam() == null || assectModel.getTypeOfRam() == null
					|| assectModel.getVendor() == null || assectModel.getVendor() == null
					|| assectModel.getWarantyDoc() == null || assectModel.getWarrentEndDate() == null
					|| assectModel.getWarrentStartDate() == null) {
				response.setMessage("Assect Fields should not Be Empty");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			System.out.println(assectModel.getNoOfHardDisks());
			assectModel.setCreatedBy(employeeid);
			assectModel.setCreatedOn(new Date());
			Boolean assect = assectservice.addAssect(assectModel);
			if (assect) {
				log.info("Added Assest SuccessFully");
				response.setMessage("Assect Added SuccessFully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				log.info("Failed To Added Assect");
				response.setMessage("Failed To assect");
				response.setStatus("417");
				return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
			}

		} catch (Exception exception) {

			exception.printStackTrace();

			log.debug("Inside the catch Block : " + exception.getMessage());
			response.setMessage("Exception caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/getall")
	public ResponseEntity<?> getAll() {
		try {

			log.info("get all assets");
			List<AssectModel> list = assectservice.getAllAssets();

			return new ResponseEntity<>(list, HttpStatus.OK);
		}

		catch (Exception e) {
			log.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}
	}

	@GetMapping(value = "/getAllAssects/{pageno}/{pagesize}/{sortByDate}/{text}")
	public ResponseEntity<?> getAllAssect(@PathVariable("pageno") Integer pageno,
			@PathVariable("pagesize") Integer pagesize, @PathVariable("text") String text,
			@PathVariable("sortByDate") String sortByDate) {
		try {
			ResponseList list = null;
			if (text.equalsIgnoreCase("null") || text.equalsIgnoreCase("undefined")) {
				text = null;
			}

			if (text == null && sortByDate.equalsIgnoreCase("lastmodifiedDate")) {
				list = assectservice.getAllAssect(pageno, pagesize, sortByDate);
			} else {
				list = assectservice.getAllAssectBySearch(text, pageno, pagesize);
			}

			if (list == null) {
				log.error("No Data Found");
				response.setMessage("PNo Data Found");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			return new ResponseEntity<>(list, HttpStatus.OK);

		} catch (ResourceNotFoundException exception) {
			log.error(exception);
			response.setMessage("No Data Found ");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception exception) {
			log.debug("Inside the catch Block : " + exception.getMessage());
			response.setMessage("Ecxeption caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}
//	@GetMapping(value = "/getAllAssects/{pageno}/{pagesize}/{text}")
//	public ResponseEntity<?> getAllAssect(@PathVariable(required = false, name = "text") String text,
//			@PathVariable("pageno") Integer pageno, @PathVariable("pagesize") Integer pagesize) {
//		try {
//			// ResponseList list = null;
//			if (text.equalsIgnoreCase("null") || text.equalsIgnoreCase("undefined")) {
//				text = null;
//			}
//
//			if (pageno == null || pagesize == null) {
//				Response response = new Response();
//				response.setMessage("bad request");
//				response.setStatus("422");
//				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//			}
//			ResponseList list = null;
//			if (text == null) {
//				list = assectservice.getAllAssect(pageno, pagesize);
//			} else {
//				list = assectservice.getAllAssectBySearch(text, pageno, pagesize);
//			}
//
//			/*
//			 * if (list == null) { log.error("No Data Found");
//			 * response.setMessage("PNo Data Found"); response.setStatus("422"); return new
//			 * ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY); }
//			 */
//			return new ResponseEntity<>(list, HttpStatus.OK);
//		} /*
//			 * catch (ResourceNotFoundException exception) { log.debug("No data Found");
//			 * response.setMessage("No Data Found "); response.setStatus("409"); return new
//			 * ResponseEntity<>(response, HttpStatus.CONFLICT); }
//			 */catch (Exception exception) {
//			exception.printStackTrace();
//			log.debug("Inside the catch Block : " + exception.getMessage());
//			response.setMessage("Ecxeption caught");
//			response.setStatus("409");
//			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
//		}
//	}
//    @GetMapping(value = "/getAllAssects/{pageno}/{pagesize}/{sortBy}")
//    public ResponseEntity<?> getAllAssect1(
//                            @RequestParam(defaultValue = "0") Integer pageNo, 
//                            @RequestParam(defaultValue = "10") Integer pageSize,
//                            @RequestParam(defaultValue = "id") String sortBy) 
//        {
//            ResponseList list = assectservice.getAllAssect(pageNo, pageSize, sortBy);
//            return new ResponseEntity<ResponseList>(list,HttpStatus.OK);
//        }
//
//	@GetMapping(value = "/getAllAssects/{pageno}/{pagesize}/{searchBy}")
//	public ResponseEntity<?> getAllAssect(@PathVariable("pageno") Integer pageno,
//			@PathVariable("pagesize") Integer pagesize, @PathVariable("searchBy") String searchBy
//
//	) {
//		try {
//			ResponseList list = null;
//			if (searchBy.equalsIgnoreCase("null") || searchBy.equalsIgnoreCase("undefined")) {
//				searchBy = null;
//			}
//
//			if (searchBy == null) {
//				list = assectservice.getAllAssect(pageno, pagesize);
//			} else {
//				list = assectservice.getAllAssect(pageno, pagesize, searchBy);
//			}
//
//			if (list == null) {
//				log.error("No Data Found");
//				response.setMessage("PNo Data Found");
//				response.setStatus("422");
//				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
//			}
//			return new ResponseEntity<>(list, HttpStatus.OK);
//
//		} catch (ResourceNotFoundException exception) {
//			log.debug("No data Found");
//			response.setMessage("No Data Found ");
//			response.setStatus("409");
//			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
//		} catch (Exception exception) {
//			log.debug("Inside the catch Block : " + exception.getMessage());
//			response.setMessage("Ecxeption caught");
//			response.setStatus("409");
//			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
//		}
//	}

	@GetMapping(value = "/getBrands")
	public ResponseEntity<?> getBrandNames() {
		try {
			List<Brand> list = assectservice.getBrandNames();
			if (list == null) {
				log.error("No data found");
				response.setMessage("No Data Found");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (ResourceNotFoundException exception) {
			log.error(exception);
			response.setMessage("No Data Found For Models");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception exception) {
			log.debug("Inside the catch Block : " + exception.getMessage());
			response.setMessage("Ecxeption caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping(value = "/getModels")
	public ResponseEntity<?> getModelNames() {
		try {
			List<Model> list = assectservice.getModelNames();
			if (list == null) {
				log.error("No data found");
				response.setMessage("No Data Found");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (ResourceNotFoundException exception) {
			log.error(exception);
			response.setMessage("No Data Found For Models");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception exception) {
			log.debug("Inside the catch Block : " + exception.getMessage());
			response.setMessage("Ecxeption caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

	@GetMapping(value = "/getVendors")
	public ResponseEntity<?> getVendors() {
		try {
			List<Vendor> list = assectservice.getVendors();
			if (list == null) {
				log.error("No data found");
				response.setMessage("No Data Found");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (ResourceNotFoundException exception) {
			log.error(exception);
			response.setMessage("No Data Found For Vendor");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception exception) {
			log.debug("Inside the catch Block : " + exception.getMessage());
			response.setMessage("Ecxeption caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

	@GetMapping(value = "/getHardDiskType")
	public ResponseEntity<?> getHardDiskType() {
		try {
			List<HardDiskTypeMaster> list = assectservice.getHardDiskType();
			if (list == null) {
				log.error("No data found");
				response.setMessage("No Data Found");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (ResourceNotFoundException exception) {
			log.debug("No Data Found For HardDiskType");
			log.error(exception);
			response.setMessage("No Data Found For HardDiskType");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception exception) {

			log.debug("Inside the catch Block : " + exception.getMessage());
			response.setMessage("Ecxeption caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

	@GetMapping(value = "/getHardDiskCapacity")
	public ResponseEntity<?> getHardDiskCapacity() {
		try {
			List<HardDiskCapacity> list = assectservice.getHardDiskCapacity();
			if (list == null) {
				log.error("No data found");
				response.setMessage("No Data Found");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (ResourceNotFoundException exception) {
			log.error(exception);
			response.setMessage("No Data Found For HardDiskCapacity");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception exception) {

			log.debug("Inside the catch Block : " + exception.getMessage());
			response.setMessage("Ecxeption caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

	@GetMapping(value = "/getRamType")
	public ResponseEntity<?> getRamType() {
		try {
			List<RamTypeMaster> list = assectservice.getRamType();
			if (list == null) {
				log.error("No data found");
				response.setMessage("No Data Found");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (ResourceNotFoundException exception) {
			log.error(exception);
			response.setMessage("No Data Found For RamType");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception exception) {
			log.debug("Inside the catch Block : " + exception.getMessage());
			response.setMessage("Ecxeption caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

	@GetMapping(value = "/getRamCapacity")
	public ResponseEntity<?> getRamCapacity() {
		try {
			List<RamCapacityMaster> list = assectservice.getRamCapacity();
			if (list == null) {
				log.error("No data found");
				response.setMessage("No Data Found");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (ResourceNotFoundException exception) {
			log.error(exception);
			response.setMessage("No Data Found For RamCapacity");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception exception) {
			log.debug("Inside the catch Block : " + exception.getMessage());
			response.setMessage("Ecxeption caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

//    @GetMapping(value = "/getAllAssects")
//    public ResponseEntity<?> getAllAssects() {
//        return assectservice.getAllAssects();
//    }

	@GetMapping(value = "/getByid/{assectid}")
	public ResponseEntity<?> getByAssectid(@PathVariable("assectid") Integer assectid) {
		try {
			if (assectid == null) {
				log.error("Entered Assect id is Null");
				response.setMessage("Please enter the assectid");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			AssectModel assectModel = assectservice.getByAssectid(assectid);
			return new ResponseEntity<>(assectModel, HttpStatus.OK);

		} catch (RecordNotFoundException exception) {
			log.error(exception);
			response.setMessage("No Data Found with this assectid : " + assectid);
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception exception) {
			log.debug("Inside the catch Block : " + exception.getMessage());
			response.setMessage("Ecxeption caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping(value = "/deleteByid/{assectid}")
	public Map<String, Boolean> deleteByAssectid(@PathVariable("assectid") Integer assectid) {
		try {
			if (assectid == null) {
				log.error("Entered Assect id is Null");

				return (Map<String, Boolean>) new HashMap<>().put("id is null", false);
			}
			Map<String, Boolean> map = assectservice.deleteByAssectid(assectid);
			return map;
		} catch (RecordNotFoundException exception) {
			log.debug("No data Present With assectid");

			new HashMap<>().put("Record not found", false);
		} catch (Exception exception) {
			log.debug("Inside the catch Block : " + exception.getMessage());

			return (Map<String, Boolean>) new HashMap<>().put("Exception occured", false);
		}
		return null;
	}

	@PutMapping(value = "/updateByid/{employeeid}/{assectid}")
	public ResponseEntity<?> updateByid(@PathVariable("assectid") Long assectid,
			@RequestBody AssectModelRequest assectModel, @PathVariable("employeeid") String employeeid) {
		try {
			if (assectid == null || assectModel == null) {
				log.error("Assect Id are assectModel is Null");
				response.setMessage("Please do not Enter Null values");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
//            if (assectModel.getAssectType() == null || assectModel.getAssectType().isEmpty()
//                    || assectModel.getBrand() == null || assectModel.getBrand().isEmpty()
//                    || assectModel.getCost() == null || assectModel.getFloor() == null
//                    || assectModel.getFloor().isEmpty() || assectModel.getHardDiskCapacity() == null
//                    || assectModel.getHardDiskCapacity().isEmpty() || assectModel.getHardDiskType() == null
//                    || assectModel.getHardDiskType().isEmpty() || assectModel.getInvoiceDoc() == null
//                    || assectModel.getLocation() == null || assectModel.getLocation().isEmpty()
//                    || assectModel.getModels() == null || assectModel.getModels().isEmpty()
//                    || assectModel.getNoOfHardDisks() == null || assectModel.getNoOfRams() == null
//                    || assectModel.getPurchaseDate() == null || assectModel.getPurchaseDCNo() == null
//                    || assectModel.getPurchaseDCNo().isEmpty() || assectModel.getPurchaseInvoiceNo() == null
//                    || assectModel.getPurchaseInvoiceNo().isEmpty() || assectModel.getRamCapacity() == null
//                    || assectModel.getRamCapacity().isEmpty() || assectModel.getSerialNumber() == null
//                    || assectModel.getSerialNumber().isEmpty() || assectModel.getTypeOfRam() == null
//                    || assectModel.getTypeOfRam().isEmpty() || assectModel.getVendor() == null
//                    || assectModel.getVendor().isEmpty() || assectModel.getWarantyDoc() == null
//                    || assectModel.getWarrentEndDate() == null || assectModel.getWarrentStartDate() == null) {
//                response.setMessage("Assect Fields should not Be Empty");
//                response.setStatus("422");
//                return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
//            }
//            System.out.println(assectid);
//            System.out.println(assectModel.getVendor());
//            System.out.println(assectModel.getModels());
//            System.out.println(assectModel.getBrand());
//            System.out.println(assectModel.getAssetType());
//            System.out.println(assectModel.getRamCapacity());
//            System.out.println(assectModel.getTypeOfRam());
//            System.out.println(assectModel.getHardDiskCapacity());
//            System.out.println(assectModel.getHardDiskType());
			assectModel.setUpdateBy(employeeid);
			assectModel.setUpdatedOn(new Date());
			Boolean updateByid = assectservice.updateByid(assectid, assectModel);
			if (updateByid) {
				log.info("Assect Updated SuccessFully");
				response.setMessage("Assect Updated SuccessFully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				log.info("Failed to Update the Assect");
				response.setMessage("Failed to Update the Assect");
				response.setStatus("417");
				return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
			}
		} catch (com.Inventory.Project.AssectService.Exception.ResourceNotFoundException exception) {
			log.debug("No Data Found to Upadate");
			response.setMessage("No Data Found to update");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.debug("Exception caught : " + e.getMessage());
			e.printStackTrace();
			response.setMessage("Exception caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/download/AssetExcelFile")
	public ResponseEntity<?> getExcelData(HttpServletResponse httpServletResponse) {
		try {
			httpServletResponse.setContentType("application/octet-stream");
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename=Asset.xlsx");
			List<AssectModel> list = assetdao.findAll();
			ByteArrayInputStream listToExcelFile = excelfileexport.exportAssectListToExcelFile(list);
			long copy = IOUtils.copy(listToExcelFile, httpServletResponse.getOutputStream());

			response.setMessage("Successfully Excel file is Downloading");
			response.setStatus("200");
			return new ResponseEntity<>(copy, HttpStatus.OK);

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

	@GetMapping("/download/AssetCSVFile")
	public ResponseEntity<?> getCsvSData(HttpServletResponse httpServletResponse) {
		try {
			httpServletResponse.setContentType("application/csv");
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename=Asset.csv");
			List<AssectModel> list = assetdao.findAll();
			csvfileexport.getCsvData(httpServletResponse.getWriter(), list);
			response.setMessage("SuccessFully Downloading Csv File");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (IOException e) {
			log.error(e);
			response.setMessage("Input and OutPut stream Exception : " + e.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error(e);
			response.setMessage("Exception caught : " + e.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/download/dummyAssetExcelFile")
	public ResponseEntity<?> exportDummyAssectListToExcelFile(HttpServletResponse httpServletResponse) {
		try {
			httpServletResponse.setContentType("application/octet-stream");
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename=Asset.xlsx");

			ByteArrayInputStream listToExcelFile = excelfileexport.exportDummyAssectListToExcelFile();
			long copy = IOUtils.copy(listToExcelFile, httpServletResponse.getOutputStream());

			response.setMessage("Successfully Excel file is Downloading");
			response.setStatus("200");
			return new ResponseEntity<>(copy, HttpStatus.OK);
		} catch (IOException e) {
			log.error(e);
			response.setMessage("Input and OutPut stream Exception : " + e.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Exception caught : " + e.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/Upload/assetType/{employeeid}")
	public ResponseEntity<?> uploadAssetTypeFile(@RequestParam(value = "file") MultipartFile file,
			@PathVariable("employeeid") String employeeid) {
		Response response = new Response();
		if (excelfileexport.hasExcelFormat(file)) {
			try {
				assectservice.save(file, employeeid);
				response.setMessage("File Uploaded SuccessFully : " + file.getOriginalFilename());
				response.setStatus("200");
				log.info("Successfully Uploaded the AssetType Excel file");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} catch (AssetTypeNotFoundException e) {
				log.error(e);
				response.setMessage(e.getLocalizedMessage());
				response.setStatus("409");
				e.printStackTrace();
				log.debug("Asset type not found");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}

			catch (BrandTypeNotFoundException e) {
				log.error(e);
				response.setMessage(e.getLocalizedMessage());
				response.setStatus("409");
				e.printStackTrace();
				log.debug("Brand type not found");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (ModelNotFoundException e) {
				log.error(e);
				response.setMessage(e.getLocalizedMessage());
				response.setStatus("409");
				e.printStackTrace();
				log.debug("model type not found");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (HardDiskNotFoundException e) {
				log.error(e);
				response.setMessage(e.getLocalizedMessage());
				response.setStatus("409");
				e.printStackTrace();
				log.debug("harddisk type not found");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (HardDiskCapacityNotFoundException e) {
				log.error(e);
				response.setMessage(e.getLocalizedMessage());
				response.setStatus("409");
				e.printStackTrace();
				log.debug("hard disk capacity not found");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (RamTypeNotFoundException e) {
				log.error(e);
				response.setMessage(e.getLocalizedMessage());
				response.setStatus("409");
				e.printStackTrace();
				log.debug("Ram type not found");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (RamCapacityNotFoundException e) {
				log.error(e);
				response.setMessage(e.getLocalizedMessage());
				response.setStatus("409");
				e.printStackTrace();
				log.debug("ram capacity not found");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (RecordNotFoundException e) {
				log.error(e);
				response.setMessage(e.getLocalizedMessage());
				response.setStatus("409");
				e.printStackTrace();
				log.debug("Feilds should not be empty");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}

			catch (FeildsShouldNotBeEmptyException e) {
				log.error(e);
				response.setMessage(e.getLocalizedMessage());
				response.setStatus("409");
				e.printStackTrace();
				log.debug("Feilds should not be empty");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}

			catch (DataIntegrityViolationException e) {
				response.setMessage("Duplicate Entry of Serial Number");
				response.setStatus("409");
				e.printStackTrace();
				log.debug("Duplicate Entry of Serial number");
				log.error(e);
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (Exception e) {
				log.debug("IOException Caught : " + e.getMessage());
				e.printStackTrace();
				response.setMessage("Could not upload the file exception Caught : " + file.getOriginalFilename() + "!");
				response.setStatus("409");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}
		}
		log.debug("Please Upload Only Excel file");
		response.setMessage("Please upload ExcelFile");
		response.setStatus("400");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

	@GetMapping(path = { "/asset/sort/{pageNo}/{pageSize}/{searchBy}" })

	public ResponseEntity<?> getAllHardDiskCapacity(@PathVariable(required = false, name = "searchBy") String searchBy,
			@PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer pageSize) {

		if (pageNo == null || pageSize == null) {
			// logger.error("HardDiskCacity records are null");
			Response response = new Response();

			response.setMessage("Page number And pageSize Connot be null");
			response.setStatus("400");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {

			ResponseList hardDiskCapacityList = null;

			PagenationResponse allHardDiskCapacityList1 = assectservice.getAllAssetData(pageNo, pageSize, searchBy);

			if (allHardDiskCapacityList1 == null) {
				throw new RecordNotFoundException("No Data Present in the Asset");

			}
			// }

			// logger.info("getting list of Objects" + allHardDiskCapacityList1);

			return new ResponseEntity<>(allHardDiskCapacityList1, HttpStatus.OK);

		} catch (RecordNotFoundException e) {

			// logger.debug("Inside Catch" + e.getMessage());

			Response response = new Response();
			response.setMessage("Exception catch");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		} catch (Exception e) {

			// logger.debug("Inside Catch" + e.getMessage());

			Response response = new Response();
			response.setMessage("Exception catch");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}

	}

}