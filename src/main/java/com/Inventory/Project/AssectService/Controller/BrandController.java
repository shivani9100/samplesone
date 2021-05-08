package com.Inventory.Project.AssectService.Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Dao.BrandRepo;
import com.Inventory.Project.AssectService.Exceldata.BrandExcelData;
import com.Inventory.Project.AssectService.Exception.AssetTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Exception.ResourceNotFoundException;
import com.Inventory.Project.AssectService.Model.AssetTypeMaster;
import com.Inventory.Project.AssectService.Model.Brand;
import com.Inventory.Project.AssectService.Pagination.BrandDaoImpl;
import com.Inventory.Project.AssectService.Pagination.PagenationResponse;
import com.Inventory.Project.AssectService.Response.Response;
import com.Inventory.Project.AssectService.Response.ResponseList;
import com.Inventory.Project.AssectService.Service.AssetTypeServiceImpl;
import com.Inventory.Project.AssectService.Service.BrandService;

@RestController
@RequestMapping("/BrandMaster")
public class BrandController {
	@Autowired
	private BrandService brandService;

	@Autowired
	private AssetTypeServiceImpl assetServiceImpl;

	@Autowired
	private BrandRepo brandrepo;
	@Autowired
	private BrandExcelData brandexcelldata;
	private static final Logger logger = LogManager.getLogger(BrandController.class);

