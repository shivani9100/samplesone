package com.Inventory.Project.AssectService.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Inventory.Project.AssectService.Model.RamCapacityMaster;

@Repository
public interface RamCapacityDAO extends JpaRepository<RamCapacityMaster, Integer> {

	List<RamCapacityMaster> findByRamStatus(Boolean ramStatus);

	Page<RamCapacityMaster> findByRamCapacityContaining(String ramCapacity, Pageable pageable);

	List<RamCapacityMaster> findByRamTypeMastersRamtypeId(Integer ramtypeId);

	Optional<RamCapacityMaster> findByRamIdAndRamTypeMastersRamtypeId(Integer ramId, Integer ramtypeId);

	Page<RamCapacityMaster> findByRamTypeMastersRamtypeId(Integer ramtypeId, Pageable pageable);

	Page<RamCapacityMaster> findByRamTypeMastersRamtypeIdAndRamCapacityContaining(Integer ramTypeId, String searchby,
			Pageable pageale);

	RamCapacityMaster findByRamCapacity(String ramCapacity);



	RamCapacityMaster findByRamCapacityAndRamTypeMastersRamtypeId(String ramCapacity, int ramtypeId);

}