package com.Inventory.Project.AssectService.Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
///*import org.springframework.web.bind.annotation.CrossOrigin;*/
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

import com.Inventory.Project.AssectService.Dao.AssetTypeRepositry;
import com.Inventory.Project.AssectService.Exceldata.AssetTypeExcel;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Exception.ResourceNotFoundException;
import com.Inventory.Project.AssectService.Model.AssetTypeMaster;
import com.Inventory.Project.AssectService.Response.Response;
import com.Inventory.Project.AssectService.Response.ResponseList;
import com.Inventory.Project.AssectService.Service.AssetTypeServiceImpl;

@RestController
@RequestMapping("/rest/assettype")
public class AssetTypeController {

	private static final Logger logger = LogManager.getLogger(AssetTypeController.class);

	@Autowired
	private AssetTypeServiceImpl serv;

	@Autowired
	private AssetTypeExcel assetTypeexcel;

	@Autowired
	private AssetTypeRepositry assetRepo;

	@PostMapping("/{employeeId}")
	public ResponseEntity<?> saveModel(@RequestBody AssetTypeMaster atx,
			@PathVariable("employeeId") String employeeId) {
		try {
			if (atx == null) {
				logger.error("Asset Object is Null");
				Response response = new Response();
				response.setMessage("requested AssetService Object is Null");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (atx.getAssetType() == null || atx.getAssetType().isEmpty()) {
				logger.error("Asset object fields are Null");
				Response response = new Response();
				response.setMessage("Field should not be Null");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			atx.setCreatedBy(employeeId);
			Boolean service = serv.addAssetTypeService(atx);
			if (service) {
				logger.info("Asset is added successfully");
				Response response = new Response();
				response.setMessage("Asset is Inserted Successfully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				logger.error("Faild to add Model");
				Response response = new Response();
				response.setMessage("Failed To Add Model");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}

		} catch (DataIntegrityViolationException exception) {

			logger.error(exception);
			Response response = new Response();
			response.setMessage("Duplicate Entry of AssetType. AssetType is already in DB");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception exception) {
			Response response = new Response();
			// logger.debug("inside catch Block :" + exception.getMessage());
			logger.error(exception);
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/getallassettype")
	public ResponseEntity<?> getAssectType() {
		try {
			List<AssetTypeMaster> list = serv.getAssectType();
			if (list == null) {
				Response response = new Response();
//				log.error("No data found");
				response.setMessage("No Data Found");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception exception) {
			logger.error(exception);
			Response response = new Response();
//			log.debug("Inside the catch Block : " + exception.getMessage());
			response.setMessage("Ecxeption caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/{employeeId}")
	public ResponseEntity<?> update(@PathVariable("employeeId") String employeeId, @RequestBody AssetTypeMaster atx) {
		try {
			if (atx == null) {
				logger.error("Asset Object is Null");
				Response response = new Response();
				response.setMessage("requested AssetService Object is Null");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (atx.getAssetId() == null || atx.getAssetType() == null || atx.getAssetType().isEmpty()) {
				logger.error("Asset Fields Are Null");
				Response response = new Response();
				response.setMessage("Asset fields Should Not Be Empty");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			AssetTypeMaster ram = serv.getByAssetId(atx.getAssetId());

			if (ram == null) {
				throw new RecordNotFoundException("record is not present");
			}

			atx.setUpdatedBy(employeeId);
			Boolean bool = serv.updateAssetTypeById(atx);

			if (bool) {
				logger.info("Asset object Upadted successfully");
				Response response = new Response();
				response.setMessage("Asset updated");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				logger.debug("Failed To Update");
				Response response = new Response();
				response.setMessage("Failed To Update Asset");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}
		} catch (RecordNotFoundException exception) {
			logger.error(exception);
			// logger.debug("inside catch block : " + exception.getMessage());
			Response response = new Response();
			response.setMessage("No Data Found");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (DataIntegrityViolationException exception) {
			logger.error(exception);
			// logger.debug("Inside catch Block : " + exception.getMessage());
			Response response = new Response();
			response.setMessage("Duplicate Entry of AssetName");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception exception) {
			// logger.debug("Inside catch Block : " + exception.getMessage());
			logger.error(exception);
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/getByid/{id}")
	public ResponseEntity<?> getByid(@PathVariable("id") Integer id) {
		try {
			if (id == null) {
				logger.error("Asset ID Null");
				Response response = new Response();
				response.setMessage("Asset Id is Null");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			AssetTypeMaster asset = serv.getByAssetId(id);
			if (asset == null) {
				throw new ResourceNotFoundException("No Data Found This Id");
			}
			return new ResponseEntity<>(asset, HttpStatus.OK);
		} catch (ResourceNotFoundException exception) {
			logger.error(exception);

			Response response = new Response();
			response.setMessage("No Data Peresent");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception exception) {
			logger.error(exception);

			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/Byassettype/{assettype}")
	public ResponseEntity<?> getByAssetTypeName(@PathVariable("assettype") String assettype) {
		try {
			if (assettype == null || assettype.isEmpty()) {
				logger.error("name Field id Null OR Empty");
				Response response = new Response();
				response.setMessage("assettype Field should not Be Empty");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			AssetTypeMaster asset = serv.findAssetTyepeByassettype(assettype);
			if (asset == null) {
				throw new ResourceNotFoundException("No data Present with this assettype");
			}
			return new ResponseEntity<>(asset, HttpStatus.OK);

		} catch (ResourceNotFoundException exception) {
			logger.error(exception);
			Response response = new Response();
			response.setMessage("No Data Found Exception");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		} catch (Exception exception) {
			logger.error(exception);
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping(path = { "/{pageNo}/{pageSize}/{sortByDate}/{searchAssettype}" })
	public ResponseEntity<?> getAssetlList(
			@PathVariable(required = false, name = "searchAssettype") String searchAssettype,
			@PathVariable(required = true, name = "sortByDate") String sortByDate,
			@PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer pageSize) {
		if (searchAssettype.equalsIgnoreCase("null") || searchAssettype.equalsIgnoreCase("undefined")) {
			searchAssettype = null;
		}

		if (pageNo == null || pageSize == null) {
			logger.error("Asset object is null  in post");
			Response response = new Response();

			response.setMessage("page number and size per page shouldnot null");
			response.setMessage("Welcome To india");
			response.setStatus("400");

			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			ResponseList listassets = null;
			if (searchAssettype == null && sortByDate.equals("lastmodefiedDate")) {
				listassets = serv.getListOfAssetsSort(pageNo, pageSize, sortByDate);
			} else {
				listassets = serv.getAllAssetsPginate(searchAssettype, pageNo, pageSize);
			}

			logger.info("getting list of records" + listassets);
			return new ResponseEntity<>(listassets, HttpStatus.OK);
		} catch (RecordNotFoundException e) {

			logger.error("RecordNotFound Exception : " + e.getMessage());

			Response response = new Response();
			response.setMessage("RecordNotFound Exception : " + e.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		} catch (Exception e) {
			logger.error(e);
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	/*
	 * @GetMapping(path = { "/{pageNo}/{pageSize}/{sortBy}/{searchAssettype}" })
	 * 
	 * public ResponseEntity<?> getAssetlList(
	 * 
	 * @PathVariable(required = false, name = "searchAssettype") String
	 * searchAssettype,@PathVariable("sortBy") String sortBy,
	 * 
	 * @PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer
	 * pageSize) { if (searchAssettype.equalsIgnoreCase("null") ||
	 * searchAssettype.equalsIgnoreCase("undefined")) { searchAssettype = null; } if
	 * (pageNo == null || pageSize == null) {
	 * logger.error("Ram object is null  in post"); Response response = new
	 * Response();
	 * 
	 * response.setMessage("page number and size per page shouldnot null");
	 * response.setStatus("400");
	 * 
	 * return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); }
	 * 
	 * try { ResponseList listassets = null; if (searchAssettype == null ||
	 * sortBy.equalsIgnoreCase("lastmodefiedDate")) { listassets =
	 * serv.getAllAssets(pageNo, pageSize,sortBy); } else { listassets =
	 * serv.getAllAssetsPginate(searchAssettype, pageNo, pageSize); }
	 * logger.info("getting list of records" + listassets); return new
	 * ResponseEntity<>(listassets, HttpStatus.OK); } catch (Exception e) {
	 * logger.debug("inside catch block " + e.getMessage()); Response response = new
	 * Response(); response.setMessage("Exception caught");
	 * response.setStatus("409"); e.printStackTrace(); return new
	 * ResponseEntity<>(response, HttpStatus.CONFLICT); } }
	 */

	/*
	 * @GetMapping(path = {"/{pageNo}/{pageSize}/{serachByAssetType}"}) public
	 * ResponseEntity<?> getAssetlList(
	 * 
	 * @PathVariable(required = false, name = "serachByAssetType") String
	 * serachByAssetType,
	 * 
	 * @PathVariable("pageNo") Integer pageno, @PathVariable("pageSize") Integer
	 * pagesize) throws ResourceNotFoundException {
	 * 
	 * if (serachByAssetType.equalsIgnoreCase("null")) { serachByAssetType = null; }
	 * if (pageno == null || pagesize == null) {
	 * logger.error("Ram object is null  in post"); AssetResponse response = new
	 * AssetResponse();
	 * 
	 * response.setMessage("page number and size per page shouldnot null");
	 * response.setStatusCode("400");
	 * 
	 * return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); } try {
	 * 
	 * ResponseList list = null;
	 * 
	 * if (serachByAssetType == null) { list = serv.getAllAssets(pageno, pagesize);
	 * } else { list = serv.getAllAssetsPginate(serachByAssetType, pageno,
	 * pagesize); } logger.info("Getting List of Records : " + list); return new
	 * ResponseEntity<>(list, HttpStatus.OK); } catch (Exception exception) {
	 * logger.debug("inside catch block : " + exception.getMessage()); AssetResponse
	 * response = new AssetResponse(); response.setMessage("Exception caught");
	 * response.setStatusCode("409"); return new ResponseEntity<>(response,
	 * HttpStatus.CONFLICT); }
	 * 
	 * }
	 */

	@GetMapping("/getBustatus/{status}")
	public ResponseEntity<?> getByStatus(@PathVariable("status") Boolean status) {
		try {
			if (status == null) {
				logger.error("Status is Null");
				Response response = new Response();
				response.setMessage("status Value is Null");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			} else {
				List<AssetTypeMaster> list = serv.findAssetTyepeBystatus(status);
				return new ResponseEntity<>(list, HttpStatus.OK);
			}
		} catch (Exception exception) {
			logger.error(exception);
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/deleteByid/{id}")
	public Map<String, Boolean> deleteByid(@PathVariable("id") Integer id) throws RecordNotFoundException {
		logger.info("Deleted SuccessFully");
		Map<String, Boolean> deleteByid = serv.deleteByid(id);
		return deleteByid;

	}

	@GetMapping("/download/AssetExcelFile")
	public ResponseEntity<?> getExcelData(HttpServletResponse httpServletResponse) {
		Response response = new Response();
		try {
			httpServletResponse.setContentType("application/octet-stream");
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename=AssetType.xlsx");
			List<AssetTypeMaster> list = assetRepo.findAll();
			ByteArrayInputStream listToExcelFile = assetTypeexcel.exportingAssetDataToExcelFile(list);
			long copy = IOUtils.copy(listToExcelFile, httpServletResponse.getOutputStream());

			logger.info("Successfully AssetType Excel file is Downloading");

			response.setMessage("Successfully Excel file is Downloading");
			response.setStatus("200");
			return new ResponseEntity<>(copy, HttpStatus.OK);

		} catch (IOException e) {
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

	@GetMapping("/download/Dummyfile")
	public ResponseEntity<?> dummyfile(HttpServletResponse httpServletResponse) {
		Response response = new Response();
		try {
			httpServletResponse.setContentType("application/octet-stream");
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename=AssetTypeFormatExcel.xlsx");
			/* List<AssetTypeMasterEx> list = assetRepo.findAll(); */
			ByteArrayInputStream listToExcelFile = assetTypeexcel.dummyAssetTypeExcelFile();
			long copy = IOUtils.copy(listToExcelFile, httpServletResponse.getOutputStream());

			logger.info("Successfully AssetType Dummy Excel file is Downloading");
			response.setMessage("Successfully AssetType Dummy Excel file is Downloading");
			response.setStatus("200");
			return new ResponseEntity<>(copy, HttpStatus.OK);

		} catch (IOException e) {
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

	@PostMapping("/Upload/assetType/{employeeid}")
	public ResponseEntity<?> uploadAssetTypeFile(@RequestParam(value = "file") MultipartFile file,
			@PathVariable("employeeid") String employeeid) {
		Response response = new Response();
		if (assetTypeexcel.hasExcelFormat(file)) {
			try {
				serv.save(file, employeeid);
				response.setMessage("File Uploaded SuccessFully : " + file.getOriginalFilename());
				response.setStatus("200");
				logger.info("Successfully Uploaded the AssetType Excel file");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} catch (RecordNotFoundException e) {
                logger.error(e);
                e.printStackTrace();
                response.setMessage("RecordNotFound Exception Caught : " + e.getMessage() + "!");
                response.setStatus("409");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }catch (DataIntegrityViolationException e) {
				response.setMessage("Duplicate Entry of AssetType");
				response.setStatus("409");
				e.printStackTrace();
				logger.error(e);
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (Exception e) {
				logger.error(e);
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

	/*
	 * @GetMapping(path = { "/SortByDate/{pageNo}/{sizePerPage}/{sortBy}" }) public
	 * ResponseEntity<?> getListOfAssetsBySort(@PathVariable(name = "sortBy") String
	 * sortBy,
	 * 
	 * @PathVariable("pageNo") Integer pageNo, @PathVariable("sizePerPage") Integer
	 * sizePerPage) { try {
	 * 
	 * if (sortBy.equalsIgnoreCase("null")) { sortBy = null; }
	 * 
	 * logger.info("getting list of requests");
	 * 
	 * ResponseList responseList = null;
	 * 
	 * if (sortBy == null) { responseList = serv.getAllAssets(pageNo, sizePerPage);
	 * }
	 * 
	 * responseList = serv.getListOfAssetsSort(pageNo, sizePerPage, sortBy);
	 * 
	 * return new ResponseEntity<>(responseList, HttpStatus.OK); } catch (Exception
	 * e) {
	 * 
	 * logger.info("exception occured" + e.toString());
	 * 
	 * Response responseData = new Response(); responseData.setStatus("409");
	 * responseData.setMessage("Exception caught"); return new
	 * ResponseEntity<>(responseData, HttpStatus.CONFLICT); }
	 * 
	 * }
	 */
	/*
	 * @GetMapping(path = { "/{pageNo}/{pageSize}/{sortBy}/{searchAssettype}" })
	 * public ResponseEntity<?> getAssetlList(
	 * 
	 * @PathVariable(required = false, name = "searchAssettype") String
	 * searchAssettype,
	 * 
	 * @PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer
	 * pageSize,
	 * 
	 * @PathVariable("sortBy") String sortBy) { if
	 * (searchAssettype.equalsIgnoreCase("null") ||
	 * searchAssettype.equalsIgnoreCase("undefined")) { searchAssettype = null; } if
	 * (pageNo == null || pageSize == null) {
	 * logger.error("Ram object is null  in post"); Response response = new
	 * Response();
	 * 
	 * response.setMessage("page number and size per page shouldnot null");
	 * response.setStatus("400");
	 * 
	 * return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); }
	 * 
	 * try { ResponseList listassets = null; if (searchAssettype == null &&
	 * sortBy.equals("lastmodefiedDate")) { listassets =
	 * serv.getListOfAssetsSort(pageNo, pageSize, sortBy); } else { listassets =
	 * serv.getAllAssetsPginate(searchAssettype, pageNo, pageSize); }
	 * logger.info("getting list of records" + listassets); return new
	 * ResponseEntity<>(listassets, HttpStatus.OK); } catch (Exception e) {
	 * logger.debug("inside catch block " + e.getMessage()); Response response = new
	 * Response(); response.setMessage("Exception caught");
	 * response.setStatus("409"); e.printStackTrace(); return new
	 * ResponseEntity<>(response, HttpStatus.CONFLICT); } }
	 */
}
