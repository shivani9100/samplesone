package com.Inventory.Project.AssectService.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Inventory.Project.AssectService.Model.HardDiskCapacity;

@Repository
public interface HardDiskCapacityDao extends JpaRepository<HardDiskCapacity, Integer> {

	List<HardDiskCapacity> findByCapacitiesHardDiskTypeId(Integer hardDiskTypeId);

	Optional<HardDiskCapacity> findByHarddiskCapacityIdAndCapacitiesHardDiskTypeId(Integer harddiskCapacityId,
			Integer hardDiskTypeId);

	Page<HardDiskCapacity> findByHarddiskCapacityTypeContaining(String searchby, Pageable pageale);

	Page<HardDiskCapacity> findByCapacitiesHardDiskTypeIdAndHarddiskCapacityTypeContaining(Integer hardDiskTypeId,
			String searchby, Pageable pageale);

	Page<HardDiskCapacity> findByCapacitiesHardDiskTypeId(Integer hardDiskTypeId, Pageable pageale);
	
	HardDiskCapacity findByHarddiskCapacityType(String harddiskCapacityType);

	HardDiskCapacity findByHarddiskCapacityTypeAndCapacitiesHardDiskTypeId(String str7, Integer hardDiskTypeId);

}