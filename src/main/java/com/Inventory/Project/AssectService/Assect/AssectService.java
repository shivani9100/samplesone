package com.Inventory.Project.AssectService.Assect;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Dao.AssetTypeRepositry;
import com.Inventory.Project.AssectService.Dao.BrandRepo;
import com.Inventory.Project.AssectService.Dao.HardDiskCapacityDao;
import com.Inventory.Project.AssectService.Dao.HardDiskTypeMasterDao;
import com.Inventory.Project.AssectService.Dao.ModelRepo;
import com.Inventory.Project.AssectService.Dao.RamCapacityDAO;
import com.Inventory.Project.AssectService.Dao.RamTypeDao;
import com.Inventory.Project.AssectService.Dao.VendorRepository;
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
import com.Inventory.Project.AssectService.Model.AssetTypeMaster;
import com.Inventory.Project.AssectService.Model.Brand;
import com.Inventory.Project.AssectService.Model.HardDiskCapacity;
import com.Inventory.Project.AssectService.Model.HardDiskTypeMaster;
import com.Inventory.Project.AssectService.Model.Model;
import com.Inventory.Project.AssectService.Model.RamCapacityMaster;
import com.Inventory.Project.AssectService.Model.RamTypeMaster;
import com.Inventory.Project.AssectService.Model.Vendor;
import com.Inventory.Project.AssectService.Pagination.AssetDaoImpl;
import com.Inventory.Project.AssectService.Pagination.PagenationResponse;
import com.Inventory.Project.AssectService.Response.ResponseList;

@Service
public class AssectService {

	@Autowired
	private BrandRepo brandmasterdao;
	@Autowired
	private HardDiskTypeMasterDao harddisktypedao;
	@Autowired
	private VendorRepository vendordao;
	@Autowired
	private ModelRepo modelservicedao;
	@Autowired
	private AssectDao assectDao;
	@Autowired
	private HardDiskCapacityDao harddiskcapacityDao;
	@Autowired
	private RamTypeDao ramtypedao;
	@Autowired
	private RamCapacityDAO ramCapacityDAO;
	@Autowired
	private AssetTypeRepositry assettypedao;
	@Autowired
	private AssetDaoImpl assetDaoimpl;

	@Autowired
	ExcelFileExport excelfileexport;

	public List<Brand> getBrandNames() throws ResourceNotFoundException {
		List<Brand> brands = brandmasterdao.findAll();
		if (brands != null) {
			return brands;
		} else {
			return null;
		}

	}

	public List<Model> getModelNames() throws ResourceNotFoundException {
		List<Model> models = modelservicedao.findAll();
		if (models != null) {
			return models;
		} else {
			return null;
		}

	}

	public List<AssectModel> getAllAssets() {

		List<AssectModel> findAll = assectDao.findAll();

		return findAll;
	}

	public List<Vendor> getVendors() throws ResourceNotFoundException {
		List<Vendor> vendors = vendordao.findAll();
		if (vendors != null) {
			return vendors;
		} else {
			return null;
		}

	}

	public List<HardDiskTypeMaster> getHardDiskType() throws ResourceNotFoundException {
		List<HardDiskTypeMaster> harddisktype = harddisktypedao.findAll();
		if (harddisktype != null) {
			return harddisktype;
		} else {
			return null;
		}
	}

	public List<HardDiskCapacity> getHardDiskCapacity() throws ResourceNotFoundException {
		List<HardDiskCapacity> harddiskcapacity = harddiskcapacityDao.findAll();
		if (harddiskcapacity != null) {
			return harddiskcapacity;
		} else {
			return null;
		}

	}

	public ResponseList getAllAssect1(Integer pageno, Integer pagesize) throws ResourceNotFoundException {
		PageRequest paging = PageRequest.of(pageno, pagesize);
		Page<AssectModel> page = assectDao.findAll(paging);
		List<AssectModel> list = page.getContent();
		long totalElements = page.getTotalElements();
		int totalPages = page.getTotalPages();
		ResponseList responseList = new ResponseList();
		responseList.setTotalNumberOfPages(totalPages);
		responseList.setNoOfrecords(totalElements);
		responseList.setList(list);
		return responseList;

	}

//	public ResponseList getAllAssect(Integer pageno, Integer pagesize, String searchBy) {
//		PageRequest paging = PageRequest.of(pageno, pagesize);
//		Page<AssectModel> page = assectDao.findBySerialNumberContaining(searchBy, paging);
//		List<AssectModel> list = page.getContent();
//		long totalElements = page.getTotalElements();
//		int totalPages = page.getTotalPages();
//		ResponseList responseList = new ResponseList();
//		responseList.setTotalNumberOfPages(totalPages);
//		responseList.setNoOfrecords(totalElements);
//		responseList.setList(list);
//		return responseList;
//	}

