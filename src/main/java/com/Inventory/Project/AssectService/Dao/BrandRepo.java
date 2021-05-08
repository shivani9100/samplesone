package com.Inventory.Project.AssectService.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Inventory.Project.AssectService.Model.AssetTypeMaster;
import com.Inventory.Project.AssectService.Model.Brand;
import com.Inventory.Project.AssectService.Response.ResponseList;

@Repository
public interface BrandRepo extends JpaRepository<Brand, Integer> {

	/* Brand findByBrandname(String name); */

	Optional<Brand> findByBrandidAndAssetTypeMasterExAssetId(Integer brandId, Integer assetId);

	List<Brand> findByBrandstatus(Boolean status);

	Page<Brand> findByBrandname(String search, Pageable paging);

	Page<Brand> findByBrandstatus(String search, Pageable pageable);

	Page<Brand> findByBrandnameContaining(String search, PageRequest paging);

	Page<Brand> findByAssetTypeMasterExAssetIdAndBrandnameContaining(Integer assetId, String searchby,
			Pageable pageale);

	Page<Brand> findByAssetTypeMasterExAssetId(Integer assetId, Pageable pageale);

	List<Brand> findByAssetTypeMasterExAssetId(Integer assetId);

	/*
	 * @Query("select b from Brand b where b.brandid=?1 and b.assetTypeMasterEx.assetId= ?2"
	 * ) Brand findByBrandnameAndAssetTypeMasterExAssetId(String modelname, Integer
	 * assetId);
	 */

	Brand findByBrandnameAndAssetTypeMasterExAssetType(String modelname, String assetType);

	Brand findByBrandname(String brandname);
	

}
