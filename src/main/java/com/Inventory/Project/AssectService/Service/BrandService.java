package com.Inventory.Project.AssectService.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Dao.AssetTypeRepositry;
import com.Inventory.Project.AssectService.Dao.BrandRepo;
import com.Inventory.Project.AssectService.Exceldata.BrandExcelData;
import com.Inventory.Project.AssectService.Exception.AssetTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Exception.ResourceNotFoundException;
import com.Inventory.Project.AssectService.Model.AssetTypeMaster;
import com.Inventory.Project.AssectService.Model.Brand;
import com.Inventory.Project.AssectService.Response.ResponseList;

@Service
public class BrandService {

	@Autowired
	private BrandRepo brandRepo;

	@Autowired
	private AssetTypeRepositry assetTypeRepositry;
//	@Autowired
//	private List<Brand> responselist;

	@Autowired
	private BrandExcelData brandExcelData;

	public ResponseList getAllBrands(int pageno, long pagesize) {
		PageRequest paging = PageRequest.of(pageno, (int) pagesize);
		Page<Brand> page = brandRepo.findAll(paging);
		long totalElements = page.getTotalElements();
		int pages = page.getTotalPages();
		List<Brand> list = page.getContent();

		ResponseList responseList = new ResponseList();
		responseList.setTotalNumberOfPages(pages);
		responseList.setNoOfrecords(totalElements);
		responseList.setList(list);
		return responseList;
	}

	public ResponseList getByByandname(String search, int pageno, long pagesize) {
		PageRequest paging = PageRequest.of(pageno, (int) pagesize);
		Page<Brand> list = brandRepo.findByBrandnameContaining(search, paging);
		long totalElements = list.getTotalElements();
		int pages = list.getTotalPages();
		List<Brand> content = list.getContent();
		ResponseList responseList = new ResponseList();
		responseList.setTotalNumberOfPages(pages);
		responseList.setNoOfrecords(totalElements);
		responseList.setList(content);
		return responseList;

	}

	public void getByBrandstatus(Boolean byBrandstaus, int pageno, long pagesize) {
		PageRequest paging = PageRequest.of(pageno, (int) pagesize);

	}

	public Brand getBrandById(Integer brandId) {
		Optional<Brand> optional = brandRepo.findById(brandId);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;

	}

	public Boolean createBrand(Brand brand) {
		brand.setLastmodefiedDate(new Date());
		Brand save = brandRepo.save(brand);
		if (save == null) {
			return false;
		}
		return true;
	}

	public Boolean updateBrand(Brand brand) throws ResourceNotFoundException {
		brand.setLastmodefiedDate(new Date());
		Brand save = brandRepo.save(brand);
		if (save == null) {
			return false;
		}
		return true;
	}

	/*
	 * public Brand findByBrandname(String name) { Brand brand =
	 * brandRepo.findByBrandname(name); return brand; }
	 */
	/*
	 * Brand brand = brandRepo.findById(brandId) .orElseThrow(() -> new
	 * ResourceNotFoundException("brand not found :: " + brandId));
	 * brand.setBrandname(branddetails.getBrandname());
	 * brand.setBrandstatus(branddetails.getBrandstatus());
	 * 
	 * final Brand updatedBrand = brandRepo.save(brand); return
	 * ResponseEntity.ok(updatedBrand);
	 */