	@PostMapping("/addBrandService/{assetid}/{employeeId}")
	public ResponseEntity<?> createBrand(@PathVariable(value = "assetid") Integer assetId, @RequestBody Brand brand,
			@PathVariable(value = "employeeId") String employeeId) {

		try {

			if (brand == null || assetId == null) {
				Response response = new Response();
				response.setMessage(" request object not null");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			if (brand.getBrandname() == null || brand.getBrandname().isEmpty()) {
				Response response = new Response();
				response.setMessage("feilds should not null");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			if (assetServiceImpl.getByAssetId(assetId) == null) {
				throw new ResourceNotFoundException("asset Id not found");
			}

			AssetTypeMaster assetType = assetServiceImpl.getByAssetId(assetId);

			List<Brand> brandList = assetType.getBrandList();

			brandList.forEach(brand1 -> {
				if (brand1.getBrandname().equals(brand.getBrandname())) {
					throw new DataIntegrityViolationException("brand name already exists");
				}
			});

			brand.setCreatedBy(employeeId);
			brand.setCreatedOn(new Date());

			Boolean createModel = brandService.createBrand(assetId, brand);
			Response response = new Response();
			if (createModel) {

				response.setMessage("success");
				response.setStatus("200");
			} else {
				response.setMessage("not success");
				response.setStatus("200");
			}

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ResourceNotFoundException exception) {
			logger.error(exception);
			Response response = new Response();
			response.setMessage("asset id not found");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

		catch (DataIntegrityViolationException exception) {
			logger.error(exception);
			Response response = new Response();
			response.setMessage("Duplicate entry of brand Name not allowed");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception exception) {
			logger.error(exception);
			Response response = new Response();
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/upadateBrandService/{assetid}/{employeeId}")
	public ResponseEntity<?> updateBrand(@PathVariable(value = "assetid") Integer assetid,
			@RequestBody Brand brandRequest, @PathVariable(value = "employeeId") String employeeId) {
		try {

			if (brandRequest == null || assetid == null) {
				Response response = new Response();

				response.setMessage("request object not null");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			if (brandRequest.getBrandname() == null || brandRequest.getBrandname().isEmpty()) {
				Response response = new Response();
				response.setMessage("feilds should not null");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

			}

			if (assetServiceImpl.getByAssetId(assetid) == null) {

				Response response = new Response();
				response.setMessage("assetType not found");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);

			}

			AssetTypeMaster assetType = assetServiceImpl.getByAssetId(assetid);

			List<Brand> brandList = assetType.getBrandList();

			brandList.forEach(brand1 -> {
				if (brand1.getBrandname().equals(brandRequest.getBrandname())) {
					throw new DataIntegrityViolationException("brand name already exists");
				}
			});
			if (brandService.findBybrandIdAssetId(assetid, brandRequest.getBrandid()) == null) {
				throw new ResourceNotFoundException(
						"assettype with " + assetid + "contains brand id" + brandRequest.getBrandid() + "not found");
			}
			brandRequest.setUpdatedBy(employeeId);
			brandRequest.setUpdatedOn(new Date());
			Boolean updateBrand = brandService.updateBrand(assetid, brandRequest.getBrandid(), brandRequest);
			Response response = new Response();
			if (updateBrand) {

				response.setMessage("success");
				response.setStatus("200");
			} else {
				response.setMessage("not success");
				response.setStatus("200");
			}

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ResourceNotFoundException exception) {
			logger.error(exception);
			Response response = new Response();
			response.setMessage("brand not found");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

		catch (DataIntegrityViolationException exception) {
			logger.error(exception);
			Response response = new Response();
			response.setMessage("Duplicate entry of brand Name not allowed");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception exception) {
			logger.error(exception);
			Response response = new Response();
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

	@GetMapping("/BrandServicefindByid/{assetId}/{brandId}")

	public ResponseEntity<?> getBrandByBrandidassetId(@PathVariable(value = "brandId") Integer brandId,
			@PathVariable(value = "assetId") Integer assetId) {
		try {
			if (brandId == null || assetId == null) {
				Response response = new Response();
				response.setMessage("bad request");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			if (assetServiceImpl.getByAssetId(assetId) == null) {
				throw new ResourceNotFoundException("asset not found");
			}

			Brand brandByAssetId = brandService.findBybrandIdAssetId(assetId, brandId);

			if (brandByAssetId == null) {
				throw new ResourceNotFoundException("brand not found");
			}

			return new ResponseEntity<>(brandByAssetId, HttpStatus.OK);
		} catch (ResourceNotFoundException exception) {
			logger.error(exception);
			Response response = new Response();
			response.setMessage("brand with model not found");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

		catch (Exception e) {
			logger.error(e);
			Response response = new Response();
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/brandservicefindByid/{assetId}/{pageNumber}/{pageSize}/{searchby}")
	public ResponseEntity<?> getBrandByAssetId(@PathVariable(value = "assetId") Integer assetId,
			@PathVariable(required = false, name = "searchby") String searchby,

			@PathVariable("pageNumber") Integer pageno, @PathVariable("pageSize") Integer pagesize) {

		try {

			if (searchby.equalsIgnoreCase("null") || searchby.equalsIgnoreCase("undefined")) {
				searchby = null;
			}

			if (pageno == null || pagesize == null) {
				Response response = new Response();
				response.setMessage("bad request");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			ResponseList data = null;
			if (searchby == null) {
				data = brandService.getAllBrands(assetId, pageno, pagesize);
			} else {
				data = brandService.getAllBrands(assetId, pageno, pagesize, searchby);
			}
			return new ResponseEntity<>(data, HttpStatus.OK);
		} catch (Exception exception) {
			logger.error(exception);
			Response response = new Response();
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

	@GetMapping("/BrandservicefindByid/{assetId}")

	public ResponseEntity<?> getBrandByAsset(@PathVariable(value = "assetId") Integer assetId) {
		try {
			if (assetId == null) {
				Response response = new Response();
				response.setMessage("bad request");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			if (assetServiceImpl.getByAssetId(assetId) == null) {
				throw new ResourceNotFoundException("asset not found");
			}

			List<Brand> brandByassetId = brandService.getBrandByAssetId(assetId);

			return new ResponseEntity<>(brandByassetId, HttpStatus.OK);
		} catch (ResourceNotFoundException exception) {
			logger.error(exception);
			Response response = new Response();
			response.setMessage("asset id not found");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

		catch (Exception e) {
			logger.error(e);
			Response response = new Response();
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	/*
	 * @GetMapping("byname/{name}") public ResponseEntity<?>
	 * findByBrandname(@PathVariable("name") String name) { try { if (name == null
	 * || name.isEmpty()) { log.error("Entered name Field is Null are empty");
	 * response.setMessage("Please Enter the name field it should not be Empty");
	 * response.setStatus("422"); return new ResponseEntity<>(response,
	 * HttpStatus.UNPROCESSABLE_ENTITY); } Brand brand =
	 * brandService.findByBrandname(name); if (brand == null) { throw new
	 * ResourceNotFoundException("No Data Found with This Brand name"); } return new
	 * ResponseEntity<>(brand, HttpStatus.OK);
	 * 
	 * } catch (ResourceNotFoundException exception) {
	 * log.debug("No Data Found with this Brand name");
	 * response.setMessage("No data present with this Brand name");
	 * response.setMessage("409"); return new ResponseEntity<>(response,
	 * HttpStatus.CONFLICT); } catch (Exception e) {
	 * log.debug("Inside the catch Block Exception : " + e.getMessage());
	 * response.setMessage("Exception caught : " + e.getMessage());
	 * response.setStatus("409"); return new ResponseEntity<>(response,
	 * HttpStatus.CONFLICT); } }
	 */

	/*
	 * @GetMapping("bystatus/{status}") public ResponseEntity<?>
	 * findByBrandstatus(@PathVariable("status") Boolean status) { try { if (status
	 * == null) { logger.error("Status is false"); Response response = new
	 * Response(); response.setMessage("Please Enter the status field is Empty");
	 * response.setStatus("422"); return new ResponseEntity<>(response,
	 * HttpStatus.UNPROCESSABLE_ENTITY); } List<Brand> list =
	 * brandService.findByBrandstatus(status); if (list == null) { throw new
	 * ResourceNotFoundException("No data found with this status : " + status); }
	 * return new ResponseEntity<>(list, HttpStatus.OK); } catch
	 * (ResourceNotFoundException exception) { logger.debug("No Data Found " +
	 * exception.getMessage()); Response response = new Response();
	 * response.setMessage("No data Found"); response.setStatus("409"); return new
	 * ResponseEntity<>(response, HttpStatus.CONFLICT); } catch (Exception
	 * exceptions) { logger.debug("Inside the catch Block : " +
	 * exceptions.getMessage()); Response response = new Response();
	 * response.setMessage("Exception caught"); response.setStatus("409"); return
	 * new ResponseEntity<>(response, HttpStatus.CONFLICT);
	 * 
	 * } }
	 */

	/*
	 * @DeleteMapping("/{id}") public Map<String, Boolean>
	 * deleteBrand(@PathVariable(value = "id") Integer brandId) throws
	 * ResourceNotFoundException { return brandService.deleteBrand(brandId);
	 * 
	 * }
	 */

	/*
	 * @GetMapping(value = { "/{pageno}/{pagesize}",
	 * "/{pageNo}/{pagesize}/{byBrandname}", "/{pageno}/{pagesize}/{byBrandstatus}"
	 * }) public ResponseEntity<?> getBrands(@PathVariable(required = false, name =
	 * "byBrandname") String byBrandname,
	 * 
	 * @PathVariable(required = false, name = "byBrandstatus") Boolean byBrandstaus,
	 * 
	 * @PathVariable("pageno") int pageno, @PathVariable("pagesize") long pagesize)
	 * {
	 * 
	 * try { ResponseList list = null; if (byBrandname == null || byBrandstaus ==
	 * null) { list = brandService.getAllBrands(pageno, pagesize); } if (byBrandname
	 * != null) { list = brandService.getByByandname(byBrandname, pageno, pagesize);
	 * } if (byBrandstaus != null) { brandService.getByBrandstatus(byBrandstaus,
	 * pageno, pagesize); }
	 * 
	 * if (list == null) {
	 * 
	 * throw new ResourceNotFoundException("No data Found"); } return new
	 * ResponseEntity<>(list, HttpStatus.OK);
	 * 
	 * } catch (ResourceNotFoundException exception) { log.debug("No data Found");
	 * response.setMessage("No data Found"); response.setStatus("409"); return new
	 * ResponseEntity<>(response, HttpStatus.CONFLICT); } catch (Exception
	 * exception) { log.debug("Inside the catch Block : " + exception.getMessage());
	 * response.setMessage("Exception Caught"); response.setStatus("409"); return
	 * new ResponseEntity<>(response, HttpStatus.CONFLICT); }
	 */
//	}
	
	@GetMapping("/download/BrandExcelFile")
	public ResponseEntity<?> getBrandExcelData(HttpServletResponse httpServletResponse) {
		Response response = new Response();

		try {

			httpServletResponse.setContentType("application/octet-stream");
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename = Brand.xlsx");
			List<Brand> list = brandrepo.findAll();
			ByteArrayInputStream excelFile = brandexcelldata.exportingBrandDataToExcelFile(list);
			long copy = IOUtils.copy(excelFile, httpServletResponse.getOutputStream());
			logger.info("Successfully Downloaded Brand Excel File");
			response.setMessage("Succesfully ExcelFile Downloaded");
			response.setStatus("200");
			return new ResponseEntity<>(copy, HttpStatus.OK);

		} catch (IOException exception) {
			logger.error(exception);

			response.setMessage("Input and Output exception : " + exception.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		} catch (Exception exception) {
			logger.error(exception);

			response.setMessage("Exception Caught : " + exception.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

	@GetMapping("/download/BrandExcelFileDummyFile")
	public ResponseEntity<?> getBrandExcelDataDummyFile(HttpServletResponse httpServletResponse) {
		Response response = new Response();

		try {

			httpServletResponse.setContentType("application/octet-stream");
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename = BrandFormatExcel.xlsx");
			ByteArrayInputStream dummyBrandExcelFile = brandexcelldata.dummyBrandExcelFile();
			long copy = IOUtils.copy(dummyBrandExcelFile, httpServletResponse.getOutputStream());
			logger.info("Successfully Downloaded the Dummy Brand Excel file");
			response.setMessage("Succesfully ExcelFile Downloaded");
			response.setStatus("200");
			return new ResponseEntity<>(copy, HttpStatus.OK);

		} catch (IOException e) {
			logger.error(e);

			response.setMessage("Input and Output exception : " + e.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		} catch (Exception e) {
			logger.error(e);

			response.setMessage("Exception Caught : " + e.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

	@PostMapping("/upload/brandexcelfile/{employeeid}")
	public ResponseEntity<?> uploadBrandExcelfile(@RequestParam("file") MultipartFile file,
			@PathVariable("employeeid") String employeeid) {
		Response response = new Response();
		if (brandexcelldata.hasExcelFormat(file)) {
			try {

				brandService.saveexceldata(file, employeeid);

				response.setMessage("File uploadede SuccessFully : " + file.getOriginalFilename());
				response.setStatus("200");
				logger.info("Successfully Uploaded the Brand Excel File");
				return new ResponseEntity<>(response, HttpStatus.OK);

			} catch (DataIntegrityViolationException e) {
				response.setMessage("Duplicate Entry of Brand");
				response.setStatus("409");
				e.printStackTrace();
				logger.error(e);
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (AssetTypeNotFoundException e) {
				response.setMessage("Asset type not found");
				response.setStatus("409");
				e.printStackTrace();
				logger.error(e);
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (FeildsShouldNotBeEmptyException e) {
				logger.error(e);
				e.printStackTrace();
				response.setMessage("FeildsShouldNotBeEmptyException caught : " + e.getMessage() + "!");
				response.setStatus("409");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (RecordNotFoundException e) {
				logger.error(e);
				e.printStackTrace();
				response.setMessage("RecordNotFound Exception caught : " + e.getMessage() + "!");
				response.setStatus("409");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
				response.setMessage(
						"Could not Upload the File. Exception caught : " + file.getOriginalFilename() + "!");
				response.setStatus("409");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}
		}
		logger.info("Please Upload Only ExcelFile");
		response.setMessage("Please Upload the Excel File Only");
		response.setStatus("400");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@GetMapping(path = { "/getallbrands/{pageNo}/{sizePerPage}/{searchByBrandName}" })
	public ResponseEntity<?> getAllBrandinfo(
			@PathVariable(required = false, name = "searchByBrandName") String searchByBrandName,
			@PathVariable("pageNo") Integer pageNo, @PathVariable("sizePerPage") Integer sizePerPage) {
		if (searchByBrandName.equalsIgnoreCase("null") || searchByBrandName.equalsIgnoreCase("undefined")) {
			searchByBrandName = null;
		}
		if (pageNo == null || sizePerPage == null) {
//                logger.error("Ram object is null  in post");
			Response response = new Response();

			response.setMessage("page number and size per page shouldnot null");
			response.setStatus("400");

			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			ResponseList listOfBrands = null;

			if (searchByBrandName == null) {
				listOfBrands = brandService.getAllBrands(pageNo, sizePerPage);
			}

			else {
				listOfBrands = brandService.getByByandname(searchByBrandName, pageNo, sizePerPage);
			}

//                logger.info("getting list of records" + listOfRams);

			return new ResponseEntity<>(listOfBrands, HttpStatus.OK);
		} catch (Exception e) {

//                logger.debug("inside catch block " + e.getMessage());
			logger.error(e);
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/brandservicefindByid/{assetId}/{pageNumber}/{pageSize}/{sortBy}/{searchby}")
	public ResponseEntity<?> getModelByBrand1(@PathVariable(value = "assetId") Integer assetId,
			@PathVariable(required = false, name = "searchby") String searchby,

			@PathVariable("pageNumber") Integer pageno, @PathVariable("pageSize") Integer pagesize,
			@PathVariable("sortBy") String sortBy) {

		try {

			if (searchby.equalsIgnoreCase("null") || searchby.equalsIgnoreCase("undefined")) {
				searchby = null;
			}

			if (pageno == null || pagesize == null) {
				Response response = new Response();
				response.setMessage("bad request");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			ResponseList data = null;
			if (searchby == null && sortBy.equals("lastmodefiedDate")) {
				data = brandService.getAllBrands1(assetId, pageno, pagesize, sortBy);
			} else {
				data = brandService.getAllBrands(assetId, pageno, pagesize, searchby);
			}
			return new ResponseEntity<>(data, HttpStatus.OK);
		} catch (Exception exception) {
			logger.error(exception);
			Response response = new Response();
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

	@Autowired
	BrandDaoImpl brandimpl;

	@GetMapping("/sort/{pageNumber}/{pageSize}/{searchby}")
	public ResponseEntity<?> getModelByBrand11(@PathVariable(required = false, name = "searchby") String searchby,

			@PathVariable("pageNumber") Integer pageno, @PathVariable("pageSize") Integer pagesize) {
		try {
			PagenationResponse allBrands = brandimpl.getAllBrands(pageno, pagesize, searchby);

			return new ResponseEntity(allBrands, HttpStatus.OK);

		} catch (RecordNotFoundException e) {

			logger.error(e);
			
			Response response = new Response();
			response.setMessage("RecordNotFoundException : " + e.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		} catch (Exception e) {
			
			logger.error(e);
			
			Response response = new Response();
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

}
