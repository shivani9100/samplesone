package com.Inventory.Project.AssectService.Assect;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Inventory.Project.AssectService.Model.Model;

@Repository
public interface AssectDao extends JpaRepository<AssectModel, Long> {

	/* List<AssectModel> findByEmployeeEmployeeid(String employeeid); */

	Page<AssectModel> findBySerialNumberContaining(String serialNumber, Pageable paging);

	// @Query("SELECT a from AssectModel a concat(a.serialNumber) Like %?1%")
	AssectModel findBySerialNumberContaining(String keyword);

	AssectModel findByModels(Model keyword);

	List<AssectModel> findByWarrentEndDateBetween(Date startDate, Date endDate);

	@Query(value = "select a from AssectModel a where a.brand.brandname like %:text% or a.serialNumber like %:text% or a.purchaseDCNo like %:text% or a.vendor.vendorName like %:text% or a.assectType.assetType like %:text% or a.models.modelname like %:text% or a.purchaseInvoiceNo like %:text% or a.cost like %:text% or a.hardDiskCapacity.harddiskCapacityType like %:text% or a.hardDiskType.hardDiskType like %:text% or a.typeOfRam.ramtypeName like %:text% or a.ramCapacity.ramCapacity like %:text% or a.location like %:text% or a.floor like %:text% ")
	Page<AssectModel> fullTextSearch(String text, Pageable pageable);
	
	

}
