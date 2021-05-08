
package com.Inventory.Project.AssectService.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Dao.HardDiskCapacityDao;
import com.Inventory.Project.AssectService.Dao.HardDiskTypeMasterDao;
import com.Inventory.Project.AssectService.Exceldata.HardDiskCapacityExcelData;
import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.HardDiskCapacityNotFoundException;
import com.Inventory.Project.AssectService.Exception.HardDiskNotFoundException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Model.HardDiskCapacity;
import com.Inventory.Project.AssectService.Model.HardDiskTypeMaster;
import com.Inventory.Project.AssectService.Pagination.HardDiskCapacityDaoImpl;
import com.Inventory.Project.AssectService.Pagination.PagenationResponse;
import com.Inventory.Project.AssectService.Response.ResponseList;

@Service

public class HardDiskCapacityImp {

	private static final Logger logger = LogManager.getLogger(HardDiskCapacityImp.class);

	@Autowired
	HardDiskCapacityDao hardDiskCapacityDao;
	@Autowired
	HardDiskCapacityDaoImpl hardDiskCapacityDaoImp;

//	@Autowired
//	HardDiskTypeMasterS hardDiskMasterDao;

	@Autowired
	HardDiskTypeMasterDao diskTypeMasterDao;

	@Autowired
	HardDiskCapacityExcelData hardDiskCapacityExcelData;

	Integer x = null;

	public Boolean saveHardDiskCapacity(HardDiskCapacity hardDiskCapacity, Integer hardDiskTypeId) {

		hardDiskCapacity.setCreatedOn(new Date());
		hardDiskCapacity.setLastmodefiedDate(new Date());

		logger.info("Inserting the HardDiskCapacity Data");

		Optional<HardDiskTypeMaster> findById = diskTypeMasterDao.findById(hardDiskTypeId);
		Set<HardDiskTypeMaster> capa = new HashSet<>();
		capa.add(findById.get());
		hardDiskCapacity.setCapacities(capa);

		HardDiskCapacity DiskCapacity = hardDiskCapacityDao.save(hardDiskCapacity);
		if (DiskCapacity == null) {
			return false;
		} else {
			return true;
		}

	}

	public List<HardDiskCapacity> getListOfHardDiskCapacity(Integer hardDiskTypeId) {
//        logger.info("get all HardDiskCapacity");
		List<HardDiskCapacity> findAll = hardDiskCapacityDao.findByCapacitiesHardDiskTypeId(hardDiskTypeId);
		return findAll;
	}

	public ResponseList getAllHardDiskCapacityList(Integer hardDiskTypeId, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub

		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<HardDiskCapacity> pages = hardDiskCapacityDao.findByCapacitiesHardDiskTypeId(hardDiskTypeId, pageable);
		List<HardDiskCapacity> hardDiskList = pages.getContent();
		int totalPages = pages.getTotalPages();
		long totalElements = pages.getTotalElements();
		ResponseList capacityResponse = new ResponseList();

		capacityResponse.setNoOfrecords(totalElements);
		capacityResponse.setTotalNumberOfPages(totalPages);

		capacityResponse.setList(hardDiskList);

		return capacityResponse;
	}

	public Optional<HardDiskCapacity> getById(int harddiskCapacityId) throws RecordNotFoundException {
		logger.info("getting HardDiskCapacity record by Id");
		Optional<HardDiskCapacity> hardDiskCapacityById = hardDiskCapacityDao.findById(harddiskCapacityId);

		return hardDiskCapacityById;

	}

	public Boolean updateByHardDiskCapacityId(HardDiskCapacity hardDiskCapacity, Integer hardDiskTypeId)
			throws RecordNotFoundException {
		hardDiskCapacity.setUpdatedOn(new Date());
		hardDiskCapacity.setLastmodefiedDate(new Date());
		Optional<HardDiskTypeMaster> findById = diskTypeMasterDao.findById(hardDiskTypeId);
		Set<HardDiskTypeMaster> capa = new HashSet<>();
		if (findById.isPresent()) {
			HardDiskTypeMaster hardDiskTypeMaster = findById.get();

			capa.add(findById.get());
			hardDiskCapacity.setCapacities(capa);

		}
		Optional<HardDiskCapacity> findById2 = hardDiskCapacityDao.findById(hardDiskCapacity.getHarddiskCapacityId());
		HardDiskCapacity diskCapacity = null;
		if (findById2.isPresent()) {

			HardDiskCapacity hardDiskCapacity2 = findById2.get();
			hardDiskCapacity2.setHarddiskCapacityType(hardDiskCapacity.getHarddiskCapacityType());
			hardDiskCapacity2.setHarddiskCapacityStatus(hardDiskCapacity.isHarddiskCapacityStatus());
			hardDiskCapacity2.setCreatedBy(hardDiskCapacity.getCreatedBy());
			hardDiskCapacity2.setCreatedOn(hardDiskCapacity.getCreatedOn());
			hardDiskCapacity2.setUpdatedBy(hardDiskCapacity.getUpdatedBy());
			hardDiskCapacity2.setUpdatedOn(hardDiskCapacity.getUpdatedOn());
			hardDiskCapacity2.setLastmodefiedDate(new Date());
			hardDiskCapacity2.setCapacities(capa);
			diskCapacity = hardDiskCapacityDao.save(hardDiskCapacity2);
		}

		if (diskCapacity == null) {
			return false;
		}

		return true;
	}

