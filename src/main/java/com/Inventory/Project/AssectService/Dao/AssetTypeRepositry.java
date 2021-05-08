package com.Inventory.Project.AssectService.Dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.Inventory.Project.AssectService.Model.AssetTypeMaster;
@Repository
@EnableJpaRepositories
public interface AssetTypeRepositry extends JpaRepository<AssetTypeMaster, Integer> {
	
	AssetTypeMaster findByassetType(String assettype);
	
	AssetTypeMaster findByAssetType(String assettype);
	
	List<AssetTypeMaster> findByAssetStatus(boolean assetstatus);
	
	Page<AssetTypeMaster> findByAssetTypeContaining(String assetType,Pageable pageable);
}
