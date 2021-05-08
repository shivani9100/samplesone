package com.Inventory.Project.AssectService.Dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Inventory.Project.AssectService.Model.RamTypeMaster;

@Repository
public interface RamTypeDao extends JpaRepository<RamTypeMaster, Integer> {
	List<RamTypeMaster> findByRamtypeStatus(Boolean ramtypeStatus);

//    Page<RamTypeMaster> findByHarddiskCapacityTypeContaining(String harddiskCapacityType, Pageable pageable);
	Page<RamTypeMaster> findByramtypeName(String ramtypeName, Pageable pageable);

//    Page<RamTypeMaster> findByRamtypeName(String ramtypeName, Pageable pageable);
//    Page<RamTypeMaster> findByramCapacityContaining(String ramtypeName, Pageable pageable);
	Page<RamTypeMaster> findByRamtypeNameContaining(String ramtypeName, Pageable pageable);

	RamTypeMaster findByRamtypeName(String stringCellValue);






}