	public List<RamTypeMaster> getRamType() throws ResourceNotFoundException {
		List<RamTypeMaster> ramtype = ramtypedao.findAll();
		if (ramtype != null) {
			return ramtype;
		} else {
			return null;
		}

	}

	public List<RamCapacityMaster> getRamCapacity() throws ResourceNotFoundException {
		List<RamCapacityMaster> ramcapacity = ramCapacityDAO.findAll();
		if (ramcapacity != null) {
			return ramcapacity;
		} else {
			return null;
		}

	}

	public List<AssetTypeMaster> getAssectType() throws ResourceNotFoundException {
		List<AssetTypeMaster> assecttype = assettypedao.findAll();
		if (assecttype != null) {
			return assecttype;
		} else {
			return null;
		}

	}

	/*
	 * public ResponseEntity<?> getAllAssects() { List<AssectModel> list =
	 * assectDao.findAll(); if (list != null) { return new ResponseEntity<>(list,
	 * HttpStatus.PROCESSING); } else { String string = new String("No Data Found");
	 * return new ResponseEntity<String>(string, HttpStatus.EXPECTATION_FAILED); }
	 * 
	 * }
	 */

	public AssectModel getByAssectid(long assectid) throws RecordNotFoundException {
		Optional<AssectModel> optional = assectDao.findById(assectid);
		if (optional.isPresent()) {
			return optional.get();
		} else {
			return null;
		}

	}

	public ResponseList getAllAssect(String text, Integer pageno, Integer pagesize) {
		PageRequest paging = PageRequest.of(pageno, pagesize);
		Page page = assectDao.fullTextSearch(text, paging);
		List<AssectModel> list = page.getContent();
		long totalElements = page.getTotalElements();
		int totalPages = page.getTotalPages();
		ResponseList responseList = new ResponseList();
		responseList.setTotalNumberOfPages(totalPages);
		responseList.setNoOfrecords(totalElements);
		responseList.setList(list);
		return responseList;
	}

//	public ResponseList getAllAssect(Integer pageno, Integer pagesize, String sortBy) {
//
//		Pageable pageable = PageRequest.of(pageno, pagesize, Sort.by(sortBy).descending());
//
//		Page<AssectModel> page = assectDao.findAll(pageable);
//		List<AssectModel> list = page.getContent();
//		long totalElements = page.getTotalElements();
//		int totalPages = page.getTotalPages();
//		ResponseList responseList = new ResponseList();
//		responseList.setTotalNumberOfPages(totalPages);
//		responseList.setNoOfrecords(totalElements);
//		responseList.setList(list);
//		return responseList;
//	}

	public Map<String, Boolean> deleteByAssectid(long assectid) throws RecordNotFoundException {
		Optional<AssectModel> optional = assectDao.findById(assectid);
		if (optional.isPresent()) {
			Map<String, Boolean> map = new HashMap<>();
			assectDao.delete(optional.get());
			map.put("Deleted SuccessFully", Boolean.TRUE);
			return map;
		}
		return null;
	}

