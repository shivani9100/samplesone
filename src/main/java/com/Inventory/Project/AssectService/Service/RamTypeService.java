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

import com.Inventory.Project.AssectService.Dao.RamTypeDao;
import com.Inventory.Project.AssectService.Exceldata.RamTypeExcelData;
import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Model.Model;
import com.Inventory.Project.AssectService.Model.RamTypeMaster;
import com.Inventory.Project.AssectService.Response.ResponseList;

@Service
public class RamTypeService {
	@Autowired
	private RamTypeDao ramTypeDao;
	@Autowired
	RamTypeExcelData ramTypeExcel;

	public Boolean saveRamDeatils(RamTypeMaster master) {
		master.setCreatedOn(new Date());
		master.setLastmodefiedDate(new Date());
		RamTypeMaster ram = ramTypeDao.save(master);
		if (ram == null) {
			return false;
		} else {
			return true;
		}
	}

	public List<RamTypeMaster> getListOfRamType() {
		List<RamTypeMaster> findAll = ramTypeDao.findAll();
		return findAll;
	}

	public List<RamTypeMaster> getRamDetailsByStatus(Boolean status) {

		List<RamTypeMaster> list = ramTypeDao.findByRamtypeStatus(status);
		return list;

	}

	public ResponseList getAllRamDetails(int pageNo, int sizePerPage) {
		Pageable paging = PageRequest.of(pageNo, sizePerPage);

		Page<RamTypeMaster> page = ramTypeDao.findAll(paging);

		List<RamTypeMaster> list = page.getContent();

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

		Page<RamTypeMaster> page = ramTypeDao.findByRamtypeNameContaining(search, paging);

		int totalPages = page.getTotalPages();
		System.out.println(totalPages);
		long totalElements = page.getTotalElements();
		System.out.println(totalElements);
		List<RamTypeMaster> list = page.getContent();

		ResponseList response = new ResponseList();
		response.setNoOfrecords(totalElements);
		response.setTotalNumberOfPages(totalPages);
		response.setList(list);
		return response;

	}

	public RamTypeMaster getRamDetailsById(Integer id) {
		Optional<RamTypeMaster> master = ramTypeDao.findById(id);
		if (master.isPresent()) {
			return master.get();
		} else {
			return null;
		}
	}

	public Boolean updateRamDetailsById(RamTypeMaster master) throws RecordNotFoundException {
		master.setUpdatedOn(new Date());
		master.setLastmodefiedDate(new Date());
		RamTypeMaster ramMaster = ramTypeDao.save(master);
		if (ramMaster == null) {
			return false;
		}

		return true;
	}

	public Map<String, Boolean> deleteByRamId(Integer id) throws RecordNotFoundException {
		ramTypeDao.deleteById(id);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	public void save(MultipartFile file, String employeeid) throws IOException, RecordNotFoundException, FeildsShouldNotBeEmptyException {

		List<RamTypeMaster> ramTypeMasterFromExcel = ramTypeExcel.readingRamTypeMasterFromExcel(file.getInputStream());

		for (RamTypeMaster ramTypeMaster : ramTypeMasterFromExcel) {

			List<RamTypeMaster> findAll = ramTypeDao.findAll();

			findAll.forEach(ramType -> {
				if (ramType.getRamtypeName().equals(ramTypeMaster.getRamtypeName())) {
					throw new DataIntegrityViolationException("ram type already exists");
				}
			});	

			ramTypeMaster.setLastmodefiedDate(new Date());
			ramTypeMaster.setCreatedBy(employeeid);
			ramTypeMaster.setCreatedOn(new Date());
			ramTypeMaster.setRamtypeStatus(true);
			ramTypeDao.save(ramTypeMaster);
		}

	}

	public ResponseList getAllRamDetails(int pageNo, int sizePerPage, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, sizePerPage, Sort.by(sortBy).descending());

		Page<RamTypeMaster> page = ramTypeDao.findAll(paging);

		List<RamTypeMaster> list = page.getContent();

		int totalPages = page.getTotalPages();

		long totalElements = page.getTotalElements();

		ResponseList response = new ResponseList();
		response.setNoOfrecords(totalElements);
		response.setTotalNumberOfPages(totalPages);
		response.setList(list);
		return response;
	}

}