	public Map<String, Boolean> deleteBrand(Integer brandId) throws ResourceNotFoundException {
		Brand brand = brandRepo.findById(brandId)
				.orElseThrow(() -> new ResourceNotFoundException("brand not found :: " + brandId));

		brandRepo.delete(brand);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	public List<Brand> findByBrandstatus(Boolean status) {
		List<Brand> list = brandRepo.findByBrandstatus(status);
		return list;

	}

	public List<Brand> getAllBrands() {
		List<Brand> list = brandRepo.findAll();
		return list;

	}

	public Boolean createBrand(Integer assetId, Brand brand) {

		Optional<AssetTypeMaster> assetType = assetTypeRepositry.findById(assetId);
		brand.setAssetTypeMasterEx(assetType.get());
		brand.setLastmodefiedDate(new Date());
		Brand brand1 = brandRepo.save(brand);

		if (brand1 == null) {
			return false;
		}

		return true;

	}

	public Brand findBybrandIdAssetId(Integer assetid, Integer brandid) {

		Optional<Brand> optional = brandRepo.findByBrandidAndAssetTypeMasterExAssetId(brandid, assetid);

		if (optional.isPresent()) {
			return optional.get();
		}

		return null;

	}

	public Boolean updateBrand(Integer assetid, Integer brandid, Brand brandRequest) {
		Optional<Brand> findById = brandRepo.findById(brandid);

		Optional<AssetTypeMaster> findById2 = assetTypeRepositry.findById(assetid);
		Brand save = null;
		if (findById.isPresent()) {
			Brand brand = findById.get();
			brand.setLastmodefiedDate(new Date());
			brand.setBrandname(brandRequest.getBrandname());
			brand.setAssetTypeMasterEx(findById2.get());
			brand.setBrandstatus(brandRequest.getBrandstatus());
			brand.setCreatedBy(brandRequest.getCreatedBy());
			brand.setCreatedOn(brandRequest.getCreatedOn());
			brand.setUpdatedBy(brandRequest.getUpdatedBy());
			brand.setUpdatedOn(brandRequest.getUpdatedOn());
			save = brandRepo.save(brand);
		}

		if (save == null) {
			return false;
		} else {
			return true;
		}
	}

	public ResponseList getAllBrands(Integer assetId, Integer pageno, Integer pagesize) {
		Pageable pageale = PageRequest.of(pageno, pagesize);

		Page<Brand> findAll = brandRepo.findByAssetTypeMasterExAssetId(assetId, pageale);

		ResponseList responseList = new ResponseList();
		responseList.setList(findAll.getContent());
		responseList.setTotalNumberOfPages(findAll.getTotalPages());
		responseList.setNoOfrecords(findAll.getTotalElements());

		return responseList;
	}

	public List<Brand> getBrandByAssetId(Integer assetId) {
		List<Brand> findByAssetTypeMasterExAssetId = brandRepo.findByAssetTypeMasterExAssetId(assetId);

		return findByAssetTypeMasterExAssetId;
	}

	public void saveexceldata(MultipartFile file, String employeeid) throws IOException, AssetTypeNotFoundException, FeildsShouldNotBeEmptyException, RecordNotFoundException {

		List<Brand> dataFromExcel = brandExcelData.readingBrandDataFromExcel(file.getInputStream());
		for (Brand brand : dataFromExcel) {
			List<Brand> findAll = brandRepo.findAll();
			findAll.forEach(brand1 -> {
				if (brand1.getBrandname().equals(brand.getBrandname())) {
					throw new DataIntegrityViolationException("brand name already exists");
				}
			});

			brand.setLastmodefiedDate(new Date());
			brand.setBrandstatus(true);
			brand.setCreatedBy(employeeid);
			brand.setCreatedOn(new Date());
			brandRepo.save(brand);

		}

	}

	public ResponseList getAllBrands1(Integer assetId, Integer pageno, Integer pagesize, String sortBy) {
		Pageable pageale = PageRequest.of(pageno, pagesize, Sort.by(sortBy).descending());

		Page<Brand> findAll = brandRepo.findByAssetTypeMasterExAssetId(assetId, pageale);

		ResponseList responseList = new ResponseList();
		responseList.setList(findAll.getContent());
		responseList.setTotalNumberOfPages(findAll.getTotalPages());
		responseList.setNoOfrecords(findAll.getTotalElements());

		return responseList;
	}

	public ResponseList getAllBrands(Integer assetId, Integer pageno, Integer pagesize, String searchby) {

		Pageable pageale = PageRequest.of(pageno, pagesize);

		Page<Brand> findAll = brandRepo.findByAssetTypeMasterExAssetIdAndBrandnameContaining(assetId, searchby,
				pageale);

		ResponseList responseList = new ResponseList();
		responseList.setList(findAll.getContent());
		responseList.setTotalNumberOfPages(findAll.getTotalPages());
		responseList.setNoOfrecords(findAll.getTotalElements());

		return responseList;
	}

}
