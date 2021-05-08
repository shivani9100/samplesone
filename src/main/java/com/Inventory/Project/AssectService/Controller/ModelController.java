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

import com.Inventory.Project.AssectService.Exceldata.ModelExcelData;
import com.Inventory.Project.AssectService.Exception.AssetTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.BrandTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Exception.ResourceNotFoundException;
import com.Inventory.Project.AssectService.Model.Model;
import com.Inventory.Project.AssectService.Pagination.ModelDaoImpl;
import com.Inventory.Project.AssectService.Pagination.ModelPagination;
import com.Inventory.Project.AssectService.Pagination.PagenationResponse;
import com.Inventory.Project.AssectService.Response.Response;
import com.Inventory.Project.AssectService.Response.ResponseList;
import com.Inventory.Project.AssectService.Service.BrandService;
import com.Inventory.Project.AssectService.Service.ModelService;

@RestController
@RequestMapping("/Modelservice")
public class ModelController {

	private static final Logger logger = LogManager.getLogger(ModelController.class);

	@Autowired
	private ModelService modelService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private ModelExcelData modelExcelData;

	@Autowired
	private ModelDaoImpl modelDaoImpl;

	@GetMapping("/ModelServicefindByid/{brandId}/{modelId}")

	public ResponseEntity<?> getModelByBrandidModelid(@PathVariable(value = "brandId") Integer brandId,
			@PathVariable(value = "modelId") Integer modelId) {
		try {
			if (brandId == null || modelId == null) {
				Response response = new Response();
				response.setMessage("bad request");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			if (brandService.getBrandById(brandId) == null) {
				throw new ResourceNotFoundException("brand not found");
			}

			Model modelByBrandId = modelService.getModelByBrandIdModelId(brandId, modelId);

			if (modelByBrandId == null) {
				throw new ResourceNotFoundException("model not found");
			}

			return new ResponseEntity<>(modelByBrandId, HttpStatus.OK);
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

	/*
	 * @GetMapping(
	 * "/ModelServicefindByid/{brandId}/{pageNumber}/{pageSize}/{searchby}") public
	 * ResponseEntity<?> getModelByBrand1(@PathVariable(value = "brandId") Integer
	 * brandId,
	 * 
	 * @PathVariable(required = false, name = "searchby") String searchby,
	 * 
	 * @PathVariable("pageNumber") Integer pageno, @PathVariable("pageSize") Integer
	 * pagesize) {
	 * 
	 * try {
	 * 
	 * if (searchby.equalsIgnoreCase("null") ||
	 * searchby.equalsIgnoreCase("undefined")) { searchby = null; }
	 * 
	 * if (pageno == null || pagesize == null) { Response response = new Response();
	 * response.setMessage("bad request"); response.setStatus("422"); return new
	 * ResponseEntity<>(response, HttpStatus.BAD_REQUEST); } ResponseList data =
	 * null; if (searchby == null) { data = modelService.getAllModels(brandId,
	 * pageno, pagesize); } else { data = modelService.getAllModels(brandId, pageno,
	 * pagesize, searchby); } return new ResponseEntity<>(data, HttpStatus.OK); }
	 * catch (Exception exception) { logger.debug("Inside Catch Block : " +
	 * exception.getMessage()); Response response = new Response();
	 * response.setMessage("Exception Caught"); response.setStatus("409"); return
	 * new ResponseEntity<>(response, HttpStatus.CONFLICT); }
	 * 
	 * }
	 */

	@GetMapping("/ModelServicefindByid/{brandId}")

	public ResponseEntity<?> getModelByBrand(@PathVariable(value = "brandId") Integer brandId) {
		try {
			if (brandId == null) {
				Response response = new Response();
				response.setMessage("bad request");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			if (brandService.getBrandById(brandId) == null) {
				throw new ResourceNotFoundException("brand not found");
			}

			List<Model> modelByBrandId = modelService.getModelByBrandId(brandId);

			return new ResponseEntity<>(modelByBrandId, HttpStatus.OK);
		} catch (ResourceNotFoundException exception) {
			logger.debug("brand id not found " + exception.getMessage());
			Response response = new Response();
			response.setMessage("brand id not found");
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

	@GetMapping("getAll/{pageNumber}/{pageSize}/{searchby}")

	public ResponseEntity<?> getListOfModels(@PathVariable(required = false, name = "searchby") String searchby,

			@PathVariable("pageNumber") Integer pageno, @PathVariable("pageSize") Integer pagesize) {

		try {

			if (searchby.equalsIgnoreCase("null")) {
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
				data = modelService.getAllModels(pageno, pagesize);
			} else {
				data = modelService.getAllModels(pageno, pagesize, searchby);
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

	@PostMapping("/addModelService/{brandId}/{employeeId}")
	public ResponseEntity<?> createModel(@PathVariable(value = "brandId") Integer brandId, @RequestBody Model model,
			@PathVariable(value = "employeeId") String employeeId) {

		try {

			if (model == null || brandId == null) {
				Response response = new Response();
				response.setMessage(" request object not null");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			if (model.getModelname() == null || model.getModelname().isEmpty()) {
				Response response = new Response();
				response.setMessage("feilds should not null");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			if (brandService.getBrandById(brandId) == null) {
				throw new ResourceNotFoundException("brand Id not found");
			}

			model.setCreatedBy(employeeId);
			model.setCreatedOn(new Date());

			Boolean createModel = modelService.createModel(brandId, model);
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
			logger.debug("brand id not found " + exception.getMessage());
			Response response = new Response();
			response.setMessage("brand id not found");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

		catch (DataIntegrityViolationException exception) {
			logger.debug("Duplicate Entry of data " + exception.getMessage());
			Response response = new Response();
			response.setMessage("Duplicate Entry of model Name not allowed");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception exception) {
			logger.debug("Inside Catch Block : " + exception.getMessage());
			Response response = new Response();
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/upadateModelService/{brandId}/{employeeId}")
	public ResponseEntity<?> updateModel(@PathVariable(value = "brandId") Integer brandId,
			@RequestBody Model modelRequest, @PathVariable(value = "employeeId") String employeeId)
			throws ResourceNotFoundException {
		try {

			if (modelRequest == null || brandId == null || modelRequest.getModelid() == null) {
				Response response = new Response();
				response.setMessage("request object not null");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			if (modelRequest.getModelname() == null || modelRequest.getModelname().isEmpty()) {
				Response response = new Response();
				response.setMessage("feilds should not null");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

			}

			if (brandService.getBrandById(brandId) == null) {

				Response response = new Response();
				response.setMessage("brand not found");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);

			}

			if (modelService.findByModelIdBrandId(brandId, modelRequest.getModelid()) == null) {
				throw new ResourceNotFoundException(
						"brand with " + brandId + "contains model id" + modelRequest.getModelid() + "not found");
			}
			modelRequest.setUpdatedBy(employeeId);
			modelRequest.setUpdatedOn(new Date());
			Boolean updateModel = modelService.updateModel(brandId, modelRequest.getModelid(), modelRequest);
			Response response = new Response();
			if (updateModel) {

				response.setMessage("success");
				response.setStatus("200");
			} else {
				response.setMessage("not success");
				response.setStatus("200");
			}

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ResourceNotFoundException exception) {
			logger.debug("model id not found " + exception.getMessage());
			Response response = new Response();
			response.setMessage("model not found");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

		catch (DataIntegrityViolationException exception) {
			logger.debug("Duplicate Entry of data " + exception.getMessage());
			Response response = new Response();
			response.setMessage("Duplicate Entry of model Name not allowed");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception exception) {
			logger.debug("Inside Catch Block : " + exception.getMessage());
			Response response = new Response();
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}
//

	@DeleteMapping("/deleteModelService/{brandId}/{modelId}")
	public ResponseEntity<?> deleteModel(@PathVariable(value = "brandId") Integer brandId,
			@PathVariable(value = "modelId") Integer modelId) throws ResourceNotFoundException {
		return modelService.deleteModel(brandId, modelId);
	}

	@GetMapping("/model/download/Dummyfile")
	public ResponseEntity<?> dummyfile(HttpServletResponse httpServletResponse) {
		Response response = new Response();
		try {
			httpServletResponse.setContentType("application/octet-stream");
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename=ModelFormatExcel.xlsx");
			ByteArrayInputStream listToExcelFile = modelExcelData.exportingDummyModelDataToExcelFile();
			long copy = IOUtils.copy(listToExcelFile, httpServletResponse.getOutputStream());

			logger.info("Successfully AssetType Dummy Excel file is Downloading");
			response.setMessage("Successfully AssetType Dummy Excel file is Downloading");
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

	@PostMapping("/Upload/exceldata/{employeeid}")
	public ResponseEntity<?> uploadModel(@RequestParam(value = "file") MultipartFile file,
			@PathVariable("employeeid") String employeeid) {
		Response response = new Response();
		if (modelExcelData.hasExcelFormat(file)) {
			try {
				modelService.save(file, employeeid);
				response.setMessage("File Uploaded SuccessFully : " + file.getOriginalFilename());
				response.setStatus("200");
				logger.info("Successfully Uploaded the AssetType Excel file");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

			catch (AssetTypeNotFoundException e) {
				response.setMessage(e.getLocalizedMessage());
				response.setStatus("409");
				e.printStackTrace();
				logger.error(e);
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (BrandTypeNotFoundException e) {
				response.setMessage(e.getLocalizedMessage());
				response.setStatus("409");
				e.printStackTrace();
				logger.error(e);
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}

			catch (DataIntegrityViolationException e) {
				response.setMessage("Duplicate Entry of Model Name");
				response.setStatus("409");
				e.printStackTrace();
				logger.error(e);
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
//		} 
//				catch (BrandTypeNotFoundException e) {
//				response.setMessage("brand not found");
//				response.setStatus("409");
//				e.printStackTrace();
//				logger.error(e);
//				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			} catch (FeildsShouldNotBeEmptyException e) {
				logger.debug("IOException Caught : " + e.getMessage());
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

	}

	@GetMapping("/ModelServicefindByid/{brandId}/{pageNumber}/{pageSize}/{sortBy}/{searchby}")
	public ResponseEntity<?> getModelByBrand1(@PathVariable(value = "brandId") Integer brandId,
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
				data = modelService.getAllModels1(brandId, pageno, pagesize, sortBy);
			} else {
				data = modelService.getAllModels(brandId, pageno, pagesize, searchby);
			}
			return new ResponseEntity<>(data, HttpStatus.OK);
		} catch (Exception exception) {
			logger.debug("Inside Catch Block : " + exception.getMessage());
			exception.printStackTrace();
			Response response = new Response();
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

	@GetMapping(path = { "/sort/{pageNo}/{pageSize}/{searchby}" })
	public ResponseEntity<?> getAllModels(@PathVariable(required = false, name = "searchby") String searchby,
			@PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize") Integer pageSize) {

		if (searchby.equalsIgnoreCase("null") || searchby.equalsIgnoreCase("undefined")) {
			searchby = null;
		}

		if (pageNo == null || pageSize == null) {
			logger.error("Models records are null");
			Response response = new Response();

			response.setMessage("Page number And pageSize Connot be null");
			response.setStatus("400");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {

			ResponseList ramCapacityList = null;
			PagenationResponse modelresponse = modelDaoImpl.getAllModels(pageNo, pageSize, searchby);
			logger.info("getting list of Objects" + modelresponse);

			return new ResponseEntity<>(modelresponse, HttpStatus.OK);

		} catch (RecordNotFoundException e) {

			logger.error("RecordNotFound Exception Caught : " + e.getMessage());

			Response response = new Response();

			response.setMessage("RecordNotFoundException : " + e.getMessage());
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		} catch (Exception e) {

			logger.error("Inside Catch" + e.getMessage());

			Response response = new Response();
			response.setMessage("Exception catch");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/ModelAssetTypeBrand/getIdBased/{assetid}/{brandid}/{modelid}")
	public ResponseEntity<?> getModelAsstTypeBrandBasedOnIds(@PathVariable("assetid") Integer assetid,
			@PathVariable("brandid") Integer brandid, @PathVariable("modelid") Integer modelid) {
		try {

			List<ModelPagination> response = modelDaoImpl.getAllModelWithAssettypeBrandIds(assetid, brandid, modelid);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}
}
