package com.Inventory.Project.AssectService.Dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Inventory.Project.AssectService.Model.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Integer> {

	Vendor findByVendorName(String vendorName);
	
	
	List<Vendor> findByVendorStatus(Boolean vendorStatus);
	
	
	 Page<Vendor> findByVendorNameContaining(String vendorName, Pageable pageable);
	
	

}
