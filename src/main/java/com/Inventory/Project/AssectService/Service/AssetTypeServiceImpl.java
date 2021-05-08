package com.Inventory.Project.AssectService.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Dao.AssetTypeRepositry;
import com.Inventory.Project.AssectService.Exceldata.AssetTypeExcel;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Model.AssetTypeMaster;
import com.Inventory.Project.AssectService.Response.ResponseList;

@Service
public class AssetTypeServiceImpl {

	@Autowired
	private AssetTypeRepositry repo;

	@Autowired
	private AssetTypeExcel assetTypeExcel;

	//
	public Boolean addAssetTypeService(AssetTypeMaster atx) {
		atx.setCreatedOn(new Date());
		atx.setLastmodefiedDate(new Date());
		AssetTypeMaster asset = repo.save(atx);
		if (asset == null) {
			return false;
		}
		return true;
	}

	//
	public AssetTypeMaster getByAssetId(Integer integer) {
		Optional<AssetTypeMaster> optional = repo.findById(integer);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	//
	public ResponseList getAllAssets(int pageno, int pagesize, String sortBy) {
		PageRequest paging = PageRequest.of(pageno, pagesize, Sort.by(sortBy).descending());
		Page<AssetTypeMaster> page = repo.findAll(paging);
		List<AssetTypeMaster> list = page.getContent();
		int totalPages = page.getTotalPages();
		long totalElements = page.getTotalElements();
		ResponseList response = new ResponseList();
		response.setNoOfrecords(totalElements);
		response.setTotalNumberOfPages(totalPages);
		response.setList(list);
		return response;

	}

	
	

	//
	public Boolean updateAssetTypeById(AssetTypeMaster atx) throws RecordNotFoundException {
		atx.setUpdatedOn(new Date());
		atx.setLastmodefiedDate(new Date());
		AssetTypeMaster service = repo.save(atx);
		if (service == null) {
			return false;
		}
		return true;
	}

	//
	public List<AssetTypeMaster> findAssetTyepeBystatus(Boolean status) {
		List<AssetTypeMaster> list = repo.findByAssetStatus(status);
		return list;
	}

	//
	public AssetTypeMaster findAssetTyepeByassettype(String assettype) {
		AssetTypeMaster list = repo.findByassetType(assettype);
		return list;
	}

	public Map<String, Boolean> deleteByid(Integer id) {
		repo.deleteById(id);
		Map<String, Boolean> response = new HashMap<>();
		response.put("Deleted", Boolean.TRUE);
		return response;
	}

	public List<AssetTypeMaster> getAssectType() {
		// TODO Auto-generated method stub
		List<AssetTypeMaster> findAll = repo.findAll();
		return findAll;
	}

	public void save(MultipartFile file, String employeeid) throws IOException, ParseException, RecordNotFoundException {
		List<AssetTypeMaster> fromExcel = assetTypeExcel.readingAssetTypeDataFromExcel(file.getInputStream());
		for (AssetTypeMaster assetTypeMasterEx : fromExcel) {
			
			List<AssetTypeMaster> findAll = repo.findAll();
			
			findAll.forEach(assetType -> {
				if (assetType.getAssetType().equals(assetTypeMasterEx.getAssetType())) {
					throw new DataIntegrityViolationException("Asset Type already exists");
				}
			});

			
			assetTypeMasterEx.setLastmodefiedDate(new Date());
			assetTypeMasterEx.setAssetStatus(true);
			assetTypeMasterEx.setCreatedBy(employeeid);
			assetTypeMasterEx.setCreatedOn(new Date());

			repo.save(assetTypeMasterEx);
		}

	}

	/*
	 * public ResponseList getListOfAssetsSort(Integer pageNo, Integer sizePerPage,
	 * String sortBy) { PageRequest paging = PageRequest.of(pageNo, sizePerPage,
	 * Sort.by(sortBy).descending()); Page<AssetTypeMasterEx> page =
	 * repo.findAll(paging); List<AssetTypeMasterEx> list = page.getContent(); int
	 * totalPages = page.getTotalPages(); long totalElements =
	 * page.getTotalElements(); ResponseList response = new ResponseList();
	 * response.setNoOfrecords(totalElements);
	 * response.setTotalNumberOfPages(totalPages); response.setList(list); return
	 * response;
	 * 
	 * }
	 */

	public ResponseList getListOfAssetsSort(Integer pageNo, Integer sizePerPage, String sortBy) {
		PageRequest paging = PageRequest.of(pageNo, sizePerPage, Sort.by(sortBy).descending());
		Page<AssetTypeMaster> page = repo.findAll(paging);
		List<AssetTypeMaster> list = page.getContent();
		int totalPages = page.getTotalPages();
		long totalElements = page.getTotalElements();
		ResponseList response = new ResponseList();
		response.setNoOfrecords(totalElements);
		response.setTotalNumberOfPages(totalPages);
		response.setList(list);
		return response;

	}

	public ResponseList getAllAssetsPginate(String assettype, int pageno, int pagesize) throws RecordNotFoundException {
		PageRequest paging = PageRequest.of(pageno, pagesize);
		Page<AssetTypeMaster> page = repo.findByAssetTypeContaining(assettype, paging);
		int totalPages = page.getTotalPages();
		System.out.println(totalPages);
		long totalElements = page.getTotalElements();
		System.out.println(totalElements);
		List<AssetTypeMaster> list = page.getContent();
		if(list != null) {
		ResponseList response = new ResponseList();
		response.setNoOfrecords(totalElements);
		response.setTotalNumberOfPages(totalPages);
		response.setList(list);	
		return response;
		}else {
			throw new RecordNotFoundException("No Data present In Assect");
		}
		
	}
}
