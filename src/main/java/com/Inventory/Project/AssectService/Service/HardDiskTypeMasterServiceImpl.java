package com.Inventory.Project.AssectService.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Dao.HardDiskTypeMasterDao;
import com.Inventory.Project.AssectService.Exceldata.HardDiskTypeExcelData;
import com.Inventory.Project.AssectService.Exception.HardDiskNotFoundException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Model.HardDiskTypeMaster;
import com.Inventory.Project.AssectService.Response.ResponseList;

@Service
@Transactional
public class HardDiskTypeMasterServiceImpl implements HardDiskTypeMasterService {

	private static final Logger logger = LogManager.getLogger(HardDiskTypeMasterServiceImpl.class);

	@Autowired
	private HardDiskTypeMasterDao hardDiskTypeMasterDao;

	@Autowired
	private HardDiskTypeExcelData hardDiskExcel;

	@Override
	public Boolean insertHardDisktTpe(HardDiskTypeMaster hardDiskTypeMaster) {

		logger.info("inserting HardDiskType");

		hardDiskTypeMaster.setCreatedOn(new Date());
		hardDiskTypeMaster.setLastmodefiedDate(new Date());

		HardDiskTypeMaster hardDiskTypeMasterObj = hardDiskTypeMasterDao.save(hardDiskTypeMaster);

		if (hardDiskTypeMasterObj == null) {
			return false;
		} else
			return true;

	}

	@Override
	public List<HardDiskTypeMaster> getListOfHardDiskTypes() {
		logger.info("get all HardDiskTypes");
		List<HardDiskTypeMaster> findAll = hardDiskTypeMasterDao.findAll();
		return findAll;
	}

	@Override
	public ResponseList getListOfHardDiskTypes(int pageNo, int sizePerPage) {

		logger.info("getting  HardDiskTypeMaster list");

		Pageable paging = PageRequest.of(pageNo, sizePerPage);

		Page<HardDiskTypeMaster> page = hardDiskTypeMasterDao.findAll(paging);

		List<HardDiskTypeMaster> list = page.getContent();

		int totalPages = page.getTotalPages();

		long totalElements = page.getTotalElements();

		ResponseList HardDiskTypeResponse = new ResponseList();
		HardDiskTypeResponse.setNoOfrecords(totalElements);
		HardDiskTypeResponse.setTotalNumberOfPages(totalPages);
		HardDiskTypeResponse.setList(list);
		return HardDiskTypeResponse;

	}

	@Override
	public ResponseList getListOfHardDiskTypes(String search, int pageNo, int sizePerPage) {

		logger.info("getting  HarddiskType list");

		Pageable paging = PageRequest.of(pageNo, sizePerPage);

		Page<HardDiskTypeMaster> page = hardDiskTypeMasterDao.findByHardDiskTypeContaining(search, paging);

		int totalPages = page.getTotalPages();
		long totalElements = page.getTotalElements();

		List<HardDiskTypeMaster> list = page.getContent();

		ResponseList HardDiskTypeResponse = new ResponseList();
		HardDiskTypeResponse.setNoOfrecords(totalElements);
		HardDiskTypeResponse.setTotalNumberOfPages(totalPages);
		HardDiskTypeResponse.setList(list);
		return HardDiskTypeResponse;

	}

	@Override
	public HardDiskTypeMaster getHardDiskTypeById(Integer hardDiskId) {

		logger.info("getting hardDistType by id");

		Optional<HardDiskTypeMaster> hardDiskTypeMasterObj = hardDiskTypeMasterDao.findById(hardDiskId);

		if (hardDiskTypeMasterObj.isPresent()) {
			return hardDiskTypeMasterObj.get();
		} else {
			return null;
		}
	}

	@Override
	public HardDiskTypeMaster getHardDiskTypeByType(String hardDiskType) {

		logger.info("getting HarddiskType by its type");

		HardDiskTypeMaster hardDiskTypeModel = hardDiskTypeMasterDao.findByHardDiskType(hardDiskType);

		return hardDiskTypeModel;
	}

	@Override
	public List<HardDiskTypeMaster> getHardDiskTypeByStatus(Boolean hardDiskStatus) {

		logger.info("getting list by HardDiskStatus");

		List<HardDiskTypeMaster> list = hardDiskTypeMasterDao.findByHardDiskStatus(hardDiskStatus);

		return list;

	}

	@Override
	public Map<String, Boolean> deleteHardDiskTypeById(Integer hardDiskId) throws RecordNotFoundException {

		logger.info("deleting HarddiskType by its Id");

		hardDiskTypeMasterDao.deleteById(hardDiskId);

		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	@Override
	public Boolean updateHardDiskType(HardDiskTypeMaster hardDiskTypeMaster) throws RecordNotFoundException {

		logger.info("updating HardDiskType");

		hardDiskTypeMaster.setUpdatedOn(new Date());
		hardDiskTypeMaster.setLastmodefiedDate(new Date());

		HardDiskTypeMaster hardDiskTypeMasterObj = hardDiskTypeMasterDao.save(hardDiskTypeMaster);
		if (hardDiskTypeMasterObj == null) {
			return false;
		}

		return true;
	}

	public void save(MultipartFile file, String employeeid) throws IOException, HardDiskNotFoundException, RecordNotFoundException {

		List<HardDiskTypeMaster> hardDiskTypeDataFromExcel = hardDiskExcel
				.readingHardDiskTypeDataFromExcel(file.getInputStream());

		for (HardDiskTypeMaster hardDiskTypeMaster : hardDiskTypeDataFromExcel) {
			
			List<HardDiskTypeMaster> findAll = hardDiskTypeMasterDao.findAll();
			findAll.forEach(harddiskType -> {
				if (harddiskType.getHardDiskType().equals(hardDiskTypeMaster.getHardDiskType())) {
					throw new DataIntegrityViolationException("harddisk type already exists");
				}
			});

			
			
			hardDiskTypeMaster.setLastmodefiedDate(new Date());
			hardDiskTypeMaster.setHardDiskStatus(true);
			hardDiskTypeMaster.setCreatedBy(employeeid);
			hardDiskTypeMaster.setCreatedOn(new Date());
			hardDiskTypeMasterDao.save(hardDiskTypeMaster);
		}

	}

	@Override
	public ResponseList getListOfHardDiskTypes(int pageNo, int sizePerPage, String sortBy) {

		logger.info("getting  HardDiskTypeMaster list");

		Pageable paging = PageRequest.of(pageNo, sizePerPage, Sort.by(sortBy).descending());

		Page<HardDiskTypeMaster> page = hardDiskTypeMasterDao.findAll(paging);

		List<HardDiskTypeMaster> list = page.getContent();

		int totalPages = page.getTotalPages();

		long totalElements = page.getTotalElements();

		ResponseList HardDiskTypeResponse = new ResponseList();
		HardDiskTypeResponse.setNoOfrecords(totalElements);
		HardDiskTypeResponse.setTotalNumberOfPages(totalPages);
		HardDiskTypeResponse.setList(list);
		return HardDiskTypeResponse;

	}

}