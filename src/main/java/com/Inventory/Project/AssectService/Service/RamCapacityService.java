package com.Inventory.Project.AssectService.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Dao.RamCapacityDAO;
import com.Inventory.Project.AssectService.Dao.RamTypeDao;
import com.Inventory.Project.AssectService.Exceldata.RamCapacityExcelData;
import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.RamTypeNotFoundException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Model.HardDiskCapacity;
import com.Inventory.Project.AssectService.Model.HardDiskTypeMaster;
import com.Inventory.Project.AssectService.Model.RamCapacityMaster;
import com.Inventory.Project.AssectService.Model.RamTypeMaster;
import com.Inventory.Project.AssectService.Response.ResponseList;

@Service
public class RamCapacityService {

	@Autowired
	private RamCapacityDAO capacityDAO;

	@Autowired
	private RamTypeDao ramTypeDao;
	@Autowired
	RamCapacityExcelData ramCapacityExcel;

	public Boolean saveRamDeatils(RamCapacityMaster master, Integer ramTypeId) {
		master.setCreatedOn(new Date());
		master.setLastmodefiedDate(new Date());
		Optional<RamTypeMaster> ramTypeMaster = ramTypeDao.findById(ramTypeId);
		List<RamTypeMaster> ramMaster = new ArrayList<>();
		if (ramTypeMaster.isPresent()) {
			ramMaster.add(ramTypeMaster.get());
		}
		master.setRamTypeMasters(ramMaster);
		RamCapacityMaster ram = capacityDAO.save(master);
		if (ram == null) {
			return false;
		} else {
			return true;
		}
	}

	public List<RamCapacityMaster> getListOfRamMaster() {

		List<RamCapacityMaster> findAll = capacityDAO.findAll();
		return findAll;
	}

	public List<RamCapacityMaster> getRamDetailsByStatus(Boolean status) {

		List<RamCapacityMaster> list = capacityDAO.findByRamStatus(status);
		return list;

	}

	public List<RamCapacityMaster> getRamByRamTypeId(Integer ramTypeId) {
		return capacityDAO.findByRamTypeMastersRamtypeId(ramTypeId);
	}

	public ResponseList getAllRamDetails(Integer pageNo, Integer sizePerPage) {
		Pageable paging = PageRequest.of(pageNo, sizePerPage);

		Page<RamCapacityMaster> page = capacityDAO.findAll(paging);

		List<RamCapacityMaster> list = page.getContent();

		int totalPages = page.getTotalPages();

		long totalElements = page.getTotalElements();

		ResponseList response = new ResponseList();
		response.setNoOfrecords(totalElements);
		response.setTotalNumberOfPages(totalPages);
		response.setList(list);
		return response;
	}

	public ResponseList getAllRamDetails(String search, int pageNo, int sizePerPage) {

		Pageable paging = PageRequest.of(pageNo, sizePerPage);

		Page<RamCapacityMaster> page = capacityDAO.findByRamCapacityContaining(search, paging);

		int totalPages = page.getTotalPages();
		System.out.println(totalPages);
		long totalElements = page.getTotalElements();
		System.out.println(totalElements);
		List<RamCapacityMaster> list = page.getContent();

		ResponseList response = new ResponseList();
		response.setNoOfrecords(totalElements);
		response.setTotalNumberOfPages(totalPages);
		response.setList(list);
		return response;

	}

	public RamCapacityMaster getRamDetailsById(Integer id) {
		Optional<RamCapacityMaster> master = capacityDAO.findById(id);

		if (master.isPresent()) {
			return master.get();
		} else {
			return null;
		}
	}

	public ResponseList getAllRamMaster(Integer ramTypeId, Integer pageno, Integer pagesize, String searchby) {

		Pageable pageale = PageRequest.of(pageno, pagesize);

		Page<RamCapacityMaster> findAll = capacityDAO.findByRamTypeMastersRamtypeIdAndRamCapacityContaining(ramTypeId,
				searchby, pageale);

		ResponseList responseList = new ResponseList();
		responseList.setList(findAll.getContent());
		responseList.setTotalNumberOfPages(findAll.getTotalPages());
		responseList.setNoOfrecords(findAll.getTotalElements());

		return responseList;

	}

	public ResponseList getAllOfRamDetails(Integer ramtypeId, Integer pageno, Integer pagesize) {
		Pageable pageale = PageRequest.of(pageno, pagesize);

		Page<RamCapacityMaster> findAll = capacityDAO.findByRamTypeMastersRamtypeId(ramtypeId, pageale);

		ResponseList responseList = new ResponseList();
		responseList.setList(findAll.getContent());
		responseList.setTotalNumberOfPages(findAll.getTotalPages());
		responseList.setNoOfrecords(findAll.getTotalElements());

		return responseList;
	}

