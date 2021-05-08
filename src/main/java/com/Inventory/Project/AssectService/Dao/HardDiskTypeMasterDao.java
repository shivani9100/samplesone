package com.Inventory.Project.AssectService.Dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Inventory.Project.AssectService.Model.HardDiskTypeMaster;

@Repository
public interface HardDiskTypeMasterDao extends JpaRepository<HardDiskTypeMaster, Integer> {

	HardDiskTypeMaster findByHardDiskType(String hardDiSkType);

	List<HardDiskTypeMaster> findByHardDiskStatus(Boolean hardDiskStatus);

	Page<HardDiskTypeMaster> findByHardDiskTypeContaining(String hardDiSkType, Pageable pageable);

}