	public ResponseList getAllHardDiskCapacityList(Integer hardDiskTypeId, String harddiskCapacityType, Integer pageNo,
			Integer pagSize) {
		Pageable pageable = PageRequest.of(pageNo, pagSize);

		Page<HardDiskCapacity> hardDiskPage = hardDiskCapacityDao
				.findByCapacitiesHardDiskTypeIdAndHarddiskCapacityTypeContaining(hardDiskTypeId, harddiskCapacityType,
						pageable);

		int totalPages = hardDiskPage.getTotalPages();
		long totalElements = hardDiskPage.getTotalElements();
		List<HardDiskCapacity> list = hardDiskPage.getContent();
		ResponseList capacityResponse = new ResponseList();

		capacityResponse.setNoOfrecords(totalElements);
		capacityResponse.setTotalNumberOfPages(totalPages);

		capacityResponse.setList(list);

		return capacityResponse;

	}

	public HardDiskCapacity findByHarddisktypeIdAndHardiskcapacityId(Integer hardDiskTypeId,
			Integer hardDiskcapacityId) {
		Optional<HardDiskCapacity> hdc = hardDiskCapacityDao
				.findByHarddiskCapacityIdAndCapacitiesHardDiskTypeId(hardDiskcapacityId, hardDiskTypeId);
		if (hdc.isPresent()) {
			return hdc.get();
		} else {
			return null;
		}

	}

	public void save(MultipartFile file, String employeeid) throws IOException, HardDiskCapacityNotFoundException, FeildsShouldNotBeEmptyException, RecordNotFoundException, HardDiskNotFoundException {

		List<HardDiskCapacity> hardDiskCapacityFromExcel = hardDiskCapacityExcelData
				.readingHarddiskDataFromExcel(file.getInputStream());

		for (HardDiskCapacity hardDiskCapacity : hardDiskCapacityFromExcel) {

			Set<HardDiskTypeMaster> capacities = hardDiskCapacity.getCapacities();
			capacities.forEach(capac -> {
				String type = capac.getHardDiskType();
				HardDiskTypeMaster diskType = diskTypeMasterDao.findByHardDiskType(type);

				HardDiskCapacity diskCapacity = hardDiskCapacityDao
						.findByHarddiskCapacityTypeAndCapacitiesHardDiskTypeId(
								hardDiskCapacity.getHarddiskCapacityType(), diskType.getHardDiskTypeId());

				if (diskCapacity != null) {
					throw new DataIntegrityViolationException("harddisk capacity already exists");
				}

			});

			hardDiskCapacity.setLastmodefiedDate(new Date());
			hardDiskCapacity.setHarddiskCapacityStatus(true);
			hardDiskCapacity.setCreatedBy(employeeid);
			hardDiskCapacity.setCreatedOn(new Date());
			hardDiskCapacityDao.save(hardDiskCapacity);

		}
	}

	public ResponseList getAllHardDiskCapacityList(Integer hardDiskTypeId, Integer pageNo, Integer pageSize,
			String sortBy) {
		// TODO Auto-generated method stub

		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
		Page<HardDiskCapacity> pages = hardDiskCapacityDao.findByCapacitiesHardDiskTypeId(hardDiskTypeId, pageable);
		List<HardDiskCapacity> hardDiskList = pages.getContent();
		int totalPages = pages.getTotalPages();
		long totalElements = pages.getTotalElements();
		ResponseList capacityResponse = new ResponseList();

		capacityResponse.setNoOfrecords(totalElements);
		capacityResponse.setTotalNumberOfPages(totalPages);

		capacityResponse.setList(hardDiskList);

		return capacityResponse;
	}

}