	public Boolean addAssect(AssectModelRequest assectModel) {

		AssectModel assectModel2 = new AssectModel();

		assectModel2.setAssectType(assettypedao.findById(assectModel.getAssetType()).get());
		assectModel2.setBrand(brandmasterdao
				.findByBrandidAndAssetTypeMasterExAssetId(assectModel.getBrand(), assectModel.getAssetType()).get());

		assectModel2.setModels(
				modelservicedao.findByModelidAndBrandBrandid(assectModel.getModels(), assectModel.getBrand()).get());

		assectModel2.setVendor(vendordao.findById(assectModel.getVendor()).get());
		assectModel2.setSerialNumber(assectModel.getSerialNumber());
		assectModel2.setPurchaseDCNo(assectModel.getPurchaseDCNo());
		assectModel2.setWarrentStartDate(assectModel.getWarrentStartDate());
		assectModel2.setWarrentEndDate(assectModel.getWarrentEndDate());

		assectModel2.setPurchaseDate(assectModel.getPurchaseDate());
		assectModel2.setPurchaseInvoiceNo(assectModel.getPurchaseInvoiceNo());

		assectModel2.setCost(assectModel.getCost());

		assectModel2.setInvoiceDoc(assectModel.getInvoiceDoc());
		assectModel2.setWarantyDoc(assectModel.getWarantyDoc());

		assectModel2.setLocation(assectModel.getLocation());
		assectModel2.setFloor(assectModel.getFloor());

		assectModel2.setCreatedBy(assectModel.getCreatedBy());
		assectModel2.setUpdateBy(assectModel.getUpdateBy());

		assectModel2.setCreatedOn(assectModel.getCreatedOn());
		assectModel2.setUpdatedOn(assectModel.getUpdatedOn());

		if (assectModel.getHardDiskCapacity() != null && assectModel.getHardDiskType() != null

				&& assectModel.getNoOfHardDisks() != null) {

			assectModel2.setHardDiskCapacity(harddiskcapacityDao.findById(assectModel.getHardDiskCapacity()).get());
			assectModel2.setHardDiskType(harddisktypedao.findById(assectModel.getHardDiskType()).get());
			assectModel2.setNoOfHardDisks(assectModel.getNoOfHardDisks());
		}

		if (assectModel.getRamCapacity() != null && assectModel.getTypeOfRam() != null
				&& assectModel.getNoOfRams() != null) {

			assectModel2.setRamCapacity(ramCapacityDAO.findById(assectModel.getRamCapacity()).get());
			assectModel2.setTypeOfRam(ramtypedao.findById(assectModel.getTypeOfRam()).get());
			assectModel2.setNoOfRams(assectModel.getNoOfRams());
			assectModel2.setLastmodifiedDate(new Date());
		}

		AssectModel save = assectDao.save(assectModel2);
		if (save == null) {
			return false;
		}
		return true;
	}

	public Boolean updateByid(long assectid, AssectModelRequest assectModel) throws ResourceNotFoundException {

		Optional<AssectModel> optional = assectDao.findById(assectid);

		if (!optional.isPresent()) {
			System.out.println("not present");
		}
		AssectModel assectmodel3 = null;
		if (optional.isPresent()) {
			AssectModel assectModel2 = optional.get();
			assectModel2.setAssectid(assectid);

			if (assettypedao.findById(assectModel.getAssetType()).isPresent()) {
				System.out.println("assetType");
				assectModel2.setAssectType(assettypedao.findById(assectModel.getAssetType()).get());
			}

			if (brandmasterdao
					.findByBrandidAndAssetTypeMasterExAssetId(assectModel.getBrand(), assectModel.getAssetType())
					.isPresent()) {
				System.out.println("brand");
				assectModel2.setBrand(brandmasterdao
						.findByBrandidAndAssetTypeMasterExAssetId(assectModel.getBrand(), assectModel.getAssetType())
						.get());
			}

			if (modelservicedao.findByModelidAndBrandBrandid(assectModel.getModels(), assectModel.getBrand())
					.isPresent()) {
				System.out.println("model");
				assectModel2.setModels(modelservicedao
						.findByModelidAndBrandBrandid(assectModel.getModels(), assectModel.getBrand()).get());
			}

			assectModel2.setCost(assectModel.getCost());
			assectModel2.setFloor(assectModel.getFloor());

			assectModel2.setInvoiceDoc(assectModel.getInvoiceDoc());
			assectModel2.setLocation(assectModel.getLocation());

			assectModel2.setPurchaseDCNo(assectModel.getPurchaseDCNo());
			assectModel2.setPurchaseDate(assectModel.getPurchaseDate());
			assectModel2.setPurchaseInvoiceNo(assectModel.getPurchaseDCNo());

			assectModel2.setSerialNumber(assectModel.getSerialNumber());

			if (vendordao.findById(assectModel.getVendor()).isPresent()) {
				assectModel2.setVendor(vendordao.findById(assectModel.getVendor()).get());
			}
			assectModel2.setWarantyDoc(assectModel.getWarantyDoc());
			assectModel2.setWarrentEndDate(assectModel.getWarrentEndDate());
			assectModel2.setWarrentStartDate(assectModel.getWarrentStartDate());
			if (assectModel.getHardDiskCapacity() != null && assectModel.getHardDiskType() != null

					&& assectModel.getNoOfHardDisks() != null) {

				if (harddiskcapacityDao.findById(assectModel.getHardDiskCapacity()).isPresent()) {

					assectModel2
							.setHardDiskCapacity(harddiskcapacityDao.findById(assectModel.getHardDiskCapacity()).get());
				}
				if (harddisktypedao.findById(assectModel.getHardDiskType()).isPresent()) {

					assectModel2.setHardDiskType(harddisktypedao.findById(assectModel.getHardDiskType()).get());
				}

				assectModel2.setNoOfHardDisks(assectModel.getNoOfHardDisks());

			}
			if (assectModel.getRamCapacity() != null && assectModel.getTypeOfRam() != null
					&& assectModel.getNoOfRams() != null) {

				if (ramtypedao.findById(assectModel.getTypeOfRam()).isPresent()) {

					assectModel2.setTypeOfRam(ramtypedao.findById(assectModel.getTypeOfRam()).get());
				}
				if (ramCapacityDAO.findById(assectModel.getRamCapacity()).isPresent()) {

					assectModel2.setRamCapacity(ramCapacityDAO.findById(assectModel.getRamCapacity()).get());
				}
				assectModel2.setNoOfRams(assectModel.getNoOfRams());

			}

			assectModel2.setCreatedBy(assectModel.getCreatedBy());
			// assectModel2.setUpdateBy(assectModel.getUpdateBy());
			assectModel2.setCreatedOn(assectModel.getCreatedOn());
			// assectModel2.setUpdatedOn(assectModel.getUpdatedOn());
			assectModel2.setLastmodifiedDate(new Date());
			assectmodel3 = assectDao.save(assectModel2);

		}
		if (assectmodel3 == null) {
			return false;
		} else {
			return true;
		}

	}