	public List<RamCapacityMaster> getRamCapacityByRamType(int id) {

		List<RamCapacityMaster> ram = capacityDAO.findByRamTypeMastersRamtypeId(id);

		return ram;

	}

	public RamCapacityMaster getRamByRamTypeIdRamId(Integer ramTypeId, Integer ramId) {

		Optional<RamCapacityMaster> optional = capacityDAO.findByRamIdAndRamTypeMastersRamtypeId(ramId, ramTypeId);

		if (optional.isPresent()) {
			return optional.get();
		}

		return null;
	}

	public Boolean updateRamDetailsById(RamCapacityMaster master, Integer ramTypeId, Integer ramId)
			throws RecordNotFoundException {
		master.setUpdatedOn(new Date());
		Optional<RamCapacityMaster> ramMaster = capacityDAO.findById(ramId);
		RamCapacityMaster ramMaster1 = null;

		if (ramMaster.isPresent()) {
			RamCapacityMaster ram = ramMaster.get();
			ram.setRamCapacity(master.getRamCapacity());
			ram.setRamStatus(master.getRamStatus());
			ram.setCreatedBy(master.getCreatedBy());
			ram.setCreatedOn(master.getCreatedOn());
			ram.setUpdatedBy(master.getUpdatedBy());
			ram.setUpdatedOn(master.getUpdatedOn());
			ram.setLastmodefiedDate(new Date());
			ramMaster1 = capacityDAO.save(ram);
		}

		if (ramMaster1 == null) {
			return false;
		} else {
			return true;
		}

	}

	public ResponseEntity<?> deleteByRamId(Integer ramId, Integer ramTypeId) throws RecordNotFoundException {
		return capacityDAO.findByRamIdAndRamTypeMastersRamtypeId(ramId, ramTypeId).map(master -> {
			capacityDAO.delete(master);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new RecordNotFoundException(
				"Course not found with id " + ramId + " and instructorId " + ramTypeId));
	}

	public RamCapacityMaster findByRamIdRamTypeId(Integer ramId, Integer ramTypeId) {
		Optional<RamCapacityMaster> optional = capacityDAO.findByRamIdAndRamTypeMastersRamtypeId(ramId, ramTypeId);
		if (optional.isPresent()) {
			return optional.get();
		}

		return null;

	}

	public void save(MultipartFile file, String employeeid) throws IOException, RamTypeNotFoundException, RecordNotFoundException, FeildsShouldNotBeEmptyException {

		List<RamCapacityMaster> ramCapacityFromExcel = ramCapacityExcel
				.readingRamCapacityDataFromExcel(file.getInputStream());

//	        Optional<RamTypeMaster> optional = ramTypeDao.findById(ramTypeid);
//	       
//	        ArrayList<RamTypeMaster> list = new ArrayList<RamTypeMaster>();
//	       
//	        list.add(optional.get());
		//

		for (RamCapacityMaster ramMaster : ramCapacityFromExcel) {

			List<RamTypeMaster> ramTypeMasters = ramMaster.getRamTypeMasters();
			ramTypeMasters.forEach(capac -> {
				String type = capac.getRamtypeName();
				 
				RamTypeMaster ramTypeMaster = ramTypeDao.findByRamtypeName(type);

				
				RamCapacityMaster ramCapacityMaster = capacityDAO.findByRamCapacityAndRamTypeMastersRamtypeId(
								ramMaster.getRamCapacity(), ramTypeMaster.getRamtypeId());
				
				System.out.println(ramMaster.getRamCapacity());
				System.out.println(ramTypeMaster.getRamtypeId());

				if (ramCapacityMaster != null) {
					throw new DataIntegrityViolationException("ram capacity already exists");
				}

			});
			
			
			
			
			ramMaster.setLastmodefiedDate(new Date());
			ramMaster.setCreatedBy(employeeid);
			ramMaster.setCreatedOn(new Date());
			ramMaster.setRamStatus(true);
			capacityDAO.save(ramMaster);

		}

	}

	public ResponseList getAllRamDetails(Integer ramtypeId,Integer pageNo, Integer sizePerPage, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, sizePerPage, Sort.by(sortBy).descending());

		Page<RamCapacityMaster> page = capacityDAO.findByRamTypeMastersRamtypeId(ramtypeId,paging);

		List<RamCapacityMaster> list = page.getContent();

		int totalPages = page.getTotalPages();

		long totalElements = page.getTotalElements();

		ResponseList response = new ResponseList();
		response.setNoOfrecords(totalElements);
		response.setTotalNumberOfPages(totalPages);
		response.setList(list);
		return response;
	}
}