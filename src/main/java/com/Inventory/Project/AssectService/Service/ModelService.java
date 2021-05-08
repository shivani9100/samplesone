package com.Inventory.Project.AssectService.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Dao.BrandRepo;
import com.Inventory.Project.AssectService.Dao.ModelRepo;
import com.Inventory.Project.AssectService.Exceldata.ModelExcelData;
import com.Inventory.Project.AssectService.Exception.AssetTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.BrandTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Exception.ResourceNotFoundException;
import com.Inventory.Project.AssectService.Model.Brand;
import com.Inventory.Project.AssectService.Model.Model;
import com.Inventory.Project.AssectService.Response.ResponseList;

@Service
public class ModelService {

	@Autowired
	private ModelRepo modelRepo;

	@Autowired
	private BrandRepo brandRepo;

	@Autowired
	private ModelExcelData modelExcelData;

	public List<Model> getModelByBrandId(Integer brandId) {
		return modelRepo.findByBrandBrandid(brandId);
	}

	public ResponseList getAllModels(Integer pageNumer, Integer pageSize) {
		Pageable pageale = PageRequest.of(pageNumer, pageSize);

		Page<Model> findAll = modelRepo.findAll(pageale);

		ResponseList responseList = new ResponseList();
		responseList.setList(findAll.getContent());
		responseList.setTotalNumberOfPages(findAll.getTotalPages());
		responseList.setNoOfrecords(findAll.getTotalElements());

		return responseList;
	}

	public Model findByModelIdBrandId(Integer brandId, Integer modelId) {
		Optional<Model> optional = modelRepo.findByModelidAndBrandBrandid(modelId, brandId);
		if (optional.isPresent()) {
			return optional.get();
		}

		return null;

	}

	public ResponseList getAllModels(Integer brandId, Integer pageno, Integer pagesize) {
		Pageable pageale = PageRequest.of(pageno, pagesize);

		Page<Model> findAll = modelRepo.findByBrandBrandid(brandId, pageale);

		ResponseList responseList = new ResponseList();
		responseList.setList(findAll.getContent());
		responseList.setTotalNumberOfPages(findAll.getTotalPages());
		responseList.setNoOfrecords(findAll.getTotalElements());
	
		

		return responseList;
	}

	public Boolean createModel(Integer brandId, Model model) throws ResourceNotFoundException {

		Optional<Brand> brand = brandRepo.findById(brandId);
		model.setBrand(brand.get());
		model.setLastmodefiedDate(new Date());
		Model model2 = modelRepo.save(model);

		if (model2 == null) {
			return false;
		}

		return true;
	}

	public Boolean updateModel(Integer brandId, Integer modelId, Model modelRequest) throws ResourceNotFoundException {

		Optional<Model> findById = modelRepo.findById(modelId);

		Model save = null;

		if (findById.isPresent()) {
			Model model = findById.get();
			model.setModelname(modelRequest.getModelname());
			model.setModelstatus(modelRequest.getModelstatus());
			model.setCreatedBy(modelRequest.getCreatedBy());
			model.setCreatedOn(modelRequest.getCreatedOn());
			model.setUpdatedBy(modelRequest.getUpdatedBy());
			model.setUpdatedOn(modelRequest.getUpdatedOn());
			model.setLastmodefiedDate(new Date());
			save = modelRepo.save(model);
		}

		if (save == null) {
			return false;
		} else {
			return true;
		}

	}

	public ResponseEntity<?> deleteModel(Integer brandId, Integer modelId) throws ResourceNotFoundException {
		return modelRepo.findByModelidAndBrandBrandid(modelId, brandId).map(model -> {
			modelRepo.delete(model);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException(
				"Course not found with id " + modelId + " and instructorId " + brandId));
	}

	public ResponseList getAllModels(Integer pageno, Integer pagesize, String searchby) {

		Pageable pageale = PageRequest.of(pageno, pagesize);

		Page<Model> findAll = modelRepo.findByModelnameContaining(searchby, pageale);

		ResponseList responseList = new ResponseList();
		responseList.setList(findAll.getContent());
		responseList.setTotalNumberOfPages(findAll.getTotalPages());
		responseList.setNoOfrecords(findAll.getTotalElements());

		return responseList;

	}

	public Model getModelByBrandIdModelId(Integer brandId, Integer modelId) {

		Optional<Model> optional = modelRepo.findByModelidAndBrandBrandid(modelId, brandId);

		if (optional.isPresent()) {
			return optional.get();
		}

		return null;
	}

	public void save(MultipartFile file, String employeeid)
			throws IOException, ParseException, AssetTypeNotFoundException, BrandTypeNotFoundException, FeildsShouldNotBeEmptyException, RecordNotFoundException {
		List<Model> fromExcel = modelExcelData.readingModelDataFromExcel(file.getInputStream());
		for (Model model : fromExcel) {
			
			Model model2 = modelRepo.findByModelnameAndBrandBrandid(model.getModelname(), model.getBrand().getBrandid());
			
			if(model2 !=null) {
				throw new DataIntegrityViolationException("model already exists");
			}
			model.setLastmodefiedDate(new Date());
			model.setModelstatus(true);
			model.setCreatedBy(employeeid);
			model.setCreatedOn(new Date());
			 

			modelRepo.save(model);
		}

	}

	public ResponseList getAllModels1(Integer brandId, Integer pageno, Integer pagesize, String sortBy) {
		Pageable pageale = PageRequest.of(pageno, pagesize, Sort.by(sortBy).descending());

		Page<Model> findAll = modelRepo.findByBrandBrandid(brandId, pageale);

		ResponseList responseList = new ResponseList();
		responseList.setList(findAll.getContent());
		responseList.setTotalNumberOfPages(findAll.getTotalPages());
		responseList.setNoOfrecords(findAll.getTotalElements());

		return responseList;
	}

	public ResponseList getAllModels(Integer brandId, Integer pageno, Integer pagesize, String searchby) {

		Pageable pageale = PageRequest.of(pageno, pagesize);

		Page<Model> findAll = modelRepo.findByBrandBrandidAndModelnameContaining(brandId, searchby, pageale);

		ResponseList responseList = new ResponseList();
		responseList.setList(findAll.getContent());
		responseList.setTotalNumberOfPages(findAll.getTotalPages());
		responseList.setNoOfrecords(findAll.getTotalElements());

		return responseList;

	}

}