	public ResponseList getAllAssect(Integer pageno, Integer pagesize) throws ResourceNotFoundException {
		Pageable paging = PageRequest.of(pageno, pagesize, Sort.by("lastmodifiedDate").descending());
		Page<AssectModel> page = assectDao.findAll(paging);
		List<AssectModel> list = page.getContent();
		int totalPages = page.getTotalPages();
		long totalElements = page.getTotalElements();
		ResponseList response = new ResponseList();
		response.setNoOfrecords(totalElements);
		response.setTotalNumberOfPages(totalPages);
		response.setList(list);
		return response;

	}

	public List<AssectModel> getAssetsBasedOnWarrantyBetween(Date startDate, Date endDate) {

		return assectDao.findByWarrentEndDateBetween(startDate, endDate);

	}

	/*
	 * public ResponseList getAllAssectBySearch(String text, int pageno, int
	 * pagesize) { PageRequest paging = PageRequest.of(pageno, pagesize);
	 * Page<AssectModel> page = assectDao.fullTextSearch(text, paging);
	 * List<AssectModel> list = page.getContent(); int totalPages =
	 * page.getTotalPages(); long totalElements = page.getTotalElements();
	 * ResponseList response = new ResponseList();
	 * response.setNoOfrecords(totalElements);
	 * response.setTotalNumberOfPages(totalPages); response.setList(list); return
	 * response; }
	 */
	public ResponseList getAllAssect(Integer pageno, Integer pagesize, String sortBy) throws ResourceNotFoundException {
		Pageable paging = PageRequest.of(pageno, pagesize, Sort.by(sortBy).descending());
		Page<AssectModel> page = assectDao.findAll(paging);
		List<AssectModel> list = page.getContent();
		int totalPages = page.getTotalPages();
		long totalElements = page.getTotalElements();
		ResponseList response = new ResponseList();
		response.setNoOfrecords(totalElements);
		response.setTotalNumberOfPages(totalPages);
		response.setList(list);
		return response;

	}

	public ResponseList getAllAssectBySearch(String text, int pageno, int pagesize) {
		PageRequest paging = PageRequest.of(pageno, pagesize, Sort.by("lastmodifiedDate").descending());
		Page<AssectModel> page = assectDao.fullTextSearch(text, paging);
		List<AssectModel> list = page.getContent();
		int totalPages = page.getTotalPages();
		long totalElements = page.getTotalElements();
		ResponseList response = new ResponseList();
		response.setNoOfrecords(totalElements);
		response.setTotalNumberOfPages(totalPages);
		response.setList(list);
		return response;
	}

	public void save(MultipartFile file, String employeeid)
			throws IOException, FeildsShouldNotBeEmptyException, RamCapacityNotFoundException,
			HardDiskNotFoundException, HardDiskCapacityNotFoundException, RamTypeNotFoundException,
			RecordNotFoundException, AssetTypeNotFoundException, BrandTypeNotFoundException, ModelNotFoundException {
		List<AssectModel> fromExcel = excelfileexport.uploadingExcel(file.getInputStream());
		for (AssectModel asset : fromExcel) {
			asset.setLastmodifiedDate(new Date());

			asset.setCreatedBy(employeeid);
			asset.setCreatedOn(new Date());

			assectDao.save(asset);

		}

	}

	public PagenationResponse getAllAssetData(Integer pageNo, Integer pageSize, String searchBy) {
		return assetDaoimpl.getAllAssetData(pageNo, pageSize, searchBy);
	}
}