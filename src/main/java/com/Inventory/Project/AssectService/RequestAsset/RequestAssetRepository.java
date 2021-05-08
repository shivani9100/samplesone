package com.Inventory.Project.AssectService.RequestAsset;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestAssetRepository extends JpaRepository<RequestAssetModel, Integer> {

	List<RequestAssetModel> findByEmployeeEmployeeid(String employeeid);

	List<RequestAssetModel> findByRequestStatus(Boolean requestStatus);

	Page<RequestAssetModel> findByEmployeeEmployeeidContaining(String employeeid, Pageable pageable);

}
