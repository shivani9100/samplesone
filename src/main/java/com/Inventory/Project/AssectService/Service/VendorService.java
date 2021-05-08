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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.Inventory.Project.AssectService.Dao.VendorRepository;
import com.Inventory.Project.AssectService.Exceldata.VenderExcelData;
import com.Inventory.Project.AssectService.Exception.FeildsShouldNotBeEmptyException;
import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Exception.ResourceNotFoundException;
import com.Inventory.Project.AssectService.Model.Vendor;
import com.Inventory.Project.AssectService.Response.ResponseList;

@Service
@Transactional
public class VendorService {

	private static final Logger logger = LogManager.getLogger(VendorService.class);

	@Autowired
	private VendorRepository vendorRepository;

	@Autowired
	private VenderExcelData vendorExcel;

	public Boolean insertVendor(Vendor vendor) {

		logger.info("inserting vendor");
		vendor.setLastmodefiedDate(new Date());
		vendor.setCreatedOn(new Date());
		Vendor vendor2 = vendorRepository.save(vendor);
		if (vendor2 == null) {
			return false;
		} else
			return true;

	}

	public List<Vendor> getVendorByStatus(Boolean vendorstatus) {

		logger.info("getting list by vendorstatus");
		List<Vendor> list = vendorRepository.findByVendorStatus(vendorstatus);
		return list;

	}

	public ResponseList getListOfVendors(int pageNo, int sizePerPage) {

		logger.info("getting  vendor list");

		Pageable paging = PageRequest.of(pageNo, sizePerPage);

		Page<Vendor> page = vendorRepository.findAll(paging);

		List<Vendor> list = page.getContent();

		int totalPages = page.getTotalPages();

		long totalElements = page.getTotalElements();

		ResponseList vendorResponse = new ResponseList();
		vendorResponse.setNoOfrecords(totalElements);
		vendorResponse.setTotalNumberOfPages(totalPages);
		vendorResponse.setList(list);
		return vendorResponse;

	}

	public List<Vendor> getAllVendor() {
		List<Vendor> findAll = vendorRepository.findAll();
		return findAll;
	}

	public Vendor getVendorById(Integer id) {

		Optional<Vendor> vendor = vendorRepository.findById(id);

		if (vendor.isPresent()) {
			return vendor.get();
		} else {
			return null;
		}

	}

	public Vendor getVendorByName(String name) {

		logger.info("getting  vendor by name");

		Vendor vendor = vendorRepository.findByVendorName(name);

		return vendor;
	}

	public Boolean upateVendor(Vendor vendor) throws ResourceNotFoundException {

		vendor.setUpdatedOn(new Date());
		vendor.setLastmodefiedDate(new Date());

		Vendor vendor1 = vendorRepository.save(vendor);
		if (vendor1 == null) {
			return false;
		}

		return true;
	}

	public Map<String, Boolean> deleteVendorById(Integer id) throws ResourceNotFoundException {
		vendorRepository.deleteById(id);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;

	}

	public ResponseList getListOfVendors(String search, int pageNo, int sizePerPage) {

		logger.info("getting  vendor list");

		Pageable paging = PageRequest.of(pageNo, sizePerPage);

		Page<Vendor> page = vendorRepository.findByVendorNameContaining(search, paging);

		int totalPages = page.getTotalPages();

		long totalElements = page.getTotalElements();

		List<Vendor> list = page.getContent();

		ResponseList vendorResponse = new ResponseList();
		vendorResponse.setNoOfrecords(totalElements);
		vendorResponse.setTotalNumberOfPages(totalPages);
		vendorResponse.setList(list);
		return vendorResponse;

	}

	public void save(MultipartFile file, String employeeid)
			throws IOException, FeildsShouldNotBeEmptyException, RecordNotFoundException {

		List<Vendor> vendorDataFromExcelFile = vendorExcel.readingVendorDataFromExcelFile(file.getInputStream());

		for (Vendor vendor : vendorDataFromExcelFile) {
			vendor.setLastmodefiedDate(new Date());
			vendor.setVendorStatus(true);
			vendor.setCreatedBy(employeeid);
			vendor.setCreatedOn(new Date());
			vendorRepository.save(vendor);
		}

	}

	public ResponseList getListOfVendors(int pageNo, int sizePerPage, String sortBy) {

		logger.info("getting  vendor list");

		Pageable paging = PageRequest.of(pageNo, sizePerPage, Sort.by(sortBy).descending());

		Page<Vendor> page = vendorRepository.findAll(paging);

		List<Vendor> list = page.getContent();

		int totalPages = page.getTotalPages();

		long totalElements = page.getTotalElements();

		ResponseList vendorResponse = new ResponseList();
		vendorResponse.setNoOfrecords(totalElements);
		vendorResponse.setTotalNumberOfPages(totalPages);
		vendorResponse.setList(list);
		return vendorResponse;

	}

}
