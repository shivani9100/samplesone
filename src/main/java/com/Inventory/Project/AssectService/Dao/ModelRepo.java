package com.Inventory.Project.AssectService.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Inventory.Project.AssectService.Model.Model;

@Repository
public interface ModelRepo extends JpaRepository<Model, Integer> {

	List<Model> findByBrandBrandid(Integer brandid);

	 

    Optional<Model> findByModelidAndBrandBrandid(Integer modelid, Integer brandid);

 

    Page<Model> findByModelnameContaining(String searchby, Pageable pageale);
    Page<Model> findByBrandBrandidAndModelnameContaining(Integer brandId, String searchby, Pageable pageale);

 

    Page<Model> findByBrandBrandid(Integer brandId, Pageable pageale);
    
    Model findByModelnameContaining(String keyword);



	



	Model findByModelnameAndBrandBrandid(String str3, Integer brandid);